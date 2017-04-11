package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.VoucherEntryFlow;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface VoucherEntryFlowMapper extends CrudMapper<VoucherEntryFlow,String> {

    List<VoucherEntryFlow> findByVoucherEntryId(@Param("voucherEntryId")String voucherEntryId);
}
