package net.myspring.mybatis.provider;

import net.myspring.mybatis.context.ProviderContextUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * Created by liuj on 2017/3/18.
 */
public class PagingAndSortingProvider extends CrudProvider {

    public String findAllBySort(Sort sort) {
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable();
        if(sort != null) {
            sql = sql + ProviderContextUtils.getSql(sort);
        }
        return sql;
    }

    public String findAllByPageable(Pageable pageable) {
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable();
        return sql;
    }
}
