package net.myspring.basic.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.hr.mapper.AccountTaskMapper;

@Service
public class AccountTaskService {

    @Autowired
    private AccountTaskMapper accountTaskMapper;

}
