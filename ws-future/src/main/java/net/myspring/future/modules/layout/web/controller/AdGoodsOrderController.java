package net.myspring.future.modules.layout.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.service.AdGoodsOrderService;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderForm;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/adGoodsOrder")
public class  AdGoodsOrderController {

    @Autowired
    private AdGoodsOrderService adGoodsOrderService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<AdGoodsOrderDto> list(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
       Page<AdGoodsOrderDto> page = adGoodsOrderService.findPage(pageable,adGoodsOrderQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public AdGoodsOrderQuery getQuery(AdGoodsOrderQuery adGoodsOrderQuery) {
        adGoodsOrderQuery.setBillTypes(BillTypeEnum.getList());
        return adGoodsOrderQuery;
    }

    @RequestMapping(value = "detail")
    public AdGoodsOrderDto detail(AdGoodsOrderDto adGoodsOrderDto){
        adGoodsOrderDto = adGoodsOrderService.getAdGoodsOrderDetail(adGoodsOrderDto);
        return adGoodsOrderDto;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderService.audit(adGoodsOrderForm);
        return new RestResponse("审核成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "outShopChange", method = RequestMethod.GET)
    public String outShopChange(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("adGoodsOrder", adGoodsOrder);
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public AdGoodsOrderForm getFormProperty(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderForm = adGoodsOrderService.getFormProperty(adGoodsOrderForm);
        return adGoodsOrderForm;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        Map<String, Object> map = Maps.newHashMap();
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderService.save(adGoodsOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("type", "标准出库单");
        return null;
    }

    @RequestMapping(value = "bill", method = RequestMethod.GET)
    public String bill(AdGoodsOrder adGoodsOrder) {
        return null;
    }

    @RequestMapping(value = "bill", method = RequestMethod.POST)
    public RestResponse bill(Model model, AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {
        return null;
    }

    @RequestMapping(value = "getShipFormProperty", method = RequestMethod.GET)
    public String getShipFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        BigDecimal smallPrice = BigDecimal.ZERO;
        BigDecimal mediumPrice = BigDecimal.ZERO;
        BigDecimal bigPrice = BigDecimal.ZERO;
        return null;
    }

    @RequestMapping(value = "ship", method = RequestMethod.GET)
    public String shipForm(AdGoodsOrder adGoodsOrder) {
        return null;
    }

    @RequestMapping(value = "ship", method = RequestMethod.POST)
    public RestResponse ship(AdGoodsOrder adGoodsOrder) {
        return null;
    }

    @RequestMapping(value = "sign", method = RequestMethod.GET)
    public String sign(AdGoodsOrder adGoodsOrder) {
        return null;
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public RestResponse sign(AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {

        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderService.logicDelete(adGoodsOrderForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.saved.name());
    }

    private List<String> getActionList(AdGoodsOrder adGoodsOrder) {
        List<String> actionList = Lists.newArrayList();
        return actionList;
    }
}
