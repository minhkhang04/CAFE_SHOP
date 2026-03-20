/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Duyke
 */
public class CongThuc {
    private String maNguyenLieu, maSP;
    private Double SoLuong;

    public CongThuc() {
    }

    public CongThuc(String maNguyenLieu, String maSP, Double SoLuong) {
        this.maNguyenLieu = maNguyenLieu;
        this.maSP = maSP;
        this.SoLuong = SoLuong;
    }

    public String getMaNguyenLieu() {
        return maNguyenLieu;
    }

    public String getMaSP() {
        return maSP;
    }

    public Double getSoLuong() {
        return SoLuong;
    }

    public void setMaNguyenLieu(String maNguyenLieu) {
        this.maNguyenLieu = maNguyenLieu;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public void setSoLuong(Double SoLuong) {
        this.SoLuong = SoLuong;
    }
    
}
