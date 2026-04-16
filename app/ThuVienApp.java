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
        // ==================== 1. QUẢN TRỊ VIÊN & THỦ THƯ ====================
        quanLyNguoiDung.themAdmin(new Admin("AD001", "Quản trị hệ thống", "admin", "123"));
        
        // Thủ thư tại PTIT Hà Nội
        quanLyNguoiDung.themThuThu(new ThuThu("TT001", "Cô Nguyễn Thị Lan Hương", "lanhuong", "123",
            "0983012345", "huong.nguyen@ptit.edu.vn", "Tầng 2 - Thư viện Trung tâm PTIT Hà Đông"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT002", "Chú Trần Văn Bình", "vanbinh", "123",
            "0983123456", "binh.tran@ptit.edu.vn", "Tầng 3 - Phòng đọc mở PTIT Hà Đông"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT003", "Chị Phạm Thị Thu Hằng", "thuhang", "123",
            "0983456789", "hang.pham@ptit.edu.vn", "Tầng 1 - Quầy mượn trả (Cơ sở Hà Nội)"));
        quanLyNguoiDung.themThuThu(new ThuThu("TT004", "Anh Lê Văn Thành", "vanthanh", "123",
            "0983567890", "thanh.le@ptit.edu.vn", "Phòng số hóa tài liệu - Thư viện PTIT"));

        // ==================== 2. ĐỘC GIẢ (Sinh viên + Giảng viên PTIT Hà Nội) ====================
        // Sinh viên khóa K21, K22, K23, K24
        quanLyDocGia.themDocGia(new DocGia("DG001", "Nguyễn Văn Anh", "0981123456", "anh.nv21@ptit.edu.vn", "Ký túc xá PTIT Hà Đông - Phòng 201"));
        quanLyDocGia.themDocGia(new DocGia("DG002", "Trần Thị Bích Ngọc", "0981765432", "ngoc.tt22@ptit.edu.vn", "Phường Văn Quán, Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG003", "Phạm Văn Cường", "0981987654", "cuong.pv22@ptit.edu.vn", "Ký túc xá PTIT - Phòng 308"));
        quanLyDocGia.themDocGia(new DocGia("DG004", "Lê Thị Dung", "0981456789", "dung.lt23@ptit.edu.vn", "Phường Mỗ Lao, Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG005", "Hoàng Minh Đức", "0981888999", "duc.hm23@ptit.edu.vn", "Ký túc xá PTIT - Phòng 112"));
        quanLyDocGia.themDocGia(new DocGia("DG006", "Vũ Thị Hạnh", "0981234987", "hanh.vt24@ptit.edu.vn", "Phường Yên Nghĩa, Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG007", "Đỗ Văn Hùng", "0981543210", "hung.dv21@ptit.edu.vn", "Ký túc xá PTIT - Phòng 405"));
        quanLyDocGia.themDocGia(new DocGia("DG008", "Nguyễn Thị Linh", "0981678901", "linh.nt22@ptit.edu.vn", "Phường La Khê, Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG009", "Trần Văn Long", "0981901234", "long.tv23@ptit.edu.vn", "Ký túc xá PTIT - Phòng 509"));
        quanLyDocGia.themDocGia(new DocGia("DG010", "Phạm Thị Mai", "0981789456", "mai.pt24@ptit.edu.vn", "Phường Phúc La, Hà Đông"));
        
        // Giảng viên PTIT Hà Nội
        quanLyDocGia.themDocGia(new DocGia("DG011", "PGS.TS Nguyễn Đình Hóa", "0903123456", "hoa.nd@ptit.edu.vn", "Khoa CNTT1 - PTIT Hà Đông"));
        quanLyDocGia.themDocGia(new DocGia("DG012", "TS Lê Minh Hoàng", "0903456789", "hoang.lm@ptit.edu.vn", "Khoa Viễn thông - PTIT"));
        quanLyDocGia.themDocGia(new DocGia("DG013", "ThS Phạm Thị Thu Hương", "0903789012", "huong.ptt@ptit.edu.vn", "Khoa CNTT2 - PTIT"));
        quanLyDocGia.themDocGia(new DocGia("DG014", "TS Hoàng Văn Thắng", "0903987654", "thang.hv@ptit.edu.vn", "Trung tâm Đào tạo từ xa PTIT"));
        
        // ==================== 3. SÁCH - CHUYÊN NGÀNH PTIT HÀ NỘI ====================
        // ----- Công nghệ thông tin -----
        quanLySach.themSach(new Sach("S001", "Lập trình hướng đối tượng với Java", "PGS.TS Nguyễn Đình Hóa", "Công nghệ thông tin", "NXB Bưu điện", 2023, 25, 25));
        quanLySach.themSach(new Sach("S002", "Cấu trúc dữ liệu và giải thuật", "TS Lê Minh Hoàng", "Công nghệ thông tin", "NXB PTIT", 2022, 20, 20));
        quanLySach.themSach(new Sach("S003", "Cơ sở dữ liệu - Lý thuyết và thực hành", "ThS Phạm Thị Thu Hương", "Công nghệ thông tin", "NXB Khoa học Kỹ thuật", 2023, 18, 18));
        quanLySach.themSach(new Sach("S004", "Phát triển ứng dụng Android", "TS Hoàng Văn Thắng", "Công nghệ thông tin", "NXB PTIT", 2024, 15, 15));
        quanLySach.themSach(new Sach("S005", "Lập trình web với JavaScript và ReactJS", "ThS Nguyễn Quang Hưng", "Công nghệ thông tin", "NXB Bưu điện", 2023, 12, 12));
        quanLySach.themSach(new Sach("S006", "Trí tuệ nhân tạo trong viễn thông", "GS.TS Trần Văn Long", "AI & Dữ liệu lớn", "NXB PTIT", 2023, 10, 10));
        quanLySach.themSach(new Sach("S007", "Lập trình Python cho khoa học dữ liệu", "TS Nguyễn Thị Lan", "Công nghệ thông tin", "NXB Bưu điện", 2024, 14, 14));
        
        // ----- Viễn thông & Mạng máy tính -----
        quanLySach.themSach(new Sach("S008", "Kỹ thuật truyền dẫn số", "PGS.TS Lê Văn Cường", "Kỹ thuật viễn thông", "NXB Bưu điện", 2022, 12, 12));
        quanLySach.themSach(new Sach("S009", "Quản trị mạng Cisco CCNA v7", "ThS Trần Văn Nam", "Mạng máy tính", "NXB PTIT", 2024, 22, 22));
        quanLySach.themSach(new Sach("S010", "An toàn và bảo mật hệ thống thông tin", "PGS.TS Trần Đình Long", "An toàn thông tin", "NXB PTIT", 2023, 15, 15));
        quanLySach.themSach(new Sach("S011", "Mạng máy tính và truyền thông dữ liệu", "TS Phạm Văn Hùng", "Mạng máy tính", "NXB Bưu điện", 2022, 17, 17));
        quanLySach.themSach(new Sach("S012", "Điện tử viễn thông cơ bản", "ThS Nguyễn Thị Hồng", "Điện tử viễn thông", "NXB Khoa học Kỹ thuật", 2023, 10, 10));
        
        // ----- Kinh tế số & Quản trị -----
        quanLySach.themSach(new Sach("S013", "Kinh tế số và chuyển đổi số", "TS Lê Thị Mai", "Quản trị kinh doanh số", "NXB Lao động", 2023, 8, 8));
        quanLySach.themSach(new Sach("S014", "Thương mại điện tử cơ bản", "ThS Phạm Thị Thu", "Kinh tế số", "NXB PTIT", 2024, 11, 11));
        quanLySach.themSach(new Sach("S015", "Marketing số", "TS Hoàng Văn Nam", "Quản trị kinh doanh", "NXB Bưu điện", 2023, 9, 9));
        
        // ----- Ngoại ngữ & Kỹ năng mềm -----
        quanLySach.themSach(new Sach("S016", "Tiếng Anh chuyên ngành CNTT", "ThS Nguyễn Thị Ánh", "Ngoại ngữ", "NXB PTIT", 2023, 20, 20));
        quanLySach.themSach(new Sach("S017", "Kỹ năng thuyết trình và làm việc nhóm", "TS Trần Thị Hạnh", "Kỹ năng mềm", "NXB Trẻ", 2022, 13, 13));
        
        // ==================== 4. PHIẾU MƯỢN/TRẢ THỰC TẾ ====================
        // Sinh viên mượn
        quanLyPhieuMuon.muonSach("PM001", "S001", "DG001", "TT001", 7);   // Java - Nguyễn Văn Anh
        quanLyPhieuMuon.muonSach("PM002", "S009", "DG002", "TT001", 14);  // CCNA - Trần Thị Bích Ngọc
        quanLyPhieuMuon.muonSach("PM003", "S010", "DG003", "TT002", 10);  // An toàn thông tin - Phạm Văn Cường
        quanLyPhieuMuon.muonSach("PM004", "S004", "DG004", "TT003", 7);   // Android - Lê Thị Dung
        quanLyPhieuMuon.muonSach("PM005", "S006", "DG005", "TT002", 21);  // AI - Hoàng Minh Đức
        quanLyPhieuMuon.muonSach("PM006", "S013", "DG006", "TT003", 14);  // Kinh tế số - Vũ Thị Hạnh
        quanLyPhieuMuon.muonSach("PM007", "S002", "DG007", "TT001", 7);   // Cấu trúc dữ liệu - Đỗ Văn Hùng
        quanLyPhieuMuon.muonSach("PM008", "S016", "DG008", "TT002", 10);  // Tiếng Anh CNTT - Nguyễn Thị Linh
        quanLyPhieuMuon.muonSach("PM009", "S011", "DG009", "TT003", 14);  // Mạng máy tính - Trần Văn Long
        quanLyPhieuMuon.muonSach("PM010", "S005", "DG010", "TT001", 7);   // ReactJS - Phạm Thị Mai
        
        // Giảng viên mượn
        quanLyPhieuMuon.muonSach("PM011", "S007", "DG011", "TT002", 30);  // Python - PGS.TS Nguyễn Đình Hóa
        quanLyPhieuMuon.muonSach("PM012", "S008", "DG012", "TT003", 30);  // Truyền dẫn số - TS Lê Minh Hoàng
        quanLyPhieuMuon.muonSach("PM013", "S003", "DG013", "TT001", 21);  // Cơ sở dữ liệu - ThS Phạm Thị Thu Hương
        
        // Các phiếu đã trả sách (để hiển thị lịch sử)
        quanLyPhieuMuon.traSach("PM001");  // Nguyễn Văn Anh đã trả Java
        quanLyPhieuMuon.traSach("PM004");  // Lê Thị Dung đã trả Android
        quanLyPhieuMuon.traSach("PM007");  // Đỗ Văn Hùng đã trả Cấu trúc dữ liệu
        quanLyPhieuMuon.traSach("PM010");  // Phạm Thị Mai đã trả ReactJS
        quanLyPhieuMuon.traSach("PM012");  // TS Lê Minh Hoàng đã trả Truyền dẫn số
        
        // Các phiếu quá hạn (để test chức năng nhắc trả)
        // PM005 - AI của Hoàng Minh Đức đã mượn 21 ngày, chưa trả (sắp quá hạn)
        // PM009 - Mạng máy tính của Trần Văn Long đã mượn 14 ngày, chưa trả
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
