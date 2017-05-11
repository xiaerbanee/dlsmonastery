package net.myspring.cloud.modules.sys.mapper;

import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto;
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by lihx on 2017/5/9.
 */
@Mapper
public interface AccountKingdeeBookMapper extends CrudMapper<AccountKingdeeBook,String> {

    AccountKingdeeBook findByAccountId(@Param("accountId")String accountId);

    Page<AccountKingdeeBookDto> findPage(Pageable pageable, @Param("p")AccountKingdeeBookQuery accountKingdeeBookQuery);

}
