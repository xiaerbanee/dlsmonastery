package net.myspring.uaa.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.uaa.mapper.AccountMapper;
import net.myspring.util.cahe.CacheReadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by liuj on 2017/3/19.
 */
@Service
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

}
