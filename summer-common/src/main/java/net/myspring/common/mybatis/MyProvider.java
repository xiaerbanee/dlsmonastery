package net.myspring.common.mybatis;

import com.google.common.collect.Lists;
import net.myspring.mybatis.provider.CrudProvider;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/27.
 */
public class MyProvider extends CrudProvider {
    private Logger logger = LoggerFactory.getLogger(MyProvider.class);
    public static final String LOGIC_DELETE_ONE="logicDeleteOne";
    public static final String LOGIC_DELETE_BY_IDS="logicDeleteByIds";
    public static final String FIND_ALL_ENABLED="findAllEnabled";

    public String logicDeleteOne(Object id) {
        String sql =  "UPDATE " + getTableDto().getJdbcTable() + " SET enabled=0 WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        logger.info(sql);
        return sql;
    }

    public String logicDeleteByIds(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> values = Lists.newArrayList();
        for(int i =0;i<list.size();i++) {
            values.add("#{list[" + i + "]}");
        }
        String sql = "UPDATE " + getTableDto().getJdbcTable() + " SET enabled=0 WHERE " + getTableDto().getIdColumn().getJdbcColumn() + " IN (" + StringUtils.join(values,",") + ")";
        logger.info(sql);
        return sql;
    }

    public String findAllEnabled() {
        String sql = "SELECT  * FROM " + getTableDto().getJdbcTable()+" where enabled=1";
        logger.info(sql);
        return sql;
    }

}
