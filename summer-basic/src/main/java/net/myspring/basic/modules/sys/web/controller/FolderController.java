package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.dto.FolderDto;
import net.myspring.basic.modules.sys.service.FolderService;
import net.myspring.basic.modules.sys.web.form.FolderForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
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
    public List<FolderDto> list(HttpServletRequest request){
        List<FolderDto> list = folderService.findAll(securityUtils.getAccountId());
        return list;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Folder folder,BindingResult bindingResult) {
        folderService.deleteOne(folder.getId());
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(FolderForm folderForm) {
        folderService.save(folderForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public FolderDto findOne(String id){
        FolderDto folderDto=folderService.findDto(id);
        return folderDto;
    }

    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("folders",folderService.findAll(securityUtils.getAccountId()));
        return map;
    }

}
