package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface RecruitMapper extends MyMapper<Recruit,String> {

    Page<Recruit> findPage(Pageable pageable, @Param("p")RecruitQuery recruitQuery);

    Recruit findByMobilePhone(String mobilePhone);

}
