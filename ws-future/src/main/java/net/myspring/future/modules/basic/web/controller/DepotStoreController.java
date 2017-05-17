package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.DepotStoreTypeEnum;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.service.DepotStoreService;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/5/12.
 */
@RestController
@RequestMapping(value = "basic/depotStore")
public class DepotStoreController {

    @Autowired
    private DepotStoreService depotStoreService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotStoreDto> list(Pageable pageable, DepotStoreQuery depotStoreQuery){
        Page<DepotStoreDto> page = depotStoreService.findPage(pageable,depotStoreQuery);
        return page;
    }

    @RequestMapping(value = "findForm")
    public DepotStoreForm findForm(DepotStoreForm depotStoreForm){
        depotStoreForm=depotStoreService.findForm(depotStoreForm);
        depotStoreForm.setDepotStoreTypeList(DepotStoreTypeEnum.getList());
        return depotStoreForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DepotStoreForm depotStoreForm){
        depotStoreService.save(depotStoreForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(DepotStoreForm depotStoreForm){
        depotStoreService.logicDeleteOne(depotStoreForm.getId());
        return new RestResponse("删除成功",null);
    }

}
