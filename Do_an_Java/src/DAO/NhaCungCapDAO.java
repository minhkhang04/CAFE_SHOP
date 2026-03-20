/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Model.NhaCungCap;
import java.sql.*;
import java.util.*;
/**
 *
 * @author Duyke
 */
public class NhaCungCapDAO {
    public List<NhaCungCap> getAll() throws SQLException {
        List<NhaCungCap> list = new ArrayList<>();
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "SELECT * FROM NhaCungCap";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            NhaCungCap ncc = new NhaCungCap();
            ncc.setMaNCC(rs.getString("MaNCC"));
            ncc.setTenNCC(rs.getString("TenNCC"));
            ncc.setDiaChi(rs.getString("DiaChi"));
            ncc.setSoDienThoai(rs.getString("SDT"));
            list.add(ncc);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
    
     public boolean insert(NhaCungCap ncc) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "INSERT INTO NhaCungCap VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ncc.getMaNCC());
        ps.setString(2, ncc.getTenNCC());
        ps.setString(3, ncc.getDiaChi());
        ps.setString(4, ncc.getSoDienThoai());
        int rows = ps.executeUpdate();
        ps.close(); conn.close();
        return rows > 0;
    }

    public boolean update(NhaCungCap ncc) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "UPDATE NhaCungCap SET TenNCC=?, DiaChi=?, SDT=? WHERE MaNCC=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ncc.getTenNCC());
        ps.setString(2, ncc.getDiaChi());
        ps.setString(3, ncc.getSoDienThoai());
        ps.setString(4, ncc.getMaNCC());
        int rows = ps.executeUpdate();
        ps.close(); conn.close();
        return rows > 0;
    }

    public boolean delete(String maNCC) throws SQLException {
        Connection conn = KetNoiCSDL.getConnection();
        String sql = "DELETE FROM NhaCungCap WHERE MaNCC=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maNCC);
        int rows = ps.executeUpdate();
        ps.close(); conn.close();
        return rows > 0;
    }
}
