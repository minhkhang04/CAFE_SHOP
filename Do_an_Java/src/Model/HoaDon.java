/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.Date;
public class HoaDon {
    private String maHD, maKH, trangThai;

    public HoaDon() {
    }
    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    public void settongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    public void setngayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }
    public void settrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    private double tongTien;
    private Date ngayLap;
    public HoaDon(String maHD, String maKH, double tongTien, Date ngayLap, String trangThai) {
        this.maHD = maHD; this.maKH = maKH;
        this.tongTien = tongTien; this.ngayLap = ngayLap;
        this.trangThai = trangThai;
    }
    public String getMaHD() { return maHD; }
    public String getMaKH() { return maKH; }
    public double getTongTien() { return tongTien; }
    public Date getNgayLap() { return ngayLap; }
    public String getTrangThai() { return trangThai; }
}