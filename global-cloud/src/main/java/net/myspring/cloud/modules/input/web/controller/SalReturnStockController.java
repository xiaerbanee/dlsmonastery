package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.service.SalReturnStockService;
import net.myspring.cloud.modules.input.web.form.BatchBillForm;
import net.myspring.cloud.modules.input.web.query.BatchBillQuery;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 退货单
 * Created by lihx on 2017/4/25.
 */
@RestController
@RequestMapping(value = "input/salReturnStock")
public class SalReturnStockController {
    @Autowired
    private SalReturnStockService salReturnStockService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public BatchBillQuery form (BatchBillQuery batchBillQuery) {
        batchBillQuery = salReturnStockService.getFormProperty(batchBillQuery);
        return batchBillQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(BatchBillForm batchBillForm) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynExtendDto> codeList = salReturnStockService.save(batchBillForm,kingdeeBook,accountKingdeeBook);
        return new RestResponse("批量开单成功：" + codeList,null,true);
    }
}