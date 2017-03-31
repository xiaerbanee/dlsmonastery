package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountMapper extends MyMapper<Account,String> {

    Page<Account> findPage(Pageable pageable);
}
