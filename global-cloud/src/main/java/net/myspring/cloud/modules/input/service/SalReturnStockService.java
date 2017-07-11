package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.enums.SalReturnStockBillTypeEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockFEntityDto;
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
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
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
 * 销售退货单
 * Created by liuj on 2017/5/11.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class SalReturnStockService {
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

    private KingdeeSynExtendDto save(SalReturnStockDto salReturnStockDto, KingdeeBook kingdeeBook) {
        KingdeeSynExtendDto kingdeeSynExtendDto = new KingdeeSynExtendDto(
                salReturnStockDto.getExtendId(),
                salReturnStockDto.getExtendType(),
                KingdeeFormIdEnum.SAL_RETURNSTOCK.name(),
                salReturnStockDto.getJson(),
                kingdeeBook,
                KingdeeFormIdEnum.AR_receivable.name());
        kingdeeManager.save(kingdeeSynExtendDto);
        if (!kingdeeSynExtendDto.getSuccess()){
            throw new ServiceException("销售退货单："+kingdeeSynExtendDto.getResult());
        }
        return kingdeeSynExtendDto;
    }

    @Transactional
    public List<KingdeeSynReturnDto> save (List<SalReturnStockDto> salReturnStockDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = Lists.newArrayList();
        //财务出库开单
        if (CollectionUtil.isNotEmpty(salReturnStockDtoList)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (SalReturnStockDto salReturnStockDto : salReturnStockDtoList) {
                    if (SalReturnStockBillTypeEnum.标准销售退货单.name().equals(salReturnStockDto.getBillType())) {
                        salReturnStockDto.setFBillTypeIdNumber("XSTHD01_SYS");
                    } else if(SalReturnStockBillTypeEnum.现销退货单.name().equals(salReturnStockDto.getBillType())){
                        salReturnStockDto.setFBillTypeIdNumber("XSTHD06_SYS");
                    }
                    KingdeeSynExtendDto kingdeeSynExtendDto = save(salReturnStockDto, kingdeeBook);
                    kingdeeSynExtendDtoList.add(kingdeeSynExtendDto);
                }
            }
        }
        return BeanUtil.map(kingdeeSynExtendDtoList,KingdeeSynReturnDto.class);
    }

    @Transactional
    public List<KingdeeSynReturnDto> save (SalStockForm salStockForm, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook) {
        String storeNumber = salStockForm.getStockNumber();
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
        Map<String, SalReturnStockDto> billMap = Maps.newLinkedHashMap();
        for (List<Object> row : data) {
            String materialNumber = HandsontableUtils.getValue(row,0);
            String customerName = HandsontableUtils.getValue(row,1);
            String priceStr = HandsontableUtils.getValue(row, 3);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row,4));
            String remarks = HandsontableUtils.getValue(row,5);
            String billType = HandsontableUtils.getValue(row,6);

            SalReturnStockFEntityDto salReturnStockFEntityDto = new SalReturnStockFEntityDto();
            salReturnStockFEntityDto.setMaterialNumber(materialNumber);
            salReturnStockFEntityDto.setPrice(price);
            salReturnStockFEntityDto.setQty(qty);
            salReturnStockFEntityDto.setEntryNote(remarks);
            salReturnStockFEntityDto.setStockNumber(storeNumber);

            String billKey = customerNumMap.get(customerName) + CharConstant.COMMA + billType;
            if (!billMap.containsKey(billKey)) {
                SalReturnStockDto salReturnStockDto = new SalReturnStockDto();
                salReturnStockDto.setCreator(accountKingdeeBook.getUsername());
                salReturnStockDto.setDate(date);
                salReturnStockDto.setDepartmentNumber(bdDepartmentMap.get(customerDepartmentMap.get(customerName)).getFNumber());
                salReturnStockDto.setBillType(billType);
                salReturnStockDto.setCustomerNumber(customerNumMap.get(customerName));
                salReturnStockDto.setNote(remarks);
                billMap.put(billKey, salReturnStockDto);
            }
            billMap.get(billKey).getSalReturnStockFEntityDtoList().add(salReturnStockFEntityDto);
        }

        List<SalReturnStockDto> salReturnStockDtoList = Lists.newArrayList(billMap.values());
        return save(salReturnStockDtoList,kingdeeBook,accountKingdeeBook);
    }

    @Transactional
    public List<KingdeeSynReturnDto> saveForXSTHD (List<SalReturnStockDto> salReturnStockDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<String> customerNumberList = Lists.newArrayList();
        for (SalReturnStockDto salReturnStockDto  : salReturnStockDtoList){
            customerNumberList.add(salReturnStockDto.getCustomerNumber());
        }
        Map<String, String> customerDepartmentMap = Maps.newHashMap();
        List<String> departmentIdList = Lists.newArrayList();
        for (BdCustomer bdCustomer : bdCustomerRepository.findByNumberList(customerNumberList)) {
            customerDepartmentMap.put(bdCustomer.getFNumber(), bdCustomer.getFSalDeptId());
            departmentIdList.add(bdCustomer.getFSalDeptId());
        }
        List<BdDepartment> bdDepartmentList = bdDepartmentRepository.findByIdList(departmentIdList);
        Map<String,BdDepartment> bdDepartmentMap = bdDepartmentList.stream().collect(Collectors.toMap(BdDepartment::getFDeptId, bdDepartment -> bdDepartment));
        for (SalReturnStockDto returnStockDto : salReturnStockDtoList){
           returnStockDto.setCreator(accountKingdeeBook.getUsername());
           returnStockDto.setBillType(SalReturnStockBillTypeEnum.标准销售退货单.name());
           returnStockDto.setDepartmentNumber(bdDepartmentMap.get(customerDepartmentMap.get(returnStockDto.getCustomerNumber())).getFNumber());
        }
        return save(salReturnStockDtoList,kingdeeBook,accountKingdeeBook);
    }
    
    public SalStockForm getForm(KingdeeBook kingdeeBook){
        SalStockForm salStockForm = new SalStockForm();
        Map<String,Object> map = Maps.newHashMap();
        if (KingdeeNameEnum.WZOPPO.name().equals(kingdeeBook.getName())){
            map.put("returnStockBillTypeEnums",SalReturnStockBillTypeEnum.values());
        }else{
            map.put("returnStockBillTypeEnums",SalReturnStockBillTypeEnum.标准销售退货单);
        }
        map.put("bdCustomerNameList",bdCustomerRepository.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList()));
        map.put("bdMaterialNameList",bdMaterialRepository.findAll().stream().map(BdMaterial::getFName).collect(Collectors.toList()));
        salStockForm.setExtra(map);
        return salStockForm;
    }

}
