package net.myspring.uaa.mapper;

import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountWeixinDtoMapper extends CrudMapper<AccountWeixinDto,String> {

    List<AccountWeixinDto> findByOpenId(String openId);

    AccountWeixinDto findByAccountId(String accountId);
}
