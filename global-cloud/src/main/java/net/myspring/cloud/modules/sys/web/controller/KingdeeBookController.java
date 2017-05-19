package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
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

    @RequestMapping(method = RequestMethod.GET)
    public Page<KingdeeBookDto> list(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBookDto> page = kingdeeBookService.findPage(pageable,kingdeeBookQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public KingdeeBookQuery getQuery(){
        KingdeeBookQuery kingdeeBookQuery = kingdeeBookService.getQuery();
        return kingdeeBookQuery;
    }

    @RequestMapping(value = "getForm")
    public KingdeeBookForm getForm(KingdeeBookForm kingdeeBookForm){
         kingdeeBookForm = kingdeeBookService.getForm(kingdeeBookForm);
        return kingdeeBookForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        kingdeeBookService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(KingdeeBookForm kingdeeBookForm){
        kingdeeBookService.save(kingdeeBookForm);
        return new RestResponse("保存成功",null);
    }
}
