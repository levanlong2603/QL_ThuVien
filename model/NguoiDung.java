package model;

/**
 * Lop co so cua he thong nguoi dung.
 */
public abstract class NguoiDung {
    protected String maNguoiDung;
    protected String tenNguoiDung;
    protected String tenDangNhap;
    protected String matKhau;

    public NguoiDung(String maNguoiDung, String tenNguoiDung, String tenDangNhap, String matKhau) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public boolean dangNhap(String tenDangNhapNhapVao, String matKhauNhapVao) {
        return this.tenDangNhap.equals(tenDangNhapNhapVao) && this.matKhau.equals(matKhauNhapVao);
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}

