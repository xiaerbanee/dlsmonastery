package net.myspring.tool.common.domain;


import net.myspring.util.text.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


/**
 * Created by liuj on 2016-07-25.
 */
@MappedSuperclass
public class IdEntity<T> implements Serializable {
    private static final long serialVersionUID = -864662154371775680L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null || id.length() == 0) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdEntity other = (IdEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Boolean isCreate() {
        return StringUtils.isBlank(id);
    }
}
