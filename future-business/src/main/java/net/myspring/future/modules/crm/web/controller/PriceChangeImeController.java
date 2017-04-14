package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.domain.PriceChangeIme;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/priceChangeIme")
public class PriceChangeImeController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(){
        return null;
    }


    @RequestMapping(value = "detail" ,method = RequestMethod.GET)
    public String detail(PriceChangeIme priceChangeIme){
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        return null;
    }

    @RequestMapping(value = "save")
    public String save(PriceChangeIme priceChangeIme) {
        return null;
    }

    @RequestMapping(value = "imageUpload")
    public String imageUpload(PriceChangeIme priceChangeIme) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit(PriceChangeIme priceChangeIme){
        return null;
    }

    private List<String> getActionList(PriceChangeIme priceChangeIme){
        return null;
    }
}
