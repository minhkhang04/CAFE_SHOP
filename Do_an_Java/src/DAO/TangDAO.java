/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author pc
 */
package DAO;

import Model.Tang;
import java.sql.*;
import java.util.*;

public class TangDAO {
    public static List<Tang> layDanhSachTang() {
        List<Tang> list = new ArrayList<>();
        try {
            Connection conn = KetNoiCSDL.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Tang");
            while(rs.next()) {
                Tang t = new Tang(
                    rs.getString("MaTang"),
                    rs.getString("TenTang")
                );
                list.add(t);
            }
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    // Thêm tầng mới vào CSDL
    public static boolean themTang(Tang tang) {
        try {
            Connection conn = KetNoiCSDL.getConnection();
            String sql = "INSERT INTO Tang (MaTang, TenTang) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tang.getMaTang());
            ps.setString(2, tang.getTenTang());
            int rows = ps.executeUpdate();
            conn.close();
            return rows > 0;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaTang(String maTang) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sqlCheck = "SELECT * FROM Ban WHERE MaTang = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, maTang);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                return false; // Tầng có bàn
            }

            String sqlDelete = "DELETE FROM Tang WHERE MaTang = ?";
            PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
            psDelete.setString(1, maTang);
            int rowsAffected = psDelete.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}