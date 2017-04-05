package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.GlVoucherEntry;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface GlVoucherEntryMapper extends CrudMapper<GlVoucherEntry,String> {
}
