package net.myspring.cloud.modules.kingdee.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface BasAssistantMapper {

    String findFNumberByNameAndDataValue(@Param("name") String name, @Param("dataValue") String dataValue);
}