package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.service.ClientService;
import net.myspring.future.modules.basic.service.DepotStoreService;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "basic/client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private DepotStoreService depotStoreService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ClientDto> list(Pageable pageable, ClientQuery clientQuery) {
        return clientService.findPage(pageable, clientQuery);
    }

    @RequestMapping(value = "getQuery")
    public ClientQuery getQuery(ClientQuery clientQuery){
        return clientQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ClientForm clientForm) {
        clientService.save(clientForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ClientDto findOne(String id) {
        return clientService.findOne(id);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        clientService.delete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

//TODO 貨品訂貨時，名稱顯示不對，不能動態及時相應
    @RequestMapping(value = "getClientName")
    public String getClientName(String depotId) {
        return clientService.getClientName(depotId);
    }

    @RequestMapping(value = "search")
    public List<ClientDto> search(String name){
        return clientService.findByNameContaining(name);
    }

    @RequestMapping(value = "getForm")
    public ClientForm getForm(ClientForm clientForm){
        return clientForm;
    }

    @RequestMapping(value = "syn")
    public RestResponse syn(){
        clientService.syn();
//        depotStoreService.syn();
        return new RestResponse("同步成功",null);
    }

}
