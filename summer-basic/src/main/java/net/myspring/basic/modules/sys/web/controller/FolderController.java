package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.dto.FolderDto;
import net.myspring.basic.modules.sys.dto.FolderFileDto;
import net.myspring.basic.modules.sys.service.FolderService;
import net.myspring.basic.modules.sys.web.form.FolderForm;
import net.myspring.common.domain.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        List<FolderDto> list = folderService.findAll(securityUtils.getAccountId());
        return ObjectMapperUtils.writeValueAsString(list);
    }

    @RequestMapping(value = "delete")
    public String delete(Folder folder,BindingResult bindingResult) {
        folderService.deleteOne(folder.getId());
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(FolderForm folderForm) {
        folderService.save(folderForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(Folder folder){
        return ObjectMapperUtils.writeValueAsString(folder);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("folders",folderService.findAll(securityUtils.getAccountId()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

}
