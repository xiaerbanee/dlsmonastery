package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.ArRefundBillService;
import net.myspring.cloud.modules.input.web.form.ArRefundBillForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收款退款单
 * Created by lihx on 2017/6/20.
 */
@RestController
@RequestMapping(value = "input/arRefundBill")
public class ArRefundBillController {
    @Autowired
    private ArRefundBillService arRefundBillService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public ArRefundBillForm form () {
        return arRefundBillService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(ArRefundBillForm apPayBillForm) {
        RestResponse restResponse = new RestResponse("", null, false);
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynDto> kingdeeSynDtoList = arRefundBillService.save(apPayBillForm,kingdeeBook,accountKingdeeBook);
        for (KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList) {
            if (kingdeeSynDto.getSuccess()) {
                restResponse = new RestResponse("收款退款单成功：" + kingdeeSynDto.getBillNo(), null, true);
            } else {
                throw new ServiceException("收款退款单失败："+kingdeeSynDto.getResult());
            }
        }
        return restResponse;
    }
}
