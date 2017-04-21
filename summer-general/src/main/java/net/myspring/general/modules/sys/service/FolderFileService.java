package net.myspring.general.modules.sys.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.mapper.FolderFileMapper;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import net.myspring.util.text.StringUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FolderFileService {
    @Autowired
    private FolderFileMapper folderFileMapper;
    @Autowired
    private GridFsTemplate uploadGridFsTemplate;
    @Autowired
    private GridFsTemplate uploadPreviewGridFsTemplate;

    public List<FolderFile> save(String folderId, Map<String, MultipartFile> fileMap) {
        List<FolderFile> list = Lists.newArrayList();
        try {
            for (MultipartFile multipartFile : fileMap.values()) {
                if (multipartFile.getSize() > 0) {
                    //保存到mongoDb
                    GridFSFile gridFSFile = uploadGridFsTemplate.store(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
                    GridFSFile preview = null;
                    //如果是图片类型
                    if(multipartFile.getContentType().startsWith("image/")) {
                        BufferedImage image = Scalr.resize(ImageIO.read(multipartFile.getInputStream()), 290);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        preview = uploadPreviewGridFsTemplate.store(new ByteArrayInputStream(baos.toByteArray()),multipartFile.getOriginalFilename());
                    }
                    // 保存到数据库
                    FolderFile folderFile = new FolderFile();
                    folderFile.setFolderId(folderId);
                    folderFile.setContentType(multipartFile.getContentType());
                    folderFile.setName(multipartFile.getOriginalFilename().replaceAll("/","."));
                    folderFile.setSize(Integer.valueOf(String.valueOf(multipartFile.getSize())));
                    folderFile.setPhysicalName(folderFile.getName());
                    folderFile.setMongoId(StringUtils.toString(gridFSFile.getId()));
                    if(preview != null) {
                        folderFile.setMongoPreviewId(StringUtils.toString(preview.getId()));
                    }
                    folderFileMapper.save(folderFile);
                    list.add(folderFile);
                }
            }
        } catch (Exception e) {
        }
        return list;
    }

    public FolderFile findOne(String id) {
        return folderFileMapper.findOne(id);
    }


    public List<FolderFile> findByIds(List<String> ids) {
        return folderFileMapper.findByIds(ids);
    }

    public Page<FolderFileDto> findPage(Pageable pageable, FolderFileQuery folderFileQuery) {
        Page<FolderFileDto> FolderFileDtoPage= folderFileMapper.findPage(pageable, folderFileQuery);
        return FolderFileDtoPage;
    }
}
