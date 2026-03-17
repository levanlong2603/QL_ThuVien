package ui;

import model.Sach;
import model.ThuThu;
import service.QuanLyNguoiDung;
import service.QuanLySach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Giao dien danh cho admin.
 */
public class MenuAdmin extends JFrame {
    private final QuanLySach quanLySach;
    private final QuanLyNguoiDung quanLyNguoiDung;
    private final Runnable suKienDangXuat;

    public MenuAdmin(QuanLySach quanLySach, QuanLyNguoiDung quanLyNguoiDung,
                     Runnable suKienDangXuat) {
        this.quanLySach = quanLySach;
        this.quanLyNguoiDung = quanLyNguoiDung;
        this.suKienDangXuat = suKienDangXuat;

        setTitle("Menu Admin - Quản Lý Thư Viện");
        setSize(980, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý sách", taoPanelSach());
        tabbedPane.addTab("Quản lý thủ thư", taoPanelThuThu());

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

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelNut, BorderLayout.SOUTH);

        capNhatBangSach(model);
        return panel;
    }

    private JPanel taoPanelThuThu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMaThuThu = new JTextField(10);
        JTextField txtTenThuThu = new JTextField(15);
        JTextField txtTenDangNhap = new JTextField(15);
        JPasswordField txtMatKhau = new JPasswordField(15);
        JTextField txtSoDienThoai = new JTextField(12);
        JTextField txtEmail = new JTextField(15);
        JTextField txtDiaChi = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(4, 4, 8, 8));
        form.add(new JLabel("Mã thủ thư"));
        form.add(txtMaThuThu);
        form.add(new JLabel("Tên thủ thư"));
        form.add(txtTenThuThu);
        form.add(new JLabel("Tên đăng nhập"));
        form.add(txtTenDangNhap);
        form.add(new JLabel("Mật khẩu"));
        form.add(txtMatKhau);
        form.add(new JLabel("Số điện thoại"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên", "Tên đăng nhập", "Mật khẩu", "Số điện thoại", "Email", "Địa chỉ"}, 0);
        JTable bangThuThu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bangThuThu);

        JButton btnThem = new JButton("Thêm thủ thư");
        JButton btnSua = new JButton("Sửa thủ thư");
        JButton btnXoa = new JButton("Xóa thủ thư");
        JButton btnLamMoi = new JButton("Làm mới");

        bangThuThu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int dong = bangThuThu.getSelectedRow();
            if (dong < 0) {
                return;
            }
            txtMaThuThu.setText(String.valueOf(model.getValueAt(dong, 0)));
            txtTenThuThu.setText(String.valueOf(model.getValueAt(dong, 1)));
            txtTenDangNhap.setText(String.valueOf(model.getValueAt(dong, 2)));
            txtMatKhau.setText(String.valueOf(model.getValueAt(dong, 3)));
                txtSoDienThoai.setText(String.valueOf(model.getValueAt(dong, 4)));
                txtEmail.setText(String.valueOf(model.getValueAt(dong, 5)));
                txtDiaChi.setText(String.valueOf(model.getValueAt(dong, 6)));
        });

        btnThem.addActionListener(e -> {
            ThuThu thuThu = new ThuThu(
                    txtMaThuThu.getText().trim(),
                    txtTenThuThu.getText().trim(),
                    txtTenDangNhap.getText().trim(),
                    new String(txtMatKhau.getPassword()),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            quanLyNguoiDung.themThuThu(thuThu);
            capNhatBangThuThu(model);
        });

        btnSua.addActionListener(e -> {
            ThuThu thuThu = new ThuThu(
                    txtMaThuThu.getText().trim(),
                    txtTenThuThu.getText().trim(),
                    txtTenDangNhap.getText().trim(),
                    new String(txtMatKhau.getPassword()),
                    txtSoDienThoai.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDiaChi.getText().trim()
            );
            if (quanLyNguoiDung.capNhatThuThu(thuThu)) {
                capNhatBangThuThu(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thủ thư để sửa");
            }
        });

        btnXoa.addActionListener(e -> {
            if (quanLyNguoiDung.xoaThuThu(txtMaThuThu.getText().trim())) {
                capNhatBangThuThu(model);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thủ thư để xóa");
            }
        });

        btnLamMoi.addActionListener(e -> capNhatBangThuThu(model));

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelNut, BorderLayout.SOUTH);

        capNhatBangThuThu(model);
        return panel;
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

    private void capNhatBangThuThu(DefaultTableModel model) {
        model.setRowCount(0);
        for (ThuThu thuThu : quanLyNguoiDung.layTatCaThuThu()) {
            model.addRow(new Object[]{
                    thuThu.getMaNguoiDung(),
                    thuThu.getTenNguoiDung(),
                    thuThu.getTenDangNhap(),
                    thuThu.getMatKhau(),
                    thuThu.getSoDienThoai(),
                    thuThu.getEmail(),
                    thuThu.getDiaChi()
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

