/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {
    public static List<KhuyenMai> layDanhSachKhuyenMai() {
    List<KhuyenMai> dsKhuyenMai = new ArrayList<>();
    try (Connection conn = KetNoiCSDL.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT MaKM, TenKhuyenMai, GiamGia FROM KhuyenMai");
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            KhuyenMai km = new KhuyenMai(
                rs.getString("MaKM"),
                rs.getString("TenKhuyenMai"),
                rs.getDouble("GiamGia")
            );
            dsKhuyenMai.add(km);
            System.out.println("Đã thêm khuyến mãi: " + km.getTenKhuyenMai());
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Lỗi khi tải danh sách khuyến mãi: " + e.getMessage());
    }
    return dsKhuyenMai;
}
}