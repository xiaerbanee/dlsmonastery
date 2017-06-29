package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockDto;
import net.myspring.cloud.modules.input.service.SalReturnStockService;
import net.myspring.cloud.modules.input.web.form.SalStockForm;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public SalStockForm form () {
        return salReturnStockService.getForm();
    }

    @RequestMapping(value = "save")
    public RestResponse save(SalStockForm salStockForm) {
        RestResponse restResponse = new RestResponse("",null,true);;
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = salReturnStockService.save(salStockForm,kingdeeBook,accountKingdeeBook);
        for(KingdeeSynExtendDto kingdeeSynExtendDto : kingdeeSynExtendDtoList){
            if (kingdeeSynExtendDto.getSuccess()){
                restResponse = new RestResponse("开单退货成功：" + kingdeeSynExtendDto.getNextBillNo(),null,true);
            }else {
                throw new ServiceException("开单退货失败："+kingdeeSynExtendDto.getResult());
            }
        }
        return restResponse;
    }

    @RequestMapping(value = "saveForXSTHD",method = RequestMethod.POST)
    public List<KingdeeSynReturnDto> saveForXSCKD(@RequestBody List<SalReturnStockDto> salReturnStockDtoList) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynExtendDto> kingdeeSynExtendDtoList = salReturnStockService.saveForXSTHD(salReturnStockDtoList,kingdeeBook,accountKingdeeBook);
        return BeanUtil.map(kingdeeSynExtendDtoList, KingdeeSynReturnDto.class);
    }
}
