public class Menu {
    private String nama;
    private double harga;
    private String kategori;

    public Menu(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public String getKategori() { return kategori; }

    public void setHarga(double harga) { this.harga = harga; }
    public void setNama(String nama) { this.nama = nama; }

    // Untuk keperluan manajemen menu
    public static Menu[] getDaftarMenu() {
        return new Menu[] {
            // Makanan (minimal 4)
            new Menu("Kecoa Saus Mentega",       35000, "Makanan"),
            new Menu("Kelabang Rica-Rica",        40000, "Makanan"),
            new Menu("Kupu-Kupu Goreng",          30000, "Makanan"),
            new Menu("Jangkrik Kuah",             28000, "Makanan"),
            new Menu("Cicak Tongseng",            38000, "Makanan"),
            // Minuman (minimal 4)
            new Menu("Susu Kecoa",               15000, "Minuman"),
            new Menu("Jus Mengkudu",             18000, "Minuman"),
            new Menu("Air Jentik",               12000, "Minuman"),
            new Menu("Smoothie Semut",           20000, "Minuman"),
            // Dessert (minimal 4)
            new Menu("Pudding Telur Kodok",      22000, "Dessert"),
            new Menu("Gelato Rhinoceros Beetle", 25000, "Dessert"),
            new Menu("Roti Bakar Selai Ubur-Ubur",20000,"Dessert")
        };
    }
}