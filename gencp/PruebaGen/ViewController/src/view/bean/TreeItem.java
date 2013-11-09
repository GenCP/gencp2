package view.bean;

import java.util.List;

public class TreeItem {
    String text = null;
    private List<TreeItem> children = null;
    private TreeItem parent = null;
    
    public TreeItem(String text, TreeItem parent) {
        setText(text);
        setParent(parent);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setChildren(List<TreeItem> children) {
        this.children = children;
    }

    public List<TreeItem> getChildren() {
        return children;
    }

    public void setParent(TreeItem parent) {
        this.parent = parent;
    }

    public TreeItem getParent() {
        return parent;
    }
}


