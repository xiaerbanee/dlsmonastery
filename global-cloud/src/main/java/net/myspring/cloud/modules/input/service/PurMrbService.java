package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.PurMrbDto;
import net.myspring.cloud.modules.input.dto.PurMrbFEntityDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.PurMrbForm;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购退料
 * Created by lihx on 2017/6/13.
 */
@Service
@KingdeeDataSource
@Transactional
public class PurMrbService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    private KingdeeSynDto save(PurMrbDto purMrbDto, KingdeeBook kingdeeBook) {
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                purMrbDto.getExtendId(),
                purMrbDto.getExtendType(),
                KingdeeFormIdEnum.PUR_MRB.name(),
                purMrbDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("采购退料失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    public KingdeeSynDto save(PurMrbForm purMrbForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        LocalDate billDate = purMrbForm.getBillDate();
        String stockNumber = purMrbForm.getStockNumber();
        String departmentNumber = purMrbForm.getDepartmentNumber();
        String supplierNumber = purMrbForm.getSupplierNumber();
        String json = HtmlUtils.htmlUnescape(purMrbForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> materialNameList = Lists.newArrayList();
        for (List<Object> row : data) {
            materialNameList.add(HandsontableUtils.getValue(row,0));
        }
        Map<String,String> materialNameMap = bdMaterialRepository.findByNameList(materialNameList).stream().collect(Collectors.toMap(BdMaterial::getFName,BdMaterial::getFNumber));
        PurMrbDto purMrbDto = new PurMrbDto();
        purMrbDto.setExtendType(ExtendTypeEnum.采购退料_K3.name());
        purMrbDto.setCreator(accountKingdeeBook.getUsername());
        purMrbDto.setDate(billDate);
        purMrbDto.setDepartmentNumber(departmentNumber);
        purMrbDto.setSupplierNumber(supplierNumber);
        for (List<Object> row : data){
            String materialName = HandsontableUtils.getValue(row,0);
            String priceStr = HandsontableUtils.getValue(row,1);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row,2));
            String note = HandsontableUtils.getValue(row,3);
            PurMrbFEntityDto purMrbFEntityDto = new PurMrbFEntityDto();
            purMrbFEntityDto.setMaterialNumber(materialNameMap.get(materialName));
            purMrbFEntityDto.setStockNumber(stockNumber);
            purMrbFEntityDto.setPrice(price);
            purMrbFEntityDto.setQty(qty);
            purMrbFEntityDto.setNote(note);
            purMrbDto.getEntityDtoList().add(purMrbFEntityDto);
        }
        return save(purMrbDto,kingdeeBook,accountKingdeeBook);
    }

    public KingdeeSynDto save(PurMrbDto purMrbDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(purMrbDto, kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    public PurMrbForm getForm(){
        PurMrbForm purMrbForm = new PurMrbForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("materialNameList",bdMaterialRepository.findAll().stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        purMrbForm.setExtra(map);
        return purMrbForm;
    }
}
