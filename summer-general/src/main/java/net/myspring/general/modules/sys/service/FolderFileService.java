package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSDBFile;
import net.myspring.common.exception.ServiceException;
import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.repository.FolderFileRepository;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.FileUtils;
import net.myspring.util.text.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class FolderFileService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${upload.root-path}")
    private String uploadRootPath;
    @Value("${upload.preview-size}")
    private Integer uploadPreviewSize;

    @Autowired
    private FolderFileRepository folderFileRepository;
    @Autowired
    private GridFsTemplate storageGridFsTemplate;
    @Autowired
    private GridFsTemplate previewGridFsTemplate;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    @Transactional
    public List<FolderFileDto> save(String folderId, Map<String, MultipartFile> fileMap) {
        List<FolderFileDto> list = Lists.newArrayList();
        try {
            for (MultipartFile multipartFile : fileMap.values()) {
                if (multipartFile.getSize() > 0) {
                    // 保存到数据库
                    FolderFile folderFile = new FolderFile();
                    folderFile.setFolderId(folderId);
                    folderFile.setContentType(multipartFile.getContentType());
                    folderFile.setName(multipartFile.getOriginalFilename().replaceAll("/","."));
                    folderFile.setSize(Integer.valueOf(String.valueOf(multipartFile.getSize())));
                    folderFile.setPhysicalName(UUID.randomUUID().toString() + "." + getExtendType(folderFile));
                    folderFileRepository.save(folderFile);
                    list.add(BeanUtil.map(folderFile,FolderFileDto.class));
                    // 保存文件
                    String uploadPath = getUploadPath(folderFile);
                    FileUtils.mkdirs(uploadPath);
                    FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(uploadPath));
                    if (isImage(folderFile)) {
                        String previewUploadPath = getPreviewUploadPath(folderFile);
                        FileUtils.mkdirs(previewUploadPath);
                        File file = new File(uploadPath);
                        BufferedImage image;
                        image = Scalr.resize(ImageIO.read(file), uploadPreviewSize);
                        File pngFile = new File(previewUploadPath);
                        ImageIO.write(image, "png", pngFile);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("文件上传失败" + e.getMessage());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public FolderFile findOne(String id) {
        FolderFile folderFile =  folderFileRepository.findOne(id);
        return folderFile;
    }


    @Transactional(readOnly = true)
    public List<FolderFileDto> findByIds(List<String> ids) {
        if(CollectionUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<FolderFile> folderFileList =  folderFileRepository.findAll(ids);
        return BeanUtil.map(folderFileList,FolderFileDto.class);
    }

    public String getUploadPath(FolderFile folderFile) {
        return uploadRootPath + RequestUtils.getRequestEntity().getCompanyName() + "\\upload\\" + folderFile.getPhysicalName();
    }

    public String getPreviewUploadPath(FolderFile folderFile) {
        return uploadRootPath + RequestUtils.getRequestEntity().getCompanyName() + "\\convert\\" + folderFile.getPhysicalName().substring(0, folderFile.getPhysicalName().lastIndexOf(".")) + ".png";
    }

    @Transactional(readOnly = true)
    public Page<FolderFileDto> findPage(Pageable pageable, FolderFileQuery folderFileQuery) {
        Page<FolderFileDto> folderFilePage= folderFileRepository.findPage(pageable,folderFileQuery);
        cacheUtils.initCacheInput(folderFilePage.getContent());
        return folderFilePage;
    }

    private String getExtendType(FolderFile folderFile) {
        if(StringUtils.isNotBlank(folderFile.getName())){
            String name=folderFile.getName();
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    private Boolean isImage(FolderFile folderFile) {
        String extend = getExtendType(folderFile);
        return "jpg".equals(extend) || "jpeg".equals(extend) || "gif".equals(extend) || "png".equals(extend) || "bmp".equals(extend);
    }
}
