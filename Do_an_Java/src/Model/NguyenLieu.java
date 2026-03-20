/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Duyke
 */
public class NguyenLieu {
    private String maNL, tenNL, DVT;
    private Double soLuongTon;

    public NguyenLieu() {
    }

    public NguyenLieu(String maNL, String tenNL, String DVT, Double soLuongTon) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.DVT = DVT;
        this.soLuongTon = soLuongTon;
    }

    public String getMaNguyenLieu() {
        return maNL;
    }

    public String getTenNguyenLieu() {
        return tenNL;
    }

    public String getDVT() {
        return DVT;
    }

    public Double getSoLuongTon() {
        return soLuongTon;
    }

    public void setMaNguyenLieu(String maNL) {
        this.maNL = maNL;
    }

    public void setTenNguyenLieu(String tenNL) {
        this.tenNL = tenNL;
    }

    public void setDVT(String DVT) {
        this.DVT = DVT;
    }

    public void setSoLuongTon(Double soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
    
}
