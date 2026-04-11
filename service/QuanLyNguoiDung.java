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

    public boolean themTaiKhoan(NguoiDung taiKhoan) {
        if (taiKhoan == null) {
            return false;
        }
        if (timNguoiDungTheoMa(taiKhoan.getMaNguoiDung()) != null) {
            return false;
        }
        if (tonTaiTenDangNhap(taiKhoan.getTenDangNhap(), taiKhoan.getMaNguoiDung())) {
            return false;
        }

        if (taiKhoan instanceof Admin admin) {
            danhSachAdmin.add(admin);
            return true;
        }
        if (taiKhoan instanceof ThuThu thuThu) {
            danhSachThuThu.add(thuThu);
            return true;
        }
        return false;
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

    public Admin timAdmin(String maAdmin) {
        for (Admin admin : danhSachAdmin) {
            if (admin.getMaNguoiDung().equalsIgnoreCase(maAdmin)) {
                return admin;
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

    public boolean xoaAdmin(String maAdmin) {
        return danhSachAdmin.removeIf(admin -> admin.getMaNguoiDung().equalsIgnoreCase(maAdmin));
    }

    public boolean capNhatAdmin(Admin adminMoi) {
        Admin adminCu = timAdmin(adminMoi.getMaNguoiDung());
        if (adminCu == null) {
            return false;
        }
        if (tonTaiTenDangNhap(adminMoi.getTenDangNhap(), adminMoi.getMaNguoiDung())) {
            return false;
        }

        adminCu.setTenNguoiDung(adminMoi.getTenNguoiDung());
        adminCu.setTenDangNhap(adminMoi.getTenDangNhap());
        adminCu.setMatKhau(adminMoi.getMatKhau());
        return true;
    }

    public boolean capNhatTaiKhoan(NguoiDung taiKhoanMoi) {
        if (taiKhoanMoi == null) {
            return false;
        }

        NguoiDung taiKhoanCu = timNguoiDungTheoMa(taiKhoanMoi.getMaNguoiDung());
        if (taiKhoanCu == null) {
            return false;
        }
        if (tonTaiTenDangNhap(taiKhoanMoi.getTenDangNhap(), taiKhoanMoi.getMaNguoiDung())) {
            return false;
        }

        if (taiKhoanCu instanceof Admin && taiKhoanMoi instanceof Admin adminMoi) {
            return capNhatAdmin(adminMoi);
        }
        if (taiKhoanCu instanceof ThuThu && taiKhoanMoi instanceof ThuThu thuThuMoi) {
            return capNhatThuThu(thuThuMoi);
        }

        if (!xoaNguoiDung(taiKhoanCu.getMaNguoiDung())) {
            return false;
        }
        return themTaiKhoan(taiKhoanMoi);
    }

    public boolean xoaNguoiDung(String maNguoiDung) {
        if (xoaThuThu(maNguoiDung)) {
            return true;
        }
        return xoaAdmin(maNguoiDung);
    }

    public NguoiDung timNguoiDungTheoMa(String maNguoiDung) {
        Admin admin = timAdmin(maNguoiDung);
        if (admin != null) {
            return admin;
        }
        return timThuThu(maNguoiDung);
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

    public List<Admin> layTatCaAdmin() {
        return danhSachAdmin;
    }

    public List<NguoiDung> layTatCaTaiKhoan() {
        List<NguoiDung> ketQua = new ArrayList<>();
        ketQua.addAll(danhSachAdmin);
        ketQua.addAll(danhSachThuThu);
        return ketQua;
    }

    private boolean tonTaiTenDangNhap(String tenDangNhap, String boQuaMaNguoiDung) {
        if (tenDangNhap == null || tenDangNhap.isBlank()) {
            return false;
        }

        for (Admin admin : danhSachAdmin) {
            if (admin.getTenDangNhap().equalsIgnoreCase(tenDangNhap)
                    && !admin.getMaNguoiDung().equalsIgnoreCase(boQuaMaNguoiDung)) {
                return true;
            }
        }
        for (ThuThu thuThu : danhSachThuThu) {
            if (thuThu.getTenDangNhap().equalsIgnoreCase(tenDangNhap)
                    && !thuThu.getMaNguoiDung().equalsIgnoreCase(boQuaMaNguoiDung)) {
                return true;
            }
        }
        return false;
    }
}

