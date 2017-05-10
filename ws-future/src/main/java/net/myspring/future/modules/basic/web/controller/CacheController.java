package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/4/22.
 */
@RestController
@RequestMapping(value = "basic/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "init")
    public RestResponse init() {
        cacheService.init();
        return new RestResponse("初始化缓存成功", ResponseCodeEnum.updated.name());
    }
}
