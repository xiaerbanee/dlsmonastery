package net.myspring.mybatis.provider;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * Created by liuj on 2017/3/18.
 */
public class PagingAndSortingProvider extends CrudProvider {

    public String findAllBySort(Sort sort) {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + getTableDto().getJdbcTable());
        if(sort != null) {
            sb.append(" ORDER BY ");
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sb.append(order.getProperty()).append(" ").append(order.getDirection());
                if(iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    public String findAllByPageable(Pageable pageable) {
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable();
        return sql;
    }
}
