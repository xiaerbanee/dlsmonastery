package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.enums.AuditTypeEnum;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.DepotUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.PriceChangeImeService;
import net.myspring.future.modules.crm.service.PriceChangeService;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PriceChangeImeService priceChangeImeService;
    @Autowired
    private PriceChangeService priceChangeService;
    @Autowired
    private DepotService depotService;

    private FolderService folderService;
    @Autowired
    private OfficeService officeService;
    @ModelAttribute
    public PriceChangeIme get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new PriceChangeIme() : priceChangeImeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().put("isCheck",true);
        searchEntity.getParams().put("status", AuditTypeEnum.NOT_PASS.getValue());
        if(searchEntity.getParams().get("priceChangeName")==null){
            searchEntity.getParams().put("priceChangeName",priceChangeService.findNearPriceChange().getName());
        }
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<PriceChangeIme> page = priceChangeImeService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(PriceChangeIme priceChangeIme:page.getContent()) {
            priceChangeIme.setActionList(getActionList(priceChangeIme));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("status", AuditTypeEnum.getValues());
        map.put("offices", officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "detail" ,method = RequestMethod.GET)
    public String detail(PriceChangeIme priceChangeIme){
        Map<String,Object> map = Maps.newHashMap();
        map.put("priceChangeIme",priceChangeIme);
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        LocalDateTime dateStart = LocalDate.now().plusDays(1).atStartOfDay();
        map.put("priceChanges",priceChangeService.findAllByEnabledAndDate(dateStart));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(PriceChangeIme priceChangeIme) {
        return ObjectMapperUtils.writeValueAsString(priceChangeIme);
    }

    @RequestMapping(value = "imageUpload")
    public String imageUpload(PriceChangeIme priceChangeIme) {
        RestResponse restResponse=new RestResponse("图片上传成功");
        priceChangeImeService.imageUpload(priceChangeIme);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "audit")
    public String audit(PriceChangeIme priceChangeIme){
        RestResponse restResponse=new RestResponse("审核成功");
        priceChangeImeService.audit(priceChangeIme);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    private List<String> getActionList(PriceChangeIme priceChangeIme){
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:priceChangeIme:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        if(DepotUtils.isAccess(priceChangeIme.getShop(),true)) {
            actionList.add(Const.ITEM_ACTION_UPLOAD);
            if(SecurityUtils.getAuthorityList().contains("crm:priceChangeIme:audit")){
                actionList.add(Const.ITEM_ACTION_AUDIT);
            }
        }
        return actionList;
    }
}
