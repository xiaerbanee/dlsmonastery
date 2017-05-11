package net.myspring.future.modules.layout.web.query;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

/**
 * Created by wangzm on 2017/5/11.
 */
public class ShopAttributeQuery {

    private List<Integer> typeList= Lists.newArrayList();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Integer> typeList) {
        this.typeList = typeList;
    }
}
