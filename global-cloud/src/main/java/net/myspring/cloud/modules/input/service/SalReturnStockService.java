package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;
import net.myspring.cloud.common.enums.SalReturnStockBillTypeEnum;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockFEntityDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.BatchBillForm;
import net.myspring.cloud.modules.input.web.query.BatchBillQuery;
import net.myspring.cloud.modules.kingdee.domain.ArReceivable;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.mapper.ArReceivableMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdDepartmentMapper;
import net.myspring.cloud.modules.kingdee.mapper.BdMaterialMapper;
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
 * 退货单
 * Created by liuj on 2017/5/11.
 */
@Service
@KingdeeDataSource
public class SalReturnStockService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private ArReceivableMapper arReceivableMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdDepartmentMapper bdDepartmentMapper;
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public KingdeeSynExtendDto save(SalReturnStockDto salReturnStockDto, KingdeeBook kingdeeBook) {
        KingdeeSynExtendDto kingdeeSynExtendDto = new KingdeeSynExtendDto(
                KingdeeFormIdEnum.SAL_RETURNSTOCK.name(),
                salReturnStockDto.getJson(),
                kingdeeBook,
                KingdeeFormIdEnum.AR_receivable.name()) {
            @Override
            public String getNextBillNo() {
                if(salReturnStockDto.getBillType().contains("现销")){
                    return null;
                }else{
                    List<ArReceivable> arReceivableList = arReceivableMapper.findBySourceBillNo(getBillNo());
                    if (arReceivableList.size()>0){
                        return arReceivableList.get(0).getFBillNo();
                    }else {
                        return "";
                    }

                }
            }
        };
        kingdeeManager.save(kingdeeSynExtendDto);
        return kingdeeSynExtendDto;
    }


    public List<KingdeeSynExtendDto> save (BatchBillForm batchBillForm,KingdeeBook kingdeeBook,AccountKingdeeBook accountKingdeeBook) {
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = Lists.newArrayList();
        String storeNumber = batchBillForm.getStoreNumber();
        LocalDate date = batchBillForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(batchBillForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        List<String> customerNameList = Lists.newArrayList();
        for (List<Object> row : data){
            customerNameList.add(HandsontableUtils.getValue(row,1));
        }
        Map<String, String> customerNumMap = Maps.newHashMap();
        Map<String, String> customerDepartmentMap = Maps.newHashMap();

        List<String> departmentIdList = Lists.newArrayList();
        for (BdCustomer bdCustomer : bdCustomerMapper.findByNameList(customerNameList)) {
            customerNumMap.put(bdCustomer.getFName(), bdCustomer.getFNumber());
            customerDepartmentMap.put(bdCustomer.getFName(), bdCustomer.getFSalDeptId());
            departmentIdList.add(bdCustomer.getFSalDeptId());
        }
        List<BdDepartment> bdDepartmentList = bdDepartmentMapper.findByIdList(departmentIdList);
        Map<String,BdDepartment> bdDepartmentMap = bdDepartmentList.stream().collect(Collectors.toMap(BdDepartment::getFDeptId, bdDepartment -> bdDepartment));
        Map<String, SalReturnStockDto> billMap = Maps.newLinkedHashMap();
        for (List<Object> row : data) {
            String materialNumber = HandsontableUtils.getValue(row,0);
            String customerName = HandsontableUtils.getValue(row,1);
            String priceStr = HandsontableUtils.getValue(row, 3);
            BigDecimal price = StringUtils.isEmpty(priceStr) ? BigDecimal.ZERO : new BigDecimal(priceStr);
            Integer qty = Integer.valueOf(HandsontableUtils.getValue(row,4));
            String billType = HandsontableUtils.getValue(row,5);
            String remarks = HandsontableUtils.getValue(row,6);

            SalReturnStockFEntityDto salReturnStockFEntityDto = new SalReturnStockFEntityDto();
            salReturnStockFEntityDto.setMaterialNumber(materialNumber);
            salReturnStockFEntityDto.setPrice(price);
            salReturnStockFEntityDto.setQty(qty);
            salReturnStockFEntityDto.setEntryNote(remarks);

            String billKey = customerNumMap.get(customerName) + CharConstant.COMMA + billType;
            if (!billMap.containsKey(billKey)) {
                SalReturnStockDto salReturnStockDto = new SalReturnStockDto();
                salReturnStockDto.setCreator(accountKingdeeBook.getUsername());
                salReturnStockDto.setDate(date);
                salReturnStockDto.setStoreNumber(storeNumber);
                salReturnStockDto.setDepartmentNumber(bdDepartmentMap.get(customerDepartmentMap.get(customerName)).getFNumber());
                salReturnStockDto.setBillType(billType);
                salReturnStockDto.setCustomerNumber(customerNumMap.get(customerName));
                salReturnStockDto.setNote(remarks);
                billMap.put(billKey, salReturnStockDto);
            }
            billMap.get(billKey).getSalReturnStockFEntityDtoList().add(salReturnStockFEntityDto);
        }

        List<SalReturnStockDto> batchBills = Lists.newArrayList(billMap.values());
        //财务出库开单
        if (CollectionUtil.isNotEmpty(batchBills)) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                for (SalReturnStockDto batchBill : batchBills) {
                    KingdeeSynExtendDto kingdeeSynExtendDto = save(batchBill, kingdeeBook);
                    kingdeeSynExtendDtoList.add(kingdeeSynExtendDto);
                }
            }
        }
        return kingdeeSynExtendDtoList;
    }

    public BatchBillQuery getFormProperty(BatchBillQuery batchBillQuery){
        batchBillQuery.setReturnStockBillTypeEnums(SalReturnStockBillTypeEnum.values());
        List<String> customerNameList = bdCustomerMapper.findAll().stream().map(BdCustomer::getFName).collect(Collectors.toList());
        List<String> materialNameList = bdMaterialMapper.findAll().stream().map(BdMaterial::getFName).collect(Collectors.toList());
        batchBillQuery.setBdCustomerNameList(customerNameList);
        batchBillQuery.setBdMaterialNameList(materialNameList);
        return batchBillQuery;
    }

}
