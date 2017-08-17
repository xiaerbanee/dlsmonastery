package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.dto.ReceivableDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.query.ReceivableQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ClientService {

    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private OfficeClient officeClient;

    public Page<ClientDto> findPage(Pageable pageable, ClientQuery clientQuery) {
        Page<ClientDto> page = clientRepository.findPage(pageable, clientQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public Client save(ClientForm clientForm) {
        Client client;
        if(clientForm.isCreate()) {
            client=BeanUtil.map(clientForm,Client.class);
            clientRepository.save(client);
            //保存depot
            Depot depot = new Depot();
            depot.setClientId(client.getId());
            depot.setName(client.getName());
            depot.setNamePinyin(StringUtils.getFirstSpell(client.getName()));
            depotRepository.save(depot);
            //保存depotShop
            DepotShop depotShop = new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
        } else {
            client =clientRepository.findOne(clientForm.getId());
            ReflectionUtil.copyProperties(clientForm,client);
            clientRepository.save(client);
        }
        return client;
    }

    public ClientDto findOne(String id){
        ClientDto result = new ClientDto();
        if(StringUtils.isNotBlank(id)){
            Client client=clientRepository.findOne(id);
            result = BeanUtil.map(client,ClientDto.class);
            cacheUtils.initCacheInput(result);
       }
       return result;
    }

    @Transactional
    public void delete(String id){
        clientRepository.logicDelete(id);
    }

    public String getClientName(String depotId) {
        Client clientDto = clientRepository.findByDepotId(depotId);
        if(clientDto == null) {
            return null;
        }
        return clientDto.getName();
    }

    public List<ClientDto> findByNameContaining(String name){
        List<ClientDto> clientDtoList= Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            List<Client> clientList=clientRepository.findByNameContaining(name);
            clientDtoList=BeanUtil.map(clientList,ClientDto.class);
        }
        return clientDtoList;
    }

    @Transactional
    public void syn(){
        LocalDateTime outDate=clientRepository.findMaxOutDate();
        List<BdCustomer> bdCustomers=cloudClient.findCustomerByMaxModifyDate(outDate.minusHours(1).toString());
        if(CollectionUtil.isNotEmpty(bdCustomers)){
            List<Client> clientList=clientRepository.findByOutIdIn(CollectionUtil.extractToList(bdCustomers,"FCustId"));
            Map<String,Client> outIdClientMap=CollectionUtil.extractToMap(clientList,"outId");
            for(BdCustomer bdCustomer:bdCustomers){
                Client outClient=outIdClientMap.get(bdCustomer.getFCustId());
                if(outClient==null){
                    outClient=new Client();
                }
                outClient.setName(bdCustomer.getFName().trim());
                outClient.setOutId(bdCustomer.getFCustId());
                outClient.setOutGroupId(bdCustomer.getFPrimaryGroup());
                outClient.setOutGroupName(bdCustomer.getFPrimaryGroupName());
                outClient.setOutCode(bdCustomer.getFNumber());
                outClient.setOutDate(bdCustomer.getFModifyDate());
                outClient.setEnabled(true);
                clientRepository.save(outClient);
            }
        }
    }

    public Page<ReceivableDto> findReceivableList(Pageable pageable, ReceivableQuery receivableQuery, boolean queryDetail) {

        Page<ReceivableDto> page = clientRepository.findReceivableList(pageable, receivableQuery);
        cacheUtils.initCacheInput(page.getContent());

        List<OfficeDto> officeDtoList = officeClient.findAll();
        Map<String, OfficeDto> officeMap = CollectionUtil.extractToMap(officeDtoList, "id");
        for(ReceivableDto receivableDto : page.getContent()){
            receivableDto.setDepotOfficeNames(getOfficeNames(receivableDto.getDepotOfficeIds(), officeMap));
            receivableDto.setDepotAreaNames(getOfficeNames(receivableDto.getDepotAreaIds(), officeMap));
        }

        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setQueryDetail(queryDetail);
        if (StringUtils.isNotBlank(receivableQuery.getDutyDateRange())) {
            String[] tempParamValues = receivableQuery.getDutyDateRange().split(" - ");
            customerReceiveQuery.setDateStart(LocalDate.parse(tempParamValues[0]));
            customerReceiveQuery.setDateEnd(LocalDate.parse(tempParamValues[1]));
        }
        customerReceiveQuery.setCustomerIdList(CollectionUtil.extractToList(page.getContent(), "outId"));
        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        Map<String, CustomerReceiveDto> customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList, "customerId");

        for (ReceivableDto receivableDto : page.getContent()) {
            CustomerReceiveDto customerReceiveDto = customerReceiveDtoMap.get(receivableDto.getOutId());
            if (customerReceiveDto != null) {
                receivableDto.setCustomerReceiveDetailDtoList(customerReceiveDto.getCustomerReceiveDetailDtoList());
                receivableDto.setQcys(customerReceiveDto.getBeginShouldGet());
                receivableDto.setQmys(customerReceiveDto.getEndShouldGet());
            } else {
                receivableDto.setCustomerReceiveDetailDtoList(null);
                receivableDto.setQcys(BigDecimal.ZERO);
                receivableDto.setQmys(BigDecimal.ZERO);
            }

        }

        return page;
    }

    private String getOfficeNames(String officeIds, Map<String, OfficeDto> officeMap) {
        if(StringUtils.isBlank(officeIds)){
            return null;
        }
        List<String> officeNameList = new ArrayList<>();
        String[] officeIdList = officeIds.split(",");
        for(String officeId : officeIdList){
            officeNameList.add(officeMap.get(officeId).getName());
        }
        return StringUtils.join(officeNameList, ",");
    }

}
