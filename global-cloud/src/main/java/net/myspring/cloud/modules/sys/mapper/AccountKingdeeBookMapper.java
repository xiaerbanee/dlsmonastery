package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihx on 2017/5/9.
 */
@Mapper
public interface AccountKingdeeBookMapper extends CrudMapper<AccountKingdeeBook,String> {

    AccountKingdeeBook findByAccountId(@Param("accountId")String accountId);
}
