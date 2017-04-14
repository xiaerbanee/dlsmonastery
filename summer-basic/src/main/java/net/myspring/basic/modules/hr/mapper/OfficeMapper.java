package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.web.query.OfficeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface OfficeMapper extends MyMapper<Office,String> {

    List<Office> findByType(String type);

    List<Office> findByAccountId(String accountId);

    List<Office> findByAccountIds(List<String> accountIds);

    List<Office> findByParentIdsLike(String parentId);

    List<Office> findByFilter(@Param("p") Map<String, Object> map);

    List<Office> findByFilterAll(@Param("p") Map<String, Object> map);

    List<Office> findLabels(List<String> ids);

    List<Office> findByAreaIds(List<String> areaIds);

    Page<Office> findPage(@Param("pageable") Pageable pageable, @Param("p")OfficeQuery officeQuery);
}
