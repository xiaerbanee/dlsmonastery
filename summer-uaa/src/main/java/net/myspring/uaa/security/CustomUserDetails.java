package net.myspring.uaa.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liuj on 2017/4/1.
 */
public class CustomUserDetails implements UserDetails {
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final String accountId;
    private final String companyId;
    private final String positionId;
    private final String officeId;
    private final String employeeId;

    public CustomUserDetails(String username,String password,boolean enabled,boolean accountNonExpired,boolean credentialsNonExpired,boolean accountNonLocked,Collection<? extends GrantedAuthority> authorities,String accountId,String companyId,String positionId,String officeId,String employeeId) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Sets.newHashSet(authorities);
        this.accountId = accountId;
        this.companyId = companyId;
        this.positionId = positionId;
        this.officeId=officeId;
        this.employeeId=employeeId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getPositionId() {
        return positionId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
