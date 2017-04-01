package net.myspring.util.cahe;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuj on 2017/3/31.
 */
public class CacheInputObject {
    private Object object;
    private CacheInputField cacheInputField;
    private List<String> keyList = Lists.newArrayList();

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

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }
}
