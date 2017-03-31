package net.myspring.util.cahe;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheInputObject {
    private Object object;
    private CacheInputField cacheInputField;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public CacheInputField getCacheInputField() {
        return cacheInputField;
    }

    public void setCacheInputField(CacheInputField cacheInputField) {
        this.cacheInputField = cacheInputField;
    }
}
