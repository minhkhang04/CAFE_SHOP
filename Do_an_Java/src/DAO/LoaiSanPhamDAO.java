/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author pc
 */


import Model.LoaiSanPham;
import java.sql.*;
import java.util.*;

public class LoaiSanPhamDAO {
    public static List<LoaiSanPham> layDanhSachLoaiSanPham() {
        List<LoaiSanPham> ds = new ArrayList<>();
        try (Connection conn = KetNoiCSDL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM LoaiSanPham")) {
            while (rs.next()) {
                LoaiSanPham lsp = new LoaiSanPham(
                    rs.getString("MaLoaiSP"),
                    rs.getString("TenLoai")
                );
                ds.add(lsp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    public static boolean themLoaiSanPham(LoaiSanPham lsp) {
        String sql = "INSERT INTO LoaiSanPham (MaLoaiSP, TenLoai) VALUES (?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lsp.getMaLoaiSP());
            ps.setString(2, lsp.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaLoaiSanPham(String maLoaiSP) {
        String sql = "DELETE FROM LoaiSanPham WHERE MaLoaiSP = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatLoaiSanPham(LoaiSanPham lsp) {
        String sql = "UPDATE LoaiSanPham SET TenLoai = ? WHERE MaLoaiSP = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lsp.getTenLoai());
            ps.setString(2, lsp.getMaLoaiSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<LoaiSanPham> timKiemLoaiSP(String tuKhoa) {
        List<LoaiSanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSanPham WHERE MaLoaiSP LIKE ? OR TenLoai LIKE ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String keyword = "%" + tuKhoa + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoaiSanPham lsp = new LoaiSanPham(
                        rs.getString("MaLoaiSP"),
                        rs.getString("TenLoai")
                    );
                    ds.add(lsp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }


    public static boolean themLoaiSP(LoaiSanPham lsp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static boolean xoaLoaiSP(String ma) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static boolean capNhatLoaiSP(LoaiSanPham lsp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}