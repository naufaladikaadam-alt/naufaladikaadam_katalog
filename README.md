# Katalog Produk 

Aplikasi Katalog Produk UPI Store adalah aplikasi desktop berbasis **JavaFX** yang dibuat untuk memenuhi tugas **Modul 12 - Katalog Produk dengan Database**.

Aplikasi ini digunakan untuk mengelola data produk melalui antarmuka grafis (GUI). Pengguna dapat menambahkan, melihat, mengubah, dan menghapus produk. Seluruh data disimpan dalam database **SQLite**, sehingga tetap ada ketika aplikasi ditutup lalu dibuka kembali.




## Fitur Aplikasi

- **Create (Tambah Produk)**
  Menambahkan produk baru dengan mengisi nama, harga, dan memilih foto produk.

- **Read (Tampilkan Produk)**
  Menampilkan seluruh produk dalam bentuk kartu katalog yang berisi foto, nama, harga, dan ID produk.

- **Update (Ubah Produk)**
  Mengubah nama, harga, dan foto produk yang sudah tersimpan melalui tombol **Edit** pada kartu produk.

- **Delete (Hapus Produk)**
  Menghapus produk dari database melalui tombol **Hapus**, dengan dialog konfirmasi sebelum data dihapus.

- **Batal Edit**
  Tombol **Batal** membatalkan proses pengeditan dan mengembalikan form ke mode tambah produk.

- **Validasi Input**
  Nama tidak boleh kosong, harga harus berupa bilangan bulat lebih dari nol, dan foto wajib dipilih.

- **Jumlah Produk**
  Menampilkan jumlah produk yang tersimpan di katalog.

## Teknologi yang Digunakan

- Java
- JavaFX
- FXML
- CSS
- SQLite
- JDBC
- Maven
- Apache NetBeans IDE

## Tampilan dan Dokumentasi Aplikasi

### 1. Tampilan Utama Katalog Produk

Halaman utama menampilkan formulir untuk menambahkan produk dan area katalog. Pada kondisi awal ini katalog masih kosong (Jumlah produk: 0).

<img width="959" height="562" alt="Screenshot 2026-11-19 230437" src="https://github.com/user-attachments/assets/1043da11-1981-4154-8520-fb74bc68b1d3" />



### 2. Menambahkan Produk

Pengguna mengisi nama, harga, dan memilih foto, lalu menekan tombol **Simpan**. Produk tersimpan ke database dan muncul sebagai kartu pada katalog, dengan pesan "Produk berhasil disimpan."


<img width="944" height="563" alt="Screenshot 2026-11-19 230523" src="https://github.com/user-attachments/assets/64ac45ed-5520-4bea-918c-f2845e2efb88" />


### 3. Katalog Berisi Beberapa Produk

Setiap kartu menampilkan foto, nama, harga, ID, serta tombol **Edit** dan **Hapus**. Jumlah produk diperbarui otomatis (pada contoh ini 3 produk).

<img width="949" height="563" alt="Screenshot 2026-11-19 230647" src="https://github.com/user-attachments/assets/85bb64e7-47af-4dc8-ae92-83e9bbb76b6c" />


### 4. Form Edit Produk

Ketika tombol **Edit** pada sebuah kartu ditekan, data produk tersebut dimasukkan ke dalam formulir. Label berubah menjadi "Mode: edit produk ID 1", tombol berubah menjadi **Simpan Perubahan**, dan muncul tombol **Batal**.

<img width="942" height="553" alt="Screenshot 2026-11-19 230907" src="https://github.com/user-attachments/assets/36fd44e1-cac6-4dce-9c9d-1b3f7f072384" />


### 5. Hasil Update Produk

Setelah tombol **Simpan Perubahan** ditekan, perubahan disimpan ke database dan kartu pada katalog langsung diperbarui (harga produk susu berubah dari Rp15000 menjadi Rp20000).

<img width="956" height="554" alt="Screenshot 2026-11-19 231001" src="https://github.com/user-attachments/assets/489cfc6f-04ee-4c94-8160-4d5093649f68" />

### 6. Konfirmasi Penghapusan Produk

Sebelum produk dihapus, aplikasi menampilkan dialog konfirmasi. Jika memilih **No**, data tidak berubah.

<img width="943" height="557" alt="Screenshot 2026-11-19 231049" src="https://github.com/user-attachments/assets/6284c9fe-8f7a-47e6-ab9e-3749e5b60280" />


### 7. Hasil Penghapusan Produk

Setelah memilih **Yes**, produk dihapus dari database dan katalog diperbarui. Pada contoh ini produk kopi terhapus sehingga jumlah produk menjadi 2.

<img width="942" height="560" alt="Screenshot 2026-11-19 231251" src="https://github.com/user-attachments/assets/967041ff-6151-414b-83cd-c49e54365445" />

## Struktur Project

```
KatalogProduk/
├── data/                         # katalog.db dibuat otomatis saat aplikasi dijalankan
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/
│       │   │   └── KatalogController.java
│       │   ├── data/
│       │   │   └── ProdukRepository.java
│       │   ├── db/
│       │   │   └── Database.java
│       │   ├── model/
│       │   │   └── Produk.java
│       │   └── main/
│       │       └── MainApp.java
│       └── resources/
│           ├── image/            # roti.png, susu.png, kopi.png
│           └── view/
│               ├── katalog.fxml
│               └── styles/
│                   └── app.css
├── screenshots/
├── pom.xml
└── README.md
```

## Database

Aplikasi menggunakan **SQLite** untuk menyimpan data produk. File database `data/katalog.db` dibuat otomatis oleh `Database.prepareDatabase()` saat aplikasi pertama kali dijalankan.

Struktur tabel `produk`:

| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id | INTEGER PRIMARY KEY | Dibuat otomatis oleh SQLite |
| nama | TEXT NOT NULL | Nama produk, tidak boleh kosong |
| harga | INTEGER NOT NULL | Harga rupiah, harus lebih dari nol |
| foto | TEXT NOT NULL | Nama file gambar, misalnya `roti.png` |

Seluruh operasi database dilakukan melalui JDBC pada class `ProdukRepository.java`:

| Operasi | Method | Perintah SQL |
|---------|--------|--------------|
| Create | `add()` | `INSERT` |
| Read | `getAll()` | `SELECT` |
| Update | `update()` | `UPDATE` |
| Delete | `delete()` | `DELETE` |

## Cara Menjalankan Aplikasi

**Kebutuhan:** JDK 17 atau lebih baru dan Apache NetBeans (atau Maven). Koneksi internet diperlukan saat pertama kali untuk mengunduh dependensi.

1. Clone repository ke komputer.
2. Buka Apache NetBeans, pilih **File > Open Project**, lalu pilih folder `KatalogProduk`.
3. Tunggu sampai dependensi Maven selesai dimuat.
4. Klik kanan proyek, pilih **Run Maven > Goals...**, isi `javafx:run`, lalu klik OK.

Atau lewat terminal di folder proyek:

```
mvn javafx:run
```

> Gunakan goal `javafx:run`, bukan tombol Run biasa, agar komponen JavaFX terbaca.

## Tujuan Pembuatan

Project ini bertujuan untuk menerapkan pemrograman GUI menggunakan JavaFX serta memahami penerapan operasi CRUD (Create, Read, Update, Delete) dengan database SQLite melalui JDBC.

**Modul 12 - Katalog Produk 
