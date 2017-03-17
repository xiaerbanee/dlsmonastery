package net.myspring.mybatis.dto;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/17.
 */
public class TableDto {
    private String jdbcTable;
    private List<ColumnDto> mybatisColumnList;
    private ColumnDto idColumn;
    private ColumnDto createdByColumn;
    private ColumnDto createdDateColumn;
    private ColumnDto lastModifiedByColumn;
    private ColumnDto LastModifiedDateColumn;
    private ColumnDto versionColumn;
    private ColumnDto enabledColumn;
    private String generationType;
    private Map<String,ColumnDto> columnMap;

    public String getJdbcTable() {
        return jdbcTable;
    }

    public void setJdbcTable(String jdbcTable) {
        this.jdbcTable = jdbcTable;
    }

    public List<ColumnDto> getMybatisColumnList() {
        return mybatisColumnList;
    }

    public void setMybatisColumnList(List<ColumnDto> mybatisColumnList) {
        this.mybatisColumnList = mybatisColumnList;
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

    public ColumnDto getEnabledColumn() {
        return enabledColumn;
    }

    public void setEnabledColumn(ColumnDto enabledColumn) {
        this.enabledColumn = enabledColumn;
    }

    public String getGenerationType() {
        return generationType;
    }

    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    public Map<String, ColumnDto> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, ColumnDto> columnMap) {
        this.columnMap = columnMap;
    }
}
