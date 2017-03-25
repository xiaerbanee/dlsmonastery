package net.myspring.hr.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.hr.domain.Account;
import net.myspring.hr.manager.AccountManager;
import net.myspring.hr.mapper.AccountMapper;
import net.myspring.hr.web.form.AccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Connection;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
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
        for(int i=1;i<=50000;i++) {
            String key = "account:" + i;
            Object a = connection.get(key.getBytes());
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
                Account account = (Account)deSerialize(results.get(i));
            }
        }
        System.out.println("with pipeline used [" + (end - start)  + "] millseconds ..");
    }

    public Object deSerialize(byte[] bytes){
        Object obj=null;
        try {
            ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
            ObjectInputStream ois=new ObjectInputStream(bais);
            obj=ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
