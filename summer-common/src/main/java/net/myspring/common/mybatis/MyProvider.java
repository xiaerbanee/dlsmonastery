package net.myspring.common.mybatis;

import com.google.common.collect.Lists;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;
import net.myspring.mybatis.provider.BaseProvider;
import net.myspring.mybatis.provider.CrudProvider;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/27.
 */
public class MyProvider<T, ID extends Serializable> extends BaseProvider<T, ID> {
    public static final String SAVE = "save";
    public static final String BATCH_SAVE = "batchSave";
    public static final String FIND_ONE = "findOne";
    public static final String EXISTS = "exists";
    public static final String FIND_ALL = "findAll";
    public static final String FIND_BY_IDS = "findByIds";
    public static final String COUNT = "count";
    public static final String UPDATE = "update";
    private Logger logger = LoggerFactory.getLogger(MyProvider.class);

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

    public String save(T entity) {
        ArrayList jdbcColumns = Lists.newArrayList();
        ArrayList javaInstances = Lists.newArrayList();
        Iterator sb = this.getTableDto().getColumnList().iterator();

        while(sb.hasNext()) {
            ColumnDto sql = (ColumnDto)sb.next();
            boolean insertable = this.getInsertable(entity, sql).booleanValue();
            if(insertable) {
                jdbcColumns.add(sql.getJdbcColumn());
                javaInstances.add("#{" + sql.getJavaInstance() + "}");
            }
        }

        StringBuilder sb1 = new StringBuilder("INSERT INTO ");
        sb1.append(this.getTableDto().getJdbcTable());
        sb1.append(" (");
        sb1.append(org.apache.commons.lang3.StringUtils.join(jdbcColumns, ","));
        sb1.append(") ");
        sb1.append(" VALUES(");
        sb1.append(org.apache.commons.lang3.StringUtils.join(javaInstances, ","));
        sb1.append(")");
        String sql1 = sb1.toString();
        this.logger.info(sql1);
        return sql1;
    }

    public String batchSave(Map map) {
        List<T> list = (List)map.get("list");
        ArrayList jdbcColumns = Lists.newArrayList();
        ArrayList javaInstances = Lists.newArrayList();
        ArrayList insertValues = Lists.newArrayList();
        TableDto tableDto = this.getTableDto();
        Iterator sb = tableDto.getColumnList().iterator();

        while(sb.hasNext()) {
            ColumnDto sql = (ColumnDto)sb.next();
            boolean insertItem = this.getInsertable(list.get(0), sql).booleanValue();
            if(insertItem) {
                jdbcColumns.add(sql.getJdbcColumn());
                javaInstances.add(sql.getJavaInstance());
            }
        }

        StringBuilder var13 = new StringBuilder("INSERT INTO ");
        var13.append(tableDto.getJdbcTable());
        var13.append(" (");
        var13.append(StringUtils.join(jdbcColumns, ","));
        var13.append(") ");
        var13.append(" VALUES");

        for(int var14 = 0; var14 < list.size(); ++var14) {
            ArrayList var16 = Lists.newArrayList();
            StringBuilder insertValue = new StringBuilder("(");
            Iterator var11 = javaInstances.iterator();

            while(var11.hasNext()) {
                String javaInstance = (String)var11.next();
                var16.add("#{list[" + var14 + "]." + javaInstance + "}");
            }

            insertValue.append(org.apache.commons.lang3.StringUtils.join(var16, ","));
            insertValue.append(")");
            insertValues.add(insertValue.toString());
        }

        var13.append(org.apache.commons.lang3.StringUtils.join(insertValues, ","));
        String var15 = var13.toString();
        this.logger.info(var15);
        return var15;
    }

    public String findOne(ID id) {
        String sql = "SELECT * FROM " + this.getTableDto().getJdbcTable() + " WHERE " + this.getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        this.logger.info(sql);
        return sql;
    }

    public String exists(ID id) {
        String sql = "SELECT COUNT(*) FROM " + this.getTableDto().getJdbcTable() + " WHERE " + this.getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        this.logger.info(sql);
        return sql;
    }

    public String findAll() {
        String sql = "SELECT * FROM " + this.getTableDto().getJdbcTable();
        this.logger.info(sql);
        return sql;
    }

    public String findByIds(Map map) {
        List list = (List)map.get("list");
        ArrayList values = Lists.newArrayList();

        for(int sql = 0; sql < list.size(); ++sql) {
            values.add("#{list[" + sql + "]}");
        }

        String var5 = "SELECT * FROM " + this.getTableDto().getJdbcTable() + " WHERE " + this.getTableDto().getIdColumn().getJdbcColumn() + " IN (" + org.apache.commons.lang3.StringUtils.join(values, ",") + ")";
        this.logger.info(var5);
        return var5;
    }

    public String count() {
        String sql = "SELECT COUNT(*) FROM " + this.getTableDto().getJdbcTable();
        this.logger.info(sql);
        return sql;
    }

    public String update(T entity) {
        ArrayList jdbcColumns = Lists.newArrayList();
        Iterator sb = this.getTableDto().getColumnList().iterator();

        while(sb.hasNext()) {
            ColumnDto sql = (ColumnDto)sb.next();
            boolean updatable = this.getUpdatable(entity, sql).booleanValue();
            if(updatable) {
                jdbcColumns.add(sql.getJdbcColumn() + " = #{" + sql.getJavaInstance() + "}");
            }
        }

        StringBuilder sb1 = new StringBuilder("UPDATE ");
        sb1.append(this.getTableDto().getJdbcTable());
        sb1.append(" SET ");
        sb1.append(org.apache.commons.lang3.StringUtils.join(jdbcColumns, ","));
        sb1.append(" WHERE ");
        sb1.append(this.getTableDto().getIdColumn().getJdbcColumn());
        sb1.append(" = ");
        sb1.append("#{" + this.getTableDto().getIdColumn().getJavaInstance() + "}");
        String sql1 = sb1.toString();
        this.logger.info(sql1);
        return sql1;
    }

}
