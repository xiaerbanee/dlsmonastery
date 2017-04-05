package net.myspring.uaa.mapper;

import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.uaa.dto.AccountDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountDtoMapper extends CrudMapper<AccountDto,String> {

    AccountDto findByLoginName(String loginName);
}
