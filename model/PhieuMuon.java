package model;

import java.time.LocalDate;

/**
 * Lop phieu muon sach cua doc gia.
 */
public class PhieuMuon {
    private String maPhieuMuon;
    private String maSach;
    private String maDocGia;
    private String maThuThu;
    private LocalDate ngayMuon;
    private LocalDate hanTra;
    private LocalDate ngayTra;
    private String trangThai;

    public PhieuMuon(String maPhieuMuon, String maSach, String maDocGia, String maThuThu,
                     LocalDate ngayMuon, LocalDate hanTra, LocalDate ngayTra, String trangThai) {
        this.maPhieuMuon = maPhieuMuon;
        this.maSach = maSach;
        this.maDocGia = maDocGia;
        this.maThuThu = maThuThu;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.ngayTra = ngayTra;
        this.trangThai = trangThai;
    }

    public String getMaPhieuMuon() {
        return maPhieuMuon;
    }

    public String getMaSach() {
        return maSach;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public String getMaThuThu() {
        return maThuThu;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getHanTra() {
        return hanTra;
    }

    public void setHanTra(LocalDate hanTra) {
        this.hanTra = hanTra;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return maPhieuMuon + " - Sach: " + maSach + " - Doc gia: " + maDocGia + " - " + trangThai;
    }
}

