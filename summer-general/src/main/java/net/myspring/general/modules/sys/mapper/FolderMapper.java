package net.myspring.general.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.domain.Folder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FolderMapper extends MyMapper<Folder,String> {


    Folder findByParentIdAndName(@Param("parentId") String parentId, @Param("name") String name);

    Folder findByCreatedByAndParentIds(@Param("createdBy") String createdBy, @Param("parentIds") String parentIds);

    List<Folder> findByCreatedBy(String createdBy);

    List<Folder> findByParentIdsLike(String parentIds);
}
