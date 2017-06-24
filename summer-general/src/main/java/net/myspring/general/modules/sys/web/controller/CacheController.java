package net.myspring.general.modules.sys.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.general.modules.sys.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "sys/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "init")
    public RestResponse init() {
        cacheService.init();
        return new RestResponse("初始化缓存成功", ResponseCodeEnum.updated.name());
    }
}
