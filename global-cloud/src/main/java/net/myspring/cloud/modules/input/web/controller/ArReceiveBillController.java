package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.ArReceiveBillDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.service.ArReceiveBillService;
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

import java.util.List;

/**
 * 收款单
 * Created by lihx on 2017/6/24.
 */
@RestController
@RequestMapping(value = "input/arReceiveBill")
public class ArReceiveBillController {
    @Autowired
    private ArReceiveBillService arReceiveBillService;
    @Autowired
    private KingdeeBookService kingdeeBookService;
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(value = "saveForWS",method = RequestMethod.POST)
    public List<KingdeeSynReturnDto> saveForShopDeposit(@RequestBody List<ArReceiveBillDto> arReceiveBillDtoList) {
        KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        AccountKingdeeBook accountKingdeeBook = accountKingdeeBookService.findByAccountId(RequestUtils.getAccountId());
        List<KingdeeSynDto> kingdeeSynDtoList = arReceiveBillService.saveForWS(arReceiveBillDtoList,kingdeeBook,accountKingdeeBook);
        return BeanUtil.map(kingdeeSynDtoList,KingdeeSynReturnDto.class);
    }
}
