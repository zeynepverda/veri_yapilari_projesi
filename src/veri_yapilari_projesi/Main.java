package veri_yapilari_projesi;



import java.util.*;

public class Main {
    public static void main(String[] args) {
        
        SiparisYonetim tree = new SiparisYonetim();
        
        Scanner scanner = new Scanner(System.in);
// çıkış yapmak istenilene kadar devam eder, seçim paneli yazdırılır
        while (true) {
            System.out.println("\nSiparis Yonetim Sistemi");
            System.out.println("1. Siparis Ekle");
            System.out.println("2. Siparisi Ara");
            System.out.println("3. Siparis Sil");
            System.out.println("4. Tum Siparisleri Listele");
            System.out.println("5. Urun Kumesi Sorgulama");
            System.out.println("6. Cikis");
            System.out.print("Bir islem seciniz: ");
            int secim;

            try {
                secim = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {// geçersiz giriş fırlatır
                System.out.println("Gecersiz giris. Lutfen bir sayi giriniz.");
                scanner.nextLine();
                continue;
            }

            switch (secim) {
                case 1:
                    try {
                        System.out.print("Siparisi giriniz (virgulle ayrilmis): ");
                        String order = scanner.nextLine();
                        tree.addOrder(order);// ağaca ekleme
                    } catch (IllegalArgumentException e) {
                        System.out.println("Hata: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Aranacak siparisi giriniz (virgulle ayrilmis): ");
                    String searchOrder = scanner.nextLine();// siapriş arama methodu çağırılır
                    if (tree.searchOrder(searchOrder)) {
                        System.out.println("Siparis bulundu.");
                    } else {
                        System.out.println("Siparis bulunamadi.");
                    }
                    break;
                case 3:
                    System.out.print("Silinecek siparisi giriniz (virgulle ayrilmis): ");
                    String deleteOrder = scanner.nextLine();// sipariş silme methodu
                    tree.deleteOrder(deleteOrder);
                    break;
                case 4:
                    System.out.println("\nTum Siparisler:");
                    tree.agaciYazdir();// tüm siparişleri yazdırma methodu
                    break;
                case 5:
                    System.out.print("Siparisleri giriniz (virgulle ayrilmis): ");
                    String clusterInput = scanner.nextLine();
                    List<String> siparisler = Arrays.asList(clusterInput.replaceAll("\\s+", "").split(","));
                    tree.ürünKümesiSorgulama(siparisler);// öyle bir sipariş var mı kontrlo
                    break;
                case 6:
                    System.out.println("Cikis yapiliyor...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Gecersiz islem. Lutfen tekrar deneyiniz.");
            }
        }
        
    }
}