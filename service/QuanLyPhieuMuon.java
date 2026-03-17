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
        if (maPhieuMuon == null || maPhieuMuon.isBlank()) {
            return "Mã phiếu mượn không hợp lệ";
        }
        if (timPhieuTheoMa(maPhieuMuon) != null) {
            return "Mã phiếu mượn đã tồn tại";
        }
        if (soNgayMuon <= 0) {
            return "Số ngày mượn phải lớn hơn 0";
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

        LocalDate ngayMuon = LocalDate.now();
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
        sach.setSoLuongCon(sach.getSoLuongCon() - 1);
            return "Mượn sách thành công";
    }

    public String traSach(String maPhieuMuon) {
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)
                    && "DANG_MUON".equals(phieuMuon.getTrangThai())) {
                phieuMuon.setNgayTra(LocalDate.now());
                phieuMuon.setTrangThai("DA_TRA");

                Sach sach = quanLySach.timSachTheoMa(phieuMuon.getMaSach());
                if (sach != null) {
                    sach.setSoLuongCon(sach.getSoLuongCon() + 1);
                }
                return "Trả sách thành công";
            }
        }
        return "Không tìm thấy phiếu đang mượn";
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

    private PhieuMuon timPhieuTheoMa(String maPhieuMuon) {
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)) {
                return phieuMuon;
            }
        }
        return null;
    }
}

