package service;

import model.PhieuMuon;
import model.Sach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Quan ly nghiep vu muon tra sach.
 */
public class QuanLyPhieuMuon {
    private final List<PhieuMuon> danhSachPhieuMuon = new ArrayList<>();
    private final QuanLySach quanLySach;
    private final QuanLyDocGia quanLyDocGia;

    public QuanLyPhieuMuon(QuanLySach quanLySach, QuanLyDocGia quanLyDocGia) {
        this.quanLySach = quanLySach;
        this.quanLyDocGia = quanLyDocGia;
    }

    public String muonSach(String maPhieuMuon, String maSach, String maDocGia, String maThuThu, int soNgayMuon) {
        return muonSach(maPhieuMuon, maSach, maDocGia, maThuThu, soNgayMuon, LocalDate.now());
    }

    public String muonSach(String maPhieuMuon, String maSach, String maDocGia, String maThuThu,
                           int soNgayMuon, LocalDate ngayMuon) {
        if (maPhieuMuon == null || maPhieuMuon.isBlank()) {
            return "Mã phiếu mượn không hợp lệ";
        }
        if (timPhieuTheoMa(maPhieuMuon) != null) {
            return "Mã phiếu mượn đã tồn tại";
        }
        if (soNgayMuon <= 0) {
            return "Số ngày mượn phải lớn hơn 0";
        }
        if (ngayMuon == null) {
            return "Ngày mượn không hợp lệ";
        }

        Sach sach = quanLySach.timSachTheoMa(maSach);
        if (sach == null) {
            return "Không tìm thấy sách";
        }
        if (quanLyDocGia.timDocGia(maDocGia) == null) {
            return "Không tìm thấy độc giả";
        }
        if (!sach.kiemTraConSach()) {
            return "Sách đã hết";
        }

        LocalDate hanTra = ngayMuon.plusDays(soNgayMuon);
        PhieuMuon phieuMuon = new PhieuMuon(
                maPhieuMuon,
                maSach,
                maDocGia,
                maThuThu,
                ngayMuon,
                hanTra,
                null,
                "DANG_MUON"
        );

        danhSachPhieuMuon.add(phieuMuon);
        dongBoSoLuongConTheoMaSach(maSach);
        return "Mượn sách thành công";
    }

    public String traSach(String maPhieuMuon) {
        return traSach(maPhieuMuon, LocalDate.now());
    }

    public String traSach(String maPhieuMuon, LocalDate ngayTra) {
        if (ngayTra == null) {
            return "Ngày trả không hợp lệ";
        }

        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)
                    && "DANG_MUON".equals(phieuMuon.getTrangThai())) {
                if (ngayTra.isBefore(phieuMuon.getNgayMuon())) {
                    return "Ngày trả không được nhỏ hơn ngày mượn";
                }

                phieuMuon.setNgayTra(ngayTra);
                phieuMuon.setTrangThai("DA_TRA");

                dongBoSoLuongConTheoMaSach(phieuMuon.getMaSach());
                return "Trả sách thành công";
            }
        }
        return "Không tìm thấy phiếu đang mượn";
    }

    public String capNhatPhieuMuon(String maPhieuMuon, LocalDate ngayMuonMoi, LocalDate ngayTraMoi) {
        if (maPhieuMuon == null || maPhieuMuon.isBlank()) {
            return "Mã phiếu mượn không hợp lệ";
        }
        if (ngayMuonMoi == null) {
            return "Ngày mượn không hợp lệ";
        }
        if (ngayTraMoi != null && ngayTraMoi.isBefore(ngayMuonMoi)) {
            return "Ngày trả không được nhỏ hơn ngày mượn";
        }

        PhieuMuon phieuMuon = timPhieuTheoMa(maPhieuMuon);
        if (phieuMuon == null) {
            return "Không tìm thấy phiếu mượn";
        }

        if (quanLySach.timSachTheoMa(phieuMuon.getMaSach()) == null) {
            return "Không tìm thấy sách của phiếu mượn";
        }

        String trangThaiCu = phieuMuon.getTrangThai();
        String trangThaiMoi = ngayTraMoi == null ? "DANG_MUON" : "DA_TRA";
        if (!trangThaiCu.equals(trangThaiMoi)) {
            if ("DANG_MUON".equals(trangThaiCu) && "DA_TRA".equals(trangThaiMoi)) {
                // Trang thai doi, so luong se duoc dong bo lai theo danh sach phieu.
            } else if ("DA_TRA".equals(trangThaiCu) && "DANG_MUON".equals(trangThaiMoi)) {
                if (!conSachDeChuyenSangDangMuon(phieuMuon.getMaSach())) {
                    return "Không thể chuyển sang đang mượn vì sách đã hết";
                }
            }
        }

        long soNgayMuon = phieuMuon.getHanTra().toEpochDay() - phieuMuon.getNgayMuon().toEpochDay();
        if (soNgayMuon <= 0) {
            soNgayMuon = 7;
        }

        phieuMuon.setNgayMuon(ngayMuonMoi);
        phieuMuon.setHanTra(ngayMuonMoi.plusDays(soNgayMuon));
        phieuMuon.setNgayTra(ngayTraMoi);
        phieuMuon.setTrangThai(trangThaiMoi);
        dongBoSoLuongConTheoMaSach(phieuMuon.getMaSach());
        return "Cập nhật phiếu mượn thành công";
    }

    public String capNhatPhieuMuon(String maPhieuMuon, LocalDate ngayMuonMoi, int soNgayMuonMoi) {
        if (maPhieuMuon == null || maPhieuMuon.isBlank()) {
            return "Mã phiếu mượn không hợp lệ";
        }
        if (ngayMuonMoi == null) {
            return "Ngày mượn không hợp lệ";
        }
        if (soNgayMuonMoi <= 0) {
            return "Số ngày mượn phải lớn hơn 0";
        }

        PhieuMuon phieuMuon = timPhieuTheoMa(maPhieuMuon);
        if (phieuMuon == null) {
            return "Không tìm thấy phiếu mượn";
        }

        if (phieuMuon.getNgayTra() != null && phieuMuon.getNgayTra().isBefore(ngayMuonMoi)) {
            return "Ngày mượn không được lớn hơn ngày trả";
        }

        phieuMuon.setNgayMuon(ngayMuonMoi);
        phieuMuon.setHanTra(ngayMuonMoi.plusDays(soNgayMuonMoi));
        dongBoSoLuongConTheoMaSach(phieuMuon.getMaSach());
        return "Cập nhật phiếu mượn thành công";
    }

    public String xoaPhieuMuon(String maPhieuMuon) {
        if (maPhieuMuon == null || maPhieuMuon.isBlank()) {
            return "Mã phiếu mượn không hợp lệ";
        }

        for (int i = 0; i < danhSachPhieuMuon.size(); i++) {
            PhieuMuon phieuMuon = danhSachPhieuMuon.get(i);
            if (!phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)) {
                continue;
            }

            String maSach = phieuMuon.getMaSach();
            danhSachPhieuMuon.remove(i);
            dongBoSoLuongConTheoMaSach(maSach);
            return "Xóa phiếu mượn thành công";
        }
        return "Không tìm thấy phiếu mượn để xóa";
    }

    public PhieuMuon timPhieuMuon(String maPhieuMuon) {
        return timPhieuTheoMa(maPhieuMuon);
    }

    public List<PhieuMuon> layDanhSachPhieuMuon() {
        return danhSachPhieuMuon;
    }

    public List<PhieuMuon> kiemTraQuaHan() {
        List<PhieuMuon> danhSachQuaHan = new ArrayList<>();
        LocalDate homNay = LocalDate.now();
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if ("DANG_MUON".equals(phieuMuon.getTrangThai()) && phieuMuon.getHanTra().isBefore(homNay)) {
                danhSachQuaHan.add(phieuMuon);
            }
        }
        return danhSachQuaHan;
    }

    public int demSoLuongDangMuonTheoMaSach(String maSach) {
        if (maSach == null || maSach.isBlank()) {
            return 0;
        }

        int soLuongDangMuon = 0;
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (maSach.equalsIgnoreCase(phieuMuon.getMaSach())
                    && "DANG_MUON".equals(phieuMuon.getTrangThai())) {
                soLuongDangMuon++;
            }
        }
        return soLuongDangMuon;
    }

    private void dongBoSoLuongConTheoMaSach(String maSach) {
        Sach sach = quanLySach.timSachTheoMa(maSach);
        if (sach == null) {
            return;
        }

        int soLuongDangMuon = demSoLuongDangMuonTheoMaSach(maSach);
        int soLuongCon = Math.max(0, sach.getTongSoLuong() - soLuongDangMuon);
        sach.setSoLuongCon(soLuongCon);
    }

    private boolean conSachDeChuyenSangDangMuon(String maSach) {
        Sach sach = quanLySach.timSachTheoMa(maSach);
        if (sach == null) {
            return false;
        }

        int soLuongDangMuon = demSoLuongDangMuonTheoMaSach(maSach);
        return soLuongDangMuon < sach.getTongSoLuong();
    }

    private PhieuMuon timPhieuTheoMa(String maPhieuMuon) {
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)) {
                return phieuMuon;
            }
        }
        return null;
    }
}

