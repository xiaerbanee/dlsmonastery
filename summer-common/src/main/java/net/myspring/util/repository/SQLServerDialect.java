package net.myspring.util.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * Created by liuj on 2017/4/5.
 */
public class SQLServerDialect extends Dialect {

    private static SQLServerDialect sqlServerDialect = new SQLServerDialect();

    public SQLServerDialect() {}

    public static SQLServerDialect getInstance() {
        return sqlServerDialect;
    }

    @Override
    public String getPageableSql(String sql, Pageable pageable) {
        StringBuilder sb = new StringBuilder(sql);
        if(pageable.getSort() != null) {
            sb.append(getOrder(pageable.getSort()));
        }
        sb.append(" OFFSET ").append(":offset").append(" ROWS FETCH NEXT ").append(":pageSize").append(" ROWS ONLY");
        return sb.toString();
    }

}
