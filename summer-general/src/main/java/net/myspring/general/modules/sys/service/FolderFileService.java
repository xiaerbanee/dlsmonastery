package net.myspring.general.modules.sys.service;

import net.myspring.general.common.utils.CacheUtils;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.mapper.FolderFileMapper;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class FolderFileService {
    @Autowired
    private FolderFileMapper folderFileMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Transactional
    public List<FolderFile> save(String folderId, Map<String, MultipartFile> fileMap) {
        return null;
    }

    public FolderFile findOne(String id) {
        return folderFileMapper.findOne(id);
    }


    public List<FolderFile> findByIds(List<String> ids) {
        return folderFileMapper.findByIds(ids);
    }

    public Page<FolderFileDto> findPage(Pageable pageable, FolderFileQuery folderFileQuery) {
        Page<FolderFileDto> FolderFileDtoPage= folderFileMapper.findPage(pageable, folderFileQuery);
        cacheUtils.initCacheInput(FolderFileDtoPage.getContent());
        return FolderFileDtoPage;
    }
}
