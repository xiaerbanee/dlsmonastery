package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.cloud.common.enums.CustomerReceiveEnum;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.mapper.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.mapper.CustomerReceiveMapper;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liuj on 2017/5/11.
 */
@Service
public class CustomerReceiveService {
    @Autowired
    private CustomerReceiveMapper customerReceiveMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;

    public List<CustomerReceiveDto>  findCustomerReceiveDtoList(CustomerReceiveQuery customerReceiveQuery) {
        LocalDate dateStart = customerReceiveQuery.getDateStart();
        LocalDate dateEnd = customerReceiveQuery.getDateEnd();
        List<CustomerReceiveDto> beginList = customerReceiveMapper.findEndShouldGet(dateStart,customerReceiveQuery.getCustomerIdList());
        List<CustomerReceiveDto> endList = customerReceiveMapper.findEndShouldGet(dateEnd,customerReceiveQuery.getCustomerIdList());
        //期初结余
        Map<String,BigDecimal> beginMap = beginList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId, CustomerReceiveDto::getEndShouldGet));
        //期末结余
        Map<String,BigDecimal> endMap = endList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId, CustomerReceiveDto::getEndShouldGet));
        List<String> customerIdList = Lists.newArrayList();
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
        List<BdCustomer> customerList =  bdCustomerMapper.findByIdList(customerIdList);
        List<CustomerReceiveDto> customerReceiveDtoList = Lists.newArrayList();
        for(BdCustomer bdCustomer:customerList) {
            CustomerReceiveDto customerReceiveDto = new CustomerReceiveDto();
            customerReceiveDto.setBeginShouldGet(beginMap.get(bdCustomer.getFCustId()));
            customerReceiveDto.setEndShouldGet(endMap.get(bdCustomer.getFCustId()));
            customerReceiveDto.setCustomerName(bdCustomer.getFName());
            customerReceiveDto.setCustomerGroupName(bdCustomer.getFPrimaryGroupName());
        }
        if(customerReceiveQuery.getQueryDetail()) {
            CustomerReceiveDetailQuery customerReceiveDetailQuery = new CustomerReceiveDetailQuery();
            customerReceiveDetailQuery.setCustomerIdList(customerIdList);
            customerReceiveDetailQuery.setDateRange(customerReceiveQuery.getDateRange());
            Map<String,List<CustomerReceiveDetailDto>> customerReceiveDetailMap =findCustomerReceiveDetailDtoMap(customerReceiveDetailQuery);
            for(CustomerReceiveDto customerReceiveDto:customerReceiveDtoList) {
                customerReceiveDto.setCustomerReceiveDetailDtoList(customerReceiveDetailMap.get(customerReceiveDto.getCustomerId()));
            }
        }
        return customerReceiveDtoList;
    }

    public Map<String,List<CustomerReceiveDetailDto>>  findCustomerReceiveDetailDtoMap(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        LocalDate dateStart = customerReceiveDetailQuery.getDateStart();
        LocalDate dateEnd = customerReceiveDetailQuery.getDateEnd();
        Map<String,List<CustomerReceiveDetailDto>> dataMap= Maps.newHashMap();





        return dataMap;
    }

}
