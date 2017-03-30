package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.Folder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FolderMapper extends MyMapper<Folder,String> {

}
