package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface BankInMapper extends MyMapper<BankIn,String> {

    Page<BankInDto> findPage(Pageable pageable, @Param("p") BankInQuery bankInQuery);

    BankIn findByShopAndType(@Param("shopId") String shopId, @Param("type") String type);

    BankInDto findDto(@Param("id") String id);
}
