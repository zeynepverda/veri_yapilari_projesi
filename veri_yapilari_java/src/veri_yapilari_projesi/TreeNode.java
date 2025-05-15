package veri_yapilari_projesi;



import java.util.HashMap;
import java.util.Map;

public class TreeNode {
    String data;
    int count;
    Map<String, TreeNode> cocuk;
    TreeNode ebeveyn;
    boolean kontrol;

    public TreeNode(String data) {
        this.data = data;
        this.count = 0;
        this.cocuk = new HashMap<>();
        this.ebeveyn = null;
        this.kontrol = false;
    }
}