package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.StkMisDeliveryService;
import net.myspring.cloud.modules.input.web.form.StkMisDeliveryForm;
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
 * 其他出库单
 * Created by lihx on 2017/4/25.
 */
@RestController
@RequestMapping(value = "input/stkMisDelivery")
public class StkMisDeliveryController {
    @Autowired
    private StkMisDeliveryService stkMisDeliveryService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "form")
    public StkMisDeliveryForm form () {
        return stkMisDeliveryService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(StkMisDeliveryForm stkMisDeliveryForm) {
        RestResponse restResponse = new RestResponse("",null,true);
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynDto> kingdeeSynDtoList = stkMisDeliveryService.save(stkMisDeliveryForm,kingdeeBook,accountKingdeeBook);
        for(KingdeeSynDto kingdeeSynDto : kingdeeSynDtoList){
            if (kingdeeSynDto.getSuccess()){
                 restResponse = new RestResponse("其他出库单开单成功：" + kingdeeSynDto.getBillNo(),null,true);
            }else {
                throw new ServiceException("其他出库单开单失败："+kingdeeSynDto.getResult());
            }
        }
        return restResponse;
    }
}
