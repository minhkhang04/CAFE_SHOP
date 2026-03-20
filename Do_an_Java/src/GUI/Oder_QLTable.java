package GUI;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import DAO.*;
import Model.*;

/**
 *
 * @author pc
 */
public class Oder_QLTable extends javax.swing.JFrame {

    private java.util.List<Model.LoaiSanPham> dsLoaiSP;
    private java.util.List<Model.SanPham> dsSanPham;
    private java.util.List<Model.KhuyenMai> dsKhuyenMai;
    private java.util.List<Model.KhachHang> dsKhachHang;
    private java.util.Map<String, Integer> mapSoLuong = new java.util.LinkedHashMap<>();
    private java.util.List<Model.Ban> dsBan;
    private java.util.Map<String, Model.SanPham> mapSanPham = new java.util.HashMap<>();
    private java.util.Set<String> dsMaSP_DaLuu = new java.util.HashSet<>();
    private String dangXem_MaHD = null;
    private String maBan;
    private String maKH = null;

    
    public Oder_QLTable(String maBan) {
        
        this.maBan = maBan;
        initComponents();
        dsKhuyenMai = new ArrayList<>();
        dsBan = new ArrayList<>();       
        dsLoaiSP = new ArrayList<>();
        dsSanPham = new ArrayList<>();  
        dsBan = BanDAO.layDanhSachBan();
        cboSoBan.removeAllItems();
        for (Model.Ban ban : dsBan) {
            cboSoBan.addItem(ban.getMaBan());
        }
        cboSoBan.setSelectedItem(maBan);
        try {
            dsKhuyenMai = KhuyenMaiDAO.layDanhSachKhuyenMai();
            cboKhuyenMai.removeAllItems();
            cboKhuyenMai.addItem("Không áp dụng");
            if (dsKhuyenMai != null && !dsKhuyenMai.isEmpty()) {
                for (Model.KhuyenMai km : dsKhuyenMai) {
                    cboKhuyenMai.addItem(km.getTenKhuyenMai());
                }
            } else {
                System.out.println("Danh sách khuyến mãi rỗng hoặc null!");
                cboKhuyenMai.addItem("Không có khuyến mãi");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi tải danh sách khuyến mãi: " + e.getMessage());
            cboKhuyenMai.removeAllItems();
            cboKhuyenMai.addItem("Không áp dụng");
        }
        dsLoaiSP = LoaiSanPhamDAO.layDanhSachLoaiSanPham();
        cboListMon.removeAllItems();
        for (LoaiSanPham lsp : dsLoaiSP) {
            cboListMon.addItem(lsp.getTenLoai());
        }
        if (cboListMon.getItemCount() > 0) {
            cboListMon.setSelectedIndex(0);
            hienThiDanhSachMon();
        }
        cboListMon.addActionListener((java.awt.event.ActionEvent evt) -> {
            hienThiDanhSachMon();
        });
        // Tự động tính tiền thối khi nhập tiền
        txtTienKhachDua.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienThoi();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienThoi();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienThoi();
            }
        });

        System.out.println("Đang tải hóa đơn cho bàn: " + maBan);
        taiLaiHoaDon();
        setLocationRelativeTo(null);

    }

    private String taoMaHD() {
        try (Connection conn = KetNoiCSDL.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM HoaDon")) {
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("HD%03d", count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD001";
    }

    private void taiLaiHoaDon() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT hd.MaHD FROM HoaDon hd "
                    + "JOIN SuDungBan sdb ON hd.MaHD = sdb.MaHD "
                    + "WHERE sdb.MaBan = ? AND sdb.ThoiGianKetThuc IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            System.out.println("Truy vấn SQL: " + sql.replace("?", "'" + maBan + "'"));

            if (rs.next()) {
                dangXem_MaHD = rs.getString("MaHD");
                System.out.println("Tìm thấy hóa đơn: " + dangXem_MaHD + " cho bàn " + maBan);

                String sqlCT = "SELECT cthd.MaSP, cthd.SoLuong, sp.TenSP, sp.DonGia "
                        + "FROM ChiTietHoaDon cthd JOIN SanPham sp ON cthd.MaSP = sp.MaSP "
                        + "WHERE cthd.MaHD = ?";
                PreparedStatement psCT = conn.prepareStatement(sqlCT);
                psCT.setString(1, dangXem_MaHD);
                ResultSet rsCT = psCT.executeQuery();
                System.out.println("Tải chi tiết hóa đơn cho MaHD: " + dangXem_MaHD);

                while (rsCT.next()) {
                    String maSP = rsCT.getString("MaSP");
                    int soLuong = rsCT.getInt("SoLuong");
                    mapSoLuong.put(maSP, soLuong);
                    mapSanPham.put(maSP, new SanPham(maSP, rsCT.getString("TenSP"), "", rsCT.getDouble("DonGia"), "", ""));
                    dsMaSP_DaLuu.add(maSP);
                    System.out.println("Tải món: " + maSP + " - Số lượng: " + soLuong);
                }
                capNhatBangHoaDon();
            } else {
                System.out.println("Không tìm thấy hóa đơn cho bàn " + maBan + " với trạng thái Đang sử dụng.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải hóa đơn: " + e.getMessage());
        }
    }

    private void hienThiDanhSachMon() {
        panelMon.removeAll();
        String tenLoai = (String) cboListMon.getSelectedItem();
        String maLoai = null;
        for (Model.LoaiSanPham lsp : dsLoaiSP) {
            if (lsp.getTenLoai().equals(tenLoai)) {
                maLoai = lsp.getMaLoaiSP();
            }
        }
        dsSanPham = DAO.SanPhamDAO.layDanhSachSanPhamTheoLoai(maLoai);
        for (Model.SanPham sp : dsSanPham) {
            javax.swing.JButton btnMon = new javax.swing.JButton(sp.getTenSP());
            btnMon.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            btnMon.setPreferredSize(new java.awt.Dimension(130, 40));
            btnMon.addActionListener(evt -> chonMon(sp));
            panelMon.add(btnMon);
            mapSanPham.put(sp.getMaSP(), sp);
        }
        panelMon.revalidate();
        panelMon.repaint();
    }

    private void chonMon(Model.SanPham sp) {
        String maSP = sp.getMaSP();
        mapSoLuong.put(maSP, mapSoLuong.getOrDefault(maSP, 0) + 1);
        capNhatBangHoaDon();
    }

    private void capNhatBangHoaDon() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        double tongTienGoc = 0;
        for (String maSP : mapSoLuong.keySet()) {
            int soLuong = mapSoLuong.get(maSP);
            double donGia = mapSanPham.get(maSP).getDonGia();
            double thanhTien = donGia * soLuong;
            tongTienGoc += thanhTien;
            String tenMon = mapSanPham.get(maSP).getTenSP();
            if (!dsMaSP_DaLuu.contains(maSP)) {
                tenMon += "++";
            }
            model.addRow(new Object[]{tenMon, soLuong, thanhTien});
        }

        // Áp dụng khuyến mãi, kiểm tra null
        double giamGia = 0;
        String tenKhuyenMai = (String) cboKhuyenMai.getSelectedItem();
        if (!"Không áp dụng".equals(tenKhuyenMai) && dsKhuyenMai != null) {
            for (Model.KhuyenMai km : dsKhuyenMai) {
                if (km.getTenKhuyenMai().equals(tenKhuyenMai)) {
                    giamGia = km.getGiamGia();
                    break;
                }
            }
        }
        double tongTien = tongTienGoc - giamGia;
        txtTongTien.setText(String.format("%.0f", tongTien > 0 ? tongTien : 0));
    }
    
    private void kiemTraKhachHang() {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT MaKH, TenKH, GioiTinh FROM KhachHang WHERE SoDienThoai = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                maKH = rs.getString("MaKH");
                txtHoTen.setText(rs.getString("TenKH"));
                cboGioiTinh.setSelectedItem(rs.getString("GioiTinh"));
                JOptionPane.showMessageDialog(this, "Khách hàng đã đăng ký!");
            } else {
                maKH = null;
                txtHoTen.setText("");
                cboGioiTinh.setSelectedIndex(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra khách hàng: " + e.getMessage());
        }
    }

    private void dangKyKhachHang() {
        String sdt = txtSDT.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String maLoaiKH = "LKH01";
        int khachHangTiemNang = 0;

        if (sdt.isEmpty() || hoTen.isEmpty()) {
            
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sqlCheck = "SELECT MaKH FROM KhachHang WHERE SoDienThoai = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, sdt);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                maKH = rs.getString("MaKH");
                return; // Đã tồn tại, không cần đăng ký lại
            }

            String maKHNew = taoMaKH();
            String sqlInsert = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, GioiTinh, MaLoaiKH, KhachHangTiemNang) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, maKHNew);
            psInsert.setString(2, hoTen);
            psInsert.setString(3, sdt);
            psInsert.setString(4, gioiTinh);
            psInsert.setString(5, maLoaiKH);
            psInsert.setInt(6, khachHangTiemNang);
            psInsert.executeUpdate();

            maKH = maKHNew;
            JOptionPane.showMessageDialog(this, "Đăng ký khách hàng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng ký khách hàng: " + e.getMessage());
        }
    }

    private String taoMaKH() {
        try (Connection conn = KetNoiCSDL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM KhachHang")) {
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("KH%04d", count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KH0001";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        lblSoHoaDon = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnGuiBar = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnTinhTien = new javax.swing.JButton();
        panelMon = new javax.swing.JPanel();
        cboListMon = new javax.swing.JComboBox<>();
        txtTienKhachDua = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTienConLai = new javax.swing.JTextField();
        cboSoBan = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        txtCheckKH = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cboGioiTinh = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cboKhuyenMai = new javax.swing.JComboBox<>();
        btnQLBan = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên Món ", "Số lượng", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(20);
            tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(50);
        }
        tblHoaDon.getAccessibleContext().setAccessibleName("");

        jLabel1.setText("Tổng tiền:");

        txtTongTien.setEditable(false);
        txtTongTien.setEnabled(false);
        txtTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongTienActionPerformed(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Action-cancel-icon.png"))); // NOI18N
        btnCancel.setText("Hủy bỏ");
        btnCancel.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnGuiBar.setBackground(new java.awt.Color(0, 102, 255));
        btnGuiBar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuiBar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuiBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user.png"))); // NOI18N
        btnGuiBar.setText("Gửi bar");
        btnGuiBar.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnGuiBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiBarActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Action-edit-icon.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(51, 255, 51));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Action-file-new-icon.png"))); // NOI18N
        btnSave.setText("Lưu HĐ");
        btnSave.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnTinhTien.setBackground(new java.awt.Color(255, 153, 51));
        btnTinhTien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTinhTien.setForeground(new java.awt.Color(255, 255, 255));
        btnTinhTien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/caculator.png"))); // NOI18N
        btnTinhTien.setText("Tính Tiền");
        btnTinhTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTinhTienActionPerformed(evt);
            }
        });

        panelMon.setLayout(new java.awt.GridLayout(5, 15, 15, 10));

        jLabel2.setText("Tiền nhận:");

        jLabel3.setText("Tiền còn lại:");

        txtTienConLai.setEditable(false);
        txtTienConLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienConLaiActionPerformed(evt);
            }
        });

        cboSoBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSoBanActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 12))); // NOI18N

        jLabel4.setText("Số điện thoại: ");

        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        jLabel5.setText("Họ tên khách hàng:");

        txtCheckKH.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCheckKH.setText("Kiểm tra");
        txtCheckKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckKHActionPerformed(evt);
            }
        });

        jLabel6.setText("Giới tính:");

        cboGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(txtCheckKH)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCheckKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 204, 0));
        jLabel7.setText("Khuyến mãi: ");

        cboKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKhuyenMaiActionPerformed(evt);
            }
        });

        btnQLBan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQLBan.setText("Quàn Lý Bàn");
        btnQLBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLBanActionPerformed(evt);
            }
        });
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMon, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboListMon, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnQLBan))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblSoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cboSoBan, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(4, 4, 4))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnGuiBar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnTinhTien)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTienConLai, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnQLBan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboListMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblSoHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboSoBan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cboKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTienConLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuiBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTinhTien, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mapSoLuong.clear();
        capNhatBangHoaDon();
        txtTienConLai.setText("");
        txtTienKhachDua.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnGuiBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiBarActionPerformed
        if (mapSoLuong.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Chưa có món nào trong hóa đơn!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String maSP : mapSoLuong.keySet()) {
            sb.append(mapSanPham.get(maSP).getTenSP())
                    .append(" x").append(mapSoLuong.get(maSP)).append("\n");
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Gửi bar thành công!\n" + sb);
    }//GEN-LAST:event_btnGuiBarActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (mapSoLuong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có món để lưu hóa đơn!");
            return;
        }
        if (maKH == null) {
            dangKyKhachHang();
        }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT TrangThai FROM Ban WHERE MaBan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString("TrangThai").equalsIgnoreCase("Đang sử dụng")) {
                JOptionPane.showMessageDialog(this, "Bàn đang được sử dụng. Vui lòng tính tiền trước khi tạo hóa đơn mới!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra trạng thái bàn!");
            return;
        }

        String maHD = taoMaHD();
        double tongTien = Double.parseDouble(txtTongTien.getText());
        HoaDon hd = new HoaDon(maHD, maKH, tongTien, new java.sql.Date(System.currentTimeMillis()), "Vừa nhập");
        boolean success = HoaDonDAO.themHoaDon(hd);
        if (success) {
            for (String maSP : mapSoLuong.keySet()) {
                int soLuong = mapSoLuong.get(maSP);
                double thanhTien = soLuong * mapSanPham.get(maSP).getDonGia();
                ChiTietHoaDon cthd = new ChiTietHoaDon(maHD, maSP, soLuong, thanhTien, "");
                if (!ChiTietHoaDonDAO.themChiTietHoaDon(cthd)) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu chi tiết hóa đơn!");
                    return;
                }
            }
            SuDungBan sdb = new SuDungBan(null, maBan, null, maHD,
                    new Timestamp(System.currentTimeMillis()), null, "Đang sử dụng");
            boolean sdbSuccess = SuDungBanDAO.themSuDungBan(sdb);
            if (!sdbSuccess) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin sử dụng bàn!");
                return;
            }
            if (!BanDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng")) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn mới: " + maHD);
            dangXem_MaHD = maHD;
            dsMaSP_DaLuu.clear();
            dsMaSP_DaLuu.addAll(mapSoLuong.keySet());

            Table_QLTable tableScreen = new Table_QLTable();
            this.setVisible(false);
            tableScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại!");
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnTinhTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTinhTienActionPerformed
            try {
            double tong = Double.parseDouble(txtTongTien.getText());
            double nhan = Double.parseDouble(txtTienKhachDua.getText());
            if (nhan < tong) {
                JOptionPane.showMessageDialog(this, "Tiền nhận nhỏ hơn tổng hóa đơn!");
                return;
            }
            txtTienConLai.setText(String.format("%.0f", nhan - tong));
            if (maKH == null) {
                dangKyKhachHang();
            }

            // Cập nhật trạng thái bàn
            if (!BanDAO.capNhatTrangThaiBan(maBan, "Đã rời")) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn!");
                return;
            }

            if (dangXem_MaHD != null) {
                try (Connection conn = KetNoiCSDL.getConnection()) {
                    String sql = "UPDATE SuDungBan SET ThoiGianKetThuc = ?, TrangThai = 'Đã rời' WHERE MaBan = ? AND MaHD = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    ps.setString(2, maBan);
                    ps.setString(3, dangXem_MaHD);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 0) {
                        System.out.println("Không tìm thấy bản ghi SuDungBan để cập nhật với MaBan = " + maBan + " và MaHD = " + dangXem_MaHD);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật SuDungBan: " + e.getMessage());
                    return;
                }
            }

            mapSoLuong.clear();
            dsMaSP_DaLuu.clear();
            dangXem_MaHD = null;
            capNhatBangHoaDon();
            txtTienConLai.setText("");
            txtTienKhachDua.setText("");
            JOptionPane.showMessageDialog(this, "Tính tiền thành công! Bàn đã trống.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nhập số hợp lệ cho tiền nhận!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTinhTienActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (dangXem_MaHD == null) {
            JOptionPane.showMessageDialog(this, "Chưa có hóa đơn để cập nhật!");
            return;
        }
        for (String maSP : mapSoLuong.keySet()) {
            if (!dsMaSP_DaLuu.contains(maSP)) {
                int soLuong = mapSoLuong.get(maSP);
                double thanhTien = soLuong * mapSanPham.get(maSP).getDonGia();
                ChiTietHoaDon cthd = new ChiTietHoaDon(dangXem_MaHD, maSP, soLuong, thanhTien, "Thêm mới");
                ChiTietHoaDonDAO.themChiTietHoaDon(cthd);
                dsMaSP_DaLuu.add(maSP);
            }
        }
        capNhatBangHoaDon();
        JOptionPane.showMessageDialog(this, "Đã thêm món mới. Món mới sẽ có dấu ++ trên bảng!");
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void cboSoBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSoBanActionPerformed
        String selectedBan = (String) cboSoBan.getSelectedItem();
        if (selectedBan != null && !selectedBan.equals(maBan)) {
            maBan = selectedBan;
            System.out.println("Đang tải hóa đơn cho bàn mới: " + maBan);
            mapSoLuong.clear();
            mapSanPham.clear();
            dsMaSP_DaLuu.clear();
            dangXem_MaHD = null;
            capNhatBangHoaDon();
            taiLaiHoaDon();
    }
    }//GEN-LAST:event_cboSoBanActionPerformed

    private void cboKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhuyenMaiActionPerformed
        capNhatBangHoaDon();
    }//GEN-LAST:event_cboKhuyenMaiActionPerformed

    private void txtCheckKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckKHActionPerformed
        kiemTraKhachHang();
    }//GEN-LAST:event_txtCheckKHActionPerformed

    private void btnQLBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLBanActionPerformed
        Table_QLTable tableScreen = new Table_QLTable();
        this.setVisible(false);
        tableScreen.setVisible(true);
    }//GEN-LAST:event_btnQLBanActionPerformed

    private void txtTienConLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienConLaiActionPerformed
        try {
            double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
            double tongTien = Double.parseDouble(txtTongTien.getText());
            double tienConLai = tienKhachDua - tongTien;

            if (tienConLai < 0) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ để thanh toán!");
                return;
            }

            txtTienConLai.setText(String.format("%.0f", tienConLai));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
        }

    }//GEN-LAST:event_txtTienConLaiActionPerformed

    private void txtTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongTienActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed
    private void tinhTienThoi() {
        try {
            double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
            double tongTien = Double.parseDouble(txtTongTien.getText());

            double tienThoi = tienKhachDua - tongTien;

            if (tienThoi < 0) {
                txtTienConLai.setText("Thiếu tiền");
            } else {
                txtTienConLai.setText(String.format("%.0f", tienThoi));
            }
        } catch (NumberFormatException e) {
            txtTienConLai.setText("");
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Oder_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Oder_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Oder_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Oder_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                java.awt.EventQueue.invokeLater(() -> new Oder_QLTable("B101").setVisible(true));
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnGuiBar;
    private javax.swing.JButton btnQLBan;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTinhTien;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboGioiTinh;
    private javax.swing.JComboBox<String> cboKhuyenMai;
    private javax.swing.JComboBox<String> cboListMon;
    private javax.swing.JComboBox<String> cboSoBan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSoHoaDon;
    private javax.swing.JPanel panelMon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JButton txtCheckKH;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTienConLai;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
