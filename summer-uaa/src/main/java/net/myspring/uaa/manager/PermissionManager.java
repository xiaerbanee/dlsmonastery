package net.myspring.uaa.manager;

import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.uaa.dto.PermissionDto;
import net.myspring.uaa.repository.AccountPermissionRepository;
import net.myspring.uaa.repository.PermissionRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private RedisTemplate redisTemplate;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    public void permissionCachePut(String companyName,String accountId,String roleId){
        String key = "authorityCache:" + companyName+accountId;
        redisTemplate.expire(key,24, TimeUnit.HOURS);
        List<PermissionDto> permissionList;
        if(net.myspring.util.text.StringUtils.getSplitList(adminIdList, CharConstant.COMMA).contains(accountId)){
            permissionList=permissionRepository.findAllEnabled();
        }else {
            List<String> accountPermissions=accountPermissionRepository.findPermissionIdByAccountId(accountId);
            if(CollectionUtil.isNotEmpty(accountPermissions)){
                permissionList=permissionRepository.findByRoleAndAccount(roleId,accountId);
            }else {
                permissionList=permissionRepository.findByRoleId(roleId);
            }
        }
        Map<String,String> map= Maps.newHashMap();
        for(PermissionDto permissionDto:permissionList){
            List<String> uslList= StringUtils.getSplitList(permissionDto.getUrl(),CharConstant.COMMA);
            for(String url:uslList){
                String permissionKey=url+permissionDto.getMethod();
                if(!map.containsKey(permissionKey)){
                    map.put(permissionKey,permissionDto.getPermission());
                }
            }
        }
        redisTemplate.opsForValue().set(key,map);
    }
}
