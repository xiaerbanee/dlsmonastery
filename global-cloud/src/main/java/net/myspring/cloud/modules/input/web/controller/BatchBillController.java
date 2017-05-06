package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;
import net.myspring.cloud.modules.input.service.BatchBillService;
import net.myspring.cloud.modules.input.web.form.BatchBillForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lihx on 2017/4/25.
 */
@RestController
@RequestMapping(value = "input/batchBill")
public class BatchBillController {
    @Autowired
    private BatchBillService batchBillService;

    @RequestMapping(value = "form")
    public BatchBillForm form (BatchBillForm batchBillForm) {
        batchBillForm = batchBillService.getFormProperty(batchBillForm);
        return batchBillForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data, String storeCode, String billDateBTW) {
        RestResponse restResponse = new RestResponse();
        data = HtmlUtils.htmlUnescape(data);
        List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
        List<String> codeList = batchBillService.save(datas, storeCode, LocalDate.parse(billDateBTW));
        restResponse.setMessage("批量开单成功：" + codeList);
        return restResponse;
    }
}
