package net.myspring.uaa.security;

import com.google.common.collect.Sets;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.mapper.AccountDtoMapper;
import net.myspring.uaa.mapper.AccountWeixinDtoMapper;
import net.myspring.util.base.ObjectUtil;
import net.myspring.util.collection.CollectionUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by liuj on 2017/4/1.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountDtoMapper accountMapper;
    @Autowired
    private AccountWeixinDtoMapper accountWeixinDtoMapper;
    @Autowired
    private WeixinManager weixinManager;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = null;
        AccountDto accountDto = null;
        HttpServletRequest request  = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String weixinCode = request.getParameter("weixinCode");
        if(StringUtils.isNotBlank(weixinCode)) {
            String accountId = ObjectUtils.toString(request.getAttribute("accountId"));
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinCode);
            if(weixinSessionDto != null && StringUtils.isNotBlank(weixinSessionDto.getOpenid())) {
                List<AccountWeixinDto> accountWeixinDtoList = accountWeixinDtoMapper.findByOpenId(weixinSessionDto.getOpenid());
                if(CollectionUtil.isNotEmpty(accountWeixinDtoList)) {
                    Map<String,AccountWeixinDto>  accountWeixinDtoMap = CollectionUtil.extractToMap(accountWeixinDtoList,"accountId");
                    if(!accountWeixinDtoMap.containsKey(accountId)) {
                        accountId = null;
                    }
                    if(StringUtils.isBlank(accountId)) {
                        accountId = accountWeixinDtoList.get(0).getAccountId();
                    }
                }
            }
            if(StringUtils.isNotBlank(accountId)) {
                accountDto = accountMapper.findById(accountId);
                username=accountDto.getUsername();
            }
        } else {
            accountDto = accountMapper.findByLoginName(username);
        }
        if(accountDto != null) {
            LocalDate leaveDate = accountDto.getLeaveDate();
            boolean accountNoExpired = leaveDate == null || leaveDate.isAfter(LocalDate.now());
            Set<SimpleGrantedAuthority> authList = Sets.newHashSet();
            authList.add(new SimpleGrantedAuthority(accountDto.getPositionId()));
            customUserDetails = new CustomUserDetails(
                    username,
                    accountDto.getPassword(),
                    accountDto.getEnabled(),
                    accountNoExpired,
                    true,
                    !accountDto.getLocked(),
                    authList,
                    accountDto.getId(),
                    accountDto.getCompanyId(),
                    accountDto.getPositionId(),
                    accountDto.getOfficeId(),
                    accountDto.getEmployeeId()
            );
        }
        return customUserDetails;
    }

}
