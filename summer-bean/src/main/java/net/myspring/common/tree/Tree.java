package net.myspring.common.tree;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by wangzm on 2017/4/22.
 */
public class Tree {

    private List<String> checked = Lists.newArrayList();
    private List<TreeNode> children = Lists.newArrayList();

    public List<String> getChecked() {
        return checked;
    }

    public void setChecked(List<String> checked) {
        this.checked = checked;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
