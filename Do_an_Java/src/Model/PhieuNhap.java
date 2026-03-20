/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 *
 * @author Duyke
 */
public class PhieuNhap {
    private String maPN, maNCC, TrangThai, MaNV;
    private Date ngayNhap;
    private Double tongTien;

    public PhieuNhap() {
    }

    public PhieuNhap(String maPN,  String MaNV, Date ngayNhap, String maNCC, Double tongTien,  String TrangThai) {
        this.maPN = maPN;
        this.maNCC = maNCC;
        this.TrangThai = TrangThai;
        this.MaNV = MaNV;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    

    public String getMaPN() {
        return maPN;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }
    
    
}
