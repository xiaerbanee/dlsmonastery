package net.myspring.basic.common.mybatis;

import com.google.common.collect.Lists;
import net.myspring.mybatis.provider.CrudProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/27.
 */
public class MyProvider extends CrudProvider {
    private Logger logger = LoggerFactory.getLogger(MyProvider.class);

    public String logicDeleteOne(Object id) {
        String sql =  "UPDATE " + getTableDto().getJdbcTable() + "SET enabled=0 WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        logger.info(sql);
        return sql;
    }

    public String logicDeleteByIds(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> values = Lists.newArrayList();
        for(int i =0;i<list.size();i++) {
            values.add("#{list[" + i + "]}");
        }
        String sql = "UPDATE " + getTableDto().getJdbcTable() + "SET enabled=0 WHERE " + getTableDto().getIdColumn().getJdbcColumn() + " IN (" + StringUtils.join(values,",") + ")";
        logger.info(sql);
        return sql;
    }

}
