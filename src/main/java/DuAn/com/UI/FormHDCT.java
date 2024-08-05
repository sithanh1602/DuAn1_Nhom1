/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NITRO 5
 */
public class FormHDCT extends javax.swing.JFrame {

    DefaultTableModel model = new DefaultTableModel();
    Connection ketNoi;

    /**
     * Creates new form FormHDCT
     */
    public FormHDCT() throws ClassNotFoundException, SQLException {
        initComponents();
        init();
        datTenCot();
        TaiDulieuVaoBang();
    }

    public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433; databaseName = DU_AN_1_GROUP1_DIENMAY2;encrypt=true;trustServerCertificate=true";// them doan cuoi vao url
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }

    // Phương thức để tải dữ liệu dựa trên idHoaDon
    public void init() throws ClassNotFoundException, SQLException {
        setLocationRelativeTo(null);
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblCam.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });

    }

    public void datTenCot() {
        //model.setColumnCount(0);
        model.addColumn("Mã HDCT");
        model.addColumn("Mã HD");
        model.addColumn("Mã sản phẩm");
        model.addColumn("Số lượng");
        model.addColumn("Giá tiền");
        model.addColumn("Tổng tiền");
        tblDataHDCT.setModel(model);
    }

    public void TaiDulieuVaoBang() throws ClassNotFoundException, SQLException {
        //model.setRowCount(0);
        ketNoiCsdl();
        String sql = "SELECT * FROM HOA_DON_CHI_TIET";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        ResultSet ketQua = cauLenh.executeQuery();
        while (ketQua.next() == true) {
            String idHdct = ketQua.getString(1);
            String idHd = ketQua.getString(2);
            String idSp = ketQua.getString(3);
            int soLuong = ketQua.getInt(4);
            int giaTien = ketQua.getInt(5);
            int tongTien = ketQua.getInt(6);

            model.addRow(new Object[]{idHdct, idHd, idSp, soLuong, giaTien, tongTien});
        }
        tblDataHDCT.setModel(model);
        ketNoi.close();
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
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDataHDCT = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        lblCam = new javax.swing.JLabel();
        lblThoat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel2.setText("Mã hoá đơn chi tiết");

        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtSearchMouseReleased(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        tblDataHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblDataHDCT);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5))
                            .addComponent(jLabel2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Bill.png"))); // NOI18N
        jLabel1.setText("Hoá đơn chi tiết");

        lblGreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GreenPlusOven.png"))); // NOI18N
        lblGreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGreenMouseClicked(evt);
            }
        });

        lblBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BlueOven.png"))); // NOI18N
        lblBlue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBlueMouseClicked(evt);
            }
        });

        lblCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/organOvan.png"))); // NOI18N
        lblCam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCamMouseClicked(evt);
            }
        });

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addGap(2, 2, 2)
                        .addComponent(lblCam))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblThoat)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lblGreen)
                    .addComponent(lblBlue)
                    .addComponent(lblCam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblThoat)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame("Mã nhân viên", "Họ và Tên", "Chức vụ").setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        this.setExtendedState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_lblGreenMouseClicked

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked
        if (this.getExtendedState() != LoginFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(LoginFrame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(LoginFrame.NORMAL);
        }
    }//GEN-LAST:event_lblBlueMouseClicked

    private void lblCamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCamMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblCamMouseClicked

    private void txtSearchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseReleased
        
    }//GEN-LAST:event_txtSearchMouseReleased

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        try {
            ketNoiCsdl();
            String sql = "SELECT * FROM HOA_DON_CHI_TIET WHERE ID_HOA_DON_CT like N'%" + txtSearch.getText() + "%' ";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            ResultSet ketQua = cauLenh.executeQuery();
            model.setRowCount(0);
            while (ketQua.next() == true) {
                String idHdct = ketQua.getString(1);
                String idHd = ketQua.getString(2);
                String idSp = ketQua.getString(3);
                int soLuong = ketQua.getInt(4);
                int giaTien = ketQua.getInt(5);
                int tongTien = ketQua.getInt(6);

                model.addRow(new Object[]{idHdct, idHd, idSp, soLuong, giaTien, tongTien});
            }
            tblDataHDCT.setModel(model);
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FormHDCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FormHDCT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSearchKeyReleased

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
            java.util.logging.Logger.getLogger(FormHDCT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormHDCT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormHDCT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormHDCT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FormHDCT().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(FormHDCT.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(FormHDCT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblCam;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblOr1;
    private javax.swing.JLabel lblOr2;
    private javax.swing.JLabel lblOr3;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTable tblDataHDCT;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
