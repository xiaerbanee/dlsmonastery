package net.myspring.mybatis.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.persistence.GeneratedValue;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/17.
 */
public class TableDto {
    private String jdbcTable;
    private List<ColumnDto> columnList = Lists.newArrayList();
    private ColumnDto idColumn;
    private ColumnDto createdByColumn;
    private ColumnDto createdDateColumn;
    private ColumnDto lastModifiedByColumn;
    private ColumnDto LastModifiedDateColumn;
    private ColumnDto versionColumn;
    private Map<String,ColumnDto> columnMap = Maps.newHashMap();

    public String getJdbcTable() {
        return jdbcTable;
    }

    public void setJdbcTable(String jdbcTable) {
        this.jdbcTable = jdbcTable;
    }

    public List<ColumnDto> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnDto> columnList) {
        this.columnList = columnList;
    }

    public ColumnDto getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(ColumnDto idColumn) {
        this.idColumn = idColumn;
    }

    public ColumnDto getCreatedByColumn() {
        return createdByColumn;
    }

    public void setCreatedByColumn(ColumnDto createdByColumn) {
        this.createdByColumn = createdByColumn;
    }

    public ColumnDto getCreatedDateColumn() {
        return createdDateColumn;
    }

    public void setCreatedDateColumn(ColumnDto createdDateColumn) {
        this.createdDateColumn = createdDateColumn;
    }

    public ColumnDto getLastModifiedByColumn() {
        return lastModifiedByColumn;
    }

    public void setLastModifiedByColumn(ColumnDto lastModifiedByColumn) {
        this.lastModifiedByColumn = lastModifiedByColumn;
    }

    public ColumnDto getLastModifiedDateColumn() {
        return LastModifiedDateColumn;
    }

    public void setLastModifiedDateColumn(ColumnDto lastModifiedDateColumn) {
        LastModifiedDateColumn = lastModifiedDateColumn;
    }

    public ColumnDto getVersionColumn() {
        return versionColumn;
    }

    public void setVersionColumn(ColumnDto versionColumn) {
        this.versionColumn = versionColumn;
    }
    public Map<String, ColumnDto> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, ColumnDto> columnMap) {
        this.columnMap = columnMap;
    }
}
