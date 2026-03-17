package model;

/**
 * Nguoi quan tri he thong.
 */
public class Admin extends NguoiDung {

    public Admin(String maNguoiDung, String tenNguoiDung, String tenDangNhap, String matKhau) {
        super(maNguoiDung, tenNguoiDung, tenDangNhap, matKhau);
    }

    public String quanLySach() {
        return "Admin dang quan ly sach";
    }

    public String quanLyThuThu() {
        return "Admin dang quan ly thu thu";
    }

    public String xemThongKe() {
        return "Admin dang xem thong ke";
    }
}

