/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    public static KhachHang kiemTraKhachHang(String sdt) {
        KhachHang kh = null;
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaKH, HoTenKH, SDT, GioiTinh, MaLoaiKH, KhachHangTiemNang FROM KhachHang WHERE SDT = ?")) {

            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kh = new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("HoTenKH"),
                    rs.getString("SDT"),
                    rs.getString("GioiTinh"),
                    rs.getString("MaLoaiKH"),
                    rs.getInt("KhachHangTiemNang")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public static boolean themKhachHang(KhachHang kh) {
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO KhachHang (MaKH, HoTenKH, SDT, GioiTinh, MaLoaiKH, KhachHangTiemNang) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoTenKH());
            ps.setString(3, kh.getSDT());
            ps.setString(4, kh.getGioiTinh());
            ps.setString(5, kh.getMaLoaiKH());
            ps.setInt(6, kh.getKhachHangTiemNang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
