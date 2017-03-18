package net.myspring.mybatis.context;

/**
 * Created by liuj on 2017/3/18.
 */
public class MapperDefinition {
    private Class domainClass;
    private Class idClass;

    public Class getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class domainClass) {
        this.domainClass = domainClass;
    }

    public Class getIdClass() {
        return idClass;
    }

    public void setIdClass(Class idClass) {
        this.idClass = idClass;
    }
}
