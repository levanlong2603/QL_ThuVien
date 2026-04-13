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

    public List<PhieuMuon> layDanhSachPhieuMuon() {
        return danhSachPhieuMuon;
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

    private PhieuMuon timPhieuTheoMa(String maPhieuMuon) {
        for (PhieuMuon phieuMuon : danhSachPhieuMuon) {
            if (phieuMuon.getMaPhieuMuon().equalsIgnoreCase(maPhieuMuon)) {
                return phieuMuon;
            }
        }
        return null;
    }
}

