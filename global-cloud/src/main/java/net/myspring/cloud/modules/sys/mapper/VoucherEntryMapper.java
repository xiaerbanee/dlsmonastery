package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.VoucherEntry;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface VoucherEntryMapper extends CrudMapper<VoucherEntry,String> {

    List<VoucherEntry> findByGlVoucherId(@Param("voucherId")String voucherId);
}
