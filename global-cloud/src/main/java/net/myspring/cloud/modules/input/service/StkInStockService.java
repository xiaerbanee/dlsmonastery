package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.*;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.StkInStockForm;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购入库
 * Created by lihx on 2017/6/13.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class StkInStockService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;
    @Autowired
    private PurMrbService purMrbService;

    @Transactional
    private KingdeeSynDto save(StkInStockDto stkInStockDto, KingdeeBook kingdeeBook) {
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                stkInStockDto.getExtendId(),
                stkInStockDto.getExtendType(),
                KingdeeFormIdEnum.STK_InStock.name(),
                stkInStockDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("采购入库失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public List<KingdeeSynDto> save(StkInStockForm stkInStockForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook)  {
        LocalDate billDate = stkInStockForm.getBillDate();
        String stockNumber = stkInStockForm.getStockNumber();
        String departmentNumber = stkInStockForm.getDepartmentNumber();
        String supplierNumber = stkInStockForm.getSupplierNumber();
        String json = HtmlUtils.htmlUnescape(stkInStockForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> materialNameForADList = Lists.newArrayList();
        List<String> materialNameForSHList = Lists.newArrayList();
        for (List<Object> row : data){
            materialNameForADList.add(HandsontableUtils.getValue(row,6));
            materialNameForSHList.add(HandsontableUtils.getValue(row,8));
        }
        Map<String,String> materialNameForADMap = bdMaterialRepository.findByNameList(materialNameForADList).stream().collect(Collectors.toMap(BdMaterial::getFName, BdMaterial::getFNumber));
        Map<String,String> materialNameForSHMap = bdMaterialRepository.findByNameList(materialNameForSHList).stream().collect(Collectors.toMap(BdMaterial::getFName, BdMaterial::getFNumber));
        List<PurMrbFEntityDto> purMrbFEntryDtoList = Lists.newArrayList();
        StkInStockDto stkInStockDto = new StkInStockDto();
        stkInStockDto.setExtendType(ExtendTypeEnum.采购入库_K3.name());
        stkInStockDto.setCreator(accountKingdeeBook.getUsername());
        stkInStockDto.setDate(billDate);
        stkInStockDto.setDepartmentNumber(departmentNumber);
        stkInStockDto.setSupplierNumber(supplierNumber);
        for (List<Object> row : data) {
            String productNumber = HandsontableUtils.getValue(row,0);
            String productName = HandsontableUtils.getValue(row,1);
            String priceStr = HandsontableUtils.getValue(row,2);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row,3));
            String remarks = HandsontableUtils.getValue(row,4);
            String RLForADStr = HandsontableUtils.getValue(row,5);
            BigDecimal RLForAD = StringUtils.isEmpty(RLForADStr) ? BigDecimal.ZERO : new BigDecimal(RLForADStr);
            String productType = HandsontableUtils.getValue(row,6);
            String RLForSHStr = HandsontableUtils.getValue(row,7);
            BigDecimal RLForSH = StringUtils.isEmpty(RLForSHStr) ? BigDecimal.ZERO : new BigDecimal(RLForSHStr);
            String afterSaleType = HandsontableUtils.getValue(row,8);
            
            StkInStockFEntryDto stkInStockFEntryDto = new StkInStockFEntryDto();
            stkInStockFEntryDto.setMaterialNumber(productNumber);
            stkInStockFEntryDto.setPrice(price);
            stkInStockFEntryDto.setQty(qty);
            stkInStockFEntryDto.setStockNumber(stockNumber);
            stkInStockFEntryDto.setNote(remarks);
            stkInStockDto.getStkInStockFEntryDtoList().add(stkInStockFEntryDto);
            
            //如果是广告让利
            if (RLForAD.compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal returnPrice = price.multiply(RLForAD).multiply(new BigDecimal(0.01));
                BigDecimal returnAmount = new BigDecimal(qty).multiply(returnPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

                PurMrbFEntityDto purMrbFEntityDto = new PurMrbFEntityDto();
                purMrbFEntityDto.setStockNumber(stockNumber);
                purMrbFEntityDto.setMaterialNumber(materialNameForADMap.get(productType));
                purMrbFEntityDto.setPrice(returnAmount);
                purMrbFEntityDto.setQty(1);
                purMrbFEntityDto.setNote(productName+", "+qty+", "+remarks);
                purMrbFEntryDtoList.add(purMrbFEntityDto);
            }
            //如果是售后让利
            if (RLForSH.compareTo(BigDecimal.ZERO) == 1 && KingdeeNameEnum.INAVIVO.name().equals(kingdeeBook.getName())) {
                BigDecimal returnPrice = price.multiply(RLForSH).multiply(new BigDecimal(0.01));
                BigDecimal returnAmount = new BigDecimal(qty).multiply(returnPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

                PurMrbFEntityDto purMrbFEntityDto = new PurMrbFEntityDto();
                purMrbFEntityDto.setStockNumber(stockNumber);
                purMrbFEntityDto.setMaterialNumber(materialNameForSHMap.get(afterSaleType));
                purMrbFEntityDto.setPrice(returnAmount);
                purMrbFEntityDto.setQty(1);
                purMrbFEntityDto.setNote(productName+", "+qty+", "+remarks);
                purMrbFEntryDtoList.add(purMrbFEntityDto);
            }
        }
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        kingdeeSynDtoList.add(save(stkInStockDto, kingdeeBook,accountKingdeeBook));
        if (CollectionUtil.isNotEmpty(purMrbFEntryDtoList)) {
            PurMrbDto purMrbDto = new PurMrbDto();
            purMrbDto.setCreator(accountKingdeeBook.getUsername());
            purMrbDto.setDate(billDate);
            purMrbDto.setSupplierNumber(supplierNumber);
            purMrbDto.setDepartmentNumber(departmentNumber);
            purMrbDto.getEntityDtoList().addAll(purMrbFEntryDtoList);
            kingdeeSynDtoList.add(purMrbService.save(purMrbDto,kingdeeBook,accountKingdeeBook));
        }
        return kingdeeSynDtoList;
    }

    @Transactional
    public KingdeeSynDto save(StkInStockDto stkInStockDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(stkInStockDto, kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    public StkInStockForm getForm(StkInStockForm stkInStockForm,KingdeeBook kingdeeBook){
        List<String> typeList = Lists.newArrayList();
        if (KingdeeNameEnum.JXDJ.name().equals(kingdeeBook.getName())){
            typeList.add("电玩广告让利");
            typeList.add("电话广告让利");
            typeList.add("imoo广告让利");
        }else if(KingdeeNameEnum.INAVIVO.name().equals(kingdeeBook.getName())){
            typeList.add("vivo售后服务费");
        }
        stkInStockForm.getTypeList().addAll(typeList);
        stkInStockForm.setKingdeeName(kingdeeBook.getName());
        List<BdMaterial> materialList = bdMaterialRepository.findByErpCleId("1");
        stkInStockForm.setMaterialNameList(materialList.stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        return stkInStockForm;
    }
}
