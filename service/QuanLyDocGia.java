package service;

import model.DocGia;

import java.util.ArrayList;
import java.util.List;

/**
 * Quan ly danh sach doc gia.
 */
public class QuanLyDocGia {
    private final List<DocGia> danhSachDocGia = new ArrayList<>();

    public void themDocGia(DocGia docGia) {
        danhSachDocGia.add(docGia);
    }

    public boolean capNhatDocGia(DocGia docGiaMoi) {
        DocGia docGiaCu = timDocGia(docGiaMoi.getMaDocGia());
        if (docGiaCu == null) {
            return false;
        }
        docGiaCu.setTenDocGia(docGiaMoi.getTenDocGia());
        docGiaCu.setSoDienThoai(docGiaMoi.getSoDienThoai());
        docGiaCu.setEmail(docGiaMoi.getEmail());
        docGiaCu.setDiaChi(docGiaMoi.getDiaChi());
        return true;
    }

    public boolean xoaDocGia(String maDocGia) {
        return danhSachDocGia.removeIf(docGia -> docGia.getMaDocGia().equalsIgnoreCase(maDocGia));
    }

    public DocGia timDocGia(String maDocGia) {
        for (DocGia docGia : danhSachDocGia) {
            if (docGia.getMaDocGia().equalsIgnoreCase(maDocGia)) {
                return docGia;
            }
        }
        return null;
    }

    public List<DocGia> layTatCaDocGia() {
        return danhSachDocGia;
    }
}

