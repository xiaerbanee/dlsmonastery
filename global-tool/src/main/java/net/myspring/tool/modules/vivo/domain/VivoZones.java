package net.myspring.tool.modules.vivo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="vivo_zones")
public class VivoZones {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String id;
    private String zoneID;
    private String zoneName;
    private String shortCut;
    private Integer zoneDepth;
    private String zonePath;
    private String fatherID;
    private Integer subCount;
    private String zoneTypes;
    @JsonIgnore
    private LocalDateTime createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public Integer getZoneDepth() {
        return zoneDepth;
    }

    public void setZoneDepth(Integer zoneDepth) {
        this.zoneDepth = zoneDepth;
    }

    public String getZonePath() {
        return zonePath;
    }

    public void setZonePath(String zonePath) {
        this.zonePath = zonePath;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public Integer getSubCount() {
        return subCount;
    }

    public void setSubCount(Integer subCount) {
        this.subCount = subCount;
    }

    public String getZoneTypes() {
        return zoneTypes;
    }

    public void setZoneTypes(String zoneTypes) {
        this.zoneTypes = zoneTypes;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
