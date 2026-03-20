
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pc
 */
package DAO;

import Model.ChiTietHoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public static boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien, GhiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cthd.getMaHD());
            ps.setString(2, cthd.getMaSP());
            ps.setInt(3, cthd.getSoLuong());
            ps.setDouble(4, cthd.getThanhTien());
            ps.setString(5, cthd.getGhiChu());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<ChiTietHoaDon> getAll() throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "SELECT * FROM ChiTietHoaDon";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            ChiTietHoaDon hd = new ChiTietHoaDon();
            hd.setmaHD(rs.getString("maHD"));
            hd.setmaSP(rs.getString("maSP"));
            hd.setSoLuong(rs.getInt("SoLuong"));
            hd.setThanhTien(rs.getDouble("ThanhTien"));
            hd.setghiChu(rs.getString("GhiChu"));
            list.add(hd);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}
