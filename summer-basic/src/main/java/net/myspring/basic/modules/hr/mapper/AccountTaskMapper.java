package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.dto.AccountTaskDto;
import net.myspring.basic.modules.hr.web.query.AccountTaskQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountTaskMapper extends MyMapper<AccountTask,String> {

    Page<AccountTaskDto> findPage(Pageable pageable, @Param("p")AccountTaskQuery accountTaskQuery);

    AccountTask findByNameAndExtendId(@Param("name") String name, @Param("extendId") String extendId);

    List<AccountTask> findLabels(List<String> ids);

    List<AccountTask> findByPositionAndOfficeIds(@Param("positionId") String positionId, @Param("officeIds") List<String> officeIds);
}
