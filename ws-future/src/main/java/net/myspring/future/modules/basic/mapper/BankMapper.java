package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.dto.NameValueDto;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.web.query.BankQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BankMapper extends MyMapper<Bank,String> {

    Bank findByName(String name);

    Bank findByOutId(String outId);

    Page<BankDto> findPage(Pageable pageable, @Param("p")BankQuery bankQuery);

    List<NameValueDto> findByBankId(List<String> bankId);

    int deleteBankAccount(@Param("bankId") String bankId);

    int saveAccount(@Param("bankId") String bankId, @Param("accountIds") List<String> accountIds);

    List<Bank> findLabels(List<String> ids);

    List<Bank> findByAccountId(@Param("accountId") String accountId);

    LocalDateTime getMaxOutDate();
}
