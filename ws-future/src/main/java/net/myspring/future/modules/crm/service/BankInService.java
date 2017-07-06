package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.manager.ArReceiveBillManager;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.repository.BankInRepository;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import net.myspring.future.modules.crm.web.form.BankInBatchDetailForm;
import net.myspring.future.modules.crm.web.form.BankInBatchForm;
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
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BankInService {
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private BankInRepository bankInRepository;
   @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private ArReceiveBillManager arReceiveBillManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private DepotManager depotManager;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        bankInQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<BankInDto> page = bankInRepository.findPage(pageable, bankInQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void audit(BankInAuditForm bankInAuditForm){

        BankIn bankIn = bankInRepository.findOne(bankInAuditForm.getId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(new ActivitiCompleteForm(bankIn.getProcessInstanceId(), bankIn.getProcessTypeId(), bankInAuditForm.getAuditRemarks(), bankInAuditForm.getPass()));
        if("已通过".equals(activitiCompleteDto.getProcessStatus())){
            bankIn.setLocked(true);
        }

        bankIn.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        bankIn.setProcessStatus(activitiCompleteDto.getProcessStatus());
        bankIn.setPositionId(activitiCompleteDto.getPositionId());
        bankIn.setBillDate(bankInAuditForm.getBillDate() == null ? LocalDate.now() : bankInAuditForm.getBillDate());
        bankInRepository.save(bankIn);

        if(Boolean.TRUE.equals(bankInAuditForm.getSyn()) && "已通过".equals(activitiCompleteDto.getProcessStatus())){
            synToCloud(bankIn, bankInAuditForm);
        }
    }

    @Transactional
    private void synToCloud(BankIn bankIn, BankInAuditForm bankInAuditForm) {

        KingdeeSynReturnDto kingdeeSynReturnDto = arReceiveBillManager.synForBankIn(bankIn,bankInAuditForm);

        bankIn.setCloudSynId(kingdeeSynReturnDto.getId());
        bankIn.setOutCode(kingdeeSynReturnDto.getBillNo());
        bankInRepository.save(bankIn);
    }

    @Transactional
    public BankIn save(BankInForm bankInForm) {
        BankIn bankIn;
        if(bankInForm.isCreate()) {
            bankIn = new BankIn();
        }else{
            bankIn = bankInRepository.findOne(bankInForm.getId());
        }
        bankIn.setShopId(bankInForm.getShopId());
        bankIn.setType(bankInForm.getType());
        bankIn.setBankId(bankInForm.getBankId());
        bankIn.setInputDate(bankInForm.getInputDate());
        bankIn.setAmount(bankInForm.getAmount());
        bankIn.setSerialNumber(bankInForm.getSerialNumber());
        bankIn.setRemarks(bankInForm.getRemarks());
        bankInRepository.save(bankIn);

        if(bankInForm.isCreate()) {
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("销售收款",  bankIn.getId(),BankIn.class.getSimpleName(),bankIn.getAmount().toString()));
            bankIn.setProcessFlowId(activitiStartDto.getProcessFlowId());
            bankIn.setProcessStatus(activitiStartDto.getProcessStatus());
            bankIn.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
            bankIn.setPositionId(activitiStartDto.getPositionId());
            bankIn.setProcessTypeId(activitiStartDto.getProcessTypeId());
            bankInRepository.save(bankIn);
        }
        return bankIn;
    }

    @Transactional
    public void logicDelete(String id) {
        bankInRepository.logicDelete(id);
    }

    public SimpleExcelBook export(BankInQuery bankInQuery) {

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

        List<BankInDto> bankInDtoList = findPage(new PageRequest(0,10000), bankInQuery).getContent();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("销售收款列表", bankInDtoList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"销售收款列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
    }

    public BankInDto findDto(String id) {
        BankInDto bankInDto = bankInRepository.findDto(id);
        cacheUtils.initCacheInput(bankInDto);
        return bankInDto;
    }

    @Transactional
    public void batchAdd(BankInBatchForm bankInBatchForm) {
        for(BankInBatchDetailForm bankInBatchDetailForm : bankInBatchForm.getBankInBatchDetailFormList()){
            Depot depot = depotRepository.findByEnabledIsTrueAndName(bankInBatchDetailForm.getShopName());
            if(depot == null || StringUtils.isBlank(depot.getClientId())){
                throw new ServiceException("门店："+bankInBatchDetailForm.getShopName()+"不存在，或者未绑定财务门店");
            }
            Bank bank = bankRepository.findByName(bankInBatchDetailForm.getBankName());
            if(bank == null){
                throw new ServiceException("银行："+bankInBatchDetailForm.getBankName()+"不存在");
            }
            BankInForm bankInForm = new BankInForm();
            bankInForm.setShopId(depot.getId());
            bankInForm.setBankId(bank.getId());
            bankInForm.setAmount(bankInBatchDetailForm.getAmount());
            bankInForm.setInputDate(bankInBatchDetailForm.getInputDate());
            bankInForm.setType(bankInBatchDetailForm.getType());
            bankInForm.setRemarks(bankInBatchDetailForm.getRemarks());

            save(bankInForm);
        }
    }
}
