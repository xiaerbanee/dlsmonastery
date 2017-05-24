package net.myspring.general.common.config;

import net.myspring.general.common.utils.RequestUtils;
import org.springframework.data.domain.AuditorAware;

/**
 * Created by liuj on 2017/5/19.
 */
public class SecurityAuditorConfig implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return RequestUtils.getAccountId();
    }
}
