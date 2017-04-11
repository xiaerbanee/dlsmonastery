package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface VoucherMapper extends CrudMapper<Voucher,String> {

    Page<Voucher> findPage(Pageable pageable, @Param("p")VoucherQuery voucherQuery);
}
