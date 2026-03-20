/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.SanPham;
import java.sql.*;
import java.util.*;

public class SanPhamDAO {

    public static List<SanPham> layDanhSachSanPhamTheoLoai(String maLoaiSP) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE MaLoaiSP = ?";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getString("MaSP"),
                            rs.getString("TenSP"),
                            rs.getString("MaLoaiSP"),
                            rs.getDouble("DonGia"),
                            rs.getString("TrangThai"),
                            rs.getString("GhiChu")
                    );
                    ds.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static List<SanPham> layAllDanhSachSanPham() {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        try (Connection conn = KetNoiCSDL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getString("MaSP"),
                            rs.getString("TenSP"),
                            rs.getString("MaLoaiSP"),
                            rs.getDouble("DonGia"),
                            rs.getString("TrangThai"),
                            rs.getString("GhiChu")
                    );
                    ds.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    public static boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (MaSP, TenSP, MaLoaiSP, DonGia, TrangThai, GhiChu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaLoaiSP());
            ps.setDouble(4, sp.getDonGia());
            ps.setString(5, sp.getTrangThai());
            ps.setString(6, sp.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET TenSP = ?, MaLoaiSP = ?, DonGia = ?, TrangThai = ?, GhiChu = ? WHERE MaSP = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getMaLoaiSP());
            ps.setDouble(3, sp.getDonGia());
            ps.setString(4, sp.getTrangThai());
            ps.setString(5, sp.getGhiChu());
            ps.setString(6, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE MaSP = ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<SanPham> timKiemSanPham(String tuKhoa) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE MaSP LIKE ? OR TenSP LIKE ?";
        try (Connection conn = KetNoiCSDL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String keyword = "%" + tuKhoa + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getString("MaSP"),
                            rs.getString("TenSP"),
                            rs.getString("MaLoaiSP"),
                            rs.getDouble("DonGia"),
                            rs.getString("TrangThai"),
                            rs.getString("GhiChu")
                    );
                    ds.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
