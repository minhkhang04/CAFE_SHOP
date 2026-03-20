/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pc
 */
package Model;
public class ChiTietHoaDon {
    private String maHD, maSP, ghiChu;

    public ChiTietHoaDon() {
        
    }
    public void setmaHD(String maHD) {
        this.maHD = maHD;
    }
    public void setmaSP(String maSP) {
        this.maSP = maSP;
    }
    public void setghiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    private int soLuong;
    private double thanhTien;
    public ChiTietHoaDon(String maHD, String maSP, int soLuong, double thanhTien, String ghiChu) {
        this.maHD = maHD; this.maSP = maSP; this.soLuong = soLuong;
        this.thanhTien = thanhTien; this.ghiChu = ghiChu;
    }
    public String getMaHD() { return maHD; }
    public String getMaSP() { return maSP; }
    public int getSoLuong() { return soLuong; }
    public double getThanhTien() { return thanhTien; }
    public String getGhiChu() { return ghiChu; }
}