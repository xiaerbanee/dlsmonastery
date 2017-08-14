package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.service.AuditFileCollectService;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/auditFileCollect")
public class AuditFileCollectController {

    @Autowired
    private AuditFileCollectService auditFileCollectService;

    @RequestMapping(value = "collect")
    public RestResponse collect(String auditFileId,String accountFavoriteId,boolean collect){
        auditFileCollectService.collect(auditFileId,accountFavoriteId,collect);
        if(collect){
            return new RestResponse("收藏成功",null);
        }else {
            return new RestResponse("取消收藏成功",null);
        }
    }
}
