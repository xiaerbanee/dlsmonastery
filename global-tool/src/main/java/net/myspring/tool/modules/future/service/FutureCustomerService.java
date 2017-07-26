package net.myspring.tool.modules.future.service;

import com.google.common.collect.Maps;
import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.future.dto.CustomerDto;
import net.myspring.tool.modules.future.repository.FutureCustomerRepository;
import net.myspring.tool.modules.vivo.domain.SStores;
import net.myspring.tool.modules.vivo.dto.SCustomerDto;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@FutureDataSource
@Transactional(readOnly = true)
public class FutureCustomerService {
    @Autowired
    private FutureCustomerRepository futureCustomerRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public List<SCustomerDto> getVivoCustomersData(String date){
        List<SCustomerDto> sCustomerDtoList = futureCustomerRepository.findVivoCustomers(LocalDateUtils.parse(date));
        cacheUtils.initCacheInput(sCustomerDtoList);
        return sCustomerDtoList;
    }

    public List<SCustomerDto> getIDVivoCustomersData(String date){
        List<SCustomerDto> sCustomerDtoList = futureCustomerRepository.findIDVivoCustomers(LocalDateUtils.parse(date));
        cacheUtils.initCacheInput(sCustomerDtoList);
        return sCustomerDtoList;
    }

    public List<CustomerDto> getOppoCustomers(){
        List<CustomerDto> customerDtoList = futureCustomerRepository.findOppoCustomers();
        return customerDtoList;
    }

    public Map<String,CustomerDto> getCustomerDtoMap(){
        List<CustomerDto> customerDtoList = getOppoCustomers();

        Map<String,CustomerDto> areaDepotMap = Maps.newHashMap();
        for (CustomerDto customerDto : customerDtoList){
            if (!areaDepotMap.containsKey(customerDto.getDepotId())){
                areaDepotMap.put(customerDto.getDepotId(),customerDto);
            }
        }

        return areaDepotMap;
    }

    public List<SStores> findIDvivoStore(){
        List<SStores> sStoresList = futureCustomerRepository.findIDvivoStore();
        cacheUtils.initCacheInput(sStoresList);
        return sStoresList;
    }

    public Map<String,String> getAreaDepotMap(){
        List<CustomerDto> customerDtoList = getOppoCustomers();

        Map<String,String> areaDepotMap = Maps.newHashMap();
        for (CustomerDto customerDto : customerDtoList){
            if(isArea(customerDto)){
                String areaId=customerDto.getAreaId();
                String depotId=customerDto.getDepotId();
                if(!areaDepotMap.containsKey(areaId)){
                    areaDepotMap.put(areaId,depotId);
                }
            }
        }

        return areaDepotMap;
    }

    private boolean isArea(CustomerDto customerDto){
        String jointLeavel=customerDto.getJointLeavel();
        String areaId=customerDto.getAreaId();
        String storeId=customerDto.getStoreId();
        if(StringUtils.isNotBlank(storeId)&&StringUtils.isNotBlank(areaId)&&"二级".equals(jointLeavel)){
            return true;
        }else{
            return false;
        }
    }

}
