package ui;

import model.DocGia;
import model.PhieuMuon;
import model.Sach;
import model.ThuThu;
import service.QuanLyDocGia;
import service.QuanLyPhieuMuon;
import service.QuanLySach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * Giao dien danh cho thu thu.
 */
public class MenuThuThu extends JFrame {
    private final ThuThu thuThuDangNhap;
    private final QuanLySach quanLySach;
    private final QuanLyDocGia quanLyDocGia;
    private final QuanLyPhieuMuon quanLyPhieuMuon;
    private final Runnable suKienDangXuat;

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
        JTextField txtMaSach = new JTextField(10);
        JTextField txtTenSach = new JTextField(18);
        JTextField txtNgayMuon = new JTextField(12);
        JTextField txtNgayTra = new JTextField(12);

        txtTenSach.setEditable(false);
        txtNgayMuon.setEditable(false);
        txtNgayTra.setEditable(false);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 8));
        form.add(taoDongNhap("Mã phiếu mượn", txtMaPhieuMuon));
        form.add(taoDongNhap("Mã sách", txtMaSach));
        form.add(taoDongNhap("Tên sách", txtTenSach));
        form.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)));
        form.add(taoDongNhap("Ngày mượn", txtNgayMuon));
        form.add(taoDongNhap("Ngày trả", txtNgayTra));

        DefaultTableModel modelPhieu = new DefaultTableModel(
                new String[]{"Mã phiếu", "Mã độc giả", "Mã sách", "Tên sách", "Số ngày", "Ngày mượn", "Ngày trả", "Trạng thái"}, 0);
        JTable bangPhieu = new JTable(modelPhieu);
        bangPhieu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangPhieu.getSelectedRow();
            if (dong >= 0) {
                txtMaPhieuMuon.setText(String.valueOf(modelPhieu.getValueAt(dong, 0)));
                txtMaSach.setText(String.valueOf(modelPhieu.getValueAt(dong, 2)));
                txtTenSach.setText(String.valueOf(modelPhieu.getValueAt(dong, 3)));
                txtNgayMuon.setText(String.valueOf(modelPhieu.getValueAt(dong, 5)));
                txtNgayTra.setText(String.valueOf(modelPhieu.getValueAt(dong, 6)));
            }
        });

        txtMaSach.addActionListener(e -> {
            Sach sach = quanLySach.timSachTheoMa(txtMaSach.getText().trim());
            txtTenSach.setText(sach == null ? "" : sach.getTenSach());
        });

        JLabel lblThongBao = new JLabel(" ");

        JButton btnMuon = new JButton("Mượn sách");
        JButton btnTra = new JButton("Trả sách");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnXoaForm = new JButton("Xóa nhập");

        JLabel lblThongTinThuThu = new JLabel("Thủ thư: " + thuThuDangNhap.getMaNguoiDung()
            + " - " + thuThuDangNhap.getTenNguoiDung()
            + " | SĐT: " + thuThuDangNhap.getSoDienThoai());

        btnMuon.addActionListener(e -> {
            try {
                String maDocGia = JOptionPane.showInputDialog(
                        this,
                        "Nhập mã độc giả cho phiếu mượn:",
                        "Mã độc giả",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (maDocGia == null) {
                    lblThongBao.setText("Đã hủy thao tác mượn sách");
                    return;
                }
                maDocGia = maDocGia.trim();
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
                        7
                );
                lblThongBao.setText(thongBao);
                capNhatBangMuonTra(modelPhieu);
                Sach sach = quanLySach.timSachTheoMa(txtMaSach.getText().trim());
                txtTenSach.setText(sach == null ? "" : sach.getTenSach());
            } catch (Exception ex) {
                lblThongBao.setText("Dữ liệu không hợp lệ");
            }
        });

        btnTra.addActionListener(e -> {
            String thongBao = quanLyPhieuMuon.traSach(txtMaPhieuMuon.getText().trim());
            lblThongBao.setText(thongBao);
            capNhatBangMuonTra(modelPhieu);

            PhieuMuon phieuMuon = timPhieuTheoMa(txtMaPhieuMuon.getText().trim());
            if (phieuMuon != null) {
                txtNgayTra.setText(chuyenNgay(phieuMuon.getNgayTra()));
            }
        });

        btnLamMoi.addActionListener(e -> {
            capNhatBangMuonTra(modelPhieu);
            lblThongBao.setText("Đã làm mới danh sách phiếu mượn/trả");
        });

        btnXoaForm.addActionListener(e -> {
            txtMaPhieuMuon.setText("");
            txtMaSach.setText("");
            txtTenSach.setText("");
            txtNgayMuon.setText("");
            txtNgayTra.setText("");
            lblThongBao.setText(" ");
            bangPhieu.clearSelection();
        });

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNut.add(btnMuon);
        panelNut.add(btnTra);
        panelNut.add(btnLamMoi);
        panelNut.add(btnXoaForm);

        JPanel panelTren = new JPanel(new BorderLayout(8, 8));
        panelTren.add(lblThongTinThuThu, BorderLayout.NORTH);
        panelTren.add(form, BorderLayout.CENTER);
        panelTren.add(panelNut, BorderLayout.SOUTH);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 8));
        panelDuoi.add(new JScrollPane(bangPhieu), BorderLayout.CENTER);
        panelDuoi.add(lblThongBao, BorderLayout.SOUTH);

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
        JTextField txtTimMaSach = new JTextField(10);

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

        JPanel panelTimKiem = new JPanel(new GridLayout(4, 1, 6, 6));
        panelTimKiem.add(new JLabel("Tìm kiếm theo mã sách", SwingConstants.CENTER));
        panelTimKiem.add(txtTimMaSach);

        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnHienThi = new JButton("Hiển thị");
        panelTimKiem.add(btnTim);
        panelTimKiem.add(btnHienThi);

        JPanel panelTren = new JPanel(new BorderLayout(10, 10));
        panelTren.add(form, BorderLayout.CENTER);
        panelTren.add(panelTimKiem, BorderLayout.EAST);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên sách", "Tác giả", "Thể loại", "Nhà XB", "Năm XB", "Tổng SL", "SL còn"}, 0);
        JTable bangKetQua = new JTable(model);

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
        });

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
                        Integer.parseInt(txtSoLuongCon.getText().trim())
                );
                quanLySach.themSach(sach);
                capNhatBangSach(model);
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
                        Integer.parseInt(txtSoLuongCon.getText().trim())
                );
                if (quanLySach.capNhatSach(sach)) {
                    capNhatBangSach(model);
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
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mã sách để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> capNhatBangSach(model));

        btnTim.addActionListener(e -> {
            String maSach = txtTimMaSach.getText().trim();
            model.setRowCount(0);
            if (maSach.isBlank()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sách cần tìm");
                return;
            }

            Sach sach = quanLySach.timSachTheoMa(maSach);
            if (sach == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sách");
                return;
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
        });

        btnHienThi.addActionListener(e -> capNhatBangSach(model));

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(new JScrollPane(bangKetQua), BorderLayout.CENTER);
        panel.add(panelNut, BorderLayout.SOUTH);

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

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        form.add(new JLabel("Mã độc giả"));
        form.add(txtMaDocGia);
        form.add(new JLabel("Tên độc giả"));
        form.add(txtTenDocGia);
        form.add(new JLabel("Số điện thoại"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);

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
            capNhatBangDocGia(model);
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
                capNhatBangDocGia(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để sửa");
            }
        });

        btnXoa.addActionListener(e -> {
            if (quanLyDocGia.xoaDocGia(txtMaDocGia.getText().trim())) {
                capNhatBangDocGia(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> capNhatBangDocGia(model));

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(bangDocGia), BorderLayout.CENTER);
        panel.add(panelNut, BorderLayout.SOUTH);

        capNhatBangDocGia(model);
        return panel;
    }

    private void capNhatBangMuonTra(DefaultTableModel model) {
        model.setRowCount(0);
        for (PhieuMuon phieuMuon : quanLyPhieuMuon.layDanhSachPhieuMuon()) {
            Sach sach = quanLySach.timSachTheoMa(phieuMuon.getMaSach());
            String tenSach = sach == null ? "" : sach.getTenSach();
            long soNgayMuon = phieuMuon.getHanTra().toEpochDay() - phieuMuon.getNgayMuon().toEpochDay();

            model.addRow(new Object[]{
                    phieuMuon.getMaPhieuMuon(),
                    phieuMuon.getMaDocGia(),
                    phieuMuon.getMaSach(),
                    tenSach,
                    soNgayMuon,
                    chuyenNgay(phieuMuon.getNgayMuon()),
                    chuyenNgay(phieuMuon.getNgayTra()),
                    phieuMuon.getTrangThai()
            });
        }
    }

    private JPanel taoDongNhap(String nhan, JTextField oNhap) {
        JPanel dong = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel lbl = new JLabel(nhan);
        lbl.setPreferredSize(new Dimension(95, 24));
        dong.add(lbl);
        dong.add(oNhap);
        return dong;
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
        return ngay == null ? "" : ngay.toString();
    }

    private void capNhatBangDocGia(DefaultTableModel model) {
        model.setRowCount(0);
        for (DocGia docGia : quanLyDocGia.layTatCaDocGia()) {
            model.addRow(new Object[]{
                    docGia.getMaDocGia(),
                    docGia.getTenDocGia(),
                    docGia.getSoDienThoai(),
                    docGia.getEmail(),
                    docGia.getDiaChi()
            });
        }
    }

    private void capNhatBangSach(DefaultTableModel model) {
        model.setRowCount(0);
        for (Sach sach : quanLySach.layTatCaSach()) {
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

    public void hienThiMenu() {
        setVisible(true);
    }

    public void xuLyLuaChon() {
        // Xu ly lua chon da duoc map thong qua cac nut su kien Swing.
    }
}

