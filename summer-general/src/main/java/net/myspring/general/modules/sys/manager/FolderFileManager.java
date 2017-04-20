package net.myspring.general.modules.sys.manager;

import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.mapper.FolderFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class FolderFileManager {

    @Autowired
    private FolderFileMapper folderFileMapper;

    @Cacheable(value = "folderFiles",key="#p0")
    public FolderFile findOne(String id) {
        return folderFileMapper.findOne(id);
    }

    @Cacheable(value = "folderFiles",key="#p0.id")
    public FolderFile save(FolderFile folderFile){
        folderFileMapper.save(folderFile);
        return  folderFile;
    }

    @CachePut(value = "folderFiles",key="#p0.id")
    public FolderFile update(FolderFile folderFile){
        folderFileMapper.update(folderFile);
        return  folderFileMapper.findOne(folderFile.getId());
    }

    @CacheEvict(value = "folderFiles",key="#p0")
    public int deleteById(String id) {
        return folderFileMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }}
