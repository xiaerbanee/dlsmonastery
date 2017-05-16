package net.myspring.mybatis.dialect;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * Created by liuj on 2017/4/5.
 */
public class SQLServerDialect extends Dialect {
    @Override
    public String getPageableSql(String sql, Pageable pageable) {
        StringBuilder sb = new StringBuilder(sql);
        if(pageable.getSort() != null) {
            sb.append(" ORDER BY ");
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sb.append(order.getProperty()).append(" ").append(order.getDirection());
                if(iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(" OFFSET ").append(pageable.getOffset()).append(" ROWS FETCH NEXT ").append(pageable.getPageSize()).append(" ROWS ONLY");
        return sb.toString();
    }

}
