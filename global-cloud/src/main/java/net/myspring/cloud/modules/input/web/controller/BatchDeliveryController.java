package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.modules.input.service.BatchDeliveryService;
import net.myspring.cloud.modules.input.web.form.BatchDeliveryForm;
import net.myspring.cloud.modules.input.web.query.BatchDeliveryQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihx on 2017/5/8.
 */
@RestController
@RequestMapping(value = "input/batchDelivery")
public class BatchDeliveryController {
    @Autowired
    private BatchDeliveryService batchDeliveryService;

    @RequestMapping(value = "form")
    public BatchDeliveryQuery form (BatchDeliveryQuery batchDeliveryQuery) {
        batchDeliveryQuery = batchDeliveryService.getFormProperty(batchDeliveryQuery);
        return batchDeliveryQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(BatchDeliveryForm batchDeliveryForm) {
        String data = HtmlUtils.htmlUnescape(batchDeliveryForm.getData());
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        String departmentNumber = batchDeliveryForm.getDepartmentNumber();
        String billDate = batchDeliveryForm.getBillDate();
        List<String> codeList = batchDeliveryService.save(datas, departmentNumber, LocalDate.parse(billDate));
        return new RestResponse("其他出库单保存成功：" + codeList,null,true);
    }
}
