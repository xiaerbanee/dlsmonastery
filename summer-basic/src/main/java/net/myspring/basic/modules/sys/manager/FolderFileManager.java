package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.basic.modules.sys.mapper.FolderFileMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
