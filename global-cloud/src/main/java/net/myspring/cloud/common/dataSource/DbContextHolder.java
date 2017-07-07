package net.myspring.cloud.common.dataSource;

import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;

public class DbContextHolder {
    private String kingdeeName;
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
        this.kingdeeName=null;
        this.dataSourceType=null;
        threadLocal.remove();
    }

    public String getKingdeeName() {
        //如果没有设置当前金蝶帐套，默认为当前登录者的公司名字
        if(StringUtils.isBlank(kingdeeName) && StringUtils.isNotBlank(RequestUtils.getCompanyName())) {
            kingdeeName = RequestUtils.getCompanyName();
        }
        return kingdeeName;
    }

    public void setKingdeeName(String kingdeeName) {
        this.kingdeeName = kingdeeName;
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
            return getDataSourceType() + "_" + getKingdeeName();
        }
    }
}
