package net.myspring.uaa.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liuj on 2017/4/1.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<SimpleGrantedAuthority> authList = new TreeSet<SimpleGrantedAuthority>(new SimpleGrantedAuthorityComparator());
        CustomUserDetails customUserDetails = new CustomUserDetails(
                username,
                "reader",
                true,
                true,
                true,
                true,
                getAuthorities(),
                "1"
        );
        return customUserDetails;
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authList = new TreeSet<SimpleGrantedAuthority>(new SimpleGrantedAuthorityComparator());
        authList.add(new SimpleGrantedAuthority("FOO_READ"));
        authList.add(new SimpleGrantedAuthority("FOO_TEST"));
        return authList;
    }


    private static class SimpleGrantedAuthorityComparator implements Comparator<SimpleGrantedAuthority> {
        @Override
        public int compare(SimpleGrantedAuthority o1, SimpleGrantedAuthority o2) {
            return o1.equals(o2) ? 0 : -1;
        }
    }
}
