package entity;

public class TreeNode {

    private String value;        // 节点的值

    private TreeNode firstChild;    // 第一个孩子节点

    private TreeNode nextBro;   // 下一个兄弟节点



    public TreeNode(String value) {
        this.value = value;
        this.firstChild = null;
        this.nextBro = null;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TreeNode getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(TreeNode firstChild) {
        this.firstChild = firstChild;
    }

    public TreeNode getNextBro() {
        return nextBro;
    }

    public void setNextBro(TreeNode nextBro) {
        this.nextBro = nextBro;
    }


    public void printTree(TreeNode head){
        if(head == null){
            return;
        }
        System.out.println(head.getValue());
        printTree(head.getFirstChild());
        printTree(head.getNextBro());
    }
}
