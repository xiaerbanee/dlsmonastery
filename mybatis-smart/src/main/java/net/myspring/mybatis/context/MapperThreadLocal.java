package net.myspring.mybatis.context;

/**
 * Created by liuj on 2017/3/17.
 */
public class MapperThreadLocal {
    private static ThreadLocal<MapperThreadLocal> threadLocal = new ThreadLocal<MapperThreadLocal>();

    private MapperDefinition mapperDefinition = new MapperDefinition();

    private MapperThreadLocal() {
    }

    public static MapperThreadLocal get() {
        return threadLocal.get();
    }

    public MapperDefinition getMapperDefinition() {
        return mapperDefinition;
    }

    public void setMapperDefinition(MapperDefinition mapperDefinition) {
        this.mapperDefinition = mapperDefinition;
    }

    public void remove() {
        threadLocal.remove();
    }
}
