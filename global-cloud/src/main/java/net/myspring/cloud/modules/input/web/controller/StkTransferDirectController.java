package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.StkTransferDirectDto;
import net.myspring.cloud.modules.input.service.StkTransferDirectService;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 直接调拨单
 * Created by lihx on 2017/6/23.
 */
@RestController
@RequestMapping(value = "input/stkTransferDirect")
public class StkTransferDirectController {
    @Autowired
    private StkTransferDirectService stkTransferDirectService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "saveForStoreAllot",method= RequestMethod.POST)
    public KingdeeSynReturnDto saveForStoreAllot(@RequestBody StkTransferDirectDto stkTransferDirectDto) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        if (accountKingdeeBook != null){
            KingdeeSynDto kingdeeSynDto = stkTransferDirectService.saveForWS(stkTransferDirectDto,kingdeeBook,accountKingdeeBook);
            return BeanUtil.map(kingdeeSynDto,KingdeeSynReturnDto.class);
        }else{
            throw new ServiceException("您没有金蝶账号，不能开单");
        }
    }
}
