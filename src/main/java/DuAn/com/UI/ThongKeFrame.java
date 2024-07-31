/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.Hide_Password;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NITRO 5
 */
public class ThongKeFrame extends javax.swing.JFrame {

    String url = "jdbc:sqlserver://localhost:1433;database=DU_AN_1_GROUP1_DIENMAY3;integratedSecurity=false;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";

    /**
     * Creates new form ThongKeFrame
     */
    public ThongKeFrame() {
        initComponents();
        init();
        loadTable_Kho();
        txtMaxDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadTable_HDCT();
                    loadTable_Doanhso();
                }
            }
        });

    }

    private void loadTable_HDCT() {
        DefaultTableModel model = (DefaultTableModel) tblHDCT_TK.getModel();
        model.setRowCount(0); // Clear all rows in the table before filling it with new data
        String minDate = txtMinDate.getText();
        String maxDate = txtMaxDate.getText();
        String query = "SELECT A.ID_HOA_DON, SUM(A.SO_LUONG) AS SL_BAN, B.NGAY_HOA_DON, B.TONG_TIEN "
                + "FROM CHI_TIET_HOA_DON A INNER JOIN HOA_DON B ON A.ID_HOA_DON = B.ID_HOA_DON "
                + "WHERE B.NGAY_HOA_DON BETWEEN ? AND ? "
                + "GROUP BY A.ID_HOA_DON, B.NGAY_HOA_DON, B.TONG_TIEN";
        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, minDate);
            pst.setString(2, maxDate);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String ID = rs.getString("ID_HOA_DON");
                    String soLuongBan = rs.getString("SL_BAN");
                    String ngayBan = rs.getString("NGAY_HOA_DON");
                    String tongTien = rs.getString("TONG_TIEN");

                    model.addRow(new Object[]{ID, soLuongBan, ngayBan, tongTien});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị HDCT thống kê: " + ex.getMessage());
        }
    }

    private void loadTable_Doanhso() {
        DefaultTableModel model = (DefaultTableModel) tblDoanhSo.getModel();
        model.setRowCount(0); // Clear all rows in the table before filling it with new data
        String minDate = txtMinDate.getText();
        String maxDate = txtMaxDate.getText();
        String query = "SELECT A.ID_NV, TEN_NV, COUNT(ID_SP) AS TONG_SP_BAN,SUM(SO_LUONG) AS TONG_SL_BAN, SUM(TONG_TIEN) AS TONG_DOANHTHU FROM NHAN_VIEN A INNER JOIN HOA_DON B ON A.ID_NV = B.ID_NV INNER JOIN CHI_TIET_HOA_DON C ON C.ID_HOA_DON = B.ID_HOA_DON WHERE NGAY_HOA_DON BETWEEN ? AND ? GROUP BY A.ID_NV, TEN_NV, TONG_TIEN";
        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, minDate);
            pst.setString(2, maxDate);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String ID = rs.getString("ID_NV");
                    String soLuongBan = rs.getString("TEN_NV");
                    String ngayBan = rs.getString("TONG_SP_BAN");
                    String tongTien = rs.getString("TONG_SP_BAN");
                                        String tongDOANHTHU = rs.getString("TONG_DOANHTHU");


                    model.addRow(new Object[]{ID, soLuongBan, ngayBan, tongTien, tongDOANHTHU});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị HDCT thống kê: " + ex.getMessage());
        }
    }

    private void loadTable_Kho() {
        DefaultTableModel model = (DefaultTableModel) tblKho_TK.getModel();
        model.setRowCount(0); // Clear all rows in the table before filling it with new data
        String query = "SELECT ID_SP, TEN_SP, SUM(SL_TONKHO) AS SO_TON_KHO FROM SAN_PHAM A GROUP BY ID_SP, TEN_SP";
        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String ID = rs.getString("ID_SP");
                    String name = rs.getString("TEN_SP");
                    String Gender = rs.getString("SO_TON_KHO");

                    // Add a new row to the table with the course information
                    model.addRow(new Object[]{ID, name, Gender});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị người học thống kê: " + ex.getMessage());
        }

        // Apply the password renderer to the password column (assuming it's column 6)
    }

    public void init() {
        setLocationRelativeTo(null);
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });

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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMinDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaxDate = new javax.swing.JTextField();
        lblThoat = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        Kho = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHDCT_TK = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKho_TK = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDoanhSo = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Circle Chart.png"))); // NOI18N
        jLabel1.setText("Thống kê");

        lblGreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GreenPlusOven.png"))); // NOI18N
        lblGreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGreenMouseClicked(evt);
            }
        });

        lblOr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/organOvan.png"))); // NOI18N
        lblOr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOrMouseClicked(evt);
            }
        });

        lblBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BlueOven.png"))); // NOI18N
        lblBlue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBlueMouseClicked(evt);
            }
        });

        jLabel2.setText("Ngày");

        jLabel3.setText("Đến");

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.setText("jLabel7");
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtMinDate, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel3)
                        .addGap(37, 37, 37)
                        .addComponent(txtMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(lblThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblThoat))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel5.setLayout(new java.awt.BorderLayout());

        tblHDCT_TK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐCT", "Số lượng bán", "Ngày bán", "Tổng tiền"
            }
        ));
        jScrollPane1.setViewportView(tblHDCT_TK);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        Kho.addTab("Số lượng bán", jPanel5);

        jPanel6.setLayout(new java.awt.BorderLayout());

        tblKho_TK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID sản phẩm", "Tên sản phẩm", "Số lượng tồn kho"
            }
        ));
        jScrollPane2.setViewportView(tblKho_TK);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        Kho.addTab("Kho", jPanel6);

        jPanel7.setLayout(new java.awt.BorderLayout());

        tblDoanhSo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Tổng sản phẩm bán", "Tổng số lượng bán", "Tổng tiền"
            }
        ));
        jScrollPane3.setViewportView(tblDoanhSo);

        jPanel7.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        Kho.addTab("Doanh số", jPanel7);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Kho)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Kho)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBlue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGreen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOr))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOr)
                    .addComponent(lblGreen)
                    .addComponent(lblBlue)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        this.setExtendedState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_lblGreenMouseClicked

    private void lblOrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOrMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblOrMouseClicked

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked
        if (this.getExtendedState() != LoginFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(LoginFrame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(LoginFrame.NORMAL);
        }
    }//GEN-LAST:event_lblBlueMouseClicked

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame().setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongKeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThongKeFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Kho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTable tblDoanhSo;
    private javax.swing.JTable tblHDCT_TK;
    private javax.swing.JTable tblKho_TK;
    private javax.swing.JTextField txtMaxDate;
    private javax.swing.JTextField txtMinDate;
    // End of variables declaration//GEN-END:variables
}
