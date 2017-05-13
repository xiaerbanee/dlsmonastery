package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.service.ClientService;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.form.ClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "crm/client")
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

    @RequestMapping(value = "findForm")
    public ClientForm findOne(ClientForm clientForm) {
        clientForm=clientService.findForm(clientForm);
        return clientForm;
    }

}
