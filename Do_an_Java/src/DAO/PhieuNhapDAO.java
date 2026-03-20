/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.PhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Duyke
 */
public class PhieuNhapDAO {

    public boolean insertPhieuNhap(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (MaPN, MaNV, NgayNhap, MaNCC,  TongTien, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPN());
            ps.setString(2, pn.getMaNV());
            ps.setDate(3, new java.sql.Date(pn.getNgayNhap().getTime()));
            ps.setString(4, pn.getMaNCC());
            ps.setDouble(5, pn.getTongTien());
            ps.setString(6, pn.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<PhieuNhap> getAll() throws SQLException {
        List<PhieuNhap> list = new ArrayList<>();
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "SELECT * FROM PhieuNhap";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            PhieuNhap pn = new PhieuNhap();
            pn.setMaPN(rs.getString("MaPN"));
            pn.setMaNV(rs.getString("MaNV"));
            pn.setMaNCC(rs.getString("MaNCC"));
            pn.setNgayNhap(rs.getDate("NgayNhap"));
            pn.setTongTien(rs.getDouble("TongTien"));
            pn.setTrangThai(rs.getString("TrangThai"));

            list.add(pn);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;
    }

    public double quyDoiDonVi(double soLuong, String donViTinh) {
        switch (donViTinh.toLowerCase()) {
            case "kilogram":
                return soLuong * 1000; // 1 kg = 1000 gram
            case "lít":
                return soLuong * 1000; // 1 lít = 1000 ml
            case "cái":
                return soLuong; // Không cần chuyển đổi
            default:
                return soLuong; // Nếu không rõ, giữ nguyên
        }
    }

    public boolean updateTrangThai(String maPN, String trangThai) {
        String sql = "UPDATE PhieuNhap SET TrangThai = ? WHERE MaPN = ?";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maPN);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoLuongTon(String maNL, double soLuongThem) {
        String sql = "UPDATE NguyenLieu SET SoLuongTon = SoLuongTon + ? WHERE MaNL = ?";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, soLuongThem);
            stmt.setString(2, maNL);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaPhieuNhap(String maPN) {
        String sql = "DELETE FROM PhieuNhap WHERE MaPN = ?";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPN);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean daDuyet(String maPN) {
        String sql = "SELECT TrangThai FROM PhieuNhap WHERE MaPN = ?";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPN);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String trangThai = rs.getString("TrangThai");
                return "Đã duyệt".equalsIgnoreCase(trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
