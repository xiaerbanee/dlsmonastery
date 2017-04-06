package net.myspring.uaa.security;

import com.google.common.collect.Sets;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.mapper.AccountDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by liuj on 2017/4/1.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountDtoMapper accountMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDto accountDto = accountMapper.findByLoginName(username);
        CustomUserDetails customUserDetails = null;
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
