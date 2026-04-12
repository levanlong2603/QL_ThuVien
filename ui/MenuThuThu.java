package ui;

import model.DocGia;
import model.PhieuMuon;
import model.Sach;
import model.ThuThu;
import service.QuanLyDocGia;
import service.QuanLyPhieuMuon;
import service.QuanLySach;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Giao dien danh cho thu thu.
 */
public class MenuThuThu extends JFrame {
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ThuThu thuThuDangNhap;
    private final QuanLySach quanLySach;
    private final QuanLyDocGia quanLyDocGia;
    private final QuanLyPhieuMuon quanLyPhieuMuon;
    private final Runnable suKienDangXuat;
    private Runnable lamMoiBangSach = () -> {};
    private final JLabel lblThongKeMuonTra = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblThongKeSach = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblThongKeDocGia = new JLabel(" ", SwingConstants.RIGHT);

    public MenuThuThu(ThuThu thuThuDangNhap, QuanLySach quanLySach, QuanLyDocGia quanLyDocGia,
                      QuanLyPhieuMuon quanLyPhieuMuon,
                      Runnable suKienDangXuat) {
        this.thuThuDangNhap = thuThuDangNhap;
        this.quanLySach = quanLySach;
        this.quanLyDocGia = quanLyDocGia;
        this.quanLyPhieuMuon = quanLyPhieuMuon;
        this.suKienDangXuat = suKienDangXuat;

        setTitle("Menu Thủ Thư - " + thuThuDangNhap.getTenNguoiDung());
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mượn/Trả sách", taoPanelMuonTraSach());
        tabbedPane.addTab("Quản lý sách", taoPanelQuanLySach());
        tabbedPane.addTab("Quản lý độc giả", taoPanelDocGia());

        JButton btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.addActionListener(e -> dangXuat());

        JPanel panelTren = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTren.add(btnDangXuat);

        add(panelTren, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void dangXuat() {
        dispose();
        if (suKienDangXuat != null) {
            suKienDangXuat.run();
        }
    }

    private JPanel taoPanelMuonTraSach() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMaPhieuMuon = new JTextField(10);
        JTextField txtMaDocGia = new JTextField(10);
        JTextField txtMaSach = new JTextField(10);
        JTextField txtTenSach = new JTextField(18);
        JTextField txtNgayMuon = new JTextField(12);
        JTextField txtSoNgayMuon = new JTextField(12);
        JTextField txtTimPhieu = taoOtimKiem(12, "Lọc phiếu theo mã/độc giả/sách");

        txtTenSach.setEditable(false);
        txtNgayMuon.setEditable(false);
        txtMaPhieuMuon.setToolTipText("Mã được gợi ý tự động, bạn có thể sửa");
        txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
        txtNgayMuon.setText(chuyenNgay(LocalDate.now()));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 8));
        form.add(taoDongNhap("Mã phiếu mượn", txtMaPhieuMuon));
        form.add(taoDongNhap("Mã độc giả", txtMaDocGia));
        form.add(taoDongNhap("Mã sách", txtMaSach));
        form.add(taoDongNhap("Tên sách", txtTenSach));
        form.add(taoDongNhap("Ngày mượn", txtNgayMuon));
        form.add(taoDongNhap("Số ngày mượn", txtSoNgayMuon));

        JButton btnTimPhieu = new JButton("Tìm");
        JPanel panelTimKiemPhieu = taoThanhTimKiem(btnTimPhieu, txtTimPhieu);

        JPanel panelFormVaTimPhieu = new JPanel(new BorderLayout(10, 10));
        panelFormVaTimPhieu.add(form, BorderLayout.CENTER);
        panelFormVaTimPhieu.add(panelTimKiemPhieu, BorderLayout.EAST);

        txtSoNgayMuon.setText("7");

        DefaultTableModel modelPhieu = new DefaultTableModel(
            new String[]{"Mã phiếu", "Mã độc giả", "Mã sách", "Tên sách", "Số ngày mượn", "Số ngày còn lại", "Ngày mượn", "Ngày trả", "Trạng thái"}, 0);
        JTable bangPhieu = new JTable(modelPhieu);
        bangPhieu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangPhieu.getSelectedRow();
            if (dong >= 0) {
                txtMaPhieuMuon.setText(String.valueOf(modelPhieu.getValueAt(dong, 0)));
                txtMaDocGia.setText(String.valueOf(modelPhieu.getValueAt(dong, 1)));
                txtMaSach.setText(String.valueOf(modelPhieu.getValueAt(dong, 2)));
                txtTenSach.setText(String.valueOf(modelPhieu.getValueAt(dong, 3)));
                txtSoNgayMuon.setText(String.valueOf(modelPhieu.getValueAt(dong, 4)));
                txtNgayMuon.setText(String.valueOf(modelPhieu.getValueAt(dong, 6)));
            }
        });

        txtMaSach.addActionListener(e -> capNhatTenSachTheoMa(txtMaSach, txtTenSach));
        ganLocTheoNhapLieu(txtMaSach, () -> capNhatTenSachTheoMa(txtMaSach, txtTenSach));
        ganLocTheoNhapLieu(txtTimPhieu, () -> locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim()));

        btnTimPhieu.addActionListener(e -> locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim()));

        JLabel lblThongBao = new JLabel(" ");

        JButton btnMuon = new JButton("Mượn sách");
        JButton btnTra = new JButton("Trả sách");
        JButton btnCapNhat = new JButton("Cập nhật phiếu");
        JButton btnXoaPhieu = new JButton("Xóa phiếu");
        JButton btnLamMoi = new JButton("Làm mới");

        JLabel lblThongTinThuThu = new JLabel("Thủ thư: " + thuThuDangNhap.getMaNguoiDung()
            + " - " + thuThuDangNhap.getTenNguoiDung()
            + " | SĐT: " + thuThuDangNhap.getSoDienThoai());

        btnMuon.addActionListener(e -> {
            try {
                String maDocGia = txtMaDocGia.getText().trim();
                if (maDocGia.isBlank()) {
                    lblThongBao.setText("Mã độc giả không được để trống");
                    return;
                }

                DocGia docGia = quanLyDocGia.timDocGia(maDocGia);
                if (docGia == null) {
                    lblThongBao.setText("Không tìm thấy độc giả. Vui lòng thêm độc giả ở tab Quản lý độc giả.");
                    return;
                }

                String thongBao = quanLyPhieuMuon.muonSach(
                        txtMaPhieuMuon.getText().trim(),
                        txtMaSach.getText().trim(),
                        maDocGia,
                        thuThuDangNhap.getMaNguoiDung(),
                    docSoNgayMuonHopLe(txtSoNgayMuon.getText().trim(), "Số ngày mượn")
                );
                lblThongBao.setText(thongBao);
                locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim());
                lamMoiBangSach.run();

                String maPhieuVuaMuon = txtMaPhieuMuon.getText().trim();
                if (thongBao.startsWith("Mượn sách thành công")) {
                    txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
                }

                PhieuMuon phieuMuon = timPhieuTheoMa(maPhieuVuaMuon);
                if (phieuMuon != null) {
                    txtNgayMuon.setText(chuyenNgay(phieuMuon.getNgayMuon()));
                    txtSoNgayMuon.setText(String.valueOf(tinhSoNgayMuon(phieuMuon)));
                }

                Sach sach = quanLySach.timSachTheoMa(txtMaSach.getText().trim());
                txtTenSach.setText(sach == null ? "" : sach.getTenSach());
            } catch (Exception ex) {
                lblThongBao.setText(ex.getMessage() == null ? "Dữ liệu không hợp lệ" : ex.getMessage());
            }
        });

        btnTra.addActionListener(e -> {
            String thongBao = quanLyPhieuMuon.traSach(txtMaPhieuMuon.getText().trim());

            lblThongBao.setText(thongBao);
            locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim());
            lamMoiBangSach.run();
        });

        btnCapNhat.addActionListener(e -> {
            try {
                String thongBao = quanLyPhieuMuon.capNhatPhieuMuon(
                        txtMaPhieuMuon.getText().trim(),
                        docSoNgayMuonHopLe(txtSoNgayMuon.getText().trim(), "Số ngày mượn")
                );
                lblThongBao.setText(thongBao);
                locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim());
                lamMoiBangSach.run();
            } catch (IllegalArgumentException ex) {
                lblThongBao.setText(ex.getMessage());
            }
        });

        btnXoaPhieu.addActionListener(e -> {
            int xacNhan = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa phiếu mượn này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );
            if (xacNhan != JOptionPane.YES_OPTION) {
                return;
            }

            String thongBao = quanLyPhieuMuon.xoaPhieuMuon(txtMaPhieuMuon.getText().trim());
            lblThongBao.setText(thongBao);
            locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim());
            lamMoiBangSach.run();
            if (thongBao.startsWith("Xóa")) {
                txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
                txtMaDocGia.setText("");
                txtMaSach.setText("");
                txtTenSach.setText("");
                txtNgayMuon.setText(chuyenNgay(LocalDate.now()));
                txtSoNgayMuon.setText("7");
                bangPhieu.clearSelection();
            }
        });

        btnLamMoi.addActionListener(e -> {
            locBangPhieuMuonTheoTuKhoa(modelPhieu, txtTimPhieu.getText().trim());
            lblThongBao.setText("Đã làm mới danh sách phiếu mượn/trả");
        });

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNut.add(btnMuon);
        panelNut.add(btnTra);
        panelNut.add(btnCapNhat);
        panelNut.add(btnXoaPhieu);
        panelNut.add(btnLamMoi);

        JPanel panelTren = new JPanel(new BorderLayout(8, 8));
        panelTren.add(lblThongTinThuThu, BorderLayout.NORTH);
        panelTren.add(panelFormVaTimPhieu, BorderLayout.CENTER);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 8));
        panelDuoi.add(new JScrollPane(bangPhieu), BorderLayout.CENTER);

        JPanel panelTrangThaiDuoi = new JPanel(new BorderLayout(8, 0));
        panelTrangThaiDuoi.add(lblThongBao, BorderLayout.WEST);
        panelTrangThaiDuoi.add(lblThongKeMuonTra, BorderLayout.EAST);

        JPanel panelChanDuoi = new JPanel(new BorderLayout(8, 0));
        panelChanDuoi.add(panelNut, BorderLayout.WEST);
        panelChanDuoi.add(panelTrangThaiDuoi, BorderLayout.CENTER);
        panelDuoi.add(panelChanDuoi, BorderLayout.SOUTH);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(panelDuoi, BorderLayout.CENTER);

        capNhatBangMuonTra(modelPhieu);
        return panel;
    }

    private JPanel taoPanelQuanLySach() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMaSach = new JTextField(10);
        JTextField txtTenSach = new JTextField(15);
        JTextField txtTacGia = new JTextField(12);
        JTextField txtTheLoai = new JTextField(10);
        JTextField txtNhaXuatBan = new JTextField(12);
        JTextField txtNamXuatBan = new JTextField(5);
        JTextField txtTongSoLuong = new JTextField(5);
        JTextField txtSoLuongCon = new JTextField(5);
        JTextField txtTimMaSach = taoOtimKiem(10, "Lọc theo mã/tên sách");
        txtMaSach.setText(goiYMaSachTiepTheo());
        txtSoLuongCon.setEditable(false);

        JPanel form = new JPanel(new GridLayout(4, 4, 8, 8));
        form.add(new JLabel("Mã sách"));
        form.add(txtMaSach);
        form.add(new JLabel("Tên sách"));
        form.add(txtTenSach);
        form.add(new JLabel("Tác giả"));
        form.add(txtTacGia);
        form.add(new JLabel("Thể loại"));
        form.add(txtTheLoai);
        form.add(new JLabel("Nhà xuất bản"));
        form.add(txtNhaXuatBan);
        form.add(new JLabel("Năm xuất bản"));
        form.add(txtNamXuatBan);
        form.add(new JLabel("Tổng số lượng"));
        form.add(txtTongSoLuong);
        form.add(new JLabel("Số lượng còn"));
        form.add(txtSoLuongCon);

        JButton btnTimSach = new JButton("Tìm");
        JPanel panelTimKiem = taoThanhTimKiem(btnTimSach, txtTimMaSach);

        JPanel panelTren = new JPanel(new BorderLayout(10, 10));
        panelTren.add(form, BorderLayout.CENTER);
        panelTren.add(panelTimKiem, BorderLayout.EAST);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên sách", "Tác giả", "Thể loại", "Nhà XB", "Năm XB", "Tổng SL", "SL còn"}, 0);
        JTable bangKetQua = new JTable(model);
        lamMoiBangSach = () -> capNhatBangSach(model);

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");

        bangKetQua.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangKetQua.getSelectedRow();
            if (dong < 0) {
                return;
            }
            txtMaSach.setText(String.valueOf(model.getValueAt(dong, 0)));
            txtTenSach.setText(String.valueOf(model.getValueAt(dong, 1)));
            txtTacGia.setText(String.valueOf(model.getValueAt(dong, 2)));
            txtTheLoai.setText(String.valueOf(model.getValueAt(dong, 3)));
            txtNhaXuatBan.setText(String.valueOf(model.getValueAt(dong, 4)));
            txtNamXuatBan.setText(String.valueOf(model.getValueAt(dong, 5)));
            txtTongSoLuong.setText(String.valueOf(model.getValueAt(dong, 6)));
            txtSoLuongCon.setText(String.valueOf(model.getValueAt(dong, 7)));
            capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
        });

        ganLocTheoNhapLieu(txtMaSach, () -> capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon));
        ganLocTheoNhapLieu(txtTongSoLuong, () -> capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon));

        btnThem.addActionListener(e -> {
            try {
                Sach sach = new Sach(
                        txtMaSach.getText().trim(),
                        txtTenSach.getText().trim(),
                        txtTacGia.getText().trim(),
                        txtTheLoai.getText().trim(),
                        txtNhaXuatBan.getText().trim(),
                        Integer.parseInt(txtNamXuatBan.getText().trim()),
                        Integer.parseInt(txtTongSoLuong.getText().trim()),
                        tinhSoLuongCon(txtMaSach.getText().trim(), Integer.parseInt(txtTongSoLuong.getText().trim()))
                );
                quanLySach.themSach(sach);
                capNhatBangSach(model);
                txtMaSach.setText(goiYMaSachTiepTheo());
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu sách không hợp lệ");
            }
        });

        btnSua.addActionListener(e -> {
            try {
                Sach sach = new Sach(
                        txtMaSach.getText().trim(),
                        txtTenSach.getText().trim(),
                        txtTacGia.getText().trim(),
                        txtTheLoai.getText().trim(),
                        txtNhaXuatBan.getText().trim(),
                        Integer.parseInt(txtNamXuatBan.getText().trim()),
                        Integer.parseInt(txtTongSoLuong.getText().trim()),
                        tinhSoLuongCon(txtMaSach.getText().trim(), Integer.parseInt(txtTongSoLuong.getText().trim()))
                );
                if (quanLySach.capNhatSach(sach)) {
                    capNhatBangSach(model);
                    capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy mã sách để sửa");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu sách không hợp lệ");
            }
        });

        btnXoa.addActionListener(e -> {
            String maSach = txtMaSach.getText().trim();
            if (quanLySach.xoaSach(maSach)) {
                capNhatBangSach(model);
                txtMaSach.setText(goiYMaSachTiepTheo());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mã sách để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> {
            capNhatBangSach(model);
            txtMaSach.setText(goiYMaSachTiepTheo());
            capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
        });

        ganLocTheoNhapLieu(txtTimMaSach, () -> locBangSachTheoTuKhoa(model, txtTimMaSach.getText().trim()));

        btnTimSach.addActionListener(e -> locBangSachTheoTuKhoa(model, txtTimMaSach.getText().trim()));

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 0));
        panelDuoi.add(panelNut, BorderLayout.WEST);
        panelDuoi.add(lblThongKeSach, BorderLayout.EAST);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(new JScrollPane(bangKetQua), BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        capNhatBangSach(model);
        return panel;
    }

    private JPanel taoPanelDocGia() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMaDocGia = new JTextField(10);
        JTextField txtTenDocGia = new JTextField(15);
        JTextField txtSoDienThoai = new JTextField(12);
        JTextField txtEmail = new JTextField(15);
        JTextField txtDiaChi = new JTextField(20);
        JTextField txtTimDocGia = taoOtimKiem(10, "Lọc theo mã/tên/SĐT");
        txtMaDocGia.setText(goiYMaDocGiaTiepTheo());

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 8));
        form.add(taoDongNhap("Mã độc giả", txtMaDocGia));
        form.add(taoDongNhap("Tên độc giả", txtTenDocGia));
        form.add(taoDongNhap("Số điện thoại", txtSoDienThoai));
        form.add(taoDongNhap("Email", txtEmail));
        form.add(taoDongNhap("Địa chỉ", txtDiaChi));

        JButton btnTimDocGia = new JButton("Tìm");
        JPanel panelTimKiemDocGia = taoThanhTimKiem(btnTimDocGia, txtTimDocGia);

        JPanel panelTren = new JPanel(new BorderLayout(10, 10));
        panelTren.add(form, BorderLayout.CENTER);
        panelTren.add(panelTimKiemDocGia, BorderLayout.EAST);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã", "Tên", "SĐT", "Email", "Địa chỉ"}, 0);
        JTable bangDocGia = new JTable(model);

        bangDocGia.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangDocGia.getSelectedRow();
            if (dong < 0) {
                return;
            }
            txtMaDocGia.setText(String.valueOf(model.getValueAt(dong, 0)));
            txtTenDocGia.setText(String.valueOf(model.getValueAt(dong, 1)));
            txtSoDienThoai.setText(String.valueOf(model.getValueAt(dong, 2)));
            txtEmail.setText(String.valueOf(model.getValueAt(dong, 3)));
            txtDiaChi.setText(String.valueOf(model.getValueAt(dong, 4)));
        });

        ganLocTheoNhapLieu(txtTimDocGia, () -> locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim()));

        btnTimDocGia.addActionListener(e -> locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim()));

        JButton btnThem = new JButton("Thêm độc giả");
        JButton btnSua = new JButton("Sửa độc giả");
        JButton btnXoa = new JButton("Xóa độc giả");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> {
            DocGia docGia = new DocGia(
                    txtMaDocGia.getText().trim(),
                    txtTenDocGia.getText().trim(),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            quanLyDocGia.themDocGia(docGia);
            locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim());
            txtMaDocGia.setText(goiYMaDocGiaTiepTheo());
        });

        btnSua.addActionListener(e -> {
            DocGia docGia = new DocGia(
                    txtMaDocGia.getText().trim(),
                    txtTenDocGia.getText().trim(),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            if (quanLyDocGia.capNhatDocGia(docGia)) {
                locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để sửa");
            }
        });

        btnXoa.addActionListener(e -> {
            if (quanLyDocGia.xoaDocGia(txtMaDocGia.getText().trim())) {
                locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim());
                txtMaDocGia.setText(goiYMaDocGiaTiepTheo());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> {
            locBangDocGiaTheoTuKhoa(model, txtTimDocGia.getText().trim());
            txtMaDocGia.setText(goiYMaDocGiaTiepTheo());
        });

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 0));
        panelDuoi.add(panelNut, BorderLayout.WEST);
        panelDuoi.add(lblThongKeDocGia, BorderLayout.EAST);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(new JScrollPane(bangDocGia), BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        capNhatBangDocGia(model);
        return panel;
    }

    private void capNhatBangMuonTra(DefaultTableModel model) {
        locBangPhieuMuonTheoTuKhoa(model, "");
    }

    private void locBangPhieuMuonTheoTuKhoa(DefaultTableModel model, String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        model.setRowCount(0);
        int soDangMuon = 0;
        int soDaTra = 0;
        for (PhieuMuon phieuMuon : quanLyPhieuMuon.layDanhSachPhieuMuon()) {
            Sach sach = quanLySach.timSachTheoMa(phieuMuon.getMaSach());
            String tenSach = sach == null ? "" : sach.getTenSach();
            String ngayMuon = chuyenNgay(phieuMuon.getNgayMuon());
            String ngayTra = chuyenNgay(phieuMuon.getNgayTra());
            String maPhieu = phieuMuon.getMaPhieuMuon();
            String maDocGia = phieuMuon.getMaDocGia();
            String maSach = phieuMuon.getMaSach();
            String trangThai = phieuMuon.getTrangThai();

            boolean khop = key.isBlank()
                    || maPhieu.toLowerCase().contains(key)
                    || maDocGia.toLowerCase().contains(key)
                    || maSach.toLowerCase().contains(key)
                    || tenSach.toLowerCase().contains(key)
                    || trangThai.toLowerCase().contains(key)
                    || ngayMuon.toLowerCase().contains(key)
                    || ngayTra.toLowerCase().contains(key);
            if (!khop) {
                continue;
            }

            long soNgayMuon = tinhSoNgayMuon(phieuMuon);
            long soNgayConLai = "DA_TRA".equals(phieuMuon.getTrangThai())
                    ? 0
                    : phieuMuon.getHanTra().toEpochDay() - LocalDate.now().toEpochDay();

            if ("DA_TRA".equals(phieuMuon.getTrangThai())) {
                soDaTra++;
            } else if ("DANG_MUON".equals(phieuMuon.getTrangThai())) {
                soDangMuon++;
            }

            model.addRow(new Object[]{
                    maPhieu,
                    maDocGia,
                    maSach,
                    tenSach,
                    soNgayMuon,
                    soNgayConLai,
                    ngayMuon,
                    ngayTra,
                    trangThai
            });
        }
        lblThongKeMuonTra.setText("Tổng phiếu: " + model.getRowCount()
                + " | Đang mượn: " + soDangMuon
                + " | Đã trả: " + soDaTra);
    }

    private JPanel taoDongNhap(String nhan, JTextField oNhap) {
        JPanel dong = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel lbl = new JLabel(nhan);
        lbl.setPreferredSize(new Dimension(95, 24));
        dong.add(lbl);
        dong.add(oNhap);
        return dong;
    }

    private JPanel taoThanhTimKiem(JButton btnTim, JTextField txtTimKiem) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        Dimension kichThuocNutMacDinh = btnTim.getPreferredSize();
        Dimension kichThuocO = txtTimKiem.getPreferredSize();
        int chieuCao = Math.max(24, Math.max(kichThuocO.height, kichThuocNutMacDinh.height));
        int chieuRongNut = Math.max(58, kichThuocNutMacDinh.width + 4);
        btnTim.setPreferredSize(new Dimension(chieuRongNut, chieuCao));
        txtTimKiem.setPreferredSize(new Dimension(Math.max(150, kichThuocO.width), chieuCao));

        panel.add(btnTim);
        panel.add(txtTimKiem);
        return panel;
    }

    private JTextField taoOtimKiem(int soCot, String goiY) {
        return new JTextField(soCot) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!getText().isEmpty()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(goiY, insets.left + 4, y);
                g2.dispose();
            }
        };
    }

    private void ganLocTheoNhapLieu(JTextField oNhap, Runnable hanhDong) {
        oNhap.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hanhDong.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hanhDong.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hanhDong.run();
            }
        });
    }

    private PhieuMuon timPhieuTheoMa(String maPhieuMuon) {
        for (PhieuMuon phieuMuon : quanLyPhieuMuon.layDanhSachPhieuMuon()) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)) {
                return phieuMuon;
            }
        }
        return null;
    }

    private String chuyenNgay(LocalDate ngay) {
        return ngay == null ? "" : ngay.format(DINH_DANG_NGAY);
    }

    private int docSoNgayMuonHopLe(String giaTri, String tenTruong) {
        if (giaTri == null || giaTri.isBlank()) {
            return 7;
        }

        try {
            int soNgay = Integer.parseInt(giaTri.trim());
            if (soNgay <= 0) {
                throw new IllegalArgumentException(tenTruong + " phải lớn hơn 0");
            }
            return soNgay;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(tenTruong + " phải là số nguyên");
        }
    }

    private long tinhSoNgayMuon(PhieuMuon phieuMuon) {
        return phieuMuon.getHanTra().toEpochDay() - phieuMuon.getNgayMuon().toEpochDay();
    }

    private void capNhatTenSachTheoMa(JTextField txtMaSach, JTextField txtTenSach) {
        Sach sach = quanLySach.timSachTheoMa(txtMaSach.getText().trim());
        txtTenSach.setText(sach == null ? "" : sach.getTenSach());
    }

    private String goiYMaPhieuMuonTiepTheo() {
        java.util.List<String> danhSachMa = new java.util.ArrayList<>();
        for (PhieuMuon phieuMuon : quanLyPhieuMuon.layDanhSachPhieuMuon()) {
            danhSachMa.add(phieuMuon.getMaPhieuMuon());
        }
        return String.format("PM%03d", timSoLonNhatTheoTienTo(danhSachMa, "PM") + 1);
    }

    private String goiYMaSachTiepTheo() {
        java.util.List<String> danhSachMa = new java.util.ArrayList<>();
        for (Sach sach : quanLySach.layTatCaSach()) {
            danhSachMa.add(sach.getMaSach());
        }
        return String.format("S%03d", timSoLonNhatTheoTienTo(danhSachMa, "S") + 1);
    }

    private String goiYMaDocGiaTiepTheo() {
        java.util.List<String> danhSachMa = new java.util.ArrayList<>();
        for (DocGia docGia : quanLyDocGia.layTatCaDocGia()) {
            danhSachMa.add(docGia.getMaDocGia());
        }
        return String.format("DG%03d", timSoLonNhatTheoTienTo(danhSachMa, "DG") + 1);
    }

    private int timSoLonNhatTheoTienTo(java.util.List<String> danhSachMa, String tienTo) {
        int soLonNhat = 0;
        for (String ma : danhSachMa) {
            if (ma == null) {
                continue;
            }

            String maChuan = ma.trim().toUpperCase();
            if (!maChuan.startsWith(tienTo) || maChuan.length() <= tienTo.length()) {
                continue;
            }

            String phanSo = maChuan.substring(tienTo.length());
            try {
                int giaTri = Integer.parseInt(phanSo);
                if (giaTri > soLonNhat) {
                    soLonNhat = giaTri;
                }
            } catch (NumberFormatException ignored) {
                // Bo qua ma khong theo dang tien to + so.
            }
        }
        return soLonNhat;
    }

    private void capNhatBangDocGia(DefaultTableModel model) {
        locBangDocGiaTheoTuKhoa(model, "");
    }

    private void locBangDocGiaTheoTuKhoa(DefaultTableModel model, String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        model.setRowCount(0);
        for (DocGia docGia : quanLyDocGia.layTatCaDocGia()) {
            String maDocGia = docGia.getMaDocGia();
            String tenDocGia = docGia.getTenDocGia();
            String soDienThoai = docGia.getSoDienThoai();
            String email = docGia.getEmail();
            String diaChi = docGia.getDiaChi();

            boolean khop = key.isBlank()
                    || maDocGia.toLowerCase().contains(key)
                    || tenDocGia.toLowerCase().contains(key)
                    || soDienThoai.toLowerCase().contains(key)
                    || email.toLowerCase().contains(key)
                    || diaChi.toLowerCase().contains(key);
            if (!khop) {
                continue;
            }

            model.addRow(new Object[]{
                    maDocGia,
                    tenDocGia,
                    soDienThoai,
                    email,
                    diaChi
            });
        }
        lblThongKeDocGia.setText("Tổng độc giả: " + model.getRowCount());
    }

    private void capNhatBangSach(DefaultTableModel model) {
        model.setRowCount(0);
        int tongSoLuong = 0;
        int tongSoLuongCon = 0;
        for (Sach sach : quanLySach.layTatCaSach()) {
            int soLuongCon = tinhSoLuongCon(sach.getMaSach(), sach.getTongSoLuong());
            sach.setSoLuongCon(soLuongCon);
            tongSoLuong += sach.getTongSoLuong();
            tongSoLuongCon += soLuongCon;
            model.addRow(new Object[]{
                    sach.getMaSach(),
                    sach.getTenSach(),
                    sach.getTacGia(),
                    sach.getTheLoai(),
                    sach.getNhaXuatBan(),
                    sach.getNamXuatBan(),
                    sach.getTongSoLuong(),
                    soLuongCon
            });
        }
        lblThongKeSach.setText("Tổng đầu sách: " + model.getRowCount()
                + " | Tổng SL: " + tongSoLuong
                + " | SL còn: " + tongSoLuongCon);
    }

    private void locBangSachTheoTuKhoa(DefaultTableModel model, String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        model.setRowCount(0);

        for (Sach sach : quanLySach.layTatCaSach()) {
            boolean khop = key.isBlank()
                    || sach.getMaSach().toLowerCase().contains(key)
                    || sach.getTenSach().toLowerCase().contains(key);
            if (!khop) {
                continue;
            }

            model.addRow(new Object[]{
                    sach.getMaSach(),
                    sach.getTenSach(),
                    sach.getTacGia(),
                    sach.getTheLoai(),
                    sach.getNhaXuatBan(),
                    sach.getNamXuatBan(),
                    sach.getTongSoLuong(),
                    sach.getSoLuongCon()
            });
        }
    }

    private void capNhatSoLuongConTuDong(JTextField txtMaSach, JTextField txtTongSoLuong, JTextField txtSoLuongCon) {
        String maSach = txtMaSach.getText().trim();
        String tongText = txtTongSoLuong.getText().trim();
        if (tongText.isBlank()) {
            txtSoLuongCon.setText("");
            return;
        }

        try {
            int tongSoLuong = Integer.parseInt(tongText);
            if (tongSoLuong < 0) {
                txtSoLuongCon.setText("");
                return;
            }
            txtSoLuongCon.setText(String.valueOf(tinhSoLuongCon(maSach, tongSoLuong)));
        } catch (NumberFormatException ex) {
            txtSoLuongCon.setText("");
        }
    }

    private int tinhSoLuongCon(String maSach, int tongSoLuong) {
        int soLuongDangMuon = quanLyPhieuMuon.demSoLuongDangMuonTheoMaSach(maSach);
        return Math.max(0, tongSoLuong - soLuongDangMuon);
    }

    public void hienThiMenu() {
        setVisible(true);
    }
}

