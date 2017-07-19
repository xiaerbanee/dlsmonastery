package net.myspring.future.common.datasource;

import net.myspring.future.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;

public class DbContextHolder {
    private String companyName;

    private static ThreadLocal<DbContextHolder> threadLocal = new ThreadLocal<DbContextHolder>();

    public static DbContextHolder get() {
        if (threadLocal.get() == null) {
            DbContextHolder dbContextHolder = new DbContextHolder();
            threadLocal.set(dbContextHolder);
        }
        return threadLocal.get();
    }

    public void remove() {
        this.companyName=null;
        threadLocal.remove();
    }

    public String getCompanyName() {
        String requestCompanyName = RequestUtils.getCompanyName();
        if(StringUtils.isNotBlank(requestCompanyName)) {
            return requestCompanyName;
        } else {
            return companyName;
        }
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
