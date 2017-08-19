package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.dto.ReceivableDto;
import net.myspring.future.modules.basic.service.ClientService;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.DepotStoreService;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.query.ReceivableQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "basic/client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private DepotStoreService depotStoreService;
    @Autowired
    private DepotService depotService;

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
        depotStoreService.syn();
        return new RestResponse("同步成功",null);
    }

    @RequestMapping(value = "getReceivableQuery")
    public ReceivableQuery getReceivableQuery(ReceivableQuery receivableQuery) {
        LocalDate now = LocalDate.now();
        LocalDate dutyDateStart = now.minusDays(30);
        LocalDate dutyDateEnd = now.plusDays(30);
        receivableQuery.setDutyDateRange(LocalDateUtils.format(dutyDateStart) + " - "+LocalDateUtils.format(dutyDateEnd));

        return receivableQuery;
    }

    @RequestMapping(value = "findReceivableList")
    public Page<ReceivableDto> findReceivableList(Pageable pageable, ReceivableQuery receivableQuery) {
        if(receivableQuery.getDutyDateRange() == null || receivableQuery.getDutyDateStart()==null){
            throw new ServiceException("请选择查询日期区间");
        }

        receivableQuery.setDepotIdList(depotService.filterDepotIds());
        return clientService.findReceivableList(pageable, receivableQuery, false);
    }

    @RequestMapping(value="receivableExportDetail")
    public ModelAndView receivableExportDetail(ReceivableQuery receivableQuery) {
        receivableQuery.setDepotIdList(depotService.filterDepotIds());
        return new ModelAndView(new ExcelView(), "simpleExcelBook", clientService.receivableExportDetail(receivableQuery));
    }

    @RequestMapping(value="receivableExportAllDepots")
    public ModelAndView receivableExportAllDepots(ReceivableQuery receivableQuery) {
        receivableQuery.setDepotIdList(depotService.filterDepotIds());
        return new ModelAndView(new ExcelView(), "simpleExcelBook", clientService.receivableExportAllDepots(receivableQuery));
    }
}
