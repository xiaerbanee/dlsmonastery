package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.modules.basic.dto.PricesystemDetailDto;
import net.myspring.future.modules.basic.service.PricesystemDetailService;
import net.myspring.future.modules.basic.web.form.PricesystemDetailForm;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "basic/pricesystemDetail")
public class PricesystemDetailController {

    @Autowired
    private PricesystemDetailService pricesystemDetailService;

    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public List<List<Object>> findByProducts(@RequestParam(value = "productIds[]") String[] productIds){
        List<List<Object>> data = pricesystemDetailService.findByProducts(productIds);
        return data;
    }

}
