package net.myspring.uaa.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.uaa.dto.PermissionDto;
import net.myspring.uaa.repository.AccountPermissionRepository;
import net.myspring.uaa.repository.PermissionRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PermissionManager {
    @Value("${setting.adminIdList}")
    private String adminIdList;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AccountPermissionRepository accountPermissionRepository;
}
