package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.modules.input.service.BatchMaterialService;
import net.myspring.cloud.modules.input.web.form.BatchMaterialForm;
import net.myspring.common.response.RestResponse;
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

    @RequestMapping(value = "form")
    public BatchMaterialForm form() {
        BatchMaterialForm batchMaterialForm = new BatchMaterialForm();
        batchMaterialForm = batchMaterialService.getFormProperty(batchMaterialForm);
        return batchMaterialForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data) {
        data = HtmlUtils.htmlUnescape(data);
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        List<String> codeList = batchMaterialService.save(datas)    ;
        return new RestResponse("物料添加成功："+codeList,null);
    }
}
