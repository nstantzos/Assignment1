package sample;

import java.util.ArrayList;
import java.util.List;

public class Node<T>
{
    // Properties of the node object, used for file storage and parent matching
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

    // Method for attaching a single child to a parent node
    public Node<T> addChild(Node<T> child)
    {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    // Method for attaching multiple children to a parent node
    public void addChildren(List<Node<T>> children)
    {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    // Returns a list of children of the node
    public List<Node<T>> getChildren()
    {
        return children;
    }

    // Get the node's data (name)
    public T getData()
    {
        return data;
    }

    // Set the node's data (name)
    public void setData(T data)
    {
        this.data = data;
    }

    // Set the node's parent
    public void setParent(Node<T> parent)
    {
        this.parent = parent;
    }

    // Get the node's parent
    public Node<T> getParent()
    {
        return parent;
    }

}
