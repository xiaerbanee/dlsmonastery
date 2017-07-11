package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.service.SalOutStockService;
import net.myspring.cloud.modules.input.web.form.SalStockForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 销售出库单
 * Created by lihx on 2017/4/25.
 */
@RestController
@RequestMapping(value = "input/salOutStock")
public class SalOutStockController {
    @Autowired
    private SalOutStockService salOutStockService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public SalStockForm form () {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        return salOutStockService.getForm(kingdeeBook);
    }

    @RequestMapping(value = "save")
    public RestResponse save(SalStockForm salStockForm) {
        RestResponse restResponse = new RestResponse("", null, false);
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        if (accountKingdeeBook != null) {
            List<KingdeeSynReturnDto> kingdeeSynExtendDtoList = salOutStockService.save(salStockForm, kingdeeBook, accountKingdeeBook);
            for (KingdeeSynReturnDto kingdeeSynExtendDto : kingdeeSynExtendDtoList) {
                if (kingdeeSynExtendDto.getSuccess()) {
                    restResponse = new RestResponse("入库开单成功：" + kingdeeSynExtendDto.getNextBillNo(), null, true);
                }
            }
        }else {
            restResponse = new RestResponse("您没有金蝶账号，不能开单", null, false);
        }
        return restResponse;
    }

    @RequestMapping(value = "saveForXSCKD",method = RequestMethod.POST)
    public List<KingdeeSynReturnDto> saveForXSCKD(@RequestBody List<SalOutStockDto> salOutStockDtoList) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynReturnDto> kingdeeSynExtendDtoList;
        if (accountKingdeeBook != null) {
             kingdeeSynExtendDtoList = salOutStockService.saveForXSCKD(salOutStockDtoList, kingdeeBook, accountKingdeeBook);
        }else{
            throw new ServiceException("您没有金蝶账号，不能开单");
        }
        return kingdeeSynExtendDtoList;
    }
}
