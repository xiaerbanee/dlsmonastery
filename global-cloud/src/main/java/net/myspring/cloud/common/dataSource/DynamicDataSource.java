package net.myspring.cloud.common.dataSource;

import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DataSourceTypeEnum.LOCAL.name();
        if(RequestUtils.getRequestEntity() != null && RequestUtils.getRequestEntity().getDataSourceType() != null) {
            dataSourceType = RequestUtils.getRequestEntity().getDataSourceType();
        };
        if(DataSourceTypeEnum.LOCAL.name().equals(dataSourceType)) {
            return dataSourceType;
        } else {
            return dataSourceType + "_" + RequestUtils.getCompanyId();
        }
    }
}
