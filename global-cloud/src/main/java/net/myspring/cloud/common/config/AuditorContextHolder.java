package net.myspring.cloud.common.config;

import net.myspring.cloud.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;

public class AuditorContextHolder {
    private String accountId;

    private static ThreadLocal<AuditorContextHolder> threadLocal = new ThreadLocal<AuditorContextHolder>();

    public static AuditorContextHolder get() {
        if (threadLocal.get() == null) {
            AuditorContextHolder auditorContextHolder = new AuditorContextHolder();
            threadLocal.set(auditorContextHolder);
        }
        return threadLocal.get();
    }

    public void remove() {
        threadLocal.remove();
    }

    public String getAccountId() {
        if(StringUtils.isBlank(accountId)) {
            accountId = RequestUtils.getAccountId();
        }
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
