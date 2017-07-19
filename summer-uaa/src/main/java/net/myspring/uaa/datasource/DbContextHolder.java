package net.myspring.uaa.datasource;

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
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
