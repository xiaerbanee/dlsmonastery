package net.myspring.mybatis.context;

/**
 * Created by liuj on 2017/3/17.
 */
public class MapperThreadLocal {
    private static ThreadLocal<MapperThreadLocal> threadLocal = new ThreadLocal<MapperThreadLocal>();

    private Class domainClass;

    private Class idClass;

    private MapperThreadLocal() {
    }

    public static MapperThreadLocal get() {
        if (threadLocal.get() == null) {
            MapperThreadLocal mapperThreadLocal = new MapperThreadLocal();
            threadLocal.set(mapperThreadLocal);
        }
        return threadLocal.get();
    }

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
