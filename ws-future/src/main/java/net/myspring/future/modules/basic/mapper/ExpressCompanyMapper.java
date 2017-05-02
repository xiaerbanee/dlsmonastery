package net.myspring.future.modules.basic.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.ExpressCompany;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ExpressCompanyMapper extends MyMapper<ExpressCompany,String> {

    Page<ExpressCompanyDto> findPage(Pageable pageable, @Param("p")ExpressCompanyQuery expressCompanyQuery);

    List<ExpressCompany> findLabels(List<String> ids);

    List<ExpressCompany> findByExpressType(String type);
}
