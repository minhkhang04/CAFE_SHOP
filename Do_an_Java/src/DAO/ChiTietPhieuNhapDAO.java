/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ChiTietPhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Duyke
 */
public class ChiTietPhieuNhapDAO {

    public boolean insert(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaPN, MaNL, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ctpn.getMaPN());
            stmt.setString(2, ctpn.getMaNL());
            stmt.setDouble(3, ctpn.getSoLuong());
            stmt.setDouble(4, ctpn.getDonGia());
            stmt.setDouble(5, ctpn.getThanhTien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietPhieuNhap> getByMaPN(String maPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN = ?";

        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPN);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
                ctpn.setMaPN(rs.getString("MaPN"));
                ctpn.setMaNL(rs.getString("MaNL"));
                ctpn.setSoLuong(rs.getDouble("SoLuong"));
                ctpn.setDonGia(rs.getDouble("DonGia"));
                ctpn.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ctpn);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public boolean xoaChiTietTheoMaPN(String maPN) {
    String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPN = ?";
    try (Connection conn = KetNoiCSDL.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maPN);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    
}
    public boolean xoaChiTietTheoMaPNVaMaNL(String maPN, String maNL) throws SQLException {
    String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPN = ? AND MaNL = ?";
    try (Connection conn = KetNoiCSDL.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maPN);
        stmt.setString(2, maNL);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
