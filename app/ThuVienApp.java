package app;

import model.Admin;
import model.DocGia;
import model.NguoiDung;
import model.Sach;
import model.ThuThu;
import service.QuanLyDocGia;
import service.QuanLyPhieuMuon;
import service.QuanLySach;
import service.QuanLyNguoiDung;
import ui.MenuAdmin;
import ui.MenuThuThu;

import javax.swing.*;
import java.awt.*;

/**
 * Chuong trinh chinh cua he thong quan ly thu vien.
 */
public class ThuVienApp {
    private final QuanLySach quanLySach = new QuanLySach();
    private final QuanLyDocGia quanLyDocGia = new QuanLyDocGia();
    private final QuanLyNguoiDung quanLyNguoiDung = new QuanLyNguoiDung();
    private final QuanLyPhieuMuon quanLyPhieuMuon = new QuanLyPhieuMuon(quanLySach, quanLyDocGia);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThuVienApp app = new ThuVienApp();
            app.khoiTaoDuLieuMau();
            app.hienThiMenuDangNhap();
        });
    }

    /**
     * Tao du lieu mau de test nhanh ung dung.
     */
    private void khoiTaoDuLieuMau() {
        quanLyNguoiDung.themAdmin(new Admin("AD001", "Quan tri he thong", "admin", "123"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT001", "Thu Thu A", "thuthu", "123",
            "0909888777", "thuthu.a@thuvien.vn", "Ha Noi"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT002", "Thu Thu B", "thuthu2", "123",
            "0909555666", "thuthu.b@thuvien.vn", "Da Nang"));

        quanLySach.themSach(new Sach("S001", "Lap trinh Java co ban", "Nguyen Van A", "CNTT", "NXB Tre", 2021, 10, 10));
        quanLySach.themSach(new Sach("S002", "Cau truc du lieu", "Tran Thi B", "CNTT", "NXB Giao Duc", 2020, 5, 5));
        quanLySach.themSach(new Sach("S003", "Co so du lieu", "Le Van E", "CNTT", "NXB Tong Hop", 2019, 8, 8));
        quanLySach.themSach(new Sach("S004", "Kinh te hoc vi mo", "Pham Thi F", "Kinh Te", "NXB Lao Dong", 2018, 6, 6));

        quanLyDocGia.themDocGia(new DocGia("DG001", "Le Van C", "0900000001", "c@example.com", "Ha Noi"));
        quanLyDocGia.themDocGia(new DocGia("DG002", "Pham Thi D", "0900000002", "d@example.com", "Da Nang"));
        quanLyDocGia.themDocGia(new DocGia("DG003", "Nguyen Thi G", "0900000003", "g@example.com", "Hai Phong"));
        quanLyDocGia.themDocGia(new DocGia("DG004", "Tran Van H", "0900000004", "h@example.com", "Can Tho"));

        // Du lieu mau phieu muon/tra de hien thi ngay o tab nghiep vu.
        quanLyPhieuMuon.muonSach("PM001", "S001", "DG001", "TT001", 7);
        quanLyPhieuMuon.muonSach("PM002", "S002", "DG002", "TT001", 14);
        quanLyPhieuMuon.muonSach("PM003", "S003", "DG003", "TT002", 10);
        quanLyPhieuMuon.traSach("PM001");
    }

    /**
     * Hien thi cua so dang nhap va dieu huong theo vai tro.
     */
    public void hienThiMenuDangNhap() {
        JFrame khungDangNhap = new JFrame("Đăng nhập hệ thống thư viện");
        khungDangNhap.setSize(430, 250);
        khungDangNhap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        khungDangNhap.setLocationRelativeTo(null);

        JPanel panelChinh = new JPanel(new BorderLayout(10, 10));
        panelChinh.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tieuDe = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN", SwingConstants.CENTER);
        tieuDe.setFont(new Font("SansSerif", Font.BOLD, 16));

        JTextField txtTenDangNhap = new JTextField();
        JPasswordField txtMatKhau = new JPasswordField();
        Dimension kichThuocNhap = new Dimension(170, 30);
        txtTenDangNhap.setPreferredSize(kichThuocNhap);
        txtMatKhau.setPreferredSize(kichThuocNhap);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 8, 8));
        panelForm.add(new JLabel("Tên đăng nhập"));
        panelForm.add(txtTenDangNhap);
        panelForm.add(new JLabel("Mật khẩu"));
        panelForm.add(txtMatKhau);

        JButton btnDangNhap = new JButton("Đăng nhập");
        JLabel lblTrangThai = new JLabel(" ");
        lblTrangThai.setHorizontalAlignment(SwingConstants.CENTER);
        lblTrangThai.setForeground(new Color(180, 30, 30));

        btnDangNhap.addActionListener(e -> {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword());
            NguoiDung nguoiDung = quanLyNguoiDung.dangNhap(tenDangNhap, matKhau);

            if (nguoiDung == null) {
                lblTrangThai.setText("Đăng nhập thất bại");
                return;
            }

            khungDangNhap.dispose();
            if (nguoiDung instanceof Admin) {
                MenuAdmin menuAdmin = new MenuAdmin(
                        (Admin) nguoiDung,
                        quanLySach,
                        quanLyDocGia,
                        quanLyNguoiDung,
                        quanLyPhieuMuon,
                        this::hienThiMenuDangNhap
                );
                menuAdmin.hienThiMenu();
            } else if (nguoiDung instanceof ThuThu thuThu) {
                MenuThuThu menuThuThu = new MenuThuThu(
                        thuThu,
                        quanLySach,
                    quanLyDocGia,
                        quanLyPhieuMuon,
                        this::hienThiMenuDangNhap
                );
                menuThuThu.hienThiMenu();
            }
        });

        panelChinh.add(tieuDe, BorderLayout.NORTH);
        panelChinh.add(panelForm, BorderLayout.CENTER);

        JPanel panelDuoi = new JPanel(new GridLayout(2, 1, 0, 6));
        panelDuoi.add(lblTrangThai);

        JPanel panelNutDangNhap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelNutDangNhap.add(btnDangNhap);
        panelDuoi.add(panelNutDangNhap);

        panelChinh.add(panelDuoi, BorderLayout.SOUTH);

        khungDangNhap.add(panelChinh);
        khungDangNhap.setVisible(true);
    }
}
