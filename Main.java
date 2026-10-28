import java.util.Scanner;

public class Main {

   
    static final int MAKS_MENU = 50;
    static final String MAKANAN = "Makanan";
    static final String MINUMAN = "Minuman";
    static final String[] KATEGORI = {MAKANAN, MINUMAN};
    static final double TARIF_PAJAK = 0.10;      
    static final int BIAYA_PELAYANAN = 20000;   
    static final int BATAS_DISKON = 100000;     
    static final double PERSEN_DISKON = 0.10;    
    static final int BATAS_B1G1 = 50000;         

    static Menu[] daftarMenu = new Menu[MAKS_MENU];
    static int jumlahMenu = 0;

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        isiMenuAwal();

        boolean berjalan = true;
        while (berjalan) {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("          RESTORAN NUSANTARA");
            System.out.println("==========================================");
            System.out.println("1. Menu Pelanggan (Pesan Makanan/Minuman)");
            System.out.println("2. Menu Pemilik (Kelola Menu)");
            System.out.println("0. Keluar");
            int pilihan = bacaAngka("Pilih menu: ", 0, 2);

            switch (pilihan) {
                case 1:
                    prosesPesanan();
                    break;
                case 2:
                    menuPemilik();
                    break;
                case 0:
                    berjalan = false;
                    System.out.println("Terima kasih. Sampai jumpa!");
                    break;
            }
        }
    }

    static void isiMenuAwal() {
        daftarMenu[jumlahMenu++] = new Menu("Nasi Goreng Spesial", 25000, MAKANAN);
        daftarMenu[jumlahMenu++] = new Menu("Mie Ayam Bakso", 20000, MAKANAN);
        daftarMenu[jumlahMenu++] = new Menu("Soto Ayam", 22000, MAKANAN);
        daftarMenu[jumlahMenu++] = new Menu("Ayam Bakar Madu", 30000, MAKANAN);
        daftarMenu[jumlahMenu++] = new Menu("Es Teh Manis", 6000, MINUMAN);
        daftarMenu[jumlahMenu++] = new Menu("Es Jeruk", 8000, MINUMAN);
        daftarMenu[jumlahMenu++] = new Menu("Kopi Susu", 15000, MINUMAN);
        daftarMenu[jumlahMenu++] = new Menu("Jus Alpukat", 18000, MINUMAN);
    }

    static void tampilkanMenu() {
        System.out.println();
        System.out.println("================= DAFTAR MENU =================");
        for (String kategori : KATEGORI) {                 
            System.out.println("--- " + kategori.toUpperCase() + " ---");
            for (int i = 0; i < jumlahMenu; i++) {          
                if (daftarMenu[i].getKategori().equals(kategori)) {
                    System.out.printf("%2d. %-24s %s%n", (i + 1),
                            daftarMenu[i].getNama(),
                            formatRupiah(daftarMenu[i].getHarga()));
                }
            }
        }
        System.out.println("================================================");
    }

    static void prosesPesanan() {
        Menu[] pesananMenu = new Menu[MAKS_MENU];  
        int[] pesananJumlah = new int[MAKS_MENU];  
        int jumlahPesanan = 0;

        tampilkanMenu();
        System.out.println("Ketik nomor menu untuk memesan, ketik 'selesai' jika sudah.");

        boolean selesai = false;
        while (!selesai) {
            String masukan = bacaTeks("Pilih nomor menu / 'selesai': ");

            if (masukan.equalsIgnoreCase("selesai")) {
                selesai = true;
            } else if (isAngka(masukan)
                    && Integer.parseInt(masukan) >= 1
                    && Integer.parseInt(masukan) <= jumlahMenu) {
                Menu dipilih = daftarMenu[Integer.parseInt(masukan) - 1];
                int jumlah = bacaAngka("Jumlah " + dipilih.getNama() + ": ", 1, 100);

                int posisi = -1;
                for (int i = 0; i < jumlahPesanan; i++) {
                    if (pesananMenu[i] == dipilih) {
                        posisi = i;
                    }
                }
                if (posisi == -1) {                 
                    pesananMenu[jumlahPesanan] = dipilih;
                    pesananJumlah[jumlahPesanan] = jumlah;
                    jumlahPesanan++;
                } else {                             
                    pesananJumlah[posisi] += jumlah;
                }
                System.out.println("  -> " + jumlah + " x " + dipilih.getNama()
                        + " ditambahkan ke pesanan.");
            } else {
                System.out.println("  Input tidak valid! Masukkan nomor 1-" + jumlahMenu
                        + " atau 'selesai'.");
            }
        }

        if (jumlahPesanan == 0) {
            System.out.println("Belum ada item yang dipesan. Kembali ke menu utama.");
        } else {
            cetakStruk(pesananMenu, pesananJumlah, jumlahPesanan);
        }
    }

    static int hitungSubtotal(Menu[] menu, int[] jumlah, int n) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            total += menu[i].getHarga() * jumlah[i];
        }
        return total;
    }

    static double hitungPajak(int subtotal) {
        return subtotal * TARIF_PAJAK;
    }

    static double hitungDiskon(int subtotal) {
        if (subtotal > BATAS_DISKON) {
            return subtotal * PERSEN_DISKON;
        } else {
            return 0;
        }
    }

    static int cariMinumanGratis(Menu[] menu, int n, int subtotal) {
        if (subtotal <= BATAS_B1G1) {
            return -1;
        }
        int indeks = -1;
        for (int i = 0; i < n; i++) {
            if (menu[i].getKategori().equals(MINUMAN)) {
                if (indeks == -1 || menu[i].getHarga() > menu[indeks].getHarga()) {
                    indeks = i;
                }
            }
        }
        return indeks;
    }

    static void cetakStruk(Menu[] menu, int[] jumlah, int n) {
        int subtotal = hitungSubtotal(menu, jumlah, n);
        double pajak = hitungPajak(subtotal);
        double diskon = hitungDiskon(subtotal);
        int idxGratis = cariMinumanGratis(menu, n, subtotal);
        double totalSebelumDiskon = subtotal + pajak + BIAYA_PELAYANAN;
        double totalBayar = totalSebelumDiskon - diskon;

        String garis = "==================================================";
        String garisTipis = "--------------------------------------------------";

        System.out.println();
        System.out.println(garis);
        System.out.println("               RESTORAN NUSANTARA");
        System.out.println("                 STRUK PESANAN");
        System.out.println(garis);
        System.out.printf("%-3s %-20s %3s %9s %10s%n", "No", "Item", "Qty", "Harga", "Total");
        System.out.println(garisTipis);
        for (int i = 0; i < n; i++) {
            System.out.printf("%-3d %-20s %3d %9s %10s%n", (i + 1),
                    menu[i].getNama(), jumlah[i],
                    formatAngka(menu[i].getHarga()),
                    formatAngka(menu[i].getHarga() * jumlah[i]));
        }
        System.out.println(garisTipis);
        cetakBaris("Total Biaya Pesanan", formatRupiah(subtotal));
        cetakBaris("Pajak 10% (dari total biaya)", formatRupiah(pajak));
        cetakBaris("Biaya Pelayanan", formatRupiah(BIAYA_PELAYANAN));
        cetakBaris("Total Sebelum Diskon", formatRupiah(totalSebelumDiskon));

        if (diskon > 0) {
            System.out.println(garisTipis);
            cetakBaris("Diskon 10% (total > Rp 100.000)", "-" + formatRupiah(diskon));
            cetakBaris("Harga Setelah Diskon", formatRupiah(totalBayar));
        }

        if (idxGratis != -1) {
            System.out.println(garisTipis);
            System.out.println("PENAWARAN: Beli 1 Gratis 1 (total > Rp 50.000)");
            System.out.println("  GRATIS " + jumlah[idxGratis] + " x "
                    + menu[idxGratis].getNama() + " (senilai "
                    + formatRupiah(menu[idxGratis].getHarga() * jumlah[idxGratis]) + ")");
        }

        System.out.println(garis);
        cetakBaris("TOTAL BAYAR", formatRupiah(totalBayar));
        System.out.println(garis);
        System.out.println("        Terima kasih atas kunjungan Anda!");
    }

    static void cetakBaris(String label, String nilai) {
        System.out.printf("%-34s %15s%n", label, nilai);
    }

    static void menuPemilik() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println();
            System.out.println("========== PENGELOLAAN MENU (PEMILIK) ==========");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Lihat Daftar Menu");
            System.out.println("0. Kembali ke Menu Utama");
            int pilihan = bacaAngka("Pilih menu: ", 0, 4);

            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    ubahHarga();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    tampilkanMenu();
                    break;
                case 0:
                    kembali = true;
                    break;
            }
        }
    }

    static void tambahMenu() {
        boolean lagi = true;
        while (lagi) {
            if (jumlahMenu >= MAKS_MENU) {
                System.out.println("Daftar menu penuh (maksimal " + MAKS_MENU + " menu).");
                lagi = false;
            } else {
                String nama = bacaNama("Nama menu baru: ");
                int harga = bacaAngka("Harga (Rp): ", 1, 1000000);
                int pilihKategori = bacaAngka("Kategori (1 = Makanan, 2 = Minuman): ", 1, 2);
                String kategori = (pilihKategori == 1) ? MAKANAN : MINUMAN;

                daftarMenu[jumlahMenu] = new Menu(nama, harga, kategori);
                jumlahMenu++;
                urutkanBerdasarkanKategori();
                System.out.println("Menu '" + nama + "' berhasil ditambahkan.");

                lagi = konfirmasi("Tambah menu lagi? (Ya/Tidak): ");
            }
        }
        tampilkanMenu();
    }

    static void ubahHarga() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }
        tampilkanMenu();
        int nomor = bacaAngka("Nomor menu yang diubah harganya (0 = kembali): ", 0, jumlahMenu);
        if (nomor == 0) {
            return;
        }
        Menu terpilih = daftarMenu[nomor - 1];
        int hargaBaru = bacaAngka("Harga baru untuk " + terpilih.getNama() + " (Rp): ", 1, 1000000);

        System.out.println("Konfirmasi: harga " + terpilih.getNama() + " diubah dari "
                + formatRupiah(terpilih.getHarga()) + " menjadi " + formatRupiah(hargaBaru));
        if (konfirmasi("Yakin ingin mengubah? (Ya/Tidak): ")) {
            terpilih.setHarga(hargaBaru);
            System.out.println("Harga berhasil diubah.");
            tampilkanMenu();
        } else {
            System.out.println("Perubahan dibatalkan. Tidak ada perubahan pada daftar menu.");
        }
    }

    static void hapusMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }
        tampilkanMenu();
        int nomor = bacaAngka("Nomor menu yang dihapus (0 = kembali): ", 0, jumlahMenu);
        if (nomor == 0) {
            return;
        }
        Menu terpilih = daftarMenu[nomor - 1];

        System.out.println("Konfirmasi: menu '" + terpilih.getNama() + "' ("
                + formatRupiah(terpilih.getHarga()) + ") akan dihapus.");
        if (konfirmasi("Yakin ingin menghapus? (Ya/Tidak): ")) {
            for (int i = nomor - 1; i < jumlahMenu - 1; i++) {   
                daftarMenu[i] = daftarMenu[i + 1];
            }
            daftarMenu[jumlahMenu - 1] = null;
            jumlahMenu--;
            System.out.println("Menu berhasil dihapus.");
            tampilkanMenu();
        } else {
            System.out.println("Penghapusan dibatalkan. Tidak ada perubahan pada daftar menu.");
        }
    }

    static void urutkanBerdasarkanKategori() {
        Menu[] baru = new Menu[MAKS_MENU];
        int n = 0;
        for (String kategori : KATEGORI) {
            for (int i = 0; i < jumlahMenu; i++) {
                if (daftarMenu[i].getKategori().equals(kategori)) {
                    baru[n++] = daftarMenu[i];
                }
            }
        }
        daftarMenu = baru;
    }

    static String bacaTeks(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    static boolean isAngka(String teks) {
        if (teks.length() == 0 || teks.length() > 9) {
            return false;
        }
        for (int i = 0; i < teks.length(); i++) {
            if (!Character.isDigit(teks.charAt(i))) {
                return false;
            }
        }
        return true;
    }
  
    static int bacaAngka(String pesan, int min, int maks) {
        String teks;
        boolean valid;
        do {                                                 
            teks = bacaTeks(pesan);
            valid = isAngka(teks)
                    && Integer.parseInt(teks) >= min
                    && Integer.parseInt(teks) <= maks;
            if (!valid) {
                System.out.println("  Input tidak valid! Masukkan angka " + min + " - " + maks + ".");
            }
        } while (!valid);
        return Integer.parseInt(teks);
    }

    static boolean konfirmasi(String pesan) {
        while (true) {
            String jawab = bacaTeks(pesan);
            if (jawab.equalsIgnoreCase("ya")) {
                return true;
            } else if (jawab.equalsIgnoreCase("tidak")) {
                return false;
            }
            System.out.println("  Input tidak valid! Ketik 'Ya' atau 'Tidak'.");
        }
    }

    static String bacaNama(String pesan) {
        String nama = bacaTeks(pesan);
        while (nama.length() == 0 || nama.length() > 24) {
            System.out.println("  Nama tidak boleh kosong dan maksimal 24 karakter.");
            nama = bacaTeks(pesan);
        }
        return nama;
    }

    static String formatAngka(double nilai) {
        String s = Long.toString(Math.round(nilai));
        StringBuilder sb = new StringBuilder();
        int hitung = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.insert(0, s.charAt(i));
            hitung++;
            if (hitung % 3 == 0 && i != 0) {
                sb.insert(0, '.');
            }
        }
        return sb.toString();
    }

    static String formatRupiah(double nilai) {
        return "Rp " + formatAngka(nilai);
    }
}
