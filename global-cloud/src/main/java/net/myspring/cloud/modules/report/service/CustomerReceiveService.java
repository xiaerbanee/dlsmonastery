package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.repository.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.repository.CustomerReceiveRepository;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.dto.NameValueDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户应收报表
 * Created by liuj on 2017/5/11.
 */
@Service
@KingdeeDataSource
public class CustomerReceiveService {
    @Autowired
    private CustomerReceiveRepository customerReceiveRepository;
    @Autowired
    private BdCustomerRepository bdCustomerRepository;

    public List<CustomerReceiveDto>  findCustomerReceiveDtoList(CustomerReceiveQuery customerReceiveQuery) {
        LocalDate dateStart = LocalDateUtils.parse(customerReceiveQuery.getDateStart(),"yyyy-MM-dd");
        LocalDate dateEnd = LocalDateUtils.parse(customerReceiveQuery.getDateEnd(),"yyyy-MM-dd");
        List<String> customerIdList = customerReceiveQuery.getCustomerIdList();
        List<CustomerReceiveDto> beginList = customerReceiveRepository.findEndShouldGet(dateStart,customerIdList);
        List<CustomerReceiveDto> endList = customerReceiveRepository.findEndShouldGet(dateEnd,customerIdList);
        //期初结余
        Map<String,BigDecimal> beginMap = beginList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId, CustomerReceiveDto::getEndShouldGet));
        //期末结余
        Map<String,BigDecimal> endMap = endList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId, CustomerReceiveDto::getEndShouldGet));
        for(CustomerReceiveDto customerReceiveDto:beginList) {
            if(!customerIdList.contains(customerReceiveDto.getCustomerId())) {
                customerIdList.add(customerReceiveDto.getCustomerId());
            }
        }
        for(CustomerReceiveDto customerReceiveDto:endList) {
            if(!customerIdList.contains(customerReceiveDto.getCustomerId())) {
                customerIdList.add(customerReceiveDto.getCustomerId());
            }
        }
        List<BdCustomer> customerList =  bdCustomerRepository.findByIdList(customerIdList);
        List<CustomerReceiveDto> customerReceiveDtoList = Lists.newArrayList();
        for(BdCustomer bdCustomer:customerList) {
            CustomerReceiveDto customerReceiveDto = new CustomerReceiveDto();
            customerReceiveDto.setCustomerId(bdCustomer.getFCustId());
            customerReceiveDto.setBeginShouldGet(beginMap.get(bdCustomer.getFCustId()));
            customerReceiveDto.setEndShouldGet(endMap.get(bdCustomer.getFCustId()));
            customerReceiveDto.setCustomerName(bdCustomer.getFName());
            customerReceiveDto.setCustomerGroupName(bdCustomer.getFPrimaryGroupName());
            customerReceiveDtoList.add(customerReceiveDto);
        }
        if(customerReceiveQuery.getQueryDetail()) {
            CustomerReceiveDetailQuery customerReceiveDetailQuery = new CustomerReceiveDetailQuery();
            customerReceiveDetailQuery.setCustomerIdList(customerIdList);
            customerReceiveDetailQuery.setDateStart(customerReceiveQuery.getDateStart());
            customerReceiveDetailQuery.setDateEnd(customerReceiveQuery.getDateEnd());
            Map<String,List<CustomerReceiveDetailDto>> customerReceiveDetailMap =findCustomerReceiveDetailDtoMap(customerReceiveDetailQuery);
            for(CustomerReceiveDto customerReceiveDto:customerReceiveDtoList) {
                customerReceiveDto.setCustomerReceiveDetailDtoList(customerReceiveDetailMap.get(customerReceiveDto.getCustomerId()));
            }
        }
        return customerReceiveDtoList;
    }

    public List<CustomerReceiveDetailDto> findCustomerReceiveDetailDtoList(LocalDate dateStart,LocalDate dateEnd,String customerId) {
        CustomerReceiveDetailQuery customerReceiveDetailQuery = new CustomerReceiveDetailQuery();
        customerReceiveDetailQuery.setDateStart(LocalDateUtils.format(dateStart,"yyyy-MM-dd"));
        customerReceiveDetailQuery.setDateEnd(LocalDateUtils.format(dateEnd,"yyyy-MM-dd"));
        customerReceiveDetailQuery.getCustomerIdList().add(customerId);
        Map<String,List<CustomerReceiveDetailDto>> map = findCustomerReceiveDetailDtoMap(customerReceiveDetailQuery);
        return map.get(customerId);
    }

    public List<CustomerReceiveDetailDto> findCustomerReceiveDetailDtoList(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        List<CustomerReceiveDetailDto> detailDtoList = Lists.newArrayList();
        List<String> customerIdList = customerReceiveDetailQuery.getCustomerIdList();
        Map<String,List<CustomerReceiveDetailDto>> map = findCustomerReceiveDetailDtoMap(customerReceiveDetailQuery);
        for (String customerId : customerIdList){
            detailDtoList.addAll(map.get(customerId));
        }
        return detailDtoList;
    }

    //一个customerId对应List<CustomerReceiveDetailDto>
    public Map<String,List<CustomerReceiveDetailDto>>  findCustomerReceiveDetailDtoMap(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        LocalDate dateStart =  LocalDateUtils.parse(customerReceiveDetailQuery.getDateStart(),"yyyy-MM-dd");
        //期初应收
        List<CustomerReceiveDto> beginList = customerReceiveRepository.findEndShouldGet(dateStart,customerReceiveDetailQuery.getCustomerIdList());
        Map<String,BigDecimal> beginMap = beginList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId, CustomerReceiveDto::getEndShouldGet));
        //主单据列表(其他应收,销售出库 销售退货，收款，退款)
        List<CustomerReceiveDetailDto> customerReceiveDetailDtoMainList = customerReceiveRepository.findMainList(customerReceiveDetailQuery);
        //查找备注
        List<NameValueDto> remarksList = customerReceiveRepository.findRemarks(customerReceiveDetailQuery);
        Map<String,String> remarksMap = remarksList.stream().collect(Collectors.toMap(NameValueDto::getName,NameValueDto::getValue));
        for (CustomerReceiveDetailDto customerReceiveDetailDto: customerReceiveDetailDtoMainList) {
            if (remarksMap.containsKey(customerReceiveDetailDto.getBillNo())) {
                customerReceiveDetailDto.setRemarks(remarksMap.get(customerReceiveDetailDto.getBillNo()));
            }
        }
        //根据customerId组织成map
        Map<String, List<CustomerReceiveDetailDto>> mainMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(customerReceiveDetailDtoMainList)) {
            for (CustomerReceiveDetailDto customerReceiveDetailDto: customerReceiveDetailDtoMainList) {
                if (!mainMap.containsKey(customerReceiveDetailDto.getCustomerId())) {
                    mainMap.put(customerReceiveDetailDto.getCustomerId(), new ArrayList<>());
                }
                mainMap.get(customerReceiveDetailDto.getCustomerId()).add(customerReceiveDetailDto);
            }
        }
        //单据明细(根据billNo组织成map)
        List<CustomerReceiveDetailDto> customerReceiveDetailDtoDetailList = customerReceiveRepository.findDetailList(customerReceiveDetailQuery);
        Map<String, List<CustomerReceiveDetailDto>> detailMap =Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(customerReceiveDetailDtoDetailList)) {
            for (CustomerReceiveDetailDto customerReceiveDetailDto: customerReceiveDetailDtoDetailList) {
                if (!detailMap.containsKey(customerReceiveDetailDto.getBillNo())) {
                    detailMap.put(customerReceiveDetailDto.getBillNo(), new ArrayList<>());
                }
                detailMap.get(customerReceiveDetailDto.getBillNo()).add(customerReceiveDetailDto);
            }
        }
        //所有客户
        List<BdCustomer> bdCustomerList = bdCustomerRepository.findByIdList(customerReceiveDetailQuery.getCustomerIdList());
        Map<String,BdCustomer> bdCustomerMap = bdCustomerList.stream().collect(Collectors.toMap(BdCustomer::getFCustId,bdCustomer -> bdCustomer));
        Map<String,List<CustomerReceiveDetailDto>> result = Maps.newHashMap();
        if (mainMap.size()>0) {
            for(String customerId:mainMap.keySet()) {
                if(!result.containsKey(customerId)) {
                    result.put(customerId,Lists.newArrayList());
                }
                int index = 0;
                List<CustomerReceiveDetailDto> list = result.get(customerId);
                BigDecimal endShouldGet = beginMap.get(customerId);
                List<CustomerReceiveDetailDto> mainList = mainMap.get(customerId);

                CustomerReceiveDetailDto customerReceiveDetailDto= new CustomerReceiveDetailDto();
                customerReceiveDetailDto.setBillType(bdCustomerMap.get(customerId).getFName());
                customerReceiveDetailDto.setBillNo("客户编码：" + customerId);
                customerReceiveDetailDto.setIndex(index++);
                list.add(customerReceiveDetailDto);

                customerReceiveDetailDto = new CustomerReceiveDetailDto();
                customerReceiveDetailDto.setBillType("期初应收");
                customerReceiveDetailDto.setEndShouldGet(endShouldGet);
                customerReceiveDetailDto.setIndex(index++);
                list.add(customerReceiveDetailDto);

                BigDecimal totalShouldGet = BigDecimal.ZERO;
                for (int i = 0; i < mainList.size(); i++) {
                    customerReceiveDetailDto.setIndex(index++);
                    CustomerReceiveDetailDto main = mainList.get(i);
                    customerReceiveDetailDto = new CustomerReceiveDetailDto();
                    customerReceiveDetailDto.setBillStatus(main.getBillStatus());
                    customerReceiveDetailDto.setBillType(main.getBillType());
                    customerReceiveDetailDto.setBillDate(main.getBillDate());
                    customerReceiveDetailDto.setBillNo(main.getBillNo());
                    customerReceiveDetailDto.setRemarks(main.getRemarks());

                    if (main.getBillType().contains("收款单")) {
                        main.setRealGet(main.getTotalAmount());
                        endShouldGet = endShouldGet.subtract(main.getRealGet());
                        customerReceiveDetailDto.setRealGet(main.getRealGet());
                        customerReceiveDetailDto.setEndShouldGet(endShouldGet);
                        list.add(customerReceiveDetailDto);
                    } else if(main.getBillType().contains("现销")){
                        main.setShouldGet(main.getTotalAmount());
                        main.setRealGet(main.getTotalAmount());
                        customerReceiveDetailDto.setRealGet(main.getRealGet());
                        customerReceiveDetailDto.setShouldGet(main.getShouldGet());
                        list.add(customerReceiveDetailDto);
                    } else {
                        if(main.getBillType().contains("销售退货")){
                            main.setShouldGet(main.getTotalAmount().multiply(new BigDecimal(-1)));
                        }else{
                            main.setShouldGet(main.getTotalAmount());
                        }
                        endShouldGet = endShouldGet.add(main.getShouldGet());
                        customerReceiveDetailDto.setShouldGet(main.getShouldGet());
                        customerReceiveDetailDto.setEndShouldGet(endShouldGet);
                        list.add(customerReceiveDetailDto);
                    }
                    if (detailMap.containsKey(main.getBillNo())) {
                        for (CustomerReceiveDetailDto detail : detailMap.get(main.getBillNo())) {
                            detail.setIndex(index);
                            detail.setMain(false);
                            if(main.getBillType().contains("销售退货")){
                                detail.setQty(0L-detail.getQty());
                            }
                            list.add(detail);
                        }
                    }
                    if (main.getShouldGet() != null) {
                        totalShouldGet = totalShouldGet.add(main.getShouldGet());
                    }
                }
                customerReceiveDetailDto = new CustomerReceiveDetailDto();
                customerReceiveDetailDto.setShouldGet(totalShouldGet);
                customerReceiveDetailDto.setBillType("期末应收");
                customerReceiveDetailDto.setEndShouldGet(endShouldGet);
                list.add(customerReceiveDetailDto);
            }
        }
        return result;
    }

    public CustomerReceiveQuery getQuery(){
        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setSort("t1.fcustid,DESC");
        customerReceiveQuery.getExtra().put("customerGroupList",bdCustomerRepository.findPrimaryGroupAndPrimaryGroupName());
        return customerReceiveQuery;
    }
}
