package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AccountWeixin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountWeixinMapper extends MyMapper<AccountWeixin,String> {

    AccountWeixin findByAccountId(String accountId);

}
