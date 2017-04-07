package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.common.exception.ServiceException;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.basic.modules.sys.dto.FolderFileDto;
import net.myspring.basic.modules.sys.service.FolderFileService;
import net.myspring.basic.modules.sys.service.FolderService;
import net.myspring.basic.modules.sys.web.query.FolderFileQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, FolderFileQuery folderFileQuery){
        Page<FolderFileDto> page = folderFileService.findPage(pageable,folderFileQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "/upload")
    public List<FolderFileDto> upload(String uploadPath, MultipartHttpServletRequest request, HttpServletResponse response) {
        Folder folder = folderService.getAccountFolder(securityUtils.getAccountId(), uploadPath);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        List<FolderFileDto> list = folderFileService.save(folder.getId(), fileMap);
        return list;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(String id, HttpServletResponse response) {
        FolderFile folderFile = folderFileService.findOne(id);
        File file = new File(folderFileService.getUploadPath(folderFile));
        try {
            response.setContentType(folderFile.getContentType());
            response.setHeader("Content-disposition", "attachment; filename=\"" + EncodeUtil.urlEncode(folderFile.getName()) + "\"");
            FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public void preview(String id, HttpServletResponse response) {
        FolderFile folderFile = folderFileService.findOne(id);
        File file  = new File(folderFileService.getPreviewUploadPath(folderFile));
        if(file !=null) {
            try {
                response.setContentType("image/png");
                FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
            } catch (IOException e) {
                logger.info(folderFileService.getPreviewUploadPath(folderFile) + "不存在");
            }
        }
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public void view(String id, HttpServletResponse response) {
        FolderFile folderFile = folderFileService.findOne(id);
        if(folderFile!=null){
            File file  = new File(folderFileService.getUploadPath(folderFile));
            try {
                response.setContentType(folderFile.getContentType());
                FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
            } catch (IOException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/findByIds")
    public String findByIds(String ids) {
        List<String> idList = StringUtils.getSplitList(ids, Const.CHAR_COMMA);
        List<FolderFileDto> folderFileDtoList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(idList)) {
            List<FolderFile> folderFileList = folderFileService.findByIds(idList);
            folderFileDtoList= BeanUtil.map(folderFileList,FolderFileDto.class);
        }
        return ObjectMapperUtils.writeValueAsString(folderFileDtoList);
    }
}
