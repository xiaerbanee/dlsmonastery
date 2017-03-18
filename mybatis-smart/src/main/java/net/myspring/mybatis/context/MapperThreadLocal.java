package net.myspring.mybatis.context;

/**
 * Created by liuj on 2017/3/17.
 */
public class MapperThreadLocal {
    private static ThreadLocal<MapperThreadLocal> threadLocal = new ThreadLocal<MapperThreadLocal>();

    private MapperDefinition mapperDefinition;

    private MapperThreadLocal() {
    }

    public static MapperThreadLocal get() {
        if (threadLocal.get() == null) {
            MapperThreadLocal mapperThreadLocal = new MapperThreadLocal();
            threadLocal.set(mapperThreadLocal);
        }
        return threadLocal.get();
    }

    public MapperDefinition getMapperDefinition() {
        return mapperDefinition;
    }

    public void setMapperDefinition(MapperDefinition mapperDefinition) {
        this.mapperDefinition = mapperDefinition;
    }
}
