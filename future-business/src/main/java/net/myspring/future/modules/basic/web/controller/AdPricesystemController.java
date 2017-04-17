package net.myspring.future.modules.basic.web.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "crm/adPricesystem")
public class AdPricesystemController {

//    @Autowired
//    private AdPricesystemService adPricesystemService;
//    @Autowired
//    private DepotService depotService;
//
//    @ModelAttribute
//    public AdPricesystem get(@RequestParam(required = false) String id) {
//        return StringUtils.isBlank(id) ? new AdPricesystem() : adPricesystemService.findOne(id);
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String list(HttpServletRequest request){
//        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
//        Page<AdPricesystem> page = adPricesystemService.findPage(searchEntity.getPageable(),searchEntity.getParams());
//        for(AdPricesystem adPricesystem: page.getContent()){
//            adPricesystem.setActionList(getActionList());
//        }
//        return ObjectMapperUtils.writeValueAsString(page);
//    }
//
//    @RequestMapping(value = "save")
//    public String save(AdPricesystem adPricesystem){
//        adPricesystemService.save(adPricesystem);
//        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
//
//    }
//
//    @RequestMapping(value = "getFormProperty")
//    public String getFormProperty(AdPricesystem adPricesystem){
//        Map<String,Object> map = Maps.newHashMap();
//        return ObjectMapperUtils.writeValueAsString(map);
//    }
//
//    @RequestMapping(value = "delete")
//    public String delete(AdPricesystem adPricesystem,BindingResult bindingResult) {
//        adPricesystemService.delete(adPricesystem);
//        RestResponse restResponse=new RestResponse("删除成功");
//        return ObjectMapperUtils.writeValueAsString(restResponse);
//    }
//
//    @RequestMapping(value = "findOne")
//    public String findOne(AdPricesystem adPricesystem){
//        return ObjectMapperUtils.writeValueAsString(adPricesystem);
//    }
//
//    private List<String> getActionList() {
//        List<String> actionList = Lists.newArrayList();
//        for(String authoritie : SecurityUtils.getAuthorityList()){
//            switch (authoritie){
//                case "crm:adPricesystem:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
//                case "crm:adPricesystem:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
//                default : break;
//            }
//        }
//        return actionList;
//    }
}
