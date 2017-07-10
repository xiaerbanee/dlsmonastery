package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.manager.ArOtherRecAbleManager;
import net.myspring.future.modules.basic.manager.CnJournalBankManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.dto.ShopDepositLatestDto;
import net.myspring.future.modules.layout.repository.ShopDepositRepository;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShopDepositService {

    @Autowired
    private ShopDepositRepository shopDepositRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ArOtherRecAbleManager arOtherRecAbleManager;
    @Autowired
    private CnJournalBankManager cnJournalBankManager;
    @Autowired
    private DepotRepository depotRepository;

    public Page<ShopDepositDto> findPage(Pageable pageable, ShopDepositQuery shopDepositQuery) {
        Page<ShopDepositDto> page = shopDepositRepository.findPage(pageable, shopDepositQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void save(ShopDepositForm shopDepositForm) {

        if(!shopDepositForm.isCreate()){
            throw new ServiceException();
        }
        Depot depot = depotRepository.findOne(shopDepositForm.getShopId());
        if(!OutBillTypeEnum.不同步到金蝶.name().equals(shopDepositForm.getOutBillType()) && StringUtils.isBlank(depot.getClientId())){
            throw new ServiceException("该门店没有绑定财务，不能同步金蝶");
        }

        if(shopDepositForm.isImageAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.形象保证金, shopDepositForm.getImageAmount());
        }
        if(shopDepositForm.isMarketAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.市场保证金, shopDepositForm.getMarketAmount());
        }
        if(shopDepositForm.isDemoPhoneAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.演示机押金, shopDepositForm.getDemoPhoneAmount());
        }

    }

    @Transactional
    public void logicDelete(String id) {
        shopDepositRepository.logicDelete(id);
    }

    @Transactional
    private void saveShopDeposit(ShopDepositForm shopDepositForm, ShopDepositTypeEnum type, BigDecimal amount) {
        ShopDeposit shopDeposit  = new ShopDeposit();
        shopDeposit.setShopId(shopDepositForm.getShopId());
        shopDeposit.setBankId(shopDepositForm.getBankId());
        shopDeposit.setBillDate(shopDepositForm.getBillDate());
        shopDeposit.setAmount(amount);
        shopDeposit.setType(type.name());
        shopDeposit.setRemarks(type.name()+"  "+ StringUtils.trimToEmpty(shopDepositForm.getRemarks()));

        ShopDeposit latest = shopDepositRepository.findLatest(type.name(), shopDepositForm.getShopId());

        BigDecimal leftAmount = (latest == null ? BigDecimal.ZERO : latest.getLeftAmount()).add(amount);
        shopDeposit.setLeftAmount(leftAmount);

        shopDepositRepository.save(shopDeposit);

        if(latest != null){
            latest.setLocked(true);
            shopDepositRepository.save(latest);
        }

        if(!OutBillTypeEnum.不同步到金蝶.name().equals(shopDepositForm.getOutBillType())){
            if (OutBillTypeEnum.其他应收单.name().equals(shopDepositForm.getOutBillType())) {
                KingdeeSynReturnDto returnDto = arOtherRecAbleManager.synForShopDeposit(shopDeposit);
                shopDeposit.setCloudSynId(returnDto.getId());
                shopDeposit.setOutCode(returnDto.getBillNo());
                shopDepositRepository.save(shopDeposit);
            } else {
                KingdeeSynReturnDto returnDto = cnJournalBankManager.synForShopDeposit(shopDeposit,shopDepositForm.getDepartMent());
                shopDeposit.setCloudSynId(returnDto.getId());
                shopDeposit.setOutCode(returnDto.getBillNo());
                shopDepositRepository.save(shopDeposit);
            }
        }
    }

    public ShopDepositDto findDto(String id) {

        ShopDepositDto shopDepositDto = shopDepositRepository.findDto(id);
        cacheUtils.initCacheInput(shopDepositDto);
        return shopDepositDto;
    }

    public BigDecimal findLeftAmount(String type, String depotId) {
        ShopDeposit latest = shopDepositRepository.findLatest(type, depotId);
        return (latest == null ? BigDecimal.ZERO : latest.getLeftAmount());
    }

    public SimpleExcelBook exportLatest() {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();

        List<SimpleExcelColumn> shopDepositLatestColumnList = Lists.newArrayList();
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "shopOfficeName", "考核区域"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "leftImageAmount", "剩余形象保证金"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "leftMarketAmount", "剩余市场保证金"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "imageDepositCreatedByName", "形象保证金创建人"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "imageDepositCreatedDate", "形象保证金创建时间"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "marketDepositCreatedByName", "市场保证金创建人"));
        shopDepositLatestColumnList.add(new SimpleExcelColumn(workbook, "marketDepositCreatedDate", "市场保证金创建时间"));
        List<ShopDepositLatestDto> shopDepositLatestDtoList = shopDepositRepository.findShopDepositLatestDto(10000);
        cacheUtils.initCacheInput(shopDepositLatestDtoList);
        SimpleExcelSheet sheet1 = new SimpleExcelSheet("门店最新押金数据", shopDepositLatestDtoList, shopDepositLatestColumnList);
        ExcelUtils.doWrite(workbook, sheet1);
        simpleExcelSheetList.add(sheet1);

        List<SimpleExcelColumn> shopDepositColumnList = Lists.newArrayList();
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "shopOfficeName", "考核区域"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "type", "类型"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "amount", "金额"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "leftAmount", "剩余金额"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "更新人"));
        shopDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));
        List<ShopDepositDto> shopDepositDtoList = shopDepositRepository.findForExport(10000);
        cacheUtils.initCacheInput(shopDepositDtoList);
        SimpleExcelSheet sheet2 = new SimpleExcelSheet("押金明细", shopDepositDtoList, shopDepositColumnList);
        ExcelUtils.doWrite(workbook, sheet2);
        simpleExcelSheetList.add(sheet2);

        return new SimpleExcelBook(workbook,"押金列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
    }

}
