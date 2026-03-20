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
public class NhanVien {
      private String maNV;
    private String tenNV;
    private String gioiTinh;
    private Date ngayVaoLam;
    private String chucVu;
    private String soDienThoai;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String gioiTinh, Date ngayVaoLam, String chucVu, String soDienThoai) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.ngayVaoLam = ngayVaoLam;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public String getChucVu() {
        return chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
}
