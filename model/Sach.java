package model;

/**
 * Lop sach trong kho thu vien.
 */
public class Sach {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String theLoai;
    private String nhaXuatBan;
    private int namXuatBan;
    private int tongSoLuong;
    private int soLuongCon;

    public Sach(String maSach, String tenSach, String tacGia, String theLoai,
                String nhaXuatBan, int namXuatBan, int tongSoLuong, int soLuongCon) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.nhaXuatBan = nhaXuatBan;
        this.namXuatBan = namXuatBan;
        this.tongSoLuong = tongSoLuong;
        this.soLuongCon = soLuongCon;
    }

    public boolean kiemTraConSach() {
        return soLuongCon > 0;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

    public int getNamXuatBan() {
        return namXuatBan;
    }

    public void setNamXuatBan(int namXuatBan) {
        this.namXuatBan = namXuatBan;
    }

    public int getTongSoLuong() {
        return tongSoLuong;
    }

    public void setTongSoLuong(int tongSoLuong) {
        this.tongSoLuong = tongSoLuong;
    }

    public int getSoLuongCon() {
        return soLuongCon;
    }

    public void setSoLuongCon(int soLuongCon) {
        this.soLuongCon = soLuongCon;
    }

    @Override
    public String toString() {
        return maSach + " - " + tenSach + " (Con: " + soLuongCon + ")";
    }
}

