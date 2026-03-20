/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
public class SanPham {
    private String maSP, tenSP, maLoaiSP, trangThai, ghiChu;
    private double donGia;
    public SanPham(String maSP, String tenSP, String maLoaiSP, double donGia, String trangThai, String ghiChu) {
        this.maSP = maSP; this.tenSP = tenSP; this.maLoaiSP = maLoaiSP;
        this.donGia = donGia; this.trangThai = trangThai; this.ghiChu = ghiChu;
    }
    public String getMaSP() { return maSP; }
    public String getTenSP() { return tenSP; }
    public String getMaLoaiSP() { return maLoaiSP; }
    public double getDonGia() { return donGia; }
    public String getTrangThai() { return trangThai; }
    public String getGhiChu() { return ghiChu; }
}