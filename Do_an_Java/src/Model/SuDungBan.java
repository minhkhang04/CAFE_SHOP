/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

public class SuDungBan {
     private String maSD, maBan, maKH, maHD, trangThai;
    private Timestamp thoiGianBatDau, thoiGianKetThuc;

    public SuDungBan(String maSD, String maBan, String maKH, String maHD, Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc, String trangThai) {
        this.maSD = maSD;
        this.maBan = maBan;
        this.maKH = maKH;
        this.maHD = maHD;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.trangThai = trangThai;
    }

    // Getters
    public String getMaSD() { return maSD; }
    public String getMaBan() { return maBan; }
    public String getMaKH() { return maKH; }
    public String getMaHD() { return maHD; }
    public Timestamp getThoiGianBatDau() { return thoiGianBatDau; }
    public Timestamp getThoiGianKetThuc() { return thoiGianKetThuc; }
    public String getTrangThai() { return trangThai; }

    // Setters
    public void setMaSD(String maSD) { this.maSD = maSD; }
    public void setMaBan(String maBan) { this.maBan = maBan; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public void setThoiGianBatDau(Timestamp thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }
    public void setThoiGianKetThuc(Timestamp thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}