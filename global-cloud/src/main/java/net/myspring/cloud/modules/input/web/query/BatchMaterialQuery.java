package net.myspring.cloud.modules.input.web.query;

import java.util.List;

/**
 * Created by lihx on 2017/5/5.
 */
public class BatchMaterialQuery {
    private List<String> materialCategoryList;
    private List<String> materialGroupList;

    public List<String> getMaterialCategoryList() {
        return materialCategoryList;
    }

    public void setMaterialCategoryList(List<String> materialCategoryList) {
        this.materialCategoryList = materialCategoryList;
    }

    public List<String> getMaterialGroupList() {
        return materialGroupList;
    }

    public void setMaterialGroupList(List<String> materialGroupList) {
        this.materialGroupList = materialGroupList;
    }
}
