package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Maps;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import net.myspring.future.modules.crm.service.ReportScoreOfficeService;
import net.myspring.future.modules.hr.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScoreOffice")
public class ReportScoreOfficeController {

    @Autowired
    private ReportScoreOfficeService reportScoreOfficeService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public ReportScoreOffice get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ReportScoreOffice() :reportScoreOfficeService.findOne(id);
    }

    @RequestMapping
    public String list(HttpServletRequest request){
        SearchEntity searchEntity= RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        Page<ReportScoreOffice> page=reportScoreOfficeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty",method = RequestMethod.GET)
    @ResponseBody
    public String getListProperty(){
        Map<String , Object> map = Maps.newHashMap();
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("offices",officeService.findByType(Const.OFFICE_TYPE_UNIT));
        return ObjectMapperUtils.writeValueAsString(map);
    }
}
