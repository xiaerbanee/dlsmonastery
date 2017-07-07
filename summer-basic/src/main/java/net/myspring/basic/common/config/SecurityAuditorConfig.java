package net.myspring.basic.common.config;

import org.springframework.data.domain.AuditorAware;

/**
 * Created by liuj on 2017/5/19.
 */
public class SecurityAuditorConfig implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return AuditorContextHolder.get().getAccountId();
    }
}
