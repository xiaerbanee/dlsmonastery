package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.AccountMessage;
import net.myspring.basic.modules.hr.dto.AccountMessageDto;
import net.myspring.basic.modules.hr.repository.AccountMessageRepository;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountMessageService {

    @Autowired
    private AccountMessageRepository accountMessageRepository;

    public List<AccountMessageDto> findByAccount(String accountId, LocalDateTime localDateTime){
        List<AccountMessage> accountMessages=accountMessageRepository.findByAccountId(accountId,localDateTime);
        List<AccountMessageDto> accountMessageDtoList= BeanUtil.map(accountMessages, AccountMessageDto.class);
        return accountMessageDtoList;
    }

}
