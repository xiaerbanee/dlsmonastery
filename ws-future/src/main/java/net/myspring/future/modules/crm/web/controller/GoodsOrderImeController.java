package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import net.myspring.future.modules.crm.web.query.GoodsOrderImeQuery;
import net.myspring.util.excel.ExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "crm/goodsOrderIme")
public class GoodsOrderImeController{

        @Autowired
        private GoodsOrderImeService goodsOrderImeService;

        @RequestMapping(method = RequestMethod.GET)
        @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
        public Page<GoodsOrderImeDto> list(Pageable pageable, GoodsOrderImeQuery goodsOrderImeQuery){
            return goodsOrderImeService.findPage(pageable, goodsOrderImeQuery);
        }

        @RequestMapping(value = "getQuery")
        @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
        public GoodsOrderImeQuery getQuery(GoodsOrderImeQuery goodsOrderImeQuery) {
            goodsOrderImeQuery.getExtra().put("statusList", GoodsOrderStatusEnum.getList());
            return goodsOrderImeQuery;
        }

        @RequestMapping(value="export")
        @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
        public ModelAndView export(GoodsOrderImeQuery goodsOrderImeQuery) {
            return new ModelAndView(new ExcelView(), "simpleExcelBook", goodsOrderImeService.export(goodsOrderImeQuery));
        }
}
