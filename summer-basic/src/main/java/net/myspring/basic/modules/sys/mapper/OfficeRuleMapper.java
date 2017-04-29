package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by wangzm on 2017/4/22.
 */
@Mapper
public interface OfficeRuleMapper extends MyMapper<OfficeRule,String>{

    Page<OfficeRuleDto> findPage(Pageable pageable, @Param("p")OfficeRuleQuery officeRuleQuery);

    OfficeRule findByName(String name);
}
