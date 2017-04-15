package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.basic.modules.sys.dto.FolderFileDto;
import net.myspring.basic.modules.sys.web.query.FolderFileQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface FolderFileMapper extends MyMapper<FolderFile,String> {

    Page<FolderFileDto> findPage(Pageable pageable, @Param("p")FolderFileQuery folderFileQuery);
}
