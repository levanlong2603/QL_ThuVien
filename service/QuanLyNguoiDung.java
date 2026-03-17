package service;

import model.Admin;
import model.NguoiDung;
import model.ThuThu;

import java.util.ArrayList;
import java.util.List;

/**
 * Quan ly tai khoan admin va thu thu.
 */
public class QuanLyNguoiDung {
    private final List<ThuThu> danhSachThuThu = new ArrayList<>();
    private final List<Admin> danhSachAdmin = new ArrayList<>();

    public void themThuThu(ThuThu thuThu) {
        danhSachThuThu.add(thuThu);
    }

    public boolean xoaThuThu(String maThuThu) {
        return danhSachThuThu.removeIf(thuThu -> thuThu.getMaNguoiDung().equalsIgnoreCase(maThuThu));
    }

    public ThuThu timThuThu(String maThuThu) {
        for (ThuThu thuThu : danhSachThuThu) {
            if (thuThu.getMaNguoiDung().equalsIgnoreCase(maThuThu)) {
                return thuThu;
            }
        }
        return null;
    }

    public boolean capNhatThuThu(ThuThu thuThuMoi) {
        ThuThu thuThuCu = timThuThu(thuThuMoi.getMaNguoiDung());
        if (thuThuCu == null) {
            return false;
        }
        thuThuCu.setTenNguoiDung(thuThuMoi.getTenNguoiDung());
        thuThuCu.setTenDangNhap(thuThuMoi.getTenDangNhap());
        thuThuCu.setMatKhau(thuThuMoi.getMatKhau());
        thuThuCu.setSoDienThoai(thuThuMoi.getSoDienThoai());
        thuThuCu.setEmail(thuThuMoi.getEmail());
        thuThuCu.setDiaChi(thuThuMoi.getDiaChi());
        return true;
    }

    public void themAdmin(Admin admin) {
        danhSachAdmin.add(admin);
    }

    public NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        for (Admin admin : danhSachAdmin) {
            if (admin.dangNhap(tenDangNhap, matKhau)) {
                return admin;
            }
        }
        for (ThuThu thuThu : danhSachThuThu) {
            if (thuThu.dangNhap(tenDangNhap, matKhau)) {
                return thuThu;
            }
        }
        return null;
    }

    public List<ThuThu> layTatCaThuThu() {
        return danhSachThuThu;
    }
}

