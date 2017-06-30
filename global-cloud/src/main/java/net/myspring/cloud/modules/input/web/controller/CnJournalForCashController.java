package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.CnJournalForCashService;
import net.myspring.cloud.modules.input.web.form.CnJournalForCashForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 手工日记账之现金日记账
 * Created by lihx on 2017/6/8.
 */
@RestController
@RequestMapping(value = "input/cnJournalForCash")
public class CnJournalForCashController {
    @Autowired
    private CnJournalForCashService cnJournalForCashService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public CnJournalForCashForm form () {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        return cnJournalForCashService.getForm(kingdeeBook);
    }

    @RequestMapping(value = "save")
    public RestResponse save(CnJournalForCashForm cnJournalForCashForm) {
        RestResponse restResponse = new RestResponse();
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        if (accountKingdeeBook != null) {
            KingdeeSynDto kingdeeSynDto = cnJournalForCashService.save(cnJournalForCashForm, kingdeeBook, accountKingdeeBook);
            if (kingdeeSynDto.getSuccess()) {
                restResponse = new RestResponse("现金日记账成功：" + kingdeeSynDto.getBillNo(), null, true);
            }
        }else {
            restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
        }
        return restResponse;
    }
}
