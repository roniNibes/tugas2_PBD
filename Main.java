import java.util.Scanner;

public class Main {

    // KONSTANTA
    static final double TARIF_PAJAK = 0.10;
    static final double BIAYA_PELAYANAN = 20000;
    static final double BATAS_DISKON = 100000;
    static final double BATAS_BOGO = 50000;
    static final double TARIF_DISKON = 0.10;

    // STATE APLIKASI

    static Menu[] daftarMenu = Menu.getDaftarMenu();
    static int jumlahMenu = daftarMenu.length;

    static Menu[] itemPesan = new Menu[100];
    static int[] jumlahPesan = new int[100];
    static int totalItem = 0;

    static Scanner scanner = new Scanner(System.in);

    // ENTRY POINT

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            tampilkanMenuUtama();
            String pilihan = inputPilihan(new String[] { "0", "1", "2" });
            switch (pilihan) {
                case "1":
                    menuPelanggan();
                    break;
                case "2":
                    menuPengelolaan();
                    break;
                case "0":
                    System.out.println("\nTerima kasih! Sampai jumpa!");
                    running = false;
                    break;
            }
        }
        scanner.close();
    }

    // UTILITAS: INPUT TERPUSAT

    /**
     * Meminta input dari pengguna dan memvalidasi
     * bahwa input ada dalam daftar pilihan yang valid.
     * Loop terus sampai input valid.
     */
    static String inputPilihan(String[] pilihanValid) {
        while (true) {
            String input = scanner.nextLine().trim();
            for (String valid : pilihanValid) {
                if (input.equalsIgnoreCase(valid)) {
                    return input;
                }
            }
            System.out.print("[!] Pilihan tidak valid. Coba lagi: ");
        }
    }

    /**
     * Meminta input angka bulat positif dari pengguna.
     * Loop terus sampai input valid.
     */
    static int inputAngka(int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int angka = Integer.parseInt(input);
                if (angka >= min && angka <= max) {
                    return angka;
                }
                System.out.print("[!] Masukkan angka antara " + min + "-" + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("[!] Input tidak valid. Masukkan angka: ");
            }
        }
    }

    /**
     * Meminta input angka desimal positif dari pengguna.
     * Loop terus sampai input valid.
     */
    static double inputAngkaDesimal(double min) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                double angka = Double.parseDouble(input);
                if (angka > min) {
                    return angka;
                }
                System.out.print("[!] Harus lebih dari " + (int) min + ". Coba lagi: ");
            } catch (NumberFormatException e) {
                System.out.print("[!] Input tidak valid. Masukkan angka: ");
            }
        }
    }

    /**
     * Meminta konfirmasi Ya/Tidak dari pengguna.
     * Loop terus sampai input valid.
     */
    static boolean inputKonfirmasi(String pertanyaan) {
        while (true) {
            System.out.print(pertanyaan + " (Ya/Tidak): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Ya"))
                return true;
            if (input.equalsIgnoreCase("Tidak"))
                return false;
            System.out.println("[!] Ketik 'Ya' atau 'Tidak'.");
        }
    }

    // TAMPILAN

    static void tampilkanMenuUtama() {
        System.out.println("============================================");
        System.out.println("      SELAMAT DATANG DI SERANG GA(?) CHAFE      ");
        System.out.println("============================================");
        System.out.println("  1. Pemesanan (Pelanggan)");
        System.out.println("  2. Manajemen Menu (Pemilik)");
        System.out.println("  0. Keluar");
        System.out.println("============================================");
        System.out.print("Pilih menu: ");
    }

    static void tampilkanDaftarMenu() {
        tampilkanDaftarMenuInternal(false);
    }

    static void tampilkanDaftarMenuNomor() {
        tampilkanDaftarMenuInternal(true);
    }

    /**
     * Method internal untuk menampilkan daftar menu.
     * Parameter showNomor menentukan apakah nomor urut ditampilkan.
     */
    static void tampilkanDaftarMenuInternal(boolean showNomor) {
        System.out.println("\n============================================");
        System.out.println(showNomor
                ? "         DAFTAR MENU (DENGAN NOMOR)        "
                : "            DAFTAR MENU RESTORAN           ");
        System.out.println("============================================");

        String[] kategoriList = { "Makanan", "Minuman", "Dessert" };

        for (String kat : kategoriList) {
            System.out.println("\n[ " + kat.toUpperCase() + " ]");
            System.out.println("--------------------------------------------");
            boolean adaMenu = false;

            for (int i = 0; i < jumlahMenu; i++) {
                if (daftarMenu[i].getKategori().equals(kat)) {
                    if (showNomor) {
                        System.out.printf("  [%2d] %-26s Rp %,.0f%n",
                                (i + 1),
                                daftarMenu[i].getNama(),
                                daftarMenu[i].getHarga());
                    } else {
                        System.out.printf("  %-30s Rp %,.0f%n",
                                daftarMenu[i].getNama(),
                                daftarMenu[i].getHarga());
                    }
                    adaMenu = true;
                }
            }

            if (!adaMenu) {
                System.out.println("  (Tidak ada menu tersedia)");
            }
        }
        System.out.println("============================================");
    }

    // MENU PELANGGAN

    static void menuPelanggan() {
        resetPesanan();
        prosesInputPesanan();
        if (totalItem > 0) {
            cetakStruk();
        }
    }

    static void resetPesanan() {
        totalItem = 0;
        itemPesan = new Menu[100];
        jumlahPesan = new int[100];
    }

    static void prosesInputPesanan() {
        boolean ordering = true;

        while (ordering) {
            tampilkanDaftarMenu();
            System.out.println("\nKetik nama menu untuk memesan.");
            System.out.println("Ketik 'selesai' untuk menyelesaikan pesanan.");
            System.out.print("Input: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("selesai")) {
                if (totalItem == 0) {
                    System.out.println("[!] Anda belum memesan apapun!\n");
                } else {
                    ordering = false;
                }
                continue;
            }

            Menu menuDitemukan = cariMenu(input);
            if (menuDitemukan == null) {
                System.out.println("[!] Menu '" + input + "' tidak ditemukan. Coba lagi.\n");
                continue;
            }

            tambahKePesanan(menuDitemukan);
        }
    }

    static void tambahKePesanan(Menu menu) {
        System.out.print("Jumlah " + menu.getNama() + ": ");
        int jumlah = inputAngka(1, 999);

        // Cek apakah menu sudah ada di pesanan
        for (int i = 0; i < totalItem; i++) {
            if (itemPesan[i].getNama().equalsIgnoreCase(menu.getNama())) {
                jumlahPesan[i] += jumlah;
                System.out.println("-> " + menu.getNama()
                        + " diperbarui. Total: " + jumlahPesan[i] + "\n");
                return;
            }
        }

        // Menu belum ada, tambahkan baru
        itemPesan[totalItem] = menu;
        jumlahPesan[totalItem] = jumlah;
        totalItem++;
        System.out.println("-> " + menu.getNama() + " x" + jumlah + " ditambahkan.\n");
    }

    // PERHITUNGAN BIAYA

    static double hitungSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < totalItem; i++) {
            subtotal += itemPesan[i].getHarga() * jumlahPesan[i];
        }
        return subtotal;
    }

    static double hitungDiskon10(double subtotal) {
        return subtotal > BATAS_DISKON ? subtotal * TARIF_DISKON : 0;
    }

    static Menu cariMinumanPertama() {
        for (int i = 0; i < totalItem; i++) {
            if (itemPesan[i].getKategori().equals("Minuman")) {
                return itemPesan[i];
            }
        }
        return null;
    }

    static double hitungDiskonBOGO(double subtotal) {
        if (subtotal <= BATAS_BOGO)
            return 0;
        Menu minuman = cariMinumanPertama();
        return minuman != null ? minuman.getHarga() : 0;
    }

    // CETAK STRUK (dipecah menjadi bagian kecil)

    static void cetakStruk() {
        double subtotal = hitungSubtotal();
        double diskon10 = hitungDiskon10(subtotal);
        double diskonBOGO = hitungDiskonBOGO(subtotal);
        Menu minumanBOGO = (diskonBOGO > 0) ? cariMinumanPertama() : null;
        double totalSetelahDiskon = subtotal - diskon10 - diskonBOGO;
        double pajak = subtotal * TARIF_PAJAK;
        double totalAkhir = totalSetelahDiskon + pajak + BIAYA_PELAYANAN;

        cetakHeaderStruk();
        cetakItemPesanan();
        cetakRingkasanBiaya(subtotal, diskon10, diskonBOGO,
                minumanBOGO, totalSetelahDiskon, pajak, totalAkhir);
        cetakFooterStruk();
    }

    static void cetakHeaderStruk() {
        System.out.println("\n\n============================================");
        System.out.println("           STRUK PEMBAYARAN                ");
        System.out.println("         SERANG GA(?) CHAFE - DPR Building State            ");
        System.out.println("============================================");
        System.out.printf("%-24s %5s %10s%n", "Item", "Qty", "Subtotal");
        System.out.println("--------------------------------------------");
    }

    static void cetakItemPesanan() {
        for (int i = 0; i < totalItem; i++) {
            double subtotalItem = itemPesan[i].getHarga() * jumlahPesan[i];
            System.out.printf("  %-22s x%-3d %10s%n",
                    itemPesan[i].getNama(),
                    jumlahPesan[i],
                    formatRupiah(subtotalItem));
            System.out.printf("  %-22s%n",
                    "  @ " + formatRupiah(itemPesan[i].getHarga()));
        }
    }

    static void cetakRingkasanBiaya(
            double subtotal,
            double diskon10,
            double diskonBOGO,
            Menu minumanBOGO,
            double totalSetelahDiskon,
            double pajak,
            double totalAkhir) {

        System.out.println("--------------------------------------------");
        System.out.printf("%-28s %12s%n", "Subtotal", formatRupiah(subtotal));

        // Bagian diskon (hanya tampil jika berlaku)
        if (diskon10 > 0 || diskonBOGO > 0) {
            System.out.println("--------------------------------------------");

            if (diskon10 > 0) {
                System.out.println("  ** DISKON BERLAKU **");
                System.out.printf("%-28s %12s%n",
                        "Diskon 10% (>Rp 100.000)",
                        "- " + formatRupiah(diskon10));
            }

            if (diskonBOGO > 0 && minumanBOGO != null) {
                System.out.println("  ** PROMO BELI 1 GRATIS 1 MINUMAN **");
                System.out.printf("%-28s %12s%n",
                        "Gratis 1x " + minumanBOGO.getNama(),
                        "- " + formatRupiah(diskonBOGO));
            }

            System.out.println("--------------------------------------------");
            System.out.printf("%-28s %12s%n",
                    "Setelah Diskon", formatRupiah(totalSetelahDiskon));
        }

        System.out.println("--------------------------------------------");
        System.out.printf("%-28s %12s%n", "Pajak (10%)", formatRupiah(pajak));
        System.out.printf("%-28s %12s%n", "Biaya Pelayanan", formatRupiah(BIAYA_PELAYANAN));
        System.out.println("============================================");
        System.out.printf("%-28s %12s%n", "TOTAL PEMBAYARAN", formatRupiah(totalAkhir));
    }

    static void cetakFooterStruk() {
        System.out.println("============================================");
        System.out.println("   Terima kasih telah makan di sini!       ");
        System.out.println("   Tentu Saja Nikmat, kan(?) :)      ");
        System.out.println("============================================\n");
    }

    // UTILITAS PENCARIAN & FORMAT

    static Menu cariMenu(String namaCari) {
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i].getNama().equalsIgnoreCase(namaCari.trim())) {
                return daftarMenu[i];
            }
        }
        return null;
    }

    static String formatRupiah(double angka) {
        return String.format("Rp %,.0f", angka);
    }

    // MENU PENGELOLAAN (PEMILIK)

    static void menuPengelolaan() {
        boolean managing = true;

        while (managing) {
            System.out.println("\n============================================");
            System.out.println("         MENU PENGELOLAAN RESTORAN         ");
            System.out.println("============================================");
            System.out.println("  1. Tampilkan Daftar Menu");
            System.out.println("  2. Tambah Menu Baru");
            System.out.println("  3. Ubah Harga Menu");
            System.out.println("  4. Hapus Menu");
            System.out.println("  0. Kembali ke Menu Utama");
            System.out.println("============================================");
            System.out.print("Pilih: ");

            String pilihan = inputPilihan(new String[] { "0", "1", "2", "3", "4" });

            switch (pilihan) {
                case "1":
                    tampilkanDaftarMenuNomor();
                    break;
                case "2":
                    tambahMenu();
                    break;
                case "3":
                    ubahHargaMenu();
                    break;
                case "4":
                    hapusMenu();
                    break;
                case "0":
                    managing = false;
                    break;
            }
        }
    }

    // TAMBAH MENU BARU

    static void tambahMenu() {
        boolean addingMore = true;

        while (addingMore) {
            System.out.println("\n--- Tambah Menu Baru ---");
            System.out.println("(Ketik 'batal' pada nama untuk membatalkan)");

            String nama = inputNamaMenu();
            if (nama == null)
                break;

            System.out.print("Harga: Rp ");
            double harga = inputAngkaDesimal(0);

            String kategori = inputKategori();

            resizeJikaPerlu();
            daftarMenu[jumlahMenu] = new Menu(nama, harga, kategori);
            jumlahMenu++;

            System.out.println("-> Menu '" + nama + "' berhasil ditambahkan!");
            addingMore = inputKonfirmasi("Tambah menu lagi?");
        }
    }

    static String inputNamaMenu() {
        while (true) {
            System.out.print("Nama menu: ");
            String nama = scanner.nextLine().trim();
            if (nama.equalsIgnoreCase("batal"))
                return null;
            if (!nama.isEmpty())
                return nama;
            System.out.println("[!] Nama tidak boleh kosong.");
        }
    }

    static String inputKategori() {
        System.out.println("Kategori:");
        System.out.println("  1. Makanan");
        System.out.println("  2. Minuman");
        System.out.println("  3. Dessert");
        System.out.print("Pilih (1/2/3): ");

        String pilihan = inputPilihan(new String[] { "1", "2", "3" });
        switch (pilihan) {
            case "1":
                return "Makanan";
            case "2":
                return "Minuman";
            default:
                return "Dessert";
        }
    }

    static void resizeJikaPerlu() {
        if (jumlahMenu >= daftarMenu.length) {
            Menu[] temp = new Menu[daftarMenu.length + 10];
            for (int i = 0; i < jumlahMenu; i++) {
                temp[i] = daftarMenu[i];
            }
            daftarMenu = temp;
        }
    }

    // UBAH HARGA MENU

    static void ubahHargaMenu() {
        tampilkanDaftarMenuNomor();

        System.out.print("\nNomor menu yang akan diubah (0 untuk batal): ");
        int nomor = inputAngka(0, jumlahMenu);
        if (nomor == 0) {
            System.out.println("Dibatalkan.");
            return;
        }

        Menu menu = daftarMenu[nomor - 1];

        System.out.println("\nMenu    : " + menu.getNama());
        System.out.println("Harga   : " + formatRupiah(menu.getHarga()));
        System.out.print("Harga baru: Rp ");
        double hargaBaru = inputAngkaDesimal(0);

        System.out.println("\nKonfirmasi Perubahan:");
        System.out.println("  " + menu.getNama() + ": "
                + formatRupiah(menu.getHarga()) + " -> " + formatRupiah(hargaBaru));

        if (inputKonfirmasi("Yakin mengubah harga?")) {
            menu.setHarga(hargaBaru);
            System.out.println("-> Harga berhasil diubah!\n");
        } else {
            System.out.println("-> Perubahan dibatalkan.\n");
        }
    }

    // HAPUS MENU

    static void hapusMenu() {
        tampilkanDaftarMenuNomor();

        System.out.print("\nNomor menu yang akan dihapus (0 untuk batal): ");
        int nomor = inputAngka(0, jumlahMenu);
        if (nomor == 0) {
            System.out.println("Dibatalkan.");
            return;
        }

        int index = nomor - 1;
        Menu menu = daftarMenu[index];

        System.out.println("\nKonfirmasi Penghapusan:");
        System.out.println("  Nama     : " + menu.getNama());
        System.out.println("  Harga    : " + formatRupiah(menu.getHarga()));
        System.out.println("  Kategori : " + menu.getKategori());

        if (inputKonfirmasi("Yakin menghapus menu ini?")) {
            geserArrayKiri(index);
            System.out.println("-> Menu '" + menu.getNama() + "' berhasil dihapus!\n");
        } else {
            System.out.println("-> Penghapusan dibatalkan.\n");
        }
    }

    static void geserArrayKiri(int index) {
        for (int i = index; i < jumlahMenu - 1; i++) {
            daftarMenu[i] = daftarMenu[i + 1];
        }
        daftarMenu[jumlahMenu - 1] = null;
        jumlahMenu--;
    }
}