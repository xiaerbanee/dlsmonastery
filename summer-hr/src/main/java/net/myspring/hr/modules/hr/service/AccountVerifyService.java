package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.AccountVerifyMapper;

@Service
public class AccountVerifyService {

    @Autowired
    private AccountVerifyMapper accountVerifyMapper;

}
