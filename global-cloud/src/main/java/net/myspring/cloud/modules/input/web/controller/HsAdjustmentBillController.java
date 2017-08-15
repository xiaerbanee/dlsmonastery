package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.HsAdjustmentBillService;
import net.myspring.cloud.modules.input.web.form.HsAdjustmentBillForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeSynService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 入库成本调整单
 */
@RestController
@RequestMapping(value = "input/hsAdjustmentBill")
public class HsAdjustmentBillController {
    @Autowired
    private HsAdjustmentBillService hsAdjustmentBillService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;
    @Autowired
    private KingdeeSynService kingdeeSynService;

    @RequestMapping(value = "form")
    public HsAdjustmentBillForm form () {
        return hsAdjustmentBillService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(HsAdjustmentBillForm hsAdjustmentBillForm) {
        RestResponse restResponse;
        StringBuilder message = new StringBuilder();
        try {
            AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountIdAndCompanyName(RequestUtils.getAccountId(),RequestUtils.getCompanyName());
            if (accountKingdeeBook != null) {
                KingdeeBook kingdeeBook = kingdeeBookService.findOne(accountKingdeeBook.getKingdeeBookId());
                KingdeeSynDto kingdeeSynDto = hsAdjustmentBillService.save(hsAdjustmentBillForm, kingdeeBook, accountKingdeeBook);
                kingdeeSynService.save(BeanUtil.map(kingdeeSynDto, KingdeeSyn.class));
//                for (KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList) {
                    if (kingdeeSynDto.getSuccess()) {
                        message.append(kingdeeSynDto.getBillNo()+",");
                    }
//                }
                restResponse = new RestResponse("成本调整单成功：" + message, null, true);
            } else {
                restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
            }
            return restResponse;
        } catch (Exception e) {
            return new RestResponse(e.getMessage(), ResponseCodeEnum.invalid.name(), false);
        }
    }

}
