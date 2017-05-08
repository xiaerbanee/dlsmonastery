package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.modules.input.domain.BdStock;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.input.service.BdStockService;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "input/bdStock")
public class BdStockController {
    @Autowired
    private BdStockService bdStockService;

    @RequestMapping(value = "getStockList", method = RequestMethod.GET)
    public List<BdStock> getStockList(String maxOutDate) {
        LocalDateTime localDateTime = null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime = LocalDateTimeUtils.parse(maxOutDate);
        }
        List<BdStock> stockList = bdStockService.findByDate(localDateTime);
        return stockList;
    }

    @RequestMapping(value = "getNameAndNumber")
    public List<NameNumberDto> getNameAndNumber(){
        return bdStockService.findNameAndNumber();
    }

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdStockService.findNameByNameLike(name);
    }

}
