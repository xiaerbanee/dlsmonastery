package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.domain.Folder;
import net.myspring.basic.modules.sys.mapper.FolderMapper;
import org.apache.ibatis.annotations.Param;
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
public class FolderManager {

    @Autowired
    private FolderMapper folderMapper;

    @Cacheable(value = "folders",key="#p0")
    public Folder findOne(String id) {
        return folderMapper.findOne(id);
    }

    @Cacheable(value = "folders",key="#p0.id")
    public Folder save(Folder folder){
        folderMapper.save(folder);
        return  folder;
    }

    @CachePut(value = "folders",key="#p0.id")
    public Folder update(Folder folder){
        folderMapper.update(folder);
        return  folderMapper.findOne(folder.getId());
    }

    @CacheEvict(value = "folders",key="#p0")
    public int deleteById(String id) {
        return folderMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }

    public void batchSave(List<Folder> folderList){
        for(Folder folder:folderList){
            save(folder);
        }
    }

}
