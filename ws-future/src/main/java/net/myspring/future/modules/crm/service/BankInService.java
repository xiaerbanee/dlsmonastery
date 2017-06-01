package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.repository.BankInRepository;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class BankInService {


    @Autowired
    private BankInRepository bankInRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        Page<BankInDto> page = bankInRepository.findPage(pageable, bankInQuery);

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void audit(BankInAuditForm bankInAuditForm){
        //TODO 需要同步金蝶
        BankIn bankIn = bankInRepository.findOne(bankInAuditForm.getId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(bankIn.getProcessInstanceId(), bankIn.getProcessTypeId(), bankInAuditForm.getAuditRemarks(), "1".equals(bankInAuditForm.getPass())));
        if("已通过".equals(activitiCompleteDto.getProcessStatus())){
            bankIn.setLocked(true);
        }

        bankIn.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        bankIn.setProcessStatus(activitiCompleteDto.getProcessStatus());
        bankIn.setPositionId(activitiCompleteDto.getPositionId());
        bankIn.setBillDate(bankInAuditForm.getBillDate() == null ? LocalDate.now() : bankInAuditForm.getBillDate());
        bankInRepository.save(bankIn);

    }

    public BankIn save(BankInForm bankInForm) {
        BankIn bankIn;
        if(bankInForm.isCreate()) {
            bankIn = BeanUtil.map(bankInForm, BankIn.class);
            bankInRepository.save(bankIn);

            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("销售收款",  bankIn.getId(),BankIn.class.getSimpleName(),bankIn.getAmount().toString()));
            bankIn.setProcessFlowId(activitiStartDto.getProcessFlowId());
            bankIn.setProcessStatus(activitiStartDto.getProcessStatus());
            bankIn.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
            bankIn.setPositionId(activitiStartDto.getPositionId());
            bankIn.setProcessTypeId(activitiStartDto.getProcessTypeId());
            bankInRepository.save(bankIn);
        } else {
            bankIn = bankInRepository.findOne(bankInForm.getId());
            ReflectionUtil.copyProperties(bankInForm,bankIn);
            bankInRepository.save(bankIn);
        }
        return bankIn;
    }

    public void logicDelete(String id) {
        bankInRepository.logicDelete(id);
    }

    public void batchAudit(String[] ids, String pass) {
        if(ids == null){
            return;
        }
        List<String> idList = Arrays.asList(ids);
        for(String id : idList){
            BankInAuditForm bankInAuditForm = new BankInAuditForm();
            bankInAuditForm.setAuditRemarks("批量通过");
            bankInAuditForm.setId(id);
            bankInAuditForm.setPass(pass);
            bankInAuditForm.setSyn("1");
            bankInAuditForm.setBillDate(LocalDate.now());

            audit(bankInAuditForm);

        }

    }

    public String export(BankInQuery bankInQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "formatId", "编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "bankName", "银行"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "amount", "金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "serialNumber", "流水号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "billDate", "开单日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "inputDate", "到账日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "processStatus", "状态"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        PageRequest pageRequest = new PageRequest(0,10000);
        List<BankInDto> bankInDtoList = bankInRepository.findPage(pageRequest, bankInQuery).getContent();
        cacheUtils.initCacheInput(bankInDtoList);

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("销售收款列表", bankInDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"销售收款列表"+ LocalDateUtils.format(LocalDate.now()) +".xlsx", simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }

    public BankInDto findDto(String id) {
        BankInDto bankInDto = bankInRepository.findDto(id);
        cacheUtils.initCacheInput(bankInDto);
        return bankInDto;
    }
}
