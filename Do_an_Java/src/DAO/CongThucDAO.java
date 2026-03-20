/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.CongThuc;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Duyke
 */
public class CongThucDAO {
      public static List<CongThuc> layCongThucTheoMaSP(String maSP) {
        List<CongThuc> ds = new ArrayList<>();
        
        // Giả sử bảng CongThuc có: MaSP, MaNL, SoLuong
        // Bảng NguyenLieu có: MaNL, TenNguyenLieu
        String sql = "SELECT MaNL, MaSP, SoLuong " +
                     "FROM CongThuc "+
                     "WHERE MaSP = ?";

        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CongThuc ct = new CongThuc(
                        rs.getString("MaNL"),
                        rs.getString("MaSP"),
                        rs.getDouble("SoLuong")
                    );
                    ds.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
      
      
     public boolean delete(String maSP) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "DELETE FROM CongThuc WHERE MaSP  =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maSP);
        int rows = ps.executeUpdate();
        ps.close();
        conn.close();
        return rows > 0;
    }
     
      public boolean insert(CongThuc ct) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "INSERT INTO CongThuc (MaNL, MaSP, SoLuong) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ct.getMaNguyenLieu());
        ps.setString(2, ct.getMaSP());
        ps.setDouble(3, ct.getSoLuong());
        int rows = ps.executeUpdate();
        ps.close();
        conn.close();
        return rows > 0;
    }
}
