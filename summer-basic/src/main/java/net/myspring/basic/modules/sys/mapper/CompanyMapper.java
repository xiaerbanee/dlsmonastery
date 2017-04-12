package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Company;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by wangzm on 2017/4/12.
 */
@Mapper
public interface CompanyMapper extends MyMapper<Company,String>{

}
