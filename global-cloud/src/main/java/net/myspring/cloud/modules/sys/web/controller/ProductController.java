package net.myspring.cloud.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.service.BdMaterialService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.ProductService;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by liuj on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private BdMaterialService bdMaterialService;

    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getFormProperty(String companyId){
        Map<String,Object> map= Maps.newHashMap();
        if (StringUtils.isNotBlank(companyId)) {
            map.putAll(productService.getFormProperty());
        }
        //用户所拥有的kingdeeName
//        map.put("kingdeeBookList",kingdeeBookService.findAll());
        map.put("companyId", companyId);
        return map;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data, ServletRequest request) {
        if("false".equals(request.getAttribute("doubleSubmit").toString())) {
            data = HtmlUtils.htmlUnescape(data);
            List<List<Object>> datas = ObjectMapperUtils.readValue(data, ArrayList.class);
            productService.save(datas);
        }
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "syn")
    public RestResponse syn(String companyId) {
        if (StringUtils.isNotBlank(companyId)) {
            LocalDateTime maxOutDate = productService.findMaxOutDate(companyId);
            List<BdMaterial> bdMaterials = bdMaterialService.findByDate(maxOutDate);
            if(CollectionUtil.isNotEmpty(bdMaterials)){
               return new RestResponse("同步成功",null);
            }
        }
        return new RestResponse("请选择账套",null);
    }

//    @RequestMapping(value = "export")
//    public ModelAndView export(String monthStart, String monthEnd, String companyName) {
//        ExcelView excelView = new ExcelView();
//        List<List<Object>> data = Lists.newArrayList();
//        List<Object> header = Lists.newArrayList();
//        header.add("货品");
//        header.add("编码");
//        header.add("单价");
//        data.add(header);
//        if (StringUtils.isNotBlank(companyName)) {
//            List<CloudProduct> productList=productService.findAllByCompany();
//            for(CloudProduct cloudProduct : productList){
//                List<Object> row = Lists.newArrayList();
//                row.add(cloudProduct.getName());
//                row.add(cloudProduct.getCode());
//                row.add(cloudProduct.getPrice1());
//                data.add(row);
//            }
//        }
//        ExcelBook excelBook = ExcelUtils.getExcelBook(companyName + "货品列表"  + ".xlsx", data);
//        return new ModelAndView(excelView, "excelBook", excelBook);
//    }

}
