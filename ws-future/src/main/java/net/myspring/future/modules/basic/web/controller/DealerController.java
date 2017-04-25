package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.DealerDto;
import net.myspring.future.modules.basic.service.DealerService;
import net.myspring.future.modules.basic.web.Query.DealerQuery;
import net.myspring.future.modules.basic.web.form.DealerForm;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/dealer")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DealerDto> list(Pageable pageable, DealerQuery dealerQuery) {
        Page<DealerDto> page = dealerService.findPage(pageable, dealerQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DealerForm dealerForm) {
        dealerService.save(dealerForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public DealerForm findOne(DealerForm dealerForm) {
        dealerForm=dealerService.findForm(dealerForm);
        return dealerForm;
    }

}
