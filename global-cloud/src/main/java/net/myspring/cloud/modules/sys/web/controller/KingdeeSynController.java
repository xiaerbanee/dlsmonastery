package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.service.KingdeeSynService;
import net.myspring.cloud.modules.sys.web.query.KingdeeSynQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/6/21.
 */
@RestController
@RequestMapping(value = "sys/kingdeeSyn")
public class KingdeeSynController {
    @Autowired
    private KingdeeSynService kingdeeSynService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<KingdeeSyn> list(Pageable pageable, KingdeeSynQuery kingdeeSynQuery){
        Page<KingdeeSyn> page = kingdeeSynService.findPage(pageable,kingdeeSynQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public KingdeeSynQuery getQuery(){
        KingdeeSynQuery kingdeeSynQuery = new KingdeeSynQuery();
        kingdeeSynQuery.getExtra().put("extendTypeList", ExtendTypeEnum.values());
        return kingdeeSynQuery;
    }
}
