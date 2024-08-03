/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AutoPasswordEncryption;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author NITRO 5
 */
public class DoiMatKhauFrame extends javax.swing.JFrame {
    ImageIcon icon;
    public void doiIcon() {
        icon = new ImageIcon("src/main/resources/images/Technology.png");
        setIconImage(icon.getImage());
    }
    Connection ketNoi;

    /**
     * Creates new form DoiMatKhauFrame
     */
    public DoiMatKhauFrame() {
        initComponents();
        init();

    }

    void init() {
        setLocationRelativeTo(null);
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
    }

    public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433; databaseName = DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true;";// them doan cuoi vao url
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }

    private boolean checkUpdatePassword() {
        boolean isValid = true;
        if (txtMaNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tài khoản không được bỏ trống!");
            isValid = false;
        }

        // Check if old password is empty
        if (txtPassword.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được bỏ trống!");
            isValid = false;
        }

        // Check if new password is empty
        if (txtnewpass.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!");
            isValid = false;
        }

        // Check if confirm password is empty
        if (txtnhaplaipass.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không được bỏ trống!");
            isValid = false;
        } else if (!txtnhaplaipass.getText().equals(txtnewpass.getText())) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp!");
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtnewpass = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        txtnhaplaipass = new javax.swing.JPasswordField();
        btndmk = new javax.swing.JButton();
        lblThoat = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtMaNV = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Key.png"))); // NOI18N
        jLabel1.setText("Đổi mật khẩu");

        jLabel2.setText("Mã nhân viên");

        jLabel4.setText("Mật khẩu mới");

        jLabel5.setText("Nhập lại mật khẩu");

        btndmk.setText("Đổi mật khẩu");
        btndmk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndmkActionPerformed(evt);
            }
        });

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        jLabel3.setText("Mật khẩu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(btndmk, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)))
                        .addGap(0, 43, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnhaplaipass)
                            .addComponent(txtnewpass)
                            .addComponent(txtPassword)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(lblThoat)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtMaNV))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnewpass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnhaplaipass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btndmk)
                .addGap(13, 13, 13)
                .addComponent(lblThoat)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame().setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void btndmkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndmkActionPerformed

        if (checkUpdatePassword()) {
            String username = txtMaNV.getText().trim();
            String oldPassword = new String(txtPassword.getPassword()); // Sử dụng String cho mật khẩu
            String newPassword = new String(txtnewpass.getPassword());

            try {
                ketNoiCsdl(); // Kết nối cơ sở dữ liệu

                // Step 1: Check if the old password matches the one in the database
                String sqlCheck = "SELECT MAT_KHAU FROM NHAN_VIEN WHERE ID_NV = ?";
                try (PreparedStatement statementCheck = ketNoi.prepareStatement(sqlCheck)) {
                    statementCheck.setString(1, username);
                    ResultSet rs = statementCheck.executeQuery();

                    if (rs.next()) {
                        String currentPasswordHash = rs.getString("MAT_KHAU");

                        // Kiểm tra mật khẩu cũ
                        if (AutoPasswordEncryption.checkPassword(oldPassword, currentPasswordHash)) {
                            // Step 2: Update the password
                            String newPasswordHash = AutoPasswordEncryption.hashPassword(newPassword);
                            String sqlUpdate = "UPDATE NHAN_VIEN SET MAT_KHAU = ? WHERE ID_NV = ?";
                            try (PreparedStatement statementUpdate = ketNoi.prepareStatement(sqlUpdate)) {
                                statementUpdate.setString(1, newPasswordHash);
                                statementUpdate.setString(2, username);

                                int rowsUpdated = statementUpdate.executeUpdate();
                                if (rowsUpdated > 0) {
                                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công.");
                                    txtMaNV.setText("");
                                    txtPassword.setText("");
                                    txtnewpass.setText("");
                                    txtnhaplaipass.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại.");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại.");
                    }

                    rs.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật mật khẩu: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                Logger.getLogger(DoiMatKhauFrame.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if (ketNoi != null && !ketNoi.isClosed()) {
                        ketNoi.close();
                    }
                } catch (SQLException e) {
                    Logger.getLogger(DoiMatKhauFrame.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đúng thông tin vào các trường.");
    }    }//GEN-LAST:event_btndmkActionPerformed

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
            java.util.logging.Logger.getLogger(DoiMatKhauFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhauFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhauFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoiMatKhauFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DoiMatKhauFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndmk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtnewpass;
    private javax.swing.JPasswordField txtnhaplaipass;
    // End of variables declaration//GEN-END:variables
}
