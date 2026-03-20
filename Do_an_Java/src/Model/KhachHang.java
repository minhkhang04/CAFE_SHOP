/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class KhachHang {
    private String maKH;
    private String hoTenKH;
    private String sdt;
    private String gioiTinh;
    private String maLoaiKH;
    private int khachHangTiemNang;

    public KhachHang(String maKH, String hoTenKH, String sdt, String gioiTinh, String maLoaiKH, int khachHangTiemNang) {
        this.maKH = maKH;
        this.hoTenKH = hoTenKH;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.maLoaiKH = maLoaiKH;
        this.khachHangTiemNang = khachHangTiemNang;
    }

    // Getters
    public String getMaKH() { return maKH; }
    public String getHoTenKH() { return hoTenKH; }
    public String getSDT() { return sdt; }
    public String getGioiTinh() { return gioiTinh; }
    public String getMaLoaiKH() { return maLoaiKH; }
    public int getKhachHangTiemNang() { return khachHangTiemNang; }
}