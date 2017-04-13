package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.model.NameValue;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Bank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface BankMapper extends MyMapper<Bank,String> {

    Bank findByName(String name);

    Bank findByOutId(String outId);

    Page<Bank> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<NameValue> findByBankId(List<String> bankId);

    int deleteBankAccount(@Param("bankId") String bankId);

    int saveAccount(@Param("bankId") String bankId, @Param("accountIds") List<String> accountIds);

    List<Bank> findLabels(List<String> ids);

    List<Bank> findByAccountId(@Param("accountId") String accountId);

    LocalDateTime getMaxOutDate();
}
