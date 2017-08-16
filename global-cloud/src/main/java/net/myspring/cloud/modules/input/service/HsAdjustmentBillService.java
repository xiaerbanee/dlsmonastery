package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.modules.input.dto.*;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.ApPayBillForm;
import net.myspring.cloud.modules.input.web.form.HsAdjustmentBillForm;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.kingdee.repository.BdStockRepository;
import net.myspring.cloud.modules.kingdee.repository.BdSupplierRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.utils.HandsontableUtils;
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
 * 入库成本调整单
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class HsAdjustmentBillService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;
    @Autowired
    private BdSupplierRepository bdSupplierRepository;
    @Autowired
    private BdStockRepository bdStockRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;

    private KingdeeSynDto save(HsAdjustmentBillDto hsAdjustmentBillDto, KingdeeBook kingdeeBook) {
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                hsAdjustmentBillDto.getExtendId(),
                hsAdjustmentBillDto.getExtendType(),
                KingdeeFormIdEnum.HS_AdjustmentBill.name(),
                hsAdjustmentBillDto.getJson(),
                kingdeeBook);
        kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("成本调整单失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(HsAdjustmentBillDto hsAdjustmentBillDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(hsAdjustmentBillDto, kingdeeBook);
        }else{
            throw new ServiceException("登入金蝶系统失败，请检查您的账户密码是否正确");
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(HsAdjustmentBillForm hsAdjustmentBillForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        LocalDate billDate = hsAdjustmentBillForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(hsAdjustmentBillForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> materialNameList = Lists.newArrayList();
        List<String> stockNameList = Lists.newArrayList();
        for (List<Object> row : data) {
            materialNameList.add(HandsontableUtils.getValue(row,0));
            stockNameList.add(HandsontableUtils.getValue(row,2));
        }
        Map<String,String> materialNameMap = bdMaterialRepository.findByNameList(materialNameList).stream().collect(Collectors.toMap(BdMaterial::getFName,BdMaterial::getFNumber));
        Map<String,String> stockNameMap = bdStockRepository.findByNameList(stockNameList).stream().collect(Collectors.toMap(BdStock::getFName,BdStock::getFNumber));
        HsAdjustmentBillDto hsAdjustmentBillDto = new HsAdjustmentBillDto();
        hsAdjustmentBillDto.setExtendType(ExtendTypeEnum.入库成本调整单_K3.name());
        hsAdjustmentBillDto.setCreator(accountKingdeeBook.getUsername());
        hsAdjustmentBillDto.setDate(billDate);
        for (List<Object> row : data){
            String materialName = HandsontableUtils.getValue(row,0);
            String priceStr = HandsontableUtils.getValue(row,1);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            String stockName = HandsontableUtils.getValue(row,2);
            HsAdjustmentBillEntityDto entityDto = new HsAdjustmentBillEntityDto();
            entityDto.setMaterialNumber(materialNameMap.get(materialName));
            entityDto.setAdjustmentAmount(price);
            entityDto.setStockNumber(stockNameMap.get(stockName));
            hsAdjustmentBillDto.getEntityDtoList().add(entityDto);
        }
        return save(hsAdjustmentBillDto,kingdeeBook,accountKingdeeBook);
    }

    public HsAdjustmentBillForm getForm(){
        HsAdjustmentBillForm hsAdjustmentBillForm = new HsAdjustmentBillForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("materialNameList",bdMaterialRepository.findAll().stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        map.put("stockNameList",bdStockRepository.findAll().stream().map(BdStock::getFName).collect(Collectors.toList()));
        hsAdjustmentBillForm.setExtra(map);
        return hsAdjustmentBillForm;
    }

}
