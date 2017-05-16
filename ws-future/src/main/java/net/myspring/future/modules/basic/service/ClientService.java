package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.mapper.ClientMapper;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

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
        Client client=BeanUtil.map(clientForm,Client.class);
        clientMapper.save(client);
        return client;
    }

    public ClientForm findForm(ClientForm clientForm){
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
}
