package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by zhucc on 2017/4/17.
 */
@Mapper
public interface CompanyConfigMapper extends MyMapper<CompanyConfig,String>{

    Page<CompanyConfigDto> findPage(Pageable pageable, @Param("p")CompanyConfigQuery companyConfigQuery);
}
