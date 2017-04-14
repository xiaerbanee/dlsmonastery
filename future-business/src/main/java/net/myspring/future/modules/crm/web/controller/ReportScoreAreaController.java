package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Maps;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ReportScoreArea;
import net.myspring.future.modules.crm.service.ReportScoreAreaService;
import net.myspring.future.modules.hr.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScoreArea")
public class ReportScoreAreaController {

    @Autowired
    private ReportScoreAreaService reportScoreAreaService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public ReportScoreArea get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ReportScoreArea() :reportScoreAreaService.findOne(id);
    }

    @RequestMapping
    public String list(HttpServletRequest request){
        SearchEntity searchEntity= RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        Page<ReportScoreArea> page=reportScoreAreaService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty",method = RequestMethod.GET)
    @ResponseBody
    public String getListProperty(){
        Map<String , Object>map = Maps.newHashMap();
        map.put("areas",officeService.findByType("100"));
        return ObjectMapperUtils.writeValueAsString(map);
    }
}
