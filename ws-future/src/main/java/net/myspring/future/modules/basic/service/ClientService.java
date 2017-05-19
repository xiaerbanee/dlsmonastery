package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.mapper.ClientMapper;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
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
    private DepotShopMapper depotShopMapper;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Client findOne(String id){
        Client client=clientMapper.findOne(id);
        return client;
    }

    public Page<ClientDto> findPage(Pageable pageable, ClientQuery clientQuery) {
        Page<ClientDto> page = clientMapper.findPage(pageable, clientQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public Client save(ClientForm clientForm) {
        Client client;
        if(clientForm.isCreate()) {
            client=BeanUtil.map(clientForm,Client.class);
            clientMapper.save(client);
            //保存depot
            Depot depot = new Depot();
            depot.setClientId(client.getId());
            depot.setName(client.getName());
            depot.setNamePinyin(StringUtils.getFirstSpell(depot.getName()));
            depotManager.save(depot);
            //保存depotShop
            DepotShop depotShop = new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopMapper.save(depotShop);

            depot.setDepotShopId(depotShop.getId());
            depotManager.save(depot);
        } else {
            client =clientMapper.findOne(clientForm.getId());
            ReflectionUtil.copyProperties(clientForm,client);
            clientMapper.update(client);
        }
        return client;
    }

    public ClientForm getFormProperty(ClientForm clientForm){
        if(!clientForm.isCreate()){
            Client client=clientMapper.findOne(clientForm.getId());
            clientForm= BeanUtil.map(client,ClientForm.class);
            cacheUtils.initCacheInput(clientForm);
       }
       return clientForm;
    }

    public String getClientName(String depotId) {
        Client c = clientMapper.findByDepotId(depotId);
        return c.getName();
    }

    public List<ClientDto> findByNameLike(String name){
        List<ClientDto> clientDtoList= Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            List<Client> clientList=clientMapper.findByNameLike(name);
            clientDtoList=BeanUtil.map(clientList,ClientDto.class);
        }
        return clientDtoList;
    }
}
