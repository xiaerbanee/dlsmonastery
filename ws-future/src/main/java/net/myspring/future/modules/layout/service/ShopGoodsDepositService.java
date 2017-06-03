package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopGoodsDepositStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositSumDto;
import net.myspring.future.modules.layout.repository.ShopGoodsDepositRepository;
import net.myspring.future.modules.layout.web.form.ShopGoodsDepositForm;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ShopGoodsDepositService {

    @Autowired
    private ShopGoodsDepositRepository shopGoodsDepositRepository;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ShopGoodsDepositDto> findPage(Pageable pageable, ShopGoodsDepositQuery shopGoodsDepositQuery) {

        Page<ShopGoodsDepositDto> page = shopGoodsDepositRepository.findPage(pageable, shopGoodsDepositQuery);

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopGoodsDepositForm getForm(ShopGoodsDepositForm shopGoodsDepositForm) {
        ShopGoodsDepositForm result;

        if(shopGoodsDepositForm.isCreate()){
            result = new ShopGoodsDepositForm();
            if(shopGoodsDepositForm.getShopId()!=null){
                result.setShopId(shopGoodsDepositForm.getShopId());
                result.setDepartMent("");//TODO 需要设置department
            }
        }else{
            ShopGoodsDeposit sgd = shopGoodsDepositRepository.findOne(shopGoodsDepositForm.getId());
            result = BeanUtil.map(sgd, ShopGoodsDepositForm.class);
        }
        return result;
    }

    public void save(ShopGoodsDepositForm shopGoodsDepositForm) {

        if(shopGoodsDepositForm.isCreate()){
            ShopGoodsDeposit shopGoodsDeposit = new ShopGoodsDeposit();
            ReflectionUtil.copyProperties(shopGoodsDepositForm, shopGoodsDeposit);
            shopGoodsDeposit.setStatus(ShopGoodsDepositStatusEnum.省公司审核.name());
            shopGoodsDeposit.setOutBillType(OutBillTypeEnum.手工日记账.name());
            shopGoodsDepositRepository.save(shopGoodsDeposit);
        }else{
            ShopGoodsDeposit shopGoodsDeposit = shopGoodsDepositRepository.findOne(shopGoodsDepositForm.getId());
            shopGoodsDeposit.setBankId(shopGoodsDepositForm.getBankId());
            shopGoodsDeposit.setRemarks(shopGoodsDepositForm.getRemarks());
            shopGoodsDeposit.setAmount(shopGoodsDepositForm.getAmount());
            shopGoodsDepositRepository.save(shopGoodsDeposit);
        }
    }

    public void batchAudit(String[] ids) {
        if(ids==null){
            return;
        }

//                TODO 要檢查確認批量審核的所有記錄對應的門店都綁定了財務
//                if(StringUtils.isBlank(shopGoodsDeposit.getShop().getRealOutId())){
//                    throw new ServiceException("审核失败,"+shopGoodsDeposit.getShopName()+"没有绑定财务门店；");
//                }
            for(String eachId : ids){
                if(eachId == null){
                    continue;
                }
                ShopGoodsDeposit shopGoodsDeposit=shopGoodsDepositRepository.findOne(eachId);
                if(shopGoodsDeposit == null || !ShopGoodsDepositStatusEnum.省公司审核.name().equals(shopGoodsDeposit.getStatus()) || StringUtils.isNotBlank(shopGoodsDeposit.getOutCode())){
                    continue;
                }

//                    String otherTypes="其他应付款-订货会订金";
                if(OutBillTypeEnum.手工日记账.name().equals(shopGoodsDeposit.getOutBillType())){
                        //TODO 同步金蝶
//                        K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.CN_JOURNAL.name(),shopGoodsDeposit.getBankJournal(otherTypes),shopGoodsDeposit.getId(),shopGoodsDeposit.getFormatId(), K3CloudSynEntity.ExtendType.定金收款.name());
//                        k3cloudSynDao.save(k3CloudSynEntity);
//                        shopGoodsDeposit.setK3CloudSynEntity(k3CloudSynEntity);
                }
                if(OutBillTypeEnum.其他应收单.name().equals(shopGoodsDeposit.getOutBillType())){
                        //TODO 同步金蝶
//                        K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.AR_OtherRecAble.name(),shopGoodsDeposit.getAROtherRecAbleImage(otherTypes),shopGoodsDeposit.getId(),shopGoodsDeposit.getFormatId(), K3CloudSynEntity.ExtendType.定金收款.name());
//                        k3cloudSynDao.save(k3CloudSynEntity);
//                        shopGoodsDeposit.setK3CloudSynEntity(k3CloudSynEntity);
                }
                shopGoodsDeposit.setStatus(ShopGoodsDepositStatusEnum.已通过.name());
                shopGoodsDeposit.setLocked(true);
                shopGoodsDeposit.setBillDate(LocalDateTime.now());
                shopGoodsDepositRepository.save(shopGoodsDeposit);
            }

    }

    public void delete(String id) {
        shopGoodsDepositRepository.logicDelete(id);
    }

    public String export(ShopGoodsDepositQuery shopGoodsDepositQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> shopGoodsDepositColumnList = Lists.newArrayList();
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "formatId", "编号"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "departMent", "部门"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "amount", "收款金额"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "outBillType", "单据类型"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedBy", "修改人"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "修改时间"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "status", "状态"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<ShopGoodsDepositDto> shopGoodsDepositDtoList = findPage(new PageRequest(0,10000), shopGoodsDepositQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("订金详细", shopGoodsDepositDtoList, shopGoodsDepositColumnList));

        List<SimpleExcelColumn> shopGoodsDepositSumColumnList = Lists.newArrayList();
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "totalAmount", "剩余订金"));

        List<ShopGoodsDepositSumDto> shopGoodsDepositSumDtoList = findShopGoodsDepositSumDtoList(RequestUtils.getCompanyId());
        simpleExcelSheetList.add(new SimpleExcelSheet("订金汇总", shopGoodsDepositSumDtoList, shopGoodsDepositSumColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"订金列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }

    private List<ShopGoodsDepositSumDto> findShopGoodsDepositSumDtoList(String companyId) {
        List<ShopGoodsDepositSumDto> result = shopGoodsDepositRepository.findShopGoodsDepositSumDtoList(companyId);
        cacheUtils.initCacheInput(result);
        return result;
    }
}
