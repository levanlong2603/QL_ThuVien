package ui;

import model.Admin;
import model.DocGia;
import model.NguoiDung;
import model.PhieuMuon;
import model.Sach;
import model.ThuThu;
import service.QuanLyDocGia;
import service.QuanLyPhieuMuon;
import service.QuanLySach;
import service.QuanLyNguoiDung;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Giao dien danh cho admin.
 */
public class MenuAdmin extends JFrame {
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Admin adminDangNhap;
    private final QuanLySach quanLySach;
    private final QuanLyDocGia quanLyDocGia;
    private final QuanLyNguoiDung quanLyNguoiDung;
    private final QuanLyPhieuMuon quanLyPhieuMuon;
    private final Runnable suKienDangXuat;
    private Runnable lamMoiBangSach = () -> {};
    private final JLabel lblThongKeMuonTra = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblThongKeSach = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblThongKeDocGia = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel lblThongKeTaiKhoan = new JLabel(" ", SwingConstants.RIGHT);

    public MenuAdmin(Admin adminDangNhap, QuanLySach quanLySach, QuanLyDocGia quanLyDocGia,
                     QuanLyNguoiDung quanLyNguoiDung, QuanLyPhieuMuon quanLyPhieuMuon,
                     Runnable suKienDangXuat) {
        this.adminDangNhap = adminDangNhap;
        this.quanLySach = quanLySach;
        this.quanLyDocGia = quanLyDocGia;
        this.quanLyNguoiDung = quanLyNguoiDung;
        this.quanLyPhieuMuon = quanLyPhieuMuon;
        this.suKienDangXuat = suKienDangXuat;

        setTitle("Menu Admin - Quản Lý Thư Viện");
        setSize(980, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý sách", taoPanelSach());
        tabbedPane.addTab("Mượn/Trả sách", taoPanelMuonTraSach());
        tabbedPane.addTab("Quản lý độc giả", taoPanelDocGia());
        tabbedPane.addTab("Quản lý tài khoản", taoPanelThuThu());

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

        txtTenSach.setEditable(false);
        txtMaPhieuMuon.setToolTipText("Mã được gợi ý tự động, bạn có thể sửa");
        txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
        batTuDongGoiYNgayMuon(txtNgayMuon);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 8));
        form.add(taoDongNhap("Mã phiếu mượn", txtMaPhieuMuon));
        form.add(taoDongNhap("Mã độc giả", txtMaDocGia));
        form.add(taoDongNhap("Mã sách", txtMaSach));
        form.add(taoDongNhap("Tên sách", txtTenSach));
        form.add(taoDongNhap("Ngày mượn", txtNgayMuon));
        form.add(taoDongNhap("Số ngày mượn", txtSoNgayMuon));
        form.add(new JLabel("Định dạng ngày: dd/MM/yyyy"));
        form.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)));

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
        txtMaSach.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                capNhatTenSachTheoMa(txtMaSach, txtTenSach);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                capNhatTenSachTheoMa(txtMaSach, txtTenSach);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                capNhatTenSachTheoMa(txtMaSach, txtTenSach);
            }
        });

        JLabel lblThongBao = new JLabel(" ");

        JButton btnMuon = new JButton("Mượn sách");
        JButton btnTra = new JButton("Trả sách");
        JButton btnCapNhat = new JButton("Cập nhật phiếu");
        JButton btnXoaPhieu = new JButton("Xóa phiếu");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnXoaForm = new JButton("Xóa nhập");

        JLabel lblThongTinNguoiDung = new JLabel("Admin: " + adminDangNhap.getMaNguoiDung()
                + " - " + adminDangNhap.getTenNguoiDung());

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
                        adminDangNhap.getMaNguoiDung(),
                        docSoNgayMuonHopLe(txtSoNgayMuon.getText().trim(), "Số ngày mượn"),
                        docNgayHoacMacDinh(txtNgayMuon.getText().trim(), LocalDate.now(), "Ngày mượn")
                );
                lblThongBao.setText(thongBao);
                capNhatBangMuonTra(modelPhieu);
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
            capNhatBangMuonTra(modelPhieu);
            lamMoiBangSach.run();
        });

        btnCapNhat.addActionListener(e -> {
            try {
                String thongBao = quanLyPhieuMuon.capNhatPhieuMuon(
                        txtMaPhieuMuon.getText().trim(),
                        docNgayHoacMacDinh(txtNgayMuon.getText().trim(), LocalDate.now(), "Ngày mượn"),
                    docSoNgayMuonHopLe(txtSoNgayMuon.getText().trim(), "Số ngày mượn")
                );
                lblThongBao.setText(thongBao);
                capNhatBangMuonTra(modelPhieu);
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
            capNhatBangMuonTra(modelPhieu);
            lamMoiBangSach.run();

            if (thongBao.startsWith("Xóa")) {
                txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
                txtMaDocGia.setText("");
                txtMaSach.setText("");
                txtTenSach.setText("");
                txtNgayMuon.setText("");
                txtSoNgayMuon.setText("7");
                bangPhieu.clearSelection();
            }
        });

        btnLamMoi.addActionListener(e -> {
            capNhatBangMuonTra(modelPhieu);
            lblThongBao.setText("Đã làm mới danh sách phiếu mượn/trả");
        });

        btnXoaForm.addActionListener(e -> {
            txtMaPhieuMuon.setText(goiYMaPhieuMuonTiepTheo());
            txtMaDocGia.setText("");
            txtMaSach.setText("");
            txtTenSach.setText("");
            txtNgayMuon.setText("");
            txtSoNgayMuon.setText("7");
            lblThongBao.setText(" ");
            bangPhieu.clearSelection();
        });

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNut.add(btnMuon);
        panelNut.add(btnTra);
        panelNut.add(btnCapNhat);
        panelNut.add(btnXoaPhieu);
        panelNut.add(btnLamMoi);
        panelNut.add(btnXoaForm);

        JPanel panelTren = new JPanel(new BorderLayout(8, 8));
        panelTren.add(lblThongTinNguoiDung, BorderLayout.NORTH);
        panelTren.add(form, BorderLayout.CENTER);
        panelTren.add(panelNut, BorderLayout.SOUTH);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 8));
        panelDuoi.add(new JScrollPane(bangPhieu), BorderLayout.CENTER);

        JPanel panelTrangThaiDuoi = new JPanel(new BorderLayout(8, 0));
        panelTrangThaiDuoi.add(lblThongBao, BorderLayout.WEST);
        panelTrangThaiDuoi.add(lblThongKeMuonTra, BorderLayout.EAST);
        panelDuoi.add(panelTrangThaiDuoi, BorderLayout.SOUTH);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(panelDuoi, BorderLayout.CENTER);

        capNhatBangMuonTra(modelPhieu);
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
        txtMaDocGia.setText(goiYMaDocGiaTiepTheo());

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
                capNhatBangDocGia(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để sửa");
            }
        });

        btnXoa.addActionListener(e -> {
            if (quanLyDocGia.xoaDocGia(txtMaDocGia.getText().trim())) {
                capNhatBangDocGia(model);
                txtMaDocGia.setText(goiYMaDocGiaTiepTheo());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy độc giả để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> {
            capNhatBangDocGia(model);
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

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(bangDocGia), BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        capNhatBangDocGia(model);
        return panel;
    }

    private JPanel taoPanelSach() {
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

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã", "Tên sách", "Tác giả", "Thể loại", "Nhà XB", "Năm XB", "Tổng SL", "SL còn"}, 0);
        JTable bangSach = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bangSach);
        lamMoiBangSach = () -> capNhatBangSach(model);

        JButton btnThem = new JButton("Thêm sách");
        JButton btnSua = new JButton("Sửa sách");
        JButton btnXoa = new JButton("Xóa sách");
        JButton btnLamMoi = new JButton("Làm mới");

        bangSach.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangSach.getSelectedRow();
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

        txtMaSach.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }
        });

        txtTongSoLuong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                capNhatSoLuongConTuDong(txtMaSach, txtTongSoLuong, txtSoLuongCon);
            }
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

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 0));
        panelDuoi.add(panelNut, BorderLayout.WEST);
        panelDuoi.add(lblThongKeSach, BorderLayout.EAST);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        capNhatBangSach(model);
        return panel;
    }

    private JPanel taoPanelThuThu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMaNguoiDung = new JTextField(10);
        JTextField txtTenNguoiDung = new JTextField(15);
        JTextField txtTenDangNhap = new JTextField(15);
        JPasswordField txtMatKhau = new JPasswordField(15);
        JComboBox<String> cboVaiTro = new JComboBox<>(new String[]{"Admin", "Thủ thư"});
        JTextField txtSoDienThoai = new JTextField(12);
        JTextField txtEmail = new JTextField(15);
        JTextField txtDiaChi = new JTextField(15);
        String[] goiYMaTaiKhoanGanNhat = {goiYMaTaiKhoanTheoVaiTro("Thủ thư")};
        txtMaNguoiDung.setText(goiYMaTaiKhoanGanNhat[0]);

        JPanel form = new JPanel(new GridLayout(4, 4, 8, 8));
        form.add(new JLabel("Mã tài khoản"));
        form.add(txtMaNguoiDung);
        form.add(new JLabel("Tên người dùng"));
        form.add(txtTenNguoiDung);
        form.add(new JLabel("Tên đăng nhập"));
        form.add(txtTenDangNhap);
        form.add(new JLabel("Mật khẩu"));
        form.add(txtMatKhau);
        form.add(new JLabel("Vai trò"));
        form.add(cboVaiTro);
        form.add(new JLabel("Số điện thoại"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);

        cboVaiTro.addActionListener(e -> {
            boolean laThuThu = "Thủ thư".equals(cboVaiTro.getSelectedItem());
            txtSoDienThoai.setEnabled(laThuThu);
            txtEmail.setEnabled(laThuThu);
            txtDiaChi.setEnabled(laThuThu);
            if (!laThuThu) {
                txtSoDienThoai.setText("");
                txtEmail.setText("");
                txtDiaChi.setText("");
            }

            String goiYMoi = goiYMaTaiKhoanTheoVaiTro(String.valueOf(cboVaiTro.getSelectedItem()));
            String maHienTai = txtMaNguoiDung.getText().trim();
            if (maHienTai.isBlank() || maHienTai.equals(goiYMaTaiKhoanGanNhat[0])) {
                txtMaNguoiDung.setText(goiYMoi);
            }
            goiYMaTaiKhoanGanNhat[0] = goiYMoi;
        });
        cboVaiTro.setSelectedItem("Thủ thư");

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên", "Tên đăng nhập", "Mật khẩu", "Vai trò", "Số điện thoại", "Email", "Địa chỉ"}, 0);
        JTable bangTaiKhoan = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bangTaiKhoan);

        JButton btnThem = new JButton("Thêm tài khoản");
        JButton btnSua = new JButton("Sửa tài khoản");
        JButton btnXoa = new JButton("Xóa tài khoản");
        JButton btnLamMoi = new JButton("Làm mới");

        bangTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangTaiKhoan.getSelectedRow();
            if (dong < 0) {
                return;
            }
            txtMaNguoiDung.setText(String.valueOf(model.getValueAt(dong, 0)));
            txtTenNguoiDung.setText(String.valueOf(model.getValueAt(dong, 1)));
            txtTenDangNhap.setText(String.valueOf(model.getValueAt(dong, 2)));
            txtMatKhau.setText(String.valueOf(model.getValueAt(dong, 3)));
            String vaiTro = String.valueOf(model.getValueAt(dong, 4));
            cboVaiTro.setSelectedItem(vaiTro);
            txtSoDienThoai.setText(String.valueOf(model.getValueAt(dong, 5)));
            txtEmail.setText(String.valueOf(model.getValueAt(dong, 6)));
            txtDiaChi.setText(String.valueOf(model.getValueAt(dong, 7)));
        });

        btnThem.addActionListener(e -> {
            NguoiDung taiKhoan = taoTaiKhoanTuForm(
                    txtMaNguoiDung.getText().trim(),
                    txtTenNguoiDung.getText().trim(),
                    txtTenDangNhap.getText().trim(),
                    new String(txtMatKhau.getPassword()),
                    String.valueOf(cboVaiTro.getSelectedItem()),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            if (taiKhoan == null) {
                JOptionPane.showMessageDialog(this, "Dữ liệu tài khoản không hợp lệ");
                return;
            }

            if (quanLyNguoiDung.themTaiKhoan(taiKhoan)) {
                capNhatBangThuThu(model);
                String goiYMoi = goiYMaTaiKhoanTheoVaiTro(String.valueOf(cboVaiTro.getSelectedItem()));
                txtMaNguoiDung.setText(goiYMoi);
                goiYMaTaiKhoanGanNhat[0] = goiYMoi;
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm tài khoản (trùng mã hoặc tên đăng nhập)");
            }
        });

        btnSua.addActionListener(e -> {
            NguoiDung taiKhoan = taoTaiKhoanTuForm(
                    txtMaNguoiDung.getText().trim(),
                    txtTenNguoiDung.getText().trim(),
                    txtTenDangNhap.getText().trim(),
                    new String(txtMatKhau.getPassword()),
                    String.valueOf(cboVaiTro.getSelectedItem()),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            if (taiKhoan == null) {
                JOptionPane.showMessageDialog(this, "Dữ liệu tài khoản không hợp lệ");
                return;
            }

            if (quanLyNguoiDung.capNhatTaiKhoan(taiKhoan)) {
                capNhatBangThuThu(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật tài khoản");
            }
        });

        btnXoa.addActionListener(e -> {
            String maNguoiDung = txtMaNguoiDung.getText().trim();
            if (maNguoiDung.equalsIgnoreCase(adminDangNhap.getMaNguoiDung())) {
                JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản admin đang đăng nhập");
                return;
            }

            if (quanLyNguoiDung.xoaNguoiDung(maNguoiDung)) {
                capNhatBangThuThu(model);
                String goiYMoi = goiYMaTaiKhoanTheoVaiTro(String.valueOf(cboVaiTro.getSelectedItem()));
                txtMaNguoiDung.setText(goiYMoi);
                goiYMaTaiKhoanGanNhat[0] = goiYMoi;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> {
            capNhatBangThuThu(model);
            String goiYMoi = goiYMaTaiKhoanTheoVaiTro(String.valueOf(cboVaiTro.getSelectedItem()));
            txtMaNguoiDung.setText(goiYMoi);
            goiYMaTaiKhoanGanNhat[0] = goiYMoi;
        });

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        JPanel panelDuoi = new JPanel(new BorderLayout(8, 0));
        panelDuoi.add(panelNut, BorderLayout.WEST);
        panelDuoi.add(lblThongKeTaiKhoan, BorderLayout.EAST);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        capNhatBangThuThu(model);
        return panel;
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

    private void capNhatBangMuonTra(DefaultTableModel model) {
        model.setRowCount(0);
        int soDangMuon = 0;
        int soDaTra = 0;
        for (PhieuMuon phieuMuon : quanLyPhieuMuon.layDanhSachPhieuMuon()) {
            Sach sach = quanLySach.timSachTheoMa(phieuMuon.getMaSach());
            String tenSach = sach == null ? "" : sach.getTenSach();
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
                    phieuMuon.getMaPhieuMuon(),
                    phieuMuon.getMaDocGia(),
                    phieuMuon.getMaSach(),
                    tenSach,
                    soNgayMuon,
                    soNgayConLai,
                    chuyenNgay(phieuMuon.getNgayMuon()),
                    chuyenNgay(phieuMuon.getNgayTra()),
                    phieuMuon.getTrangThai()
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

    private LocalDate docNgayHoacMacDinh(String giaTri, LocalDate macDinh, String tenTruong) {
        if (giaTri == null || giaTri.isBlank()) {
            return macDinh;
        }
        try {
            return LocalDate.parse(giaTri.trim(), DINH_DANG_NGAY);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(tenTruong + " không đúng định dạng dd/MM/yyyy");
        }
    }

    private LocalDate docNgayHoacNull(String giaTri, String tenTruong) {
        if (giaTri == null || giaTri.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(giaTri.trim(), DINH_DANG_NGAY);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(tenTruong + " không đúng định dạng dd/MM/yyyy");
        }
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

    private void batTuDongGoiYNgayMuon(JTextField txtNgayMuon) {
        String[] goiYGanNhat = {goiYNgayMuonHienTai()};
        txtNgayMuon.setText(goiYGanNhat[0]);

        txtNgayMuon.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtNgayMuon.getText().trim().isBlank()) {
                    txtNgayMuon.setText(goiYNgayMuonHienTai());
                }
            }
        });

        Timer boDem = new Timer(1000, e -> {
            String goiYMoi = goiYNgayMuonHienTai();
            String giaTriDangNhap = txtNgayMuon.getText().trim();
            if (!txtNgayMuon.hasFocus() && (giaTriDangNhap.isBlank() || giaTriDangNhap.equals(goiYGanNhat[0]))) {
                txtNgayMuon.setText(goiYMoi);
            }
            goiYGanNhat[0] = goiYMoi;
        });
        boDem.start();
    }

    private String goiYNgayMuonHienTai() {
        return LocalDate.now().format(DINH_DANG_NGAY);
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

    private String goiYMaTaiKhoanTheoVaiTro(String vaiTro) {
        java.util.List<String> danhSachMa = new java.util.ArrayList<>();
        if ("Admin".equalsIgnoreCase(vaiTro)) {
            for (Admin admin : quanLyNguoiDung.layTatCaAdmin()) {
                danhSachMa.add(admin.getMaNguoiDung());
            }
            return String.format("AD%03d", timSoLonNhatTheoTienTo(danhSachMa, "AD") + 1);
        }

        for (ThuThu thuThu : quanLyNguoiDung.layTatCaThuThu()) {
            danhSachMa.add(thuThu.getMaNguoiDung());
        }
        return String.format("TT%03d", timSoLonNhatTheoTienTo(danhSachMa, "TT") + 1);
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
        lblThongKeDocGia.setText("Tổng độc giả: " + model.getRowCount());
    }

    private void capNhatBangThuThu(DefaultTableModel model) {
        model.setRowCount(0);
        int soAdmin = 0;
        int soThuThu = 0;
        for (NguoiDung nguoiDung : quanLyNguoiDung.layTatCaTaiKhoan()) {
            String vaiTro = nguoiDung instanceof Admin ? "Admin" : "Thủ thư";
            String soDienThoai = "";
            String email = "";
            String diaChi = "";
            if (nguoiDung instanceof ThuThu thuThu) {
                soDienThoai = thuThu.getSoDienThoai();
                email = thuThu.getEmail();
                diaChi = thuThu.getDiaChi();
                soThuThu++;
            } else {
                soAdmin++;
            }

            model.addRow(new Object[]{
                    nguoiDung.getMaNguoiDung(),
                    nguoiDung.getTenNguoiDung(),
                    nguoiDung.getTenDangNhap(),
                    nguoiDung.getMatKhau(),
                    vaiTro,
                    soDienThoai,
                    email,
                    diaChi
            });
        }
        lblThongKeTaiKhoan.setText("Tổng tài khoản: " + model.getRowCount()
                + " | Admin: " + soAdmin
                + " | Thủ thư: " + soThuThu);
    }

    private NguoiDung taoTaiKhoanTuForm(String maNguoiDung, String tenNguoiDung, String tenDangNhap,
                                        String matKhau, String vaiTro,
                                        String soDienThoai, String email, String diaChi) {
        if (maNguoiDung.isBlank() || tenNguoiDung.isBlank() || tenDangNhap.isBlank() || matKhau.isBlank()) {
            return null;
        }

        if ("Admin".equalsIgnoreCase(vaiTro)) {
            return new Admin(maNguoiDung, tenNguoiDung, tenDangNhap, matKhau);
        }
        return new ThuThu(maNguoiDung, tenNguoiDung, tenDangNhap, matKhau, soDienThoai, email, diaChi);
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

    public void xuLyLuaChon() {
        // Xu ly lua chon da duoc map thong qua cac nut su kien Swing.
    }
}

