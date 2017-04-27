package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSDBFile;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.common.exception.ServiceException;
import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.form.ExcelExportForm;
import net.myspring.general.modules.sys.service.FolderFileService;
import net.myspring.general.modules.sys.service.FolderService;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.EncodeUtil;
import net.myspring.util.text.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/folderFile")
public class FolderFileController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderFileService folderFileService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<FolderFileDto>  list(Pageable pageable, FolderFileQuery folderFileQuery){
        Page<FolderFileDto> page = folderFileService.findPage(pageable,folderFileQuery);
        return page;
    }

    @RequestMapping(value = "/upload")
    public List<FolderFileDto> upload(String uploadPath, MultipartHttpServletRequest request) {
        Folder folder = folderService.getAccountFolder(SecurityUtils.getAccountId(), uploadPath);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        List<FolderFileDto> list = folderFileService.save(folder.getId(), fileMap);
        return list;
    }

    @RequestMapping(value = "/uploadToMongoDb")
    public String upload(@RequestBody ExcelExportForm excelExportForm) {
        String mongoDbId=folderFileService.uploadToMongoDb(excelExportForm.getOutputStream(),excelExportForm.getFileName());
        return mongoDbId;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(String token,String type,String id,HttpServletResponse response) {
        GridFSDBFile gridFSDBFile = folderFileService.getGridFSDBFile(type,id);
        if(gridFSDBFile != null) {
            try {
                response.setContentType(gridFSDBFile.getContentType());
                response.setHeader("Content-disposition", "attachment; filename=\"" + EncodeUtil.urlEncode(gridFSDBFile.getFilename()) + "\"");
                FileCopyUtils.copy(gridFSDBFile.getInputStream(), response.getOutputStream());
            } catch (IOException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/findByIds")
    public List<FolderFileDto> findByIds(String ids) {
        List<String> idList = StringUtils.getSplitList(ids, ",");
        List<FolderFileDto> folderFileDtoList = folderFileService.findByIds(idList);
        return folderFileDtoList;
    }
}
