package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.service.ClientService;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.form.ClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "basic/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ClientDto> list(Pageable pageable, ClientQuery clientQuery) {
        Page<ClientDto> page = clientService.findPage(pageable, clientQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ClientForm clientForm) {
        clientService.save(clientForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ClientDto findOne(ClientDto clientDto) {
        clientDto=clientService.findOne(clientDto);
        return clientDto;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ClientDto clientDto) {
        clientService.delete(clientDto);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

//TODO 貨品訂貨時，名稱顯示不對，不能動態及時相應
    @RequestMapping(value = "getClientName")
    public String getClientName(String depotId) {
        return clientService.getClientName(depotId);
    }

    @RequestMapping(value = "search")
    public List<ClientDto> search(String name){
        List<ClientDto> clientDtoList=clientService.findByNameContaining(name);
        return clientDtoList;
    }

}
