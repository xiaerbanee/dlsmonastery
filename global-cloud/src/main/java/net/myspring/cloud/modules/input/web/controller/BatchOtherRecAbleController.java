package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.modules.input.service.BatchOtherRecAbleService;
import net.myspring.cloud.modules.input.web.form.BatchOtherRecAbleForm;
import net.myspring.cloud.modules.input.web.query.BatchOtherRecAbleQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "input/batchOtherRecAble")
public class BatchOtherRecAbleController {
    @Autowired
    private BatchOtherRecAbleService batchOtherRecAbleService;

    @RequestMapping(value = "form")
    public BatchOtherRecAbleQuery form(BatchOtherRecAbleQuery batchOtherRecAbleQuery) {
        return batchOtherRecAbleQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(BatchOtherRecAbleForm batchOtherRecAbleForm) {
        String data = HtmlUtils.htmlUnescape(batchOtherRecAbleForm.getData());
        List<List<String>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        LocalDate billDate = LocalDate.parse(batchOtherRecAbleForm.getBillDate());
        List<String> codeList = batchOtherRecAbleService.save(datas, billDate);
        return new RestResponse("其他应收单保存成功：" +codeList,null,true);
    }
}
