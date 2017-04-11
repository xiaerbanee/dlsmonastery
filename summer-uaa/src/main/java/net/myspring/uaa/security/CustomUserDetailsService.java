package net.myspring.uaa.security;

import com.google.common.collect.Sets;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.mapper.AccountDtoMapper;
import net.myspring.util.base.ObjectUtil;
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
    private WeixinManager weixinManager;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = null;
        HttpServletRequest request  = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String weixinCode = ObjectUtils.toString(request.getAttribute("weixinCode"));
        if(StringUtils.isNotBlank(weixinCode)) {
            String accountId = ObjectUtils.toString(request.getAttribute("accountId"));
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinCode);
            if(weixinSessionDto != null && StringUtils.isNotBlank(weixinSessionDto.getOpenid())) {

            }

        } else {
            AccountDto accountDto = accountMapper.findByLoginName(username);
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
        }
        return customUserDetails;
    }

}
