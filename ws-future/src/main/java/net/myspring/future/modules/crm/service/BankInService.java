package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.SimpleProcessEndsEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.SimpleProcess;
import net.myspring.future.modules.basic.manager.ArReceiveBillManager;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.manager.SimpleProcessManager;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.repository.BankInRepository;
import net.myspring.future.modules.crm.web.form.BankInAuditForm;
import net.myspring.future.modules.crm.web.form.BankInForm;
import net.myspring.future.modules.crm.web.query.BankInQuery;
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
    private BankInRepository bankInRepository;
   @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ArReceiveBillManager arReceiveBillManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private SimpleProcessManager simpleProcessManager;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        bankInQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<BankInDto> page = bankInRepository.findPage(pageable, bankInQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void audit(BankInAuditForm bankInAuditForm){

        BankIn bankIn = bankInRepository.findOne(bankInAuditForm.getId());
        Depot depot = depotRepository.findOne(bankIn.getShopId());
        if(Boolean.TRUE.equals(bankInAuditForm.getSyn() && StringUtils.isBlank(depot.getClientId())) ){
            throw new ServiceException("该门店没有绑定财务，不能同步金蝶");
        }
        if(Boolean.TRUE.equals(bankInAuditForm.getSyn()) && StringUtils.isNotBlank(bankIn.getBankId())){
            Bank bank = bankRepository.findOne(bankIn.getBankId());
            if(StringUtils.isBlank(bank.getCode())){
                throw new ServiceException("该银行没有绑定财务，不能同步金蝶");
            }
        }

        SimpleProcess simpleProcess = simpleProcessManager.go(bankIn.getSimpleProcessId(), bankInAuditForm.getPass(), bankInAuditForm.getAuditRemarks());
        if(SimpleProcessEndsEnum.已通过.name().equals(simpleProcess.getCurrentProcessStatus())){
            bankIn.setLocked(true);
        }

        bankIn.setProcessStatus(simpleProcess.getCurrentProcessStatus());
        bankIn.setPositionId(simpleProcess.getCurrentPositionId());
        bankIn.setBillDate(bankInAuditForm.getBillDate() == null ? LocalDate.now() : bankInAuditForm.getBillDate());
        bankInRepository.save(bankIn);

        if(Boolean.TRUE.equals(bankInAuditForm.getSyn()) && SimpleProcessEndsEnum.已通过.name().equals(simpleProcess.getCurrentProcessStatus())){
            synToCloud(bankIn, bankInAuditForm);
        }
    }

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
        if(StringUtils.isBlank(bankInForm.getBankId()) || "0".equals(StringUtils.trim(bankInForm.getBankId()))){
            bankIn.setBankId(null);
        }else{
            bankIn.setBankId(bankInForm.getBankId());
        }
        bankIn.setTransferType(bankInForm.getTransferType());
        bankIn.setInputDate(bankInForm.getInputDate());
        bankIn.setAmount(bankInForm.getAmount());
        bankIn.setSerialNumber(bankInForm.getSerialNumber());
        bankIn.setRemarks(bankInForm.getRemarks());
        bankInRepository.save(bankIn);

        if(bankInForm.isCreate()) {
            SimpleProcess simpleProcess = simpleProcessManager.start(BankIn.class.getSimpleName());
            bankIn.setProcessStatus(simpleProcess.getCurrentProcessStatus());
            bankIn.setPositionId(simpleProcess.getCurrentPositionId());
            bankIn.setSimpleProcessId(simpleProcess.getId());
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
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "transferType", "转账类型"));
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

}
