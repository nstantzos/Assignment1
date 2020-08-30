package sample;

import java.util.ArrayList;
import java.util.List;

public class Node<T>
{

    private T data = null;

    private List<Node<T>> children = new ArrayList<>();

    private Node<T> parent = null;

    public String NodeName;

    public int NodeID;

    public boolean IsRoot = false;

    public boolean IsFile;

    public boolean IsFolder;

    public boolean ContainsFiles;

    public String ParentName;

    public int NumberOfFiles;

    public Long FileSize;

    public Node(T data) {
        this.data = data;
    }

    public Node<T> addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node<T>> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

}
