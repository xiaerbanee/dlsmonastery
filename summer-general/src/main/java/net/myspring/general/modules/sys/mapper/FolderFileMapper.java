package net.myspring.general.modules.sys.mapper;

import net.myspring.general.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.dto.FolderFileDto;
import net.myspring.general.modules.sys.domain.FolderFile;
import net.myspring.general.modules.sys.web.query.FolderFileQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface FolderFileMapper extends MyMapper<FolderFile,String> {

    Page<FolderFileDto> findPage(Pageable pageable, @Param("p") FolderFileQuery folderFileQuery);
}
