package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.general.modules.sys.domain.Folder;
import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import net.myspring.general.modules.sys.service.FolderFileService;
import net.myspring.general.modules.sys.service.FolderService;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.Encodes;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
    public List<FolderFileDto> upload(String uploadPath, MultipartHttpServletRequest request, HttpServletResponse response) {
        Folder folder = folderService.getAccountFolder(RequestUtils.getAccountId(), uploadPath);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        List<FolderFileDto> list = folderFileService.save(folder.getId(), fileMap);
        return list;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(FolderFileDto folderFileDto, HttpServletResponse response) {
        FolderFile folderFile = folderFileService.findOne(folderFileDto.getId());
        File file = new File(folderFileService.getUploadPath(folderFile));
        try {
            response.setContentType(folderFile.getContentType());
            response.setHeader("Content-disposition", "attachment; filename=\"" + Encodes.urlEncode(folderFile.getName()) + "\"");
            FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public void preview(FolderFile folderFile, HttpServletResponse response) {
        folderFile = folderFileService.findOne(folderFile.getId());
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
    public void view(FolderFile folderFile, HttpServletResponse response) {
        folderFile = folderFileService.findOne(folderFile.getId());
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
    public List<FolderFileDto> findByIds(String ids) {
        List<String> idList =StringUtils.getSplitList(ids, CharConstant.COMMA);
        List<FolderFileDto> folderFileList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(idList)) {
            folderFileList = folderFileService.findByIds(idList);
        }
        return folderFileList;
    }

    @RequestMapping(value = "/findById")
    public FolderFileFeignDto findById(String id) {
        FolderFileFeignDto folderFileFeignDto= new FolderFileFeignDto();
        if(StringUtils.isNotBlank(id)) {
            FolderFile folderFile = folderFileService.findOne(id);
            folderFileFeignDto= BeanUtil.map(folderFile,FolderFileFeignDto.class);
        }
        return folderFileFeignDto;
    }


    @RequestMapping(value = "/getQuery")
    public FolderFileQuery getQuery(FolderFileQuery folderFileQuery) {
        return folderFileQuery ;
    }
}
