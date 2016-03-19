package muyanmoyang.fp_growth;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
 
public class TreeNode {
     
    private TreeNode parent;
    private String name;
    private int count;
    private Set<TreeNode> children;
     
    public TreeNode(TreeNode parent,String name,int count){
        this.count = count;
        this.parent = parent;
        this.name = name;
    }
     
    public TreeNode(String name,int count){
        this.name = name;
        this.count = count;
    }
    /**
     * 当前节点计数+i
     * @param i
     */
    public void incrementCount(int i){
        this.count = count + i;
    }
    /**
     * 父节点是否包含子节点包含则返回，否则返回null
     * @param key
     * @return
     */
    public TreeNode findChild(String key){
        if(this.children == null){
            return null;
        }
        for(TreeNode child:this.children){
            if(StringUtils.equals(child.name,key)){
                return child;
            }
        }
        return null;
    }
    /**
     * 给父节点增加一个子节点
     * @param child
     * @return
     */
    public TreeNode addChild(TreeNode child){
        if(this.children == null){
            this.children = new HashSet<TreeNode>();
        }
        this.children.add(child);
        return child;
    }
    public boolean isEmpty(){
        return this.children==null || this.children.size()==0;
    }
     
    public TreeNode getParent() {
        return parent;
    }
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
 
    public Set<TreeNode> getChildren() {
        return children;
    }
 
    public void setChildren(Set<TreeNode> children) {
        this.children = children;
    }
     
}
