package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.modules.hr.domain.OfficeLeader;
import net.myspring.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangzm on 2017/5/3.
 */
@Mapper
public interface OfficeLeaderMapper extends MyMapper<OfficeLeader,String> {

    int removeOfficeLeaderByOffice(String officeId);

    List<OfficeLeader> findByOfficeId(String officeId);
}
