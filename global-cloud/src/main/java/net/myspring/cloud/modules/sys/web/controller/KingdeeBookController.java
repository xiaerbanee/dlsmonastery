package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/kingdeeBook")
public class KingdeeBookController {
    @Autowired
    private KingdeeBookService kingdeeBookService;

    @RequestMapping(value = "page",method = RequestMethod.GET)
    public Page<KingdeeBookDto> list(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBookDto> page = kingdeeBookService.findPage(pageable,kingdeeBookQuery);
        return page;
    }

}
