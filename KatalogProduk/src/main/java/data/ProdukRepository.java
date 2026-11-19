package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import db.Database;
import model.Produk;

public class ProdukRepository {

    // CREATE: tambah produk baru (id dibuat otomatis oleh SQLite)
    public void add(Produk produk) throws SQLException {
        String sql = "INSERT INTO produk (nama, harga, foto) VALUES (?, ?, ?)";
        try (Connection koneksi = Database.bukaKoneksi();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, produk.getNama());
            perintah.setLong(2, produk.getHarga());
            perintah.setString(3, produk.getFoto());
            perintah.executeUpdate();
        }
    }

    // READ: ambil semua produk
    public ArrayList<Produk> getAll() throws SQLException {
        ArrayList<Produk> daftar = new ArrayList<Produk>();
        String sql = "SELECT id, nama, harga, foto FROM produk ORDER BY id";
        try (Connection koneksi = Database.bukaKoneksi();
             PreparedStatement perintah = koneksi.prepareStatement(sql);
             ResultSet hasil = perintah.executeQuery()) {
            while (hasil.next()) {
                long id = hasil.getLong("id");
                String nama = hasil.getString("nama");
                long harga = hasil.getLong("harga");
                String foto = hasil.getString("foto");
                daftar.add(new Produk(id, nama, harga, foto));
            }
        }
        return daftar;
    }

    // UPDATE: ubah nama, harga, dan foto produk berdasarkan id.
    // Mengembalikan true jika ada baris yang berubah.
    public boolean update(Produk produk) throws SQLException {
        String sql = "UPDATE produk SET nama = ?, harga = ?, foto = ? WHERE id = ?";
        try (Connection koneksi = Database.bukaKoneksi();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, produk.getNama());
            perintah.setLong(2, produk.getHarga());
            perintah.setString(3, produk.getFoto());
            perintah.setLong(4, produk.getId());
            return perintah.executeUpdate() > 0;
        }
    }

    // DELETE: hapus produk berdasarkan id.
    // Mengembalikan true jika ada baris yang terhapus.
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM produk WHERE id = ?";
        try (Connection koneksi = Database.bukaKoneksi();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, id);
            return perintah.executeUpdate() > 0;
        }
    }
}
