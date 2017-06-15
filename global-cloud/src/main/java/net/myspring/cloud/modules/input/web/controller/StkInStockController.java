package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.StkInStockService;
import net.myspring.cloud.modules.input.web.form.StkInStockForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.ProductService;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购入库
 * Created by lihx on 2017/6/14.
 */
@RestController
@RequestMapping(value = "input/stkInStock")
public class StkInStockController {
    @Autowired
    private StkInStockService stkInStockService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public StkInStockForm form (StkInStockForm stkInStockForm) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        stkInStockForm = stkInStockService.getForm(stkInStockForm,kingdeeBook);
        Map<String,ProductDto> productOutIdMap = productService.findAll().stream().collect(Collectors.toMap(ProductDto::getOutId, ProductDto-> ProductDto));
        String returnOutId = productService.findReturnOutId();
        stkInStockForm.getTypeList().add(productOutIdMap.get(returnOutId)==null?null:productOutIdMap.get(returnOutId).getName());
        return stkInStockForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(StkInStockForm stkInStockForm) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynDto> kingdeeSynDtoList = stkInStockService.save(stkInStockForm,kingdeeBook,accountKingdeeBook);
        for(KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList){
            if (kingdeeSynDto.getSuccess()){
                return new RestResponse("采购入库成功：" + kingdeeSynDto.getResult(),null,true);
            }else {
                System.err.println(kingdeeSynDto.getResult());
                return new RestResponse("采购入库失败：" + kingdeeSynDto.getResult(),null,true);
            }
        }
        return null;
    }
}