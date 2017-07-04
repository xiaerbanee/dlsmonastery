package net.myspring.cloud.common.dataSource;

import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DataSourceTypeEnum.LOCAL.name();
        if(StringUtils.isNotBlank(RequestUtils.getDataSourceType())) {
            dataSourceType = RequestUtils.getDataSourceType();
        };
        if(DataSourceTypeEnum.LOCAL.name().equals(dataSourceType)) {
            return dataSourceType;
        } else {
            return dataSourceType + "_" + RequestUtils.getCompanyName();
        }
    }
}
