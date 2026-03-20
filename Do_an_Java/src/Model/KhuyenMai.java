/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class KhuyenMai {
    private String maKM;
    private String tenKhuyenMai;
    private double giamGia;

    public KhuyenMai(String maKM, String tenKhuyenMai, double giamGia) {
        this.maKM = maKM;
        this.tenKhuyenMai = tenKhuyenMai;
        this.giamGia = giamGia;
    }

    // Getters
    public String getMaKM() { return maKM; }
    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public double getGiamGia() { return giamGia; }
}