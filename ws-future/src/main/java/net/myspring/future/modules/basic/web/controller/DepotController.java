package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/15.
 */
@RestController
@RequestMapping(value = "basic/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;

    @RequestMapping(value = "search")
    public List<DepotDto> search(DepotQuery depotQuery) {
        return depotService.findList(depotQuery);
    }

    //直营门店查询
    @RequestMapping(value = "direct")
    public List<DepotDto>  direct(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(false);
        return depotService.findList(depotQuery);
    }

    //代理门店查询
    @RequestMapping(value = "delegate")
    public List<DepotDto>  delegate(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(true);
        return depotService.findList(depotQuery);
    }

    @RequestMapping(value = "findByIds")
    public List<DepotDto> findByListIds(List<String> ids) {
        List<DepotDto> depotDtoList =depotService.findByIds(ids);
        return depotDtoList;
    }
}
