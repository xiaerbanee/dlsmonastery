package net.myspring.uaa.mapper;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountMapper extends CrudMapper<Account,String> {
    Account findByLoginName(String loginName);
}
