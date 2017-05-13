package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangzm on 2017/5/12.
 */
@Mapper
public interface AfterSaleDetailMapper extends MyMapper<AfterSaleDetail,String>{

    List<AfterSaleDetail> findByAfterSaleListAndType(@Param("list")List<String> afterSaleIdList,@Param("type")String type);
}
