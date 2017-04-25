package net.myspring.future.modules.layout.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
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
@RequestMapping(value = "crm/adGoodsOrder")
public class  AdGoodsOrderController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
       
        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery() {
        Map<String, Object> map = Maps.newHashMap();
     
        return null;
    }


    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(AdGoodsOrder adGoodsOrder) {

        return null;
    }

    @RequestMapping(value = "audit")
    public String audit(AdGoodsOrder adGoodsOrder, boolean pass, String comment) {
     
        return null;
    }

    @RequestMapping(value = "outShopChange", method = RequestMethod.GET)
    public String outShopChange(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("adGoodsOrder", adGoodsOrder);
        return null;
    }

    @RequestMapping(value = "findForm", method = RequestMethod.GET)
    public String findOne(AdGoodsOrder adGoodsOrder) {

        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        Map<String, Object> map = Maps.newHashMap();
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(AdGoodsOrder adGoodsOrder, BindingResult bindingResult) {
        return null;
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

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(AdGoodsOrder adGoodsOrder, RedirectAttributes redirectAttributes) {

        return new RestResponse("删除成功",null);
    }

    private List<String> getActionList(AdGoodsOrder adGoodsOrder) {
        List<String> actionList = Lists.newArrayList();
        return actionList;
    }
}
