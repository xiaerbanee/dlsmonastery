package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.FolderFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FolderFileMapper extends MyMapper<FolderFile,String> {

}
