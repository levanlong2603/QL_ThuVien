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
        // 1. Người dùng hệ thống
        quanLyNguoiDung.themAdmin(new Admin("AD001", "Quản trị hệ thống", "admin", "123"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT001", "Nguyễn Thị Thủ Thư", "thuthu1", "123",
            "0983123456", "thuthu.nguyen@ptit.edu.vn", "Tầng 2, Thư viện PTIT"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT002", "Trần Văn Sách", "thuthu2", "123",
            "0983456789", "sach.tran@ptit.edu.vn", "Tầng 3, Thư viện PTIT - Cơ sở Hà Đông"));

        // 2. Sách/Tài liệu liên quan đến PTIT
        quanLySach.themSach(new Sach("S001", "Lập trình mạng với Java", "Nguyễn Hoàng Hà", "CNTT - Viễn thông", "NXB Bưu điện", 2022, 15, 15));
        quanLySach.themSach(new Sach("S002", "An toàn và bảo mật hệ thống thông tin", "PGS.TS Trần Đình Long", "An toàn thông tin", "NXB PTIT", 2021, 8, 8));
        quanLySach.themSach(new Sach("S003", "Truyền thông số và xử lý tín hiệu", "TS Lê Văn Cường", "Kỹ thuật viễn thông", "NXB Bưu điện", 2020, 12, 12));
        quanLySach.themSach(new Sach("S004", "Quản trị mạng Cisco CCNA", "ThS Phạm Thị Hồng", "Mạng máy tính", "NXB PTIT", 2023, 20, 20));
        quanLySach.themSach(new Sach("S005", "Trí tuệ nhân tạo trong viễn thông", "GS.TS Nguyễn Văn Hùng", "AI - Dữ liệu lớn", "NXB Khoa học Kỹ thuật", 2022, 5, 5));
        quanLySach.themSach(new Sach("S006", "Kinh tế số và chuyển đổi số", "TS Lê Thị Mai", "Quản trị kinh doanh số", "NXB Lao động", 2023, 7, 7));
        quanLySach.themSach(new Sach("S007", "Lập trình Android cho sinh viên PTIT", "Khoa CNTT1", "CNTT", "NXB PTIT", 2024, 10, 10));
        quanLySach.themSach(new Sach("S008", "Thiết kế vi mạch với FPGA", "TS Hoàng Văn Nam", "Điện tử viễn thông", "NXB Bưu điện", 2021, 6, 6));

        // 3. Độc giả (sinh viên, giảng viên PTIT)
        quanLyDocGia.themDocGia(new DocGia("DG001", "Nguyễn Văn Anh - SV K21", "0981123456", "anh.nv21@ptit.edu.vn", "Ký túc xá PTIT Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG002", "Trần Thị Bình - SV K22", "0981765432", "binh.tt22@ptit.edu.vn", "Phường Văn Quán, Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG003", "Phạm Văn Cường - Giảng viên Khoa CNTT2", "0981987654", "cuong.pv@ptit.edu.vn", "Nhà giáo viên PTIT"));
        quanLyDocGia.themDocGia(new DocGia("DG004", "Lê Thị Dung - SV K23", "0981456789", "dung.lt23@ptit.edu.vn", "Hà Nội - Cơ sở đào tạo PTIT"));
        quanLyDocGia.themDocGia(new DocGia("DG005", "Hoàng Minh Đức - Học viên cao học", "0981888999", "duc.hm@ptithcm.edu.vn", "Cơ sở TP.HCM"));

        // 4. Phiếu mượn/trả sách thực tế
        quanLyPhieuMuon.muonSach("PM001", "S001", "DG001", "TT001", 7);   // Lập trình mạng Java
        quanLyPhieuMuon.muonSach("PM002", "S004", "DG002", "TT001", 14);  // CCNA
        quanLyPhieuMuon.muonSach("PM003", "S002", "DG003", "TT002", 10);  // An toàn hệ thống
        quanLyPhieuMuon.muonSach("PM004", "S007", "DG004", "TT002", 7);   // Android
        quanLyPhieuMuon.muonSach("PM005", "S005", "DG005", "TT001", 21);  // AI trong viễn thông
        
        // Trả sách mẫu
        quanLyPhieuMuon.traSach("PM001");  // đã trả
        quanLyPhieuMuon.traSach("PM003");  // đã trả
        // PM002, PM004, PM005 đang mượn
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
        Dimension kichThuocNhap = new Dimension(185, 30);
        txtTenDangNhap.setPreferredSize(kichThuocNhap);
        txtMatKhau.setPreferredSize(kichThuocNhap);

        JPanel panelForm = new JPanel(new GridLayout(2, 1, 0, 8));

        JPanel dongTenDangNhap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập");
        lblTenDangNhap.setPreferredSize(new Dimension(90, 30));
        dongTenDangNhap.add(lblTenDangNhap);
        dongTenDangNhap.add(txtTenDangNhap);

        JPanel dongMatKhau = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setPreferredSize(new Dimension(90, 30));
        dongMatKhau.add(lblMatKhau);
        dongMatKhau.add(txtMatKhau);

        panelForm.add(dongTenDangNhap);
        panelForm.add(dongMatKhau);

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
