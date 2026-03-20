package GUI;

import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import DAO.BanDAO;
import DAO.KetNoiCSDL;
import DAO.TangDAO;
import Model.Ban;
import Model.Tang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author pc
 */

public class Table_QLTable extends javax.swing.JFrame {

    private java.util.List<Model.Tang> danhSachTang;
    private java.util.List<Model.Ban> danhSachBan;
    public Table_QLTable() {
        initComponents();
        danhSachBan = DAO.BanDAO.layDanhSachBan(); 
        hienThiDanhSachTang();
    }
    private void hienThiDanhSachTang() {
    danhSachTang = DAO.TangDAO.layDanhSachTang();
    cboTang.removeAllItems();
    for (Model.Tang t : danhSachTang) {
        cboTang.addItem(t.getTenTang());
    }
    if (cboTang.getItemCount() > 0) {
        cboTang.setSelectedIndex(0);
        loadBanTheoTang();
        }
    setLocationRelativeTo(null);

    }
    private void loadBanTheoTang() {
        panelBan.removeAll();
        String tenTangChon = (String) cboTang.getSelectedItem();
        
        if (tenTangChon == null) return;
        String maTang = null;
        for (Model.Tang t : danhSachTang) {
            if (t.getTenTang().equals(tenTangChon)) {
                maTang = t.getMaTang();
            }
            
        }
        for (Model.Ban ban : danhSachBan) {
            if (ban.getMaTang().equals(maTang)) {
                javax.swing.JButton btnBan = new javax.swing.JButton(ban.getSoBan());
                if ("Đang sử dụng".equalsIgnoreCase(ban.getTrangThai())) {
                    btnBan.setBackground(java.awt.Color.RED);
                    btnBan.setForeground(java.awt.Color.WHITE);
                    
                } else {
                    btnBan.setBackground(java.awt.Color.BLUE);
                    btnBan.setForeground(java.awt.Color.WHITE);
                }
                
                btnBan.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                btnBan.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        xuLyChonBan(ban);
                    }
                });
                panelBan.add(btnBan);
            }
        }
        panelBan.revalidate();
        panelBan.repaint();
    }
    private void xuLyChonBan(Model.Ban ban) {
        Oder_QLTable order_table = new Oder_QLTable(ban.getMaBan());
        this.setVisible(false);
        order_table.setVisible(true);
        JOptionPane.showMessageDialog(this, "Bạn chọn bàn số: " + ban.getSoBan());
        
}
    private void themTangMoi() {
    String tenTang = javax.swing.JOptionPane.showInputDialog(this, "Nhập tên tầng mới:");
    if (tenTang != null && !tenTang.trim().isEmpty()) {
        String maTang = "T" + (danhSachTang.size() + 1);
        Model.Tang tang = new Model.Tang(maTang, tenTang);
        DAO.TangDAO.themTang(tang);
        hienThiDanhSachTang();
        }
    }

    private void themBanMoi() {
        String soBanInput = JOptionPane.showInputDialog(this, "Nhập số bàn (ví dụ: 101, 210):");
        if (soBanInput == null || soBanInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số bàn!");
            return;
        }

        int soBan;
        try {
            soBan = Integer.parseInt(soBanInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số bàn phải là số nguyên hợp lệ!");
            return;
        }

        // Lấy tầng đã chọn từ combobox
        String tenTang = (String) cboTang.getSelectedItem();
        if (tenTang == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng trước!");
            return;
        }

        String maTang = null;
        for (Model.Tang t : danhSachTang) {
            if (t.getTenTang().equals(tenTang)) {
                maTang = t.getMaTang();
                break;
            }
        }

        if (maTang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tầng!");
            return;
        }

        // Tạo mã bàn (MaBan) dựa trên số bàn
        String maBan = "B" + soBan; 
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sqlCheck = "SELECT * FROM Ban WHERE MaBan = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, maBan);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Mã bàn " + maBan + " đã tồn tại!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã bàn!");
            return;
        }

        if (BanDAO.themBan(maBan, soBan, maTang)) {
            danhSachBan = BanDAO.layDanhSachBan();
            loadBanTheoTang();
            JOptionPane.showMessageDialog(this, "Đã thêm bàn số " + soBan + " (Mã bàn: " + maBan + ") thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm bàn thất bại!");
        }

        }
    public void thietLapBanTrong(String maBan) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Tìm MaHD hiện tại của bàn (nếu có)
            String sql = "SELECT MaHD FROM SuDungBan WHERE MaBan = ? AND ThoiGianKetThuc IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maBan);
            String maHD = null;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maHD = rs.getString("MaHD");
                }
            }

            // Cập nhật trạng thái bàn thành "Đã rời"
            if (BanDAO.capNhatTrangThaiBan(maBan, "Đã rời")) {
                // Nếu có hóa đơn, cập nhật SuDungBan
                if (maHD != null) {
                    String sqlUpdate = "UPDATE SuDungBan SET ThoiGianKetThuc = ?, TrangThai = 'Đã rời' WHERE MaBan = ? AND MaHD = ? AND ThoiGianKetThuc IS NULL";
                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    psUpdate.setString(2, maBan);
                    psUpdate.setString(3, maHD);
                    int rowsAffected = psUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Đã thiết lập bàn " + maBan + " thành bàn trống, giữ hóa đơn " + maHD);
                        
                    } else {
                        System.out.println("Không tìm thấy bản ghi SuDungBan để cập nhật với MaBan = " + maBan + " và MaHD = " + maHD);
                    }
                } else {
                    System.out.println("Không có hóa đơn để cập nhật trạng thái bàn " + maBan);
                    
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thiết lập bàn trống: " + e.getMessage());
        }
    }



        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnAllBanTrong = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cboTang = new javax.swing.JComboBox<>();
        panelBan = new javax.swing.JPanel();
        btnThietLapBanTrong = new javax.swing.JButton();
        btnThemBan = new javax.swing.JButton();
        btnThemTang = new javax.swing.JButton();
        btnXoaBan = new javax.swing.JButton();
        btnXoaTang = new javax.swing.JButton();
        btnSuaTenBan = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setBackground(new java.awt.Color(255, 0, 0));
        jTextField1.setFocusable(false);

        jLabel1.setText("Bàn đã có khách ");

        jTextField2.setBackground(new java.awt.Color(0, 0, 255));
        jTextField2.setFocusable(false);

        jLabel2.setText("Bàn trống");

        btnAllBanTrong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAllBanTrong.setText("Reset bàn (Trống)");
        btnAllBanTrong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllBanTrongActionPerformed(evt);
            }
        });

        btnQuayLai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQuayLai.setText("Quay Lại");
        btnQuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnAllBanTrong, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnQuayLai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnQuayLai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(btnAllBanTrong, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap())
        );

        cboTang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4" }));
        cboTang.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboTang, 0, 194, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(cboTang, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 443, Short.MAX_VALUE))
        );

        panelBan.setLayout(new java.awt.GridLayout(5, 0, 5, 5));

        btnThietLapBanTrong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThietLapBanTrong.setText("Thiết lập bàn trống");
        btnThietLapBanTrong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThietLapBanTrongActionPerformed(evt);
            }
        });

        btnThemBan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemBan.setText("Thêm bàn");
        btnThemBan.setPreferredSize(new java.awt.Dimension(90, 23));
        btnThemBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemBanActionPerformed(evt);
            }
        });

        btnThemTang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemTang.setText("Thêm tầng ");
        btnThemTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemTangActionPerformed(evt);
            }
        });

        btnXoaBan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaBan.setText("Xóa bàn");
        btnXoaBan.setPreferredSize(new java.awt.Dimension(90, 23));
        btnXoaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaBanActionPerformed(evt);
            }
        });

        btnXoaTang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaTang.setText("Xóa Tầng");
        btnXoaTang.setPreferredSize(new java.awt.Dimension(90, 23));
        btnXoaTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTangActionPerformed(evt);
            }
        });

        btnSuaTenBan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaTenBan.setText("Sửa tên bàn");
        btnSuaTenBan.setPreferredSize(new java.awt.Dimension(90, 23));
        btnSuaTenBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaTenBanActionPerformed(evt);
            }
        });

        jMenu1.setText("≡");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Order ");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Table");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(panelBan, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThietLapBanTrong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThemBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSuaTenBan, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemTang)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaTang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThietLapBanTrong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThemBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaTang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnXoaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemTang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSuaTenBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTangActionPerformed

        loadBanTheoTang();
    }//GEN-LAST:event_cboTangActionPerformed

    private void btnThemBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemBanActionPerformed
        themBanMoi();
    }//GEN-LAST:event_btnThemBanActionPerformed

    private void btnThemTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemTangActionPerformed
        themTangMoi();
    }//GEN-LAST:event_btnThemTangActionPerformed

    private void btnThietLapBanTrongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThietLapBanTrongActionPerformed
        String soBan = JOptionPane.showInputDialog(this, "Nhập số bàn muốn chuyển về trạng thái trống:");
        if (soBan == null || soBan.trim().isEmpty()) {
            return; // Người dùng hủy hoặc nhập rỗng
        }

        String tenTang = (String) cboTang.getSelectedItem();
        if (tenTang == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng trước!");
            return;
        }

        String maTang = null;
        for (Model.Tang t : danhSachTang) {
            if (t.getTenTang().equals(tenTang)) {
                maTang = t.getMaTang();
                break;
            }
        }

        if (maTang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tầng!");
            return;
        }

        // Tìm bàn cần thiết lập trống
        Ban banThietLap = null;
        for (Model.Ban b : danhSachBan) {
            if (b.getSoBan().equals(soBan) && b.getMaTang().equals(maTang)) {
                banThietLap = b;
                break;
            }
        }

        if (banThietLap == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bàn số " + soBan + " ở tầng " + tenTang + "!");
            return;
        }

        // Gọi phương thức thietLapBanTrong với maBan
        thietLapBanTrong(banThietLap.getMaBan());

        // Cập nhật danh sách bàn và giao diện
        danhSachBan = DAO.BanDAO.layDanhSachBan();
        loadBanTheoTang();
    }//GEN-LAST:event_btnThietLapBanTrongActionPerformed

    private void btnAllBanTrongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllBanTrongActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thiết lập tất cả bàn thành trống?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                for (Model.Ban ban : danhSachBan) {
                    thietLapBanTrong(ban.getMaBan()); // Gọi phương thức để thiết lập từng bàn
                }
                // Cập nhật danh sách bàn và giao diện
                danhSachBan = DAO.BanDAO.layDanhSachBan();
                loadBanTheoTang();
                JOptionPane.showMessageDialog(this, "Đã thiết lập tất cả bàn thành trống!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thiết lập tất cả bàn trống: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnAllBanTrongActionPerformed

    private void btnXoaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaBanActionPerformed
        String soBan = JOptionPane.showInputDialog(this, "Nhập số bàn muốn xóa:");
        if (soBan == null || soBan.trim().isEmpty()) {
            return;
        }

        String tenTang = (String) cboTang.getSelectedItem();
        if (tenTang == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng trước!");
            return;
        }

        String maTang = null;
        for (Model.Tang t : danhSachTang) {
            if (t.getTenTang().equals(tenTang)) {
                maTang = t.getMaTang();
                break;
            }
        }

        if (maTang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tầng!");
            return;
        }

        Ban banXoa = null;
        for (Model.Ban b : danhSachBan) {
            if (b.getSoBan().equals(soBan) && b.getMaTang().equals(maTang)) {
                banXoa = b;
                break;
            }
        }

        if (banXoa == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bàn số " + soBan + " ở tầng " + tenTang + "!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bàn số " + soBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (BanDAO.xoaBan(banXoa.getMaBan())) {
                JOptionPane.showMessageDialog(this, "Đã xóa bàn số " + soBan + "!");
                danhSachBan = DAO.BanDAO.layDanhSachBan();
                loadBanTheoTang();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa bàn vì bàn đang được sử dụng hoặc có dữ liệu liên quan!");
            }
        }
    }//GEN-LAST:event_btnXoaBanActionPerformed

    private void btnXoaTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTangActionPerformed
            String tenTang = (String) cboTang.getSelectedItem();
        if (tenTang == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng trước!");
            return;
        }

        String maTang = null;
        for (Model.Tang t : danhSachTang) {
            if (t.getTenTang().equals(tenTang)) {
                maTang = t.getMaTang();
                break;
            }
        }

        if (maTang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tầng!");
            return;
        }

        boolean coBan = false;
        for (Model.Ban b : danhSachBan) {
            if (b.getMaTang().equals(maTang)) {
                coBan = true;
                break;
            }
        }

        if (coBan) {
            JOptionPane.showMessageDialog(this, "Tầng " + tenTang + " vẫn còn bàn, không thể xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tầng " + tenTang + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (TangDAO.xoaTang(maTang)) {
                JOptionPane.showMessageDialog(this, "Đã xóa tầng " + tenTang + "!");
                hienThiDanhSachTang();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tầng!");
            }
        }
    }//GEN-LAST:event_btnXoaTangActionPerformed

    private void btnSuaTenBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaTenBanActionPerformed
        String maBanCu = JOptionPane.showInputDialog(this, "Nhập mã bàn cần sửa (ví dụ: B201):");
        if (maBanCu == null || maBanCu.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã bàn!");
            return;
        }

        // Kiểm tra xem MaBan có tồn tại không
        Model.Ban banCanSua = null;
        for (Model.Ban ban : danhSachBan) {
            if (ban.getMaBan().equals(maBanCu)) {
                banCanSua = ban;
                break;
            }
        }

        if (banCanSua == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã bàn " + maBanCu + "!");
            return;
        }

        // Yêu cầu người dùng nhập số bàn mới
        String soBanMoiInput = JOptionPane.showInputDialog(this, "Nhập số bàn mới (ví dụ: 202):");
        if (soBanMoiInput == null || soBanMoiInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số bàn mới!");
            return;
        }

        int soBanMoi;
        try {
            soBanMoi = Integer.parseInt(soBanMoiInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số bàn phải là số nguyên hợp lệ!");
            return;
        }

        // Lấy tầng từ bàn hiện tại
        String maTang = banCanSua.getMaTang();

        // Cập nhật số bàn và mã bàn
        if (BanDAO.suaBan(maBanCu, soBanMoi, maTang)) {
            danhSachBan = BanDAO.layDanhSachBan();
            loadBanTheoTang();
            JOptionPane.showMessageDialog(this, "Đã sửa bàn từ " + maBanCu + " (số " + banCanSua.getSoBan() + ") thành " + "B" + soBanMoi + " (số " + soBanMoi + ")!");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa bàn thất bại! Có thể bàn đang được sử dụng hoặc mã bàn mới đã tồn tại.");
        }

    }//GEN-LAST:event_btnSuaTenBanActionPerformed

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLaiActionPerformed
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có muốn quay lại?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new TrangChuNhanVien().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnQuayLaiActionPerformed

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
            java.util.logging.Logger.getLogger(Table_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Table_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Table_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Table_QLTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Table_QLTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAllBanTrong;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnSuaTenBan;
    private javax.swing.JButton btnThemBan;
    private javax.swing.JButton btnThemTang;
    private javax.swing.JButton btnThietLapBanTrong;
    private javax.swing.JButton btnXoaBan;
    private javax.swing.JButton btnXoaTang;
    private javax.swing.JComboBox<String> cboTang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel panelBan;
    // End of variables declaration//GEN-END:variables
}
