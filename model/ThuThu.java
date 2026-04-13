package model;

/**
 * Nguoi thu thu thuc hien nghiep vu muon tra.
 */
public class ThuThu extends NguoiDung {
    private String soDienThoai;
    private String email;
    private String diaChi;

    public ThuThu(String maNguoiDung, String tenNguoiDung, String tenDangNhap, String matKhau) {
        this(maNguoiDung, tenNguoiDung, tenDangNhap, matKhau, "", "", "");
    }

    public ThuThu(String maNguoiDung, String tenNguoiDung, String tenDangNhap, String matKhau,
                  String soDienThoai, String email, String diaChi) {
        super(maNguoiDung, tenNguoiDung, tenDangNhap, matKhau);
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
