package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.DynamicSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/2/9.
 */
@Mapper
public interface DynamicSubjectMapper {
    List<DynamicSubject> findAll();

    List<DynamicSubject> findByAccName(@Param("accName") String accName);

    DynamicSubject findByFyName(@Param("fyName") String fyName);

    int add(@Param("accNumber") String accNumber, @Param("accName") String accName, @Param("fyNum") String fyNum, @Param("fyName") String fyName);
}
