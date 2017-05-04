package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.service.BatchBillService;
import net.myspring.cloud.modules.input.service.BdCustomerService;
import net.myspring.cloud.modules.input.service.BdMaterialService;
import net.myspring.cloud.modules.input.service.BdStockService;
import net.myspring.cloud.modules.input.web.form.BatchBillForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
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
    private BdMaterialService bdMaterialService;
    @Autowired
    private BdCustomerService bdCustomerService;
    @Autowired
    private BdStockService bdStockService;
    @Autowired
    private BatchBillService batchBillService;

    @RequestMapping(value = "form")
    public BatchBillForm form (BatchBillForm batchBillForm) {
        String companyId = SecurityUtils.getCompanyId();
        if (StringUtils.isNotBlank(companyId)) {
            List<BdMaterial> materials = bdMaterialService.findAll();
            for(BdMaterial bdMaterial : materials){
                batchBillForm.getMaterialMap().put(bdMaterial.getfName(),bdMaterial.getfNumber());
            }
//            batchBillForm.setCustomerNameList(bdCustomerService.findName());
            batchBillForm.setProductNameList(CollectionUtil.extractToList(materials,"fName"));
            batchBillForm.setTypeList(K3CloudBillTypeEnum.values());
            batchBillForm.setStoreList(bdStockService.findAll());
        }
        return batchBillForm;
    }

    @RequestMapping(value = "getBillTypeEnum")
    public K3CloudBillTypeEnum[] getBillTypeEnum(){
        return K3CloudBillTypeEnum.values();
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data, String storeCode, String billDate,ServletRequest request) {
        RestResponse restResponse = new RestResponse();
        if("false".equals(request.getAttribute("doubleSubmit").toString())) {
            data = HtmlUtils.htmlUnescape(data);
            List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
            List<String> codeList = batchBillService.save(datas, storeCode, LocalDate.parse(billDate));
            restResponse.setMessage("批量开单成功：" + codeList);
        }else{
            restResponse.setMessage("页面已经提交过，请核对金蝶中是否已导入");
        }
        return restResponse;
    }
}
