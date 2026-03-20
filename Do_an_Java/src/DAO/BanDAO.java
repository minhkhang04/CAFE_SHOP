/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Ban;
import java.sql.*;
import java.util.*;

public class BanDAO {

  
    public static List<Ban> layDanhSachBan() {
        List<Ban> list = new ArrayList<>();
        try {
            Connection conn = KetNoiCSDL.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Ban");
            while(rs.next()) {
                Ban ban = new Ban(
                    rs.getString("MaBan"),
                    rs.getString("MaTang"),
                    rs.getString("SoBan"),
                    rs.getString("TrangThai")
                );
                list.add(ban);
            }
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean themBan(String maBan, int soBan, String maTang) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "INSERT INTO Ban (MaBan, SoBan, MaTang, TrangThai) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maBan); // MaBan: B101, B210, ...
            ps.setInt(2, soBan);    // SoBan: 101, 210, ...
            ps.setString(3, maTang); // MaTang: T001, T002, ...
            ps.setString(4, "Trống"); // Trạng thái mặc định
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatTrangThaiBan(String maBan, String trangThai) {
        try {
            Connection conn = KetNoiCSDL.getConnection();
            String sql = "UPDATE Ban SET TrangThai = ? WHERE MaBan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, trangThai);
            ps.setString(2, maBan);
            int rows = ps.executeUpdate();
            conn.close();
            return rows > 0;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean xoaBan(String maBan) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sqlCheck = "SELECT * FROM SuDungBan WHERE MaBan = ? AND ThoiGianKetThuc IS NULL";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, maBan);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                return false;
            }
            String sqlDeleteSuDungBan = "DELETE FROM SuDungBan WHERE MaBan = ?";
            PreparedStatement psDeleteSuDungBan = conn.prepareStatement(sqlDeleteSuDungBan);
            psDeleteSuDungBan.setString(1, maBan);
            psDeleteSuDungBan.executeUpdate();
            
            String sqlDeleteGhepBan = "DELETE FROM GhepBan WHERE MaBan = ?";
            PreparedStatement psDeleteGhepBan = conn.prepareStatement(sqlDeleteGhepBan);
            psDeleteGhepBan.setString(1, maBan);
            psDeleteGhepBan.executeUpdate();

            String sqlDeleteBan = "DELETE FROM Ban WHERE MaBan = ?";
            PreparedStatement psDeleteBan = conn.prepareStatement(sqlDeleteBan);
            psDeleteBan.setString(1, maBan);
            int rowsAffected = psDeleteBan.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean suaBan(String maBanCu, int soBanMoi, String maTang) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Kiểm tra xem bàn có đang được sử dụng không
            String sqlCheck = "SELECT * FROM SuDungBan WHERE MaBan = ? AND ThoiGianKetThuc IS NULL";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, maBanCu);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                return false; // Bàn đang được sử dụng, không thể sửa
            }

            // Tạo mã bàn mới dựa trên số bàn mới
            String maBanMoi = "B" + soBanMoi; // Ví dụ: SoBan = 202 -> MaBan = B202

            // Kiểm tra xem MaBan mới đã tồn tại chưa
            String sqlCheckNew = "SELECT * FROM Ban WHERE MaBan = ?";
            PreparedStatement psCheckNew = conn.prepareStatement(sqlCheckNew);
            psCheckNew.setString(1, maBanMoi);
            ResultSet rsNew = psCheckNew.executeQuery();
            if (rsNew.next()) {
                return false; // Mã bàn mới đã tồn tại
            }

            // Xóa các bản ghi liên quan trong SuDungBan
            String sqlDeleteSuDungBan = "DELETE FROM SuDungBan WHERE MaBan = ?";
            PreparedStatement psDeleteSuDungBan = conn.prepareStatement(sqlDeleteSuDungBan);
            psDeleteSuDungBan.setString(1, maBanCu);
            psDeleteSuDungBan.executeUpdate();

            // Xóa các bản ghi liên quan trong GhepBan (nếu có)
            String sqlDeleteGhepBan = "DELETE FROM GhepBan WHERE MaBan = ?";
            PreparedStatement psDeleteGhepBan = conn.prepareStatement(sqlDeleteGhepBan);
            psDeleteGhepBan.setString(1, maBanCu);
            psDeleteGhepBan.executeUpdate();

            // Xóa bản ghi cũ trong bảng Ban
            String sqlDeleteBan = "DELETE FROM Ban WHERE MaBan = ?";
            PreparedStatement psDeleteBan = conn.prepareStatement(sqlDeleteBan);
            psDeleteBan.setString(1, maBanCu);
            psDeleteBan.executeUpdate();

            // Thêm bản ghi mới với MaBan và SoBan mới
            String sqlInsert = "INSERT INTO Ban (MaBan, SoBan, MaTang, TrangThai) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, maBanMoi);
            psInsert.setInt(2, soBanMoi);
            psInsert.setString(3, maTang);
            psInsert.setString(4, "Trống"); // Trạng thái mặc định
            int rowsAffected = psInsert.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    
}