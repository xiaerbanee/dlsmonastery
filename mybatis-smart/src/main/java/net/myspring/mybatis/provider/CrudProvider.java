package net.myspring.mybatis.provider;

import com.google.common.collect.Lists;
import net.myspring.mybatis.dto.ColumnDto;
import net.myspring.mybatis.dto.TableDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2016/11/12.
 */
public class CrudProvider<T, ID extends Serializable> extends BaseProvider<T,ID> {

    private Logger logger = LoggerFactory.getLogger(CrudProvider.class);
    public static final String SAVE="save";
    public static final String BATCH_SAVE="batchSave";
    public static final String FIND_ONE="findOne";
    public static final String EXISTS="exists";
    public static final String FIND_ALL="findAll";
    public static final String FIND_BY_IDS="findByIds";
    public static final String COUNT="count";
    public static final String DELETE_BY_ID="deleteById";
    public static final String DELETE_BY_IDS="deleteByIds";
    public static final String DELETE_BY_ENTITY="deleteByEntity";
    public static final String BATCH_DELETE="batchDelete";
    public static final String DELETE_ALL="deleteAll";
    public static final String UPDATE="update";

    public String save(T entity) {
        List<String> jdbcColumns = Lists.newArrayList();
        List<String> javaInstances = Lists.newArrayList();
        for(ColumnDto columnDto:getTableDto().getColumnList()) {
            boolean insertable = getInsertable(entity,columnDto);
            if(insertable) {
                jdbcColumns.add(columnDto.getJdbcColumn());
                javaInstances.add("#{" + columnDto.getJavaInstance() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(getTableDto().getJdbcTable());
        sb.append(" (");
        sb.append(StringUtils.join(jdbcColumns,","));
        sb.append(") ");
        sb.append(" VALUES(");
        sb.append(StringUtils.join(javaInstances,","));
        sb.append(")");
        String sql = sb.toString();
        logger.info(sql);
        return sql;
    }

    public String batchSave(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> jdbcColumns = Lists.newArrayList();
        List<String> javaInstances = Lists.newArrayList();
        List<String> insertValues = Lists.newArrayList();
        TableDto tableDto = getTableDto();
        for(ColumnDto columnDto:tableDto.getColumnList()) {
            boolean insertable = getInsertable((T)list.get(0),columnDto);
            if(insertable) {
                jdbcColumns.add(columnDto.getJdbcColumn());
                javaInstances.add(columnDto.getJavaInstance());
            }
        }
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableDto.getJdbcTable());
        sb.append(" (");
        sb.append(StringUtils.join(jdbcColumns,","));
        sb.append(") ");
        sb.append(" VALUES");
        for(int i=0;i<list.size();i++) {
            List<String> insertItem = Lists.newArrayList();
            StringBuilder insertValue = new StringBuilder("(");
            for(String javaInstance:javaInstances) {
                insertItem.add("#{list[" +  i + "]." + javaInstance + "}");
            }
            insertValue.append(StringUtils.join(insertItem,","));
            insertValue.append(")");
            insertValues.add(insertValue.toString());
        }
        sb.append(StringUtils.join(insertValues,","));
        String sql = sb.toString();
        logger.info(sql);
        return sql;
    }

    public String  findOne(ID id) {
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        logger.info(sql);
        return sql;
    }

    public String  exists(ID id) {
        String sql = "SELECT COUNT(*) FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        logger.info(sql);
        return sql;
    }

    public String findAll() {
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable();
        logger.info(sql);
        return sql;
    }

    public String findByIds(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> values = Lists.newArrayList();
        for(int i =0;i<list.size();i++) {
            values.add("#{list[" + i + "]}");
        }
        String sql = "SELECT * FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + " IN (" + StringUtils.join(values,",") + ")";
        logger.info(sql);
        return sql;
    }

    public String count() {
        String sql = "SELECT COUNT(*) FROM " + getTableDto().getJdbcTable();
        logger.info(sql);
        return sql;
    }

    public String deleteById(ID id) {
        String sql =  "DELETE FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "=#{id}";
        logger.info(sql);
        return sql;
    }

    public String deleteByIds(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> values = Lists.newArrayList();
        for(int i =0;i<list.size();i++) {
            values.add("#{list[" + i + "]}");
        }
        String sql = "DELETE FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + " IN (" + StringUtils.join(values,",") + ")";
        logger.info(sql);
        return sql;
    }

    public String deleteByEntity(T entity) {
        String sql =  "DELETE FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + "#{" + getTableDto().getIdColumn().getJavaInstance() + "}";
        logger.info(sql);
        return sql;
    }

    public String batchDelete(Map map) {
        List<Object> list = (List<Object>) map.get("list");
        List<String> values = Lists.newArrayList();
        String idJdbcColumn = getTableDto().getIdColumn().getJdbcColumn();
        for(int i =0;i<list.size();i++) {
            values.add("#{list[" + i + "]." + idJdbcColumn +"}");
        }
        String sql = "DELETE FROM " + getTableDto().getJdbcTable() + " WHERE " + getTableDto().getIdColumn().getJdbcColumn() + " IN (" + StringUtils.join(values,",") + ")";
        logger.info(sql);
        return sql;
    }

    public String deleteAll() {
        String sql =  "DELETE FROM " + getTableDto().getJdbcTable();
        logger.info(sql);
        return sql;
    }

    public String update(T entity) {
        List<String>  jdbcColumns = Lists.newArrayList();
        for(ColumnDto columnDto:getTableDto().getColumnList()) {
            boolean updatable = getUpdatable(entity,columnDto);
            if(updatable) {
                jdbcColumns.add(columnDto.getJdbcColumn() + " = " + "#{" + columnDto.getJavaInstance() + "}");
            }
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(getTableDto().getJdbcTable());
        sb.append(" SET ");
        sb.append(StringUtils.join(jdbcColumns,","));
        sb.append(" WHERE ");
        sb.append(getTableDto().getIdColumn().getJdbcColumn());
        sb.append(" = ");
        sb.append("#{" + getTableDto().getIdColumn().getJavaInstance()+ "}");
        String sql = sb.toString();
        logger.info(sql);
        return sql;
    }
}