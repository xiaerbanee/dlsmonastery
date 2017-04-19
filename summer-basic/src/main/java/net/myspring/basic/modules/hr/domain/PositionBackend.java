package net.myspring.basic.modules.hr.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.util.text.StringUtils;

@Entity
@Table(name="hr_position_backend")
public class PositionBackend extends CompanyEntity<PositionBackend> {
    private Integer version = 0;
    private Position position;
    private String positionId;
    private Backend backend;
    private String backendId;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPositionId() {
        if(StringUtils.isBlank(positionId) && position!=null) {
            positionId = position.getId();
        }
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public String getBackendId() {
        if(StringUtils.isBlank(backendId) && backend!=null) {
            backendId = backend.getId();
        }
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }
}
