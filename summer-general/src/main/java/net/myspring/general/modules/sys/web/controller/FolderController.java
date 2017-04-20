package net.myspring.general.modules.sys.web.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.modules.sys.dto.FolderDto;
import net.myspring.general.modules.sys.service.FolderService;
import net.myspring.general.modules.sys.web.form.FolderForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "sys/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public List<FolderDto> list(HttpServletRequest request) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("d:/test.txt");
        DBObject metaData = new BasicDBObject();
        metaData.put("extra1", "anything 1");
        metaData.put("extra2", "anything 2");
        GridFSFile gridFSFile = gridFsTemplate.store(inputStream,metaData);

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
