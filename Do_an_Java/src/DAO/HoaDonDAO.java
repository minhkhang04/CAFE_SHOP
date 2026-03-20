/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.HoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    public static boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (MaHD, MaKH, TongTien, NgayLap, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaKH()); // Có thể null nếu chưa áp dụng khách hàng
            ps.setDouble(3, hd.getTongTien());
            ps.setDate(4, hd.getNgayLap());
            ps.setString(5, hd.getTrangThai());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean capNhatTongTien(String maHD, double tongTien) {
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tongTien);
            ps.setString(2, maHD);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<HoaDon> getAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "SELECT * FROM HoaDon";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setMaHD(rs.getString("MaHD"));
            hd.setMaKH(rs.getString("MaKH"));
            hd.settongTien(rs.getDouble("TongTien"));
            hd.setngayLap(rs.getDate("NgayLap"));
            hd.settrangThai(rs.getString("TrangThai"));
            list.add(hd);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}
