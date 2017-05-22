package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "crm/goodsOrderIme")
public class GoodsOrderImeController {

    @Autowired
    private GoodsOrderImeService goodsOrderImeService;





}
