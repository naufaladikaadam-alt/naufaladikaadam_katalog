package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import data.ProdukRepository;
import model.Produk;

public class KatalogController {

    @FXML private TextField namaField;
    @FXML private TextField hargaField;
    @FXML private ComboBox<String> fotoBox;
    @FXML private Button simpanButton;
    @FXML private Button batalButton;
    @FXML private Label modeLabel;
    @FXML private Label pesanLabel;
    @FXML private Label jumlahLabel;
    @FXML private TilePane katalogPane;

    private final ProdukRepository repository = new ProdukRepository();

    // 0 = mode tambah. Selain 0 = id produk yang sedang diedit.
    private long idDiedit = 0;

    @FXML
    private void initialize() {
        fotoBox.getItems().add("roti.png");
        fotoBox.getItems().add("susu.png");
        fotoBox.getItems().add("kopi.png");
        modeTambah();
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Gagal membaca database. Lihat Output.");
            e.printStackTrace();
        }
    }

    // ---------- Mode form ----------

    private void modeTambah() {
        idDiedit = 0;
        namaField.clear();
        hargaField.clear();
        fotoBox.setValue("roti.png");
        simpanButton.setText("Simpan");
        batalButton.setVisible(false);
        batalButton.setManaged(false);
        modeLabel.setText("Mode: tambah produk baru");
    }

    private void modeEdit(Produk produk) {
        idDiedit = produk.getId();
        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));
        fotoBox.setValue(produk.getFoto());
        simpanButton.setText("Simpan Perubahan");
        batalButton.setVisible(true);
        batalButton.setManaged(true);
        modeLabel.setText("Mode: edit produk ID " + produk.getId());
        pesanLabel.setText("");
    }

    @FXML
    private void batalEdit() {
        modeTambah();
        pesanLabel.setText("Edit dibatalkan.");
    }

    // ---------- Tampilan katalog ----------

    private void showData() throws SQLException {
        ArrayList<Produk> daftar = repository.getAll();
        katalogPane.getChildren().clear();
        for (Produk produk : daftar) {
            VBox kartu = createProductCard(produk);
            katalogPane.getChildren().add(kartu);
        }
        jumlahLabel.setText("Jumlah produk: " + daftar.size());
    }

    private VBox createProductCard(Produk produk) {
        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(170);
        fotoView.setFitHeight(110);
        fotoView.setPreserveRatio(true);

        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(180, 120);
        bingkai.setMinSize(180, 120);
        bingkai.setMaxSize(180, 120);

        URL lokasi = getClass().getResource("/image/" + produk.getFoto());
        if (lokasi != null) {
            Image gambar = new Image(lokasi.toExternalForm(), 170, 110, true, true);
            if (!gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                bingkai.getChildren().add(new Label("Gambar tidak terbaca"));
            }
        } else {
            bingkai.getChildren().add(new Label("Gambar tidak tersedia"));
        }

        Label namaLabel = new Label(produk.getNama());
        namaLabel.setWrapText(true);
        namaLabel.getStyleClass().add("nama-produk");

        Label hargaLabel = new Label("Rp" + produk.getHarga());

        Label idLabel = new Label("ID: " + produk.getId());
        idLabel.getStyleClass().add("muted");

        // Tombol aksi pada setiap kartu
        Button editButton = new Button("Edit");
        editButton.getStyleClass().addAll("button-small", "button-secondary");
        editButton.setOnAction(e -> modeEdit(produk));

        Button hapusButton = new Button("Hapus");
        hapusButton.getStyleClass().addAll("button-small", "button-danger");
        hapusButton.setOnAction(e -> hapusProduk(produk));

        HBox aksi = new HBox(8, editButton, hapusButton);

        VBox kartu = new VBox();
        kartu.setSpacing(8);
        kartu.setMinWidth(220);
        kartu.setPrefWidth(220);
        kartu.setMaxWidth(220);
        kartu.getStyleClass().add("kartu-produk");
        kartu.getChildren().add(bingkai);
        kartu.getChildren().add(namaLabel);
        kartu.getChildren().add(hargaLabel);
        kartu.getChildren().add(idLabel);
        kartu.getChildren().add(aksi);
        return kartu;
    }

    // ---------- Simpan (tambah atau edit) ----------

    @FXML
    private void saveProduct() {
        boolean modeEdit = idDiedit != 0;
        Produk produk;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            if (modeEdit) {
                produk = new Produk(idDiedit, nama, harga, foto);
            } else {
                produk = new Produk(nama, harga, foto);
            }
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }

        try {
            if (modeEdit) {
                if (!repository.update(produk)) {
                    pesanLabel.setText("Produk tidak ditemukan, mungkin sudah dihapus.");
                    modeTambah();
                    showData();
                    return;
                }
            } else {
                repository.add(produk);
            }
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menyimpan. Lihat Output.");
            e.printStackTrace();
            return;
        }

        modeTambah();
        try {
            showData();
            pesanLabel.setText(modeEdit ? "Perubahan berhasil disimpan."
                                        : "Produk berhasil disimpan.");
        } catch (SQLException e) {
            pesanLabel.setText("Data tersimpan, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }

    // ---------- Hapus ----------

    private void hapusProduk(Produk produk) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus produk \"" + produk.getNama() + "\"?",
                ButtonType.YES, ButtonType.NO);
        dialog.setHeaderText(null);
        dialog.initOwner(katalogPane.getScene().getWindow());
        ButtonType jawaban = dialog.showAndWait().orElse(ButtonType.NO);
        if (jawaban != ButtonType.YES) {
            return;   // dibatalkan, data tidak berubah
        }

        try {
            repository.delete(produk.getId());
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menghapus. Lihat Output.");
            e.printStackTrace();
            return;
        }

        // Jika produk yang sedang diedit dihapus, kembalikan form ke mode tambah
        if (idDiedit == produk.getId()) {
            modeTambah();
        }
        try {
            showData();
            pesanLabel.setText("Produk berhasil dihapus.");
        } catch (SQLException e) {
            pesanLabel.setText("Data terhapus, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }
}
