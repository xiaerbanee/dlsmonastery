package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeExtendTypeEnum;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.dto.SalOutStockFEntityDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.SalStockForm;
import net.myspring.cloud.modules.kingdee.domain.ArReceivable;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.repository.ArReceivableRepository;
import net.myspring.cloud.modules.kingdee.repository.BdCustomerRepository;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售入库单
 * Created by liuj on 2017/5/11.
 */
@Service
@KingdeeDataSource
public class SalOutStockService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private ArReceivableRepository arReceivableRepository;
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    private KingdeeSynExtendDto save(SalOutStockDto salOutStockDto,KingdeeBook kingdeeBook) {
        KingdeeSynExtendDto kingdeeSynExtendDto = new KingdeeSynExtendDto(
                salOutStockDto.getExtendId(),
                salOutStockDto.getExtendType(),
                KingdeeFormIdEnum.SAL_OUTSTOCK.name(),
                salOutStockDto.getJson(),
                kingdeeBook,
                KingdeeFormIdEnum.AR_receivable.name()) {
            @Override
            public String getNextBillNo() {
                if(salOutStockDto.getBillTypeK3().contains("现销")){
                    return null;
                }else{
                    ArReceivable receivable =arReceivableRepository.findTopOneBySourceBillNo(getBillNo());
                    return  receivable.getFBillNo();
                }

            }
        };
        kingdeeManager.save(kingdeeSynExtendDto);
        return kingdeeSynExtendDto;
    }

    public List<KingdeeSynExtendDto> save (SalStockForm salStockForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        String storeNumber = salStockForm.getStoreNumber();
        LocalDate date = salStockForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(salStockForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> customerNameList = Lists.newArrayList();
        for (List<Object> row : data){
            customerNameList.add(HandsontableUtils.getValue(row,1));
        }
        Map<String, String> customerNumMap = Maps.newHashMap();
        Map<String, String> customerDepartmentMap = Maps.newHashMap();

        List<String> departmentIdList = Lists.newArrayList();
        for (BdCustomer bdCustomer : bdCustomerRepository.findByNameList(customerNameList)) {
            customerNumMap.put(bdCustomer.getFName(), bdCustomer.getFNumber());
            customerDepartmentMap.put(bdCustomer.getFName(), bdCustomer.getFSalDeptId());
            departmentIdList.add(bdCustomer.getFSalDeptId());
        }
        List<BdDepartment> bdDepartmentList = bdDepartmentRepository.findByIdList(departmentIdList);
        Map<String,BdDepartment> bdDepartmentMap = bdDepartmentList.stream().collect(Collectors.toMap(BdDepartment::getFDeptId, bdDepartment -> bdDepartment));
        Map<String, SalOutStockDto> billMap = Maps.newLinkedHashMap();
        for (List<Object> row : data) {
            String materialNumber = HandsontableUtils.getValue(row,0);
            String customerName = HandsontableUtils.getValue(row,1);
            String priceStr = HandsontableUtils.getValue(row, 3);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row,4));
            String billType = HandsontableUtils.getValue(row,5);
            String remarks = HandsontableUtils.getValue(row,6);

            SalOutStockFEntityDto salOutStockFEntityDto = new SalOutStockFEntityDto();
            salOutStockFEntityDto.setStoreNumber(storeNumber);
            salOutStockFEntityDto.setMaterialNumber(materialNumber);
            salOutStockFEntityDto.setPrice(price);
            salOutStockFEntityDto.setQty(qty);
            salOutStockFEntityDto.setEntryNote(remarks);

            String billKey = customerNumMap.get(customerName) + CharConstant.COMMA + billType;
            if (!billMap.containsKey(billKey)) {
                SalOutStockDto salOutStockDto = new SalOutStockDto();
                salOutStockDto.setCreatorK3(accountKingdeeBook.getUsername());
                salOutStockDto.setDate(date);
                salOutStockDto.setDepartmentNumber(bdDepartmentMap.get(customerDepartmentMap.get(customerName)).getFNumber());
                salOutStockDto.setBillTypeK3(billType);
                salOutStockDto.setCustomerNumber(customerNumMap.get(customerName));
                salOutStockDto.setNote(remarks);
                billMap.put(billKey, salOutStockDto);
            }
            billMap.get(billKey).getSalOutStockFEntityDtoList().add(salOutStockFEntityDto);
        }

        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList(billMap.values());
        return save(salOutStockDtoList,kingdeeBook,accountKingdeeBook);
    }

    public List<KingdeeSynExtendDto> save (List<SalOutStockDto> salOutStockDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = Lists.newArrayList();
        //财务出库开单
        if (CollectionUtil.isNotEmpty(salOutStockDtoList)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (SalOutStockDto salOutStockDto : salOutStockDtoList) {
                    KingdeeSynExtendDto kingdeeSynExtendDto = save(salOutStockDto,kingdeeBook);
                    kingdeeSynExtendDtoList.add(kingdeeSynExtendDto);
                }
            }
        }
        return kingdeeSynExtendDtoList;
    }

    //柜台订货
    public List<KingdeeSynExtendDto> saveForAdApply (List<SalOutStockDto> salOutStockDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(salOutStockDtoList)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (SalOutStockDto salOutStockDto : salOutStockDtoList) {
                    if (StringUtils.isBlank(salOutStockDto.getExtendType())){
                        salOutStockDto.setExtendType(KingdeeExtendTypeEnum.柜台订货.name());
                    }
                    salOutStockDto.setCreatorK3(accountKingdeeBook.getUsername());
                    salOutStockDto.setBillTypeK3(SalOutStockBillTypeEnum.标准销售出库单.name());
                    KingdeeSynExtendDto kingdeeSynExtendDto = save(salOutStockDto,kingdeeBook);
                    kingdeeSynExtendDtoList.add(kingdeeSynExtendDto);
                }
            }
        }
        return kingdeeSynExtendDtoList;
    }

    public SalStockForm getForm(){
        SalStockForm salStockForm = new SalStockForm();
        Map<String,Object> map = Maps.newHashMap();
        map.put("outStockBillTypeEnums",SalOutStockBillTypeEnum.values());
        map.put("bdCustomerNameList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        map.put("bdMaterialNameList",bdMaterialRepository.findAll().stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        salStockForm.setExtra(map);
        return salStockForm;
    }

}
