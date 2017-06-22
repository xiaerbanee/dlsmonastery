package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.service.SalOutStockService;
import net.myspring.cloud.modules.input.web.form.SalStockForm;
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
        return salOutStockService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(SalStockForm salStockForm) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = salOutStockService.save(salStockForm,kingdeeBook,accountKingdeeBook);
        for(KingdeeSynExtendDto kingdeeSynExtendDto : kingdeeSynExtendDtoList){
            if (kingdeeSynExtendDto.getSuccess()){
                return new RestResponse("入库开单成功：" + kingdeeSynExtendDto.getNextBillNo(),null,true);
            }else {
                return new RestResponse("入库开单失败：" + kingdeeSynExtendDto.getResult(),null,false);
            }
        }
        return null;
    }

    @RequestMapping(value = "saveForXSCKD")
    public RestResponse saveForXSCKD(List<SalOutStockDto> salOutStockDtoList) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = salOutStockService.saveForXSCKD(salOutStockDtoList,kingdeeBook,accountKingdeeBook);
        for(KingdeeSynExtendDto kingdeeSynExtendDto : kingdeeSynExtendDtoList){
            if (kingdeeSynExtendDto.getSuccess()){
                return new RestResponse("入库开单成功：" + kingdeeSynExtendDto.getNextBillNo(),null,true);
            }else {
                return new RestResponse("入库开单失败：" + kingdeeSynExtendDto.getResult(),null,false);
            }
        }
        return null;
    }
}
