package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.KingdeeNameEnum;
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
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购入库单
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
        if (!KingdeeNameEnum.JXDJ.name().equals(kingdeeBook.getName())){
            Map<String,ProductDto> productOutIdMap = productService.findAll().stream().collect(Collectors.toMap(ProductDto::getOutId, ProductDto-> ProductDto));
            String returnOutId = productService.findReturnOutId();
            String type = productOutIdMap.get(returnOutId)==null?"":productOutIdMap.get(returnOutId).getName();
            stkInStockForm.getTypeList().add(type);
        }
        stkInStockForm = stkInStockService.getForm(stkInStockForm,kingdeeBook);
        return stkInStockForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(StkInStockForm stkInStockForm) {
        RestResponse restResponse =  new RestResponse("开单成功",null);
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        if (accountKingdeeBook != null) {
            List<KingdeeSynDto> kingdeeSynDtoList = stkInStockService.save(stkInStockForm, kingdeeBook, accountKingdeeBook);
            for (KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList) {
                if (kingdeeSynDto.getSuccess()) {
                    restResponse = new RestResponse("采购入库成功：" + kingdeeSynDto.getBillNo(), null, true);
                }
            }
        }else {
            restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
        }
        return restResponse;
    }
}
