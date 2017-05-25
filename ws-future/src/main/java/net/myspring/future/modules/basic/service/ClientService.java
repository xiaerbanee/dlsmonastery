package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Client findOne(String id){
        Client client=clientRepository.findOne(id);
        return client;
    }

    public Page<ClientDto> findPage(Pageable pageable, ClientQuery clientQuery) {
        Page<ClientDto> page = clientRepository.findPage(pageable, clientQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

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

    public ClientDto findOne(ClientDto clientDto){
        if(!clientDto.isCreate()){
            Client client=clientRepository.findOne(clientDto.getId());
            clientDto= BeanUtil.map(client,ClientDto.class);
            cacheUtils.initCacheInput(clientDto);
       }
       return clientDto;
    }

    public void delete(ClientDto clientDto){
        clientRepository.logicDeleteOne(clientDto.getId());
    }

    public String getClientName(String depotId) {
        Client c = clientRepository.findByDepotId(depotId);
        return c.getName();
    }

    public List<ClientDto> findByNameLike(String name){
        List<ClientDto> clientDtoList= Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            List<Client> clientList=clientRepository.findByNameLike(name);
            clientDtoList=BeanUtil.map(clientList,ClientDto.class);
        }
        return clientDtoList;
    }
}
