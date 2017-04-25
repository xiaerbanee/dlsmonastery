package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BasAssistant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface BasAssistantMapper {

    String findFNumberByNameAndDataValue(@Param("name") String name, @Param("dataValue") String dataValue);

    List<BasAssistant> findByType(@Param("type")String type);
}
