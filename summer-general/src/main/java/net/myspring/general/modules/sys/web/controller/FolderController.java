package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.modules.sys.dto.FolderDto;
import net.myspring.general.modules.sys.service.FolderService;
import net.myspring.general.modules.sys.web.form.FolderForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @RequestMapping(method = RequestMethod.GET)
    public List<FolderDto> list()  {
        List<FolderDto> list = folderService.findAll(RequestUtils.getAccountId());
        return list;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Folder folder) {
        folderService.logicDeleteOne(folder.getId());
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(FolderForm folderForm) {
       RestResponse restResponse =  folderService.save(folderForm);
        return restResponse;
    }

    @RequestMapping(value = "getFormProperty")
    public FolderForm findOne(FolderForm folderForm){
        folderForm=folderService.getFormProperty(folderForm.getId());
        folderForm.setFolderList(folderService.findAll(RequestUtils.getAccountId()));
        return folderForm;
    }

}
