package net.myspring.common.tree;

import com.google.common.collect.Lists;

import java.util.List;

public class TreeNode {
	private String id;
	private String label;
	private List<String> checked = Lists.newArrayList();
	private List<TreeNode> children = Lists.newArrayList();

	public TreeNode(String id, String label){
		this.id = id;
		this.label = label;
	}

	public List<String> getChecked() {
		return checked;
	}

	public void setChecked(List<String> checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
}
