package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
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
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ShopDepositService {

    @Autowired
    private ShopDepositRepository shopDepositRepository;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ShopDepositDto> findPage(Pageable pageable, ShopDepositQuery shopDepositQuery) {
        Page<ShopDepositDto> page = shopDepositRepository.findPage(pageable, shopDepositQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void save(ShopDepositForm shopDepositForm) {

        if(!shopDepositForm.isCreate()){
            throw new ServiceException();
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
//TODO 同步金蝶
//                String otherTypes="其他应付款-客户押金（批发）-市场保证金";
//                if ("其他应收单".equals(shopDepositForm.getOutBillType())) {
//                    K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.AR_OtherRecAble.name(),item.getAROtherRecAbleImage(otherTypes),item.getId(),item.getIdStr(), K3CloudSynEntity.ExtendType.押金列表.name());
//                    k3cloudSynDao.save(k3CloudSynEntity);
//                    item.setK3CloudSynEntity(k3CloudSynEntity);
//                } else {
//                    K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.CN_JOURNAL.name(),item.getBankJournal(otherTypes),item.getId(),item.getIdStr(), K3CloudSynEntity.ExtendType.押金列表.name());
//                    k3cloudSynDao.save(k3CloudSynEntity);
//                    item.setK3CloudSynEntity(k3CloudSynEntity);
//                }
//                shopDepositDao.save(item);
//
//            list.add(item.getId());
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

    public String exportLatest() {

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
        simpleExcelSheetList.add(new SimpleExcelSheet("门店最新押金数据", shopDepositLatestDtoList, shopDepositLatestColumnList));

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
        simpleExcelSheetList.add(new SimpleExcelSheet("押金明细", shopDepositDtoList, shopDepositColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"押金列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public String getDefaultDepartMent(String shopId) {
        ClientDto clientDto = clientRepository.findByDepotId(shopId);
        if(clientDto == null || StringUtils.isBlank(clientDto.getOutId())){
            return null;
        }
        BdDepartment bdDepartment = cloudClient.findDepartByCustomer(clientDto.getOutId());
        if(bdDepartment == null){
            return null;
        }
        return bdDepartment.getFNumber();

    }
}
