package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.StkMisDeliveryTypeEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.StkMisDeliveryDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.StkMisDeliveryForm;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.kingdee.repository.BdStockRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *其他出库单
 * Created by lihx on 2017/5/17.
 */
@Service
@KingdeeDataSource
public class StkMisDeliveryService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private BdStockRepository bdStockRepository;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    private KingdeeSynDto save(StkMisDeliveryDto stkMisDeliveryDto,KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                stkMisDeliveryDto.getExtendId(),
                stkMisDeliveryDto.getExtendType(),
                KingdeeFormIdEnum.STK_MisDelivery.name(),
                stkMisDeliveryDto.getJson(),
                kingdeeBook);
        kingdeeManager.save(kingdeeSynDto);
        return kingdeeSynDto;
    }

    public List<KingdeeSynDto> save(StkMisDeliveryForm stkMisDeliveryForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        Map<String, StkMisDeliveryDto> misDeliveryMap = Maps.newLinkedHashMap();
        String departmentNumber = stkMisDeliveryForm.getDepartmentNumber();
        LocalDate billDate = stkMisDeliveryForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(stkMisDeliveryForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> stockNameList = Lists.newArrayList();
        for (List<Object> row : data) {
            stockNameList.add(HandsontableUtils.getValue(row, 2));
        }
        Map<String, String> stockNumMap = Maps.newHashMap();
        for (BdStock bdstock : bdStockRepository.findByNameList(stockNameList)) {
            stockNumMap.put(bdstock.getFName(), bdstock.getFNumber());
        }
        for (List<Object> row : data) {
            String materialNumber = HandsontableUtils.getValue(row, 0);
            String stockName = HandsontableUtils.getValue(row, 2);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row, 3));
            String type = HandsontableUtils.getValue(row, 4);
            String remarks = HandsontableUtils.getValue(row, 5);

            String billKey = materialNumber + CharConstant.COMMA + stockName + CharConstant.COMMA + qty + CharConstant.COMMA + remarks + CharConstant.COMMA + type;
            if (!misDeliveryMap.containsKey(billKey)) {
                StkMisDeliveryDto misDelivery = new StkMisDeliveryDto();
                misDelivery.setCreator(accountKingdeeBook.getUsername());
                misDelivery.setBillDate(billDate);
                misDelivery.setDepartmentNumber(departmentNumber);
                misDelivery.setMaterialNumber(materialNumber);
                misDelivery.setStockNumber(stockNumMap.get(stockName));
                misDelivery.setQty(qty);
                misDelivery.setFEntryNote(remarks);
                misDelivery.setMisDeliveryType(type);
                misDeliveryMap.put(billKey, misDelivery);
            } else {
                misDeliveryMap.get(billKey).setQty(qty + qty);
            }
        }
        List<StkMisDeliveryDto> billList = Lists.newArrayList(misDeliveryMap.values());
        return save(billList,kingdeeBook,accountKingdeeBook);
    }

    public List<KingdeeSynDto> save(List<StkMisDeliveryDto> billList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(billList)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (StkMisDeliveryDto misDelivery : billList) {
                    KingdeeSynDto kingdeeSynDto = save(misDelivery, kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }else{
                kingdeeSynDtoList.add(new KingdeeSynDto(false,"未登入金蝶系统"));
            }
        }
        return kingdeeSynDtoList;
    }

    public StkMisDeliveryForm getForm(){
        StkMisDeliveryForm stkMisDeliveryForm = new StkMisDeliveryForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("stockNameList",bdStockRepository.findAll().stream().map(BdStock::getFName).collect(Collectors.toList()));
        List<BdMaterial> materialList = bdMaterialRepository.findAll();
        map.put("materialNameList",materialList.stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        map.put("materialNumberList",materialList.stream().map(BdMaterial::getFNumber).collect(Collectors.toList()));
        map.put("stkMisDeliveryTypeEnums",StkMisDeliveryTypeEnum.values());
        stkMisDeliveryForm.setExtra(map);
        return stkMisDeliveryForm;
    }
}
