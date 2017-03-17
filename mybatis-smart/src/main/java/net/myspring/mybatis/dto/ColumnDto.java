package net.myspring.mybatis.dto;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Created by liuj on 2017/3/17.
 */
public class ColumnDto {
    private TableDto tableDto;
    private String javaInstance;
    private String jdbcColumn;
    private Boolean insertable;
    private Boolean updatable;
    private  Boolean nullable;

    public TableDto getTableDto() {
        return tableDto;
    }

    public void setTableDto(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    public String getJavaInstance() {
        return javaInstance;
    }

    public void setJavaInstance(String javaInstance) {
        this.javaInstance = javaInstance;
    }

    public String getJdbcColumn() {
        return jdbcColumn;
    }

    public void setJdbcColumn(String jdbcColumn) {
        this.jdbcColumn = jdbcColumn;
    }

    public Boolean getInsertable() {
        return insertable;
    }

    public void setInsertable(Boolean insertable) {
        this.insertable = insertable;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }
}
