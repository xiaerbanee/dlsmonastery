package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.AccountMessage;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.dto.AccountMessageDto;
import net.myspring.basic.modules.hr.mapper.AccountMessageMapper;
import net.myspring.util.mapper.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountMessageService {

    @Autowired
    private AccountMessageMapper accountMessageMapper;

    public List<AccountMessageDto> findByAccount(String accountId, LocalDateTime localDateTime){
        List<AccountMessage> accountMessages=accountMessageMapper.findByAccountId(accountId,localDateTime);
        List<AccountMessageDto> accountMessageDtoList= BeanMapper.convertDtoList(accountMessages, AccountMessageDto.class);
        return accountMessageDtoList;
    }

}
