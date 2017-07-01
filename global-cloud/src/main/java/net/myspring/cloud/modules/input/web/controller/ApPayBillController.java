package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.ApPayBillService;
import net.myspring.cloud.modules.input.service.ArOtherRecAbleService;
import net.myspring.cloud.modules.input.web.form.ApPayBillForm;
import net.myspring.cloud.modules.input.web.form.ArOtherRecAbleForm;
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
 * 付款单
 * Created by lihx on 2017/6/20.
 */
@RestController
@RequestMapping(value = "input/apPayBill")
public class ApPayBillController {
    @Autowired
    private ApPayBillService apPayBillService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public ApPayBillForm form () {
        return apPayBillService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(ApPayBillForm apPayBillForm) {
        RestResponse restResponse = new RestResponse("开单成功",null);
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        if (accountKingdeeBook != null) {
            List<KingdeeSynDto> kingdeeSynDtoList = apPayBillService.save(apPayBillForm, kingdeeBook, accountKingdeeBook);
            for (KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList) {
                if (kingdeeSynDto.getSuccess()) {
                    restResponse = new RestResponse("付款单成功：" + kingdeeSynDto.getBillNo(), null, true);
                }
            }
        }else{
            restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
        }
        return restResponse;
    }

}
