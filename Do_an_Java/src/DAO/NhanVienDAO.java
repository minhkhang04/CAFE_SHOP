/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.NhanVien;
import java.util.*;
import java.sql.*;

/**
 *
 * @author Duyke
 */
public class NhanVienDAO {
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
       
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "SELECT * FROM NhanVien";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("MaNV"),
                    rs.getString("TenNV"),
                    rs.getString("GioiTinh"),
                    rs.getDate("NgayVaoLam"),
                    rs.getString("ChucVu"),
                    rs.getString("SoDienThoai")
                );
                list.add(nv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public static String checkLogin(String maNV, String soDienThoai) {
        Connection conn = KetNoiCSDL.getConnection();
        String query = "SELECT * FROM NhanVien WHERE MaNV = ? AND SoDienThoai = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, maNV);
            pst.setString(2, soDienThoai);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                if (maNV.startsWith("NV")) {
                    return "NhanVien";
                } else if (maNV.startsWith("AD")) {
                    return "QuanLy";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }
    public boolean them(NhanVien nv) {
       String sql = "INSERT INTO NhanVien (MaNV, TenNV, GioiTinh, NgayVaoLam, ChucVu, SoDienThoai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nv.getMaNV());
            pst.setString(2, nv.getTenNV());
            pst.setString(3, nv.getGioiTinh());
            pst.setDate(4, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            pst.setString(5, nv.getChucVu());
            pst.setString(6, nv.getSoDienThoai());

            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public boolean xoa(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maNV);
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public boolean capNhat(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV = ?, GioiTinh = ?, NgayVaoLam = ?, ChucVu = ?, SoDienThoai = ? WHERE MaNV = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nv.getTenNV());
            pst.setString(2, nv.getGioiTinh());
            pst.setDate(3, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            pst.setString(4, nv.getChucVu());
            pst.setString(5, nv.getSoDienThoai());
            pst.setString(6, nv.getMaNV());

            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public List<NhanVien> timKiem(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE MaNV LIKE ? OR TenNV LIKE ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("MaNV"),
                    rs.getString("TenNV"),
                    rs.getString("GioiTinh"),
                    rs.getDate("NgayVaoLam"),
                    rs.getString("ChucVu"),
                    rs.getString("SoDienThoai")
                );
                list.add(nv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }



}
