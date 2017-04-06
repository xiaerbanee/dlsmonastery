package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountTaskMapper extends MyMapper<AccountTask,String> {

    Page<AccountTask> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    AccountTask findByNameAndExtendId(@Param("name") String name, @Param("extendId") String extendId);

    List<AccountTask> findLabels(List<String> ids);

    List<AccountTask> findByPositionAndOfficeIds(@Param("positionId") String positionId, @Param("officeIds") List<String> officeIds);
}
