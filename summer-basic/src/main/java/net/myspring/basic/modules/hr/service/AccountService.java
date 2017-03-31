package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.util.cahe.CacheReadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liuj on 2017/3/19.
 */
@Service
public class AccountService {
    @Autowired
    private AccountManager accountManager;

    @Autowired
    private RedisTemplate redisTemplate;

    public Account findOne(String id) {
        return accountManager.findOne(id);
    }

    public Account save() {
        Set<String> keys = redisTemplate.keys("*");
        Object object=redisTemplate.opsForValue().get("account:1");
        sample();
        pipelineSample();
        return null;
    }
    public void sample(){
        long start = System.currentTimeMillis();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        List<byte[]> keys = Lists.newArrayList();
        for(int i=1;i<=50000;i++) {
            String key = "account:" + i;
            keys.add(key.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println(" without pipeline used [" + (end - start)  + "] millseconds ..");
    }

    public void pipelineSample(){
        long start = System.currentTimeMillis();
        RedisCallback<List<Object>> pipelineCallback = connection -> {
            connection.openPipeline();
            for(int i=1;i<=50000;i++) {
                connection.get(("account:" + i).getBytes());
            }
            return connection.closePipeline();
        };
        List<byte[]> results = (List<byte[]>)redisTemplate.execute(pipelineCallback);
        long end = System.currentTimeMillis();


        for(int i = 0;i<results.size();i++) {
            if(results.get(i) != null) {
                Account account = (Account) CacheReadUtils.deSerialize(results.get(i));
            }
        }
        System.out.println("with pipeline used [" + (end - start)  + "] millseconds ..");
    }

}
