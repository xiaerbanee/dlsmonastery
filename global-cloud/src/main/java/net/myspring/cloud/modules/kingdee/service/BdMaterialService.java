package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.dto.BdMaterialDto;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.kingdee.web.form.BatchMaterialForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.NameValueDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.utils.HandsontableUtils;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdMaterialService {
    @Autowired
    private BdMaterialRepository bdMaterialRepository;
    @Autowired
    private KingdeeManager kingdeeManager;

    public BdMaterial findByName(String nameHtml){
        if (StringUtils.isNotBlank(nameHtml)){
            String name = HtmlUtils.htmlUnescape(nameHtml);
            BdMaterial bdMaterial = bdMaterialRepository.findByName(name);
            return bdMaterial;
        }
        return null;
    }

    public BdMaterial findByNumber(String numberHtml){
        if (StringUtils.isNotBlank(numberHtml)){
            String number = HtmlUtils.htmlUnescape(numberHtml);
            return bdMaterialRepository.findByNumber(number);
        }
        return null;
    }

    public List<BdMaterial> findAll(){
        return bdMaterialRepository.findAll();
    }

    public List<BdMaterial> findByMaxModifyDate(LocalDateTime modifyDate){
        return bdMaterialRepository.findByMaxModifyDate(modifyDate);
    }

    public BatchMaterialForm form(){
        BatchMaterialForm batchMaterialForm = new BatchMaterialForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("materialGroupNameList", bdMaterialRepository.findAllGroup().stream().map(NameValueDto::getName).collect(Collectors.toList()));
        map.put("materialCategoryNameList", bdMaterialRepository.findAllCategory().stream().map(NameValueDto::getName).collect(Collectors.toList()));
        batchMaterialForm.setExtra(map);
        return batchMaterialForm;
    }

    private KingdeeSynDto save(BdMaterialDto bdMaterialDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                bdMaterialDto.getExtendId(),
                bdMaterialDto.getExtendType(),
                KingdeeFormIdEnum.BD_MATERIAL.name(),
                bdMaterialDto.getJson(),
                kingdeeBook);
        kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("物料批量添加失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(BdMaterialDto bdMaterialDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(bdMaterialDto,kingdeeBook);
        }else{
            throw new ServiceException("登入金蝶系统失败，请检查您的账户密码是否正确");
        }
        return kingdeeSynDto;
    }

    @Transactional
    public List<KingdeeSynDto> save(List<BdMaterialDto> bdMaterialDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynExtendDtoList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(bdMaterialDtoList)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (BdMaterialDto bdMaterialDto : bdMaterialDtoList) {
                    KingdeeSynDto kingdeeSynExtendDto = save(bdMaterialDto,kingdeeBook);
                    kingdeeSynExtendDtoList.add(kingdeeSynExtendDto);
                }
            }
        }else{
            throw new ServiceException("登入金蝶系统失败，请检查您的账户密码是否正确");
        }
        return kingdeeSynExtendDtoList;
    }

    @Transactional
    public List<KingdeeSynDto> save(BatchMaterialForm batchMaterialForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        Map<String, BdMaterialDto> materialMap = Maps.newLinkedHashMap();
        String json = HtmlUtils.htmlUnescape(batchMaterialForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        Map<String, NameValueDto> materialCategoryNameMap = bdMaterialRepository.findAllCategory().stream().collect(Collectors.toMap(NameValueDto::getName,NameValueDto->NameValueDto));
        Map<String, NameValueDto> materialGroupNameMap = bdMaterialRepository.findAllGroup().stream().collect(Collectors.toMap(NameValueDto::getName,NameValueDto->NameValueDto));
        for (List<Object> row : data) {
            String materialNumber = HandsontableUtils.getValue(row, 0);
            String materialName = HandsontableUtils.getValue(row, 1);
            String priceStr = HandsontableUtils.getValue(row, 2);
            BigDecimal price1 = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            String RLPriceStr = HandsontableUtils.getValue(row,3);
            BigDecimal rlprice = StringUtils.isEmpty(RLPriceStr) ? BigDecimal.ZERO : new BigDecimal(RLPriceStr);
            String materialGroupName = HandsontableUtils.getValue(row, 4);
            String materialCategoryName = HandsontableUtils.getValue(row, 5);
            String billKey = materialNumber + CharConstant.COMMA + materialName;
            if (!materialMap.containsKey(billKey)) {
                BdMaterialDto bdMaterialDto = new BdMaterialDto();
                bdMaterialDto.setFName(materialName);
                bdMaterialDto.setFNumber(materialNumber);
                bdMaterialDto.setPrice1(price1);
                bdMaterialDto.setRlPrice(rlprice);
                bdMaterialDto.setFCategoryNumber(materialCategoryNameMap.get(materialCategoryName).getValue());
                bdMaterialDto.setFGroupNumber(materialGroupNameMap.get(materialGroupName).getValue());
                materialMap.put(billKey, bdMaterialDto);
            }

        }
        return save(Lists.newArrayList(materialMap.values()),kingdeeBook,accountKingdeeBook);
    }

}
