package net.myspring.tool.modules.imoo.mapper;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ImooPrdocutImeiDeliverMapper {

    List<String> findImeis(List<String> imeis);

    int save(List<ImooPrdocutImeiDeliver> list);

    List<ImooPrdocutImeiDeliver>findSynList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("agentCodes") List<String> agentCodes);
}
