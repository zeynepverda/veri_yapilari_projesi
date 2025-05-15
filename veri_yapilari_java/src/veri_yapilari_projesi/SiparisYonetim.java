
package veri_yapilari_projesi;

import java.util.*;

public class SiparisYonetim {
    private final TreeNode root;

    public SiparisYonetim() {
        this.root = new TreeNode("root");
    }

    
    public void addOrder(String order) {  
        String[] bolme = siparisler(order);// sipariş parçalara ayırılır, sıralanıyor
        
        for(int i = 0;i<bolme.length;i++){
            for(int j = i+1;j<bolme.length;j++){
                if(bolme[i].equals(bolme[j])){// eğer ürünler birbirine eşitse 
                    System.out.println("Ayni urunden birden fazla eklemenize gerek yok.");
                    return;
                }
            }
        }
        TreeNode current = root;
        for (String part : bolme) {
            current.cocuk.putIfAbsent(part, new TreeNode(part));// eğer aynı üründen birden fazla yoksa, ekle
            TreeNode altCocuk = current.cocuk.get(part);
            altCocuk.ebeveyn = current;
            current = altCocuk;// bir sonraki düğüme geçiyor
            current.count++;
        }
        current.kontrol = true;
        System.out.println("Siparis eklendi");
    }

    public boolean deleteOrder(String order) {
        String[] bolme = siparisler(order);
        if (bolme == null) {
            System.out.println("Hatali siparis girisi");
            return false;
        }

        TreeNode hedefDugum = siparisiButunAra(root, bolme, 0);//siparişin hedef düğümünü bulur
        if (hedefDugum == null) {
            System.out.println("Siparis bulunamadi. Tam eslesme gerekli.");
            return false;
        }

        if (deleteOrderRecursive(hedefDugum)) {// hedef düğümü siler
            System.out.println("Siparisiniz silindi.");
            return true;
        }

        System.out.println("Siparis silinemedi.");
        return false;
    }
    
    
// verilen siparişi bulma
    private TreeNode siparisiButunAra(TreeNode current, String[] bolme, int index) {
        if (current == null) {
            return null;
        }

        if (index == bolme.length) {
            if (current.kontrol) {
                return current;
            }
            return null;
        }

        if (current.cocuk.containsKey(bolme[index])) {// bir çoculta varsa ilerle 
            return siparisiButunAra(current.cocuk.get(bolme[index]), bolme, index + 1);
        }
        return null;
    }

    private boolean deleteOrderRecursive(TreeNode hedefDugum) {
        if (hedefDugum.cocuk.isEmpty()) {// düğümün çocuğu yoksa
            TreeNode parent = hedefDugum.ebeveyn;
            if (parent != null) {
                parent.cocuk.remove(hedefDugum.data);// çocuğu ebeveynden kaldır
                parent.count--;
                if (parent.cocuk.isEmpty() && !parent.kontrol) {// ebeveunin başka çocuğu yoksa
                    deleteOrderRecursive(parent);// ebeveyni de kaldır
                }
            }
            return true;
        } else {
            hedefDugum.kontrol = false;
            return true;
        }
    }

    //sipariş arama methodu
    public boolean searchOrder(String order) {
        String[] orderParts = siparisler(order);// siparişi parçalar, sıraya dizer
        TreeNode current = root;
        for (String part : orderParts) {
            current = current.cocuk.get(part);// kökten çocuğa kadar gelir
            if (current == null) {
                return false;
            }
        }
        return true;
    }

    public void ürünKümesiSorgulama(List<String> siparis) {
        if (siparis.isEmpty()) {
            System.out.println("Siparis kumesi bos olamaz");
            return;
        }

        String listeSonuUrun = siparis.get(siparis.size() - 1);
        List<TreeNode> hedefDugumler = new ArrayList<>();
        hedefDugumleriBul(root, listeSonuUrun, hedefDugumler);

        int toplamSiparisSayisi = 0;
        List<String> yollar = new ArrayList<>();//sipariş yollarını tutar

        for (TreeNode dugum : hedefDugumler) {
            if (siparisYoluUygunMu(dugum, siparis)) {
                toplamSiparisSayisi += dugum.count;
                yollar.add(yoluOlustur(dugum) + " (" + dugum.count + ")");
            }
        }

        for (String yol : yollar) {
            System.out.println("Yol: " + yol);
        }
        System.out.println("Toplam Siparis Sayisi: " + toplamSiparisSayisi);
    }

    private void hedefDugumleriBul(TreeNode node, String arananUrun, List<TreeNode> hedefDugumler) {
        if (node.data.equals(arananUrun)) {
            hedefDugumler.add(node);// düğüm istenen ürünü içeriyorsa listeye ekler
        }

        for (TreeNode child : node.cocuk.values()) {
            hedefDugumleriBul(child, arananUrun, hedefDugumler);//çocuk düğümü kontrol eder
        }
    }

    private boolean siparisYoluUygunMu(TreeNode dugum, List<String> siparis) {
        int sonIndex = siparis.size() - 1;

        while (dugum != null) {
            if (sonIndex >= 0 && dugum.data.equals(siparis.get(sonIndex))) {
                sonIndex--;
            }
            dugum = dugum.ebeveyn;// bir üst ataya geçiyor
        }

        return sonIndex < 0;// tüm elemanlar eşleştiyse true döner
    }

    private String yoluOlustur(TreeNode node) {
        List<String> yol = new ArrayList<>();
        while (node != null) {
            yol.add(0, node.data);// yolun en başına ekler(0)
            node = node.ebeveyn;// ataya geçer
        }
        return String.join(" --> ", yol);// yol elemanları birleşir
    }

    public void agaciYazdir() {
        printTreeRecursive(root, "", true);
    }

    private void printTreeRecursive(TreeNode node, String prefix, boolean kuyrukMu) {
        if (node != null) {
            if (node == root) {
                System.out.println(prefix + (kuyrukMu ? "|__ " : "|--- ") + "Root");
            } else {
                System.out.println(prefix + (kuyrukMu ? "|__ " : "|--- ")
                        + (node.data == null ? "Root" : node.data)
                        + " (" + node.count + ")");
            }

            int size = node.cocuk.size();
            int index = 0;
            for (TreeNode child : node.cocuk.values()) {
                printTreeRecursive(child, prefix + (kuyrukMu ? "    " : "|   "), ++index == size);
            }
        }
    }
    // ürünler alfabetik olarak sıralaır
    public void siralama(String[] urunler) {
    for (int i = 0; i < urunler.length; i++) {
        for (int j = i + 1; j < urunler.length; j++) {
            if (urunler[i].compareTo(urunler[j]) > 0) {
                String temp = urunler[i];
                urunler[i] = urunler[j];
                urunler[j] = temp;
            }
        }
    }
}
    
// sipariş parçalara ayrılır
    private String[] siparisler(String order) {
        order = order.toLowerCase();
        if (order == null || order.trim().isEmpty()) {
            throw new IllegalArgumentException("Siparis bos olamaz.");
        }
        String[] siparisler = order.replaceAll("\\s+", "").split(",");
        if (siparisler.length == 0 || Arrays.stream(siparisler).anyMatch(String::isEmpty)) {// siparişler boş ile eşleşmez
            throw new IllegalArgumentException("Gecersiz siparis formati.");
        }
        siralama(siparisler);
        return siparisler;
    }
}
   
    
    



