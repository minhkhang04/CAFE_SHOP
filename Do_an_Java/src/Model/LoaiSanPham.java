/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
public class LoaiSanPham {
    private String maLoaiSP;
    private String tenLoai;
    public LoaiSanPham(String maLoaiSP, String tenLoai) {
        this.maLoaiSP = maLoaiSP;
        this.tenLoai = tenLoai;
    }
    public String getMaLoaiSP() { return maLoaiSP; }
    public String getTenLoai() { return tenLoai; }
}