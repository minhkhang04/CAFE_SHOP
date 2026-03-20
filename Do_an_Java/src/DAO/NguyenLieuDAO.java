/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.NguyenLieu;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Duyke
 */
public class NguyenLieuDAO {

    public List<NguyenLieu> getAll() throws SQLException {
        List<NguyenLieu> list = new ArrayList<>();
        Connection conn = KetNoiCSDL.getConnection(); // sử dụng lớp kết nối bạn có

        String sql = "SELECT * FROM NguyenLieu";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            NguyenLieu nl = new NguyenLieu();
            nl.setMaNguyenLieu(rs.getString("MaNL"));
            nl.setTenNguyenLieu(rs.getString("TenNL"));
            nl.setSoLuongTon(rs.getDouble("SoLuongTon"));
            nl.setDVT(rs.getString("DonViTinh"));

            list.add(nl);
        }

        rs.close();
        stmt.close();
        conn.close();

        return list;
    }

    public boolean insert(NguyenLieu nl) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "INSERT INTO NguyenLieu VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nl.getMaNguyenLieu());
        ps.setString(2, nl.getTenNguyenLieu());
        ps.setString(3, nl.getDVT());
        ps.setDouble(4, nl.getSoLuongTon());
        int rows = ps.executeUpdate();
        ps.close();
        conn.close();
        return rows > 0;
    }

    public boolean update(NguyenLieu nl) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "UPDATE NguyenLieu SET TenNL=?, DonViTinh=?, SoLuongTon=? WHERE MaNL=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nl.getTenNguyenLieu());
        ps.setString(2, nl.getDVT());
        ps.setDouble(3, nl.getSoLuongTon());
        ps.setString(4, nl.getMaNguyenLieu());
        int rows = ps.executeUpdate();
        ps.close();
        conn.close();
        return rows > 0;
    }

    public boolean delete(String maNL) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "DELETE FROM NguyenLieu WHERE MaNL =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maNL);
        int rows = ps.executeUpdate();
        ps.close();
        conn.close();
        return rows > 0;
    }
}
