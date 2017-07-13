package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.sys.service.CacheService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/4/22.
 */
@RestController
@RequestMapping(value = "sys/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "init")
    public RestResponse init() {
        if(RequestUtils.getAdmin()){
            cacheService.init();
        }
        return new RestResponse("初始化缓存成功", ResponseCodeEnum.updated.name());
    }
}
