package net.myspring.tool.common.dataSource;

import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;

public class DbContextHolder {
    private String companyName;
    private String dataSourceType;

    private static ThreadLocal<DbContextHolder> threadLocal = new ThreadLocal<DbContextHolder>();

    public static DbContextHolder get() {
        if (threadLocal.get() == null) {
            DbContextHolder dbContextHolder = new DbContextHolder();
            threadLocal.set(dbContextHolder);
        }
        return threadLocal.get();
    }

    public void remove() {
        threadLocal.remove();
    }

    public String getCompanyName() {
        if(StringUtils.isNotBlank(RequestUtils.getCompanyName())) {
            companyName = RequestUtils.getCompanyName();
        }
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDataSourceType() {
        if(StringUtils.isBlank(dataSourceType)) {
            dataSourceType = DataSourceTypeEnum.LOCAL.name();
        }
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getCurrentLookupKey() {
        if(DataSourceTypeEnum.LOCAL.name().equals(getDataSourceType())) {
            return DataSourceTypeEnum.LOCAL.name();
        } else {
            return getDataSourceType() + "_" + getCompanyName();
        }
    }
}
