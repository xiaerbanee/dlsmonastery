package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AfterSaleFlee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangzm on 2017/5/12.
 */
@Mapper
public interface AfterSaleFleeMapper extends MyMapper<AfterSaleFlee,String>{

    List<AfterSaleFlee> findByImeList(List<String> imeList);
}
