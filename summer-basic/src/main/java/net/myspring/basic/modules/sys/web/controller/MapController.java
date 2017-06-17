package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017-03-02.
 */
@RestController
@RequestMapping(value = "sys/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @RequestMapping("getPoiList")
    public List<String> getPoiList(String longitude, String latitude)  {
        return mapService.getAddressList(longitude,latitude);
    }

}
