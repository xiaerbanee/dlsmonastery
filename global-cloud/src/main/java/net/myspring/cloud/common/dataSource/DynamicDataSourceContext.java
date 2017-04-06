package net.myspring.cloud.common.dataSource;

public class DynamicDataSourceContext  {
    private String dataSourceType;
    private String companyId;
    private boolean automaticSetCompany = true;
    private static ThreadLocal<DynamicDataSourceContext> threadLocal = new ThreadLocal<DynamicDataSourceContext>();

    public static DynamicDataSourceContext get() {
        if (threadLocal.get() == null) {
            DynamicDataSourceContext threadLocalContext = new DynamicDataSourceContext();
            threadLocal.set(threadLocalContext);
        }
        return threadLocal.get();
    }

    public void remove() {
        this.dataSourceType=null;
        this.companyId = null;
        threadLocal.remove();
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isAutomaticSetCompany() {
        return automaticSetCompany;
    }

    public void setAutomaticSetCompany(boolean automaticSetCompany) {
        this.automaticSetCompany = automaticSetCompany;
    }
}