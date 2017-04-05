package net.myspring.cloud.common.dataSource;

import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DynamicDataSourceContext.get().getDataSourceType();
        if(DataSourceTypeEnum.SYS.name().equals(dataSourceType)) {
            return dataSourceType;
        } else {
            return dataSourceType + "_" + DynamicDataSourceContext.get().getCompanyId();
        }
    }
}
