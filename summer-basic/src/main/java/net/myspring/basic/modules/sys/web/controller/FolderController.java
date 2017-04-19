package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.exception.ServiceException;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<FolderDto> list(HttpServletRequest request){
        List<FolderDto> list = folderService.findAll(SecurityUtils.getAccountId());
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
        // 无法将上级部门设置为自己或者自己的下级部门
        folderForm.setParentIds(folderForm.getParent().getParentIds() + folderForm.getParent().getId() + ",");
        if (!folderForm.isCreate() && folderForm.getParentIds().contains("," + folderForm.getId() + ",")) {
            return new RestResponse("无法将上级目录设置为自己或者自己的下级目录",null);
        }
        folderService.save(folderForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public FolderForm findOne(FolderForm folderForm){
        folderForm=folderService.findForm(folderForm.getId());
        folderForm.setFolderList(folderService.findAll(SecurityUtils.getAccountId()));
        return folderForm;
    }

}
