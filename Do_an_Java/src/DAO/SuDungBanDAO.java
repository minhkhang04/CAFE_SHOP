/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.SuDungBan;
import java.sql.*;
import javax.swing.JOptionPane;

public class SuDungBanDAO {
    public static boolean themSuDungBan(SuDungBan sdb) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Sinh MaSD tự động
            String maSD = taoMaSD(conn);
            sdb.setMaSD(maSD); // Cập nhật MaSD vào đối tượng

            String sql = "INSERT INTO SuDungBan (MaSD, MaBan, MaKH, MaHD, ThoiGianBatDau, ThoiGianKetThuc, TrangThai) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sdb.getMaSD());
            ps.setString(2, sdb.getMaBan());
            ps.setString(3, sdb.getMaKH());
            ps.setString(4, sdb.getMaHD());
            ps.setTimestamp(5, sdb.getThoiGianBatDau());
            ps.setTimestamp(6, sdb.getThoiGianKetThuc());
            ps.setString(7, sdb.getTrangThai());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("Không thể chèn bản ghi cho MaSD: " + maSD);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu thông tin sử dụng bàn: " + e.getMessage());
            return false;
        }
    }

    private static String taoMaSD(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(MaSD, 3, 3) AS INT)), 0) + 1 FROM SuDungBan";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return String.format("SD%03d", count);
            }
        }
        return "SD001";
    }
}