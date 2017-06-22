package net.myspring.tool.common.dataSource;

import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = RequestUtils.getRequestEntity().getDataSourceType();
        if(StringUtils.isBlank(dataSourceType)){
            dataSourceType=DataSourceTypeEnum.LOCAL.name();
        }
        if(DataSourceTypeEnum.LOCAL.name().equals(dataSourceType)) {
            return dataSourceType;
        } else {
            return dataSourceType + "_" + RequestUtils.getCompanyId();
        }
    }
}
