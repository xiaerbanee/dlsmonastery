package net.myspring.uaa.mapper;

import net.myspring.uaa.dto.AccountDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountDtoMapper {

    AccountDto findByLoginName(String loginName);

    AccountDto findById(String id);
}
