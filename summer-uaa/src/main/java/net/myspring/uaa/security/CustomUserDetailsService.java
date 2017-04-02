package net.myspring.uaa.security;

import com.google.common.collect.Sets;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.uaa.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by liuj on 2017/4/1.
 */
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.findByLoginName(username);
        CustomUserDetails customUserDetails = null;
        if(account != null) {
            LocalDate leaveDate = account.getEmployee().getLeaveDate();
            boolean accountNoExpired = leaveDate == null || leaveDate.isAfter(LocalDate.now());
            Set<SimpleGrantedAuthority> authList = Sets.newHashSet();
            authList.add(new SimpleGrantedAuthority(account.getPositionId()));
            customUserDetails = new CustomUserDetails(
                    username,
                    account.getPassword(),
                    account.getEnabled(),
                    accountNoExpired,
                    true,
                    !account.getLocked(),
                    authList,
                    account.getId(),
                    account.getCompanyId(),
                    account.getPositionId()
            );
        }
        return customUserDetails;
    }

}
