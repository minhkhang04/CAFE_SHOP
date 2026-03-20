/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Duyke
 */
public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String DiaChi, soDienThoai;

    public NhaCungCap(String maNCC, String tenNCC, String DiaChi, String soDienThoai) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.DiaChi = DiaChi;
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    

    public NhaCungCap() {
    }

    public String getMaNCC() {
        return maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }
    
}
