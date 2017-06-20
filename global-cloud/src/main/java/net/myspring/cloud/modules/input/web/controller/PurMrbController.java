package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.PurMrbService;
import net.myspring.cloud.modules.input.web.form.PurMrbForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购退料
 * Created by lihx on 2017/6/13.
 */
@RestController
@RequestMapping(value = "input/purMrb")
public class PurMrbController {
    @Autowired
    private PurMrbService purMrbService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;
    
    @RequestMapping(value = "form")
    public PurMrbForm form (PurMrbForm purMrbForm) {
        purMrbForm = purMrbService.getForm(purMrbForm);
        return purMrbForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PurMrbForm cnJournalForCashForm) {
        RestResponse restResponse;
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        KingdeeSynDto kingdeeSynDto = purMrbService.save(cnJournalForCashForm,kingdeeBook,accountKingdeeBook);
        if (kingdeeSynDto.getSuccess()){
            restResponse = new RestResponse("采购退料成功：" + kingdeeSynDto.getBillNo(),null,true);
        }else {
            System.err.println(kingdeeSynDto.getResult());
            restResponse = new RestResponse("采购退料失败：" + kingdeeSynDto.getResult(),null,true);
        }
        return restResponse;
    }
}
