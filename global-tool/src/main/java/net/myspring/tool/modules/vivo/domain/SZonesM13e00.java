package net.myspring.tool.modules.vivo.domain;

import javax.persistence.*;

@Entity
@Table(name = "S_ZONEs_M13E00")
public class SZonesM13e00 {
    @Id
    private String zoneid;
    private String zonename;
    private String shortcut;
    private Integer zonedepth;
    private String zonepath;
    private String fatherid;
    private Integer subcount;
    private String zonetypes;
    private String tableName;

    @Column(name = "ZoneID")
    public String getZoneid() {
        return zoneid;
    }
    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    @Column(name = "ZoneName")
    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    @Column(name = "ShortCut")
    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Column(name = "ZoneDepth")
    public Integer getZonedepth() {
        return zonedepth;
    }

    public void setZonedepth(Integer zonedepth) {
        this.zonedepth = zonedepth;
    }

    @Column(name = "ZonePath")
    public String getZonepath() {
        return zonepath;
    }

    public void setZonepath(String zonepath) {
        this.zonepath = zonepath;
    }

    @Column(name = "FatherID")
    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    @Column(name = "SubCount")
    public Integer getSubcount() {
        return subcount;
    }

    public void setSubcount(Integer subcount) {
        this.subcount = subcount;
    }

    @Column(name = "ZoneTypes")
    public String getZonetypes() {
        return zonetypes;
    }

    public void setZonetypes(String zonetypes) {
        this.zonetypes = zonetypes;
    }

    @Transient
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
