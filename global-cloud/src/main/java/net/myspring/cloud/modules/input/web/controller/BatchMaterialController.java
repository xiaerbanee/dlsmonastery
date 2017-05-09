package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.BatchMaterialService;
import net.myspring.cloud.modules.input.web.query.BatchMaterialQuery;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "input/batchMaterial")
public class BatchMaterialController {
    @Autowired
    private BatchMaterialService batchMaterialService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public BatchMaterialQuery form(BatchMaterialQuery batchMaterialQuery) {
        batchMaterialQuery = batchMaterialService.getFormProperty(batchMaterialQuery);
        return batchMaterialQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data) {
        KingdeeBook kingdeeBook = kingdeeBookService.getByCompanyId(SecurityUtils.getCompanyId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(SecurityUtils.getAccountId());
        data = HtmlUtils.htmlUnescape(data);
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        List<String> resultList = batchMaterialService.save(datas,kingdeeBook,accountKingdeeBook);
        return new RestResponse(""+resultList,null,true);
    }
}
