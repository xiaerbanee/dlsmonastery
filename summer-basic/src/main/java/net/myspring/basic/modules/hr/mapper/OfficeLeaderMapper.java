package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.modules.hr.domain.OfficeLeader;
import net.myspring.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * Created by wangzm on 2017/5/3.
 */
@Mapper
public interface OfficeLeaderMapper extends MyMapper<OfficeLeader,String> {

    int setEnabledByOfficeId(@Param("enabled")boolean enabled,@Param("officeId") String officeId);

    int setEnabledByLeaderIds(@Param("enabled")boolean enabled,@Param("leaderList") List<String> leaderList);

    List<OfficeLeader> findByOfficeId(String officeId);

    List<OfficeLeader> findAllByOfficeId(String officeId);
}
