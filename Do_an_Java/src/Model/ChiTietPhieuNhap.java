/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Duyke
 */
public class ChiTietPhieuNhap {
    private String maPN, maNL;
    private Double SoLuong, DonGia, ThanhTien;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(String maPN, String maNL, Double SoLuong, Double DonGia, Double ThanhTien) {
        this.maPN = maPN;
        this.maNL = maNL;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
        this.ThanhTien = ThanhTien;
    }

    public String getMaPN() {
        return maPN;
    }

    public String getMaNL() {
        return maNL;
    }

    public Double getSoLuong() {
        return SoLuong;
    }

    public Double getDonGia() {
        return DonGia;
    }

    public Double getThanhTien() {
        return ThanhTien;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public void setSoLuong(Double SoLuong) {
        this.SoLuong = SoLuong;
    }

    public void setDonGia(Double DonGia) {
        this.DonGia = DonGia;
    }

    public void setThanhTien(Double ThanhTien) {
        this.ThanhTien = ThanhTien;
    }
    
}
