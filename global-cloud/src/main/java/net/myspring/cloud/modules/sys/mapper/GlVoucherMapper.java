package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.GlVoucher;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface GlVoucherMapper extends CrudMapper<GlVoucher,String> {

}
