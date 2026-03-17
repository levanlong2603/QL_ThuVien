package service;

import model.Sach;

import java.util.ArrayList;
import java.util.List;

/**
 * Quan ly danh sach sach.
 */
public class QuanLySach {
    private final List<Sach> danhSachSach = new ArrayList<>();

    public void themSach(Sach sach) {
        danhSachSach.add(sach);
    }

    public boolean capNhatSach(Sach sachMoi) {
        Sach sachCu = timSachTheoMa(sachMoi.getMaSach());
        if (sachCu == null) {
            return false;
        }
        sachCu.setTenSach(sachMoi.getTenSach());
        sachCu.setTacGia(sachMoi.getTacGia());
        sachCu.setTheLoai(sachMoi.getTheLoai());
        sachCu.setNhaXuatBan(sachMoi.getNhaXuatBan());
        sachCu.setNamXuatBan(sachMoi.getNamXuatBan());
        sachCu.setTongSoLuong(sachMoi.getTongSoLuong());
        sachCu.setSoLuongCon(sachMoi.getSoLuongCon());
        return true;
    }

    public boolean xoaSach(String maSach) {
        return danhSachSach.removeIf(sach -> sach.getMaSach().equalsIgnoreCase(maSach));
    }

    public Sach timSachTheoMa(String maSach) {
        for (Sach sach : danhSachSach) {
            if (sach.getMaSach().equalsIgnoreCase(maSach)) {
                return sach;
            }
        }
        return null;
    }

    public List<Sach> timSachTheoTen(String tuKhoa) {
        List<Sach> ketQua = new ArrayList<>();
        for (Sach sach : danhSachSach) {
            if (sach.getTenSach().toLowerCase().contains(tuKhoa.toLowerCase())) {
                ketQua.add(sach);
            }
        }
        return ketQua;
    }

    public List<Sach> layTatCaSach() {
        return danhSachSach;
    }
}

