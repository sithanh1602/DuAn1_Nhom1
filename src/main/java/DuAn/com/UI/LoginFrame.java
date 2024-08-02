/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;
import CheckForm.AutoPasswordEncryption;
import DuAn.com.UI.staff.HomeFrameStaff;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import static java.awt.Color.red;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 *
 */
public class LoginFrame extends javax.swing.JFrame{

    Connection ketNoi;
    /**
     * Creates new form LoginFrame
     */
    public LoginFrame() {
        initComponents();
        init();
    }

    void init() {
        setLocationRelativeTo(null);
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        chkRemember.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        jLabel5.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        jLabel6.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        jLabel6.setVisible(false);
        txtUser.setBackground(new java.awt.Color(0, 0, 0, 1));
        txtPass.setBackground(new java.awt.Color(0, 0, 0, 1));
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
    }
    
    private void toggleShowPassword() {
        if (txtPass.getEchoChar() == '\u2022') {
            txtPass.setEchoChar((char) 0); // Hiển thị mật khẩu
        }
    }
    
     public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433; databaseName = DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true;";// them doan cuoi vao url
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }
    

private void checkAccount() {
    String username = txtUser.getText();
    char[] passwordChars = txtPass.getPassword();
    String password = new String(passwordChars);

    try {
        ketNoiCsdl();
        String sql = "SELECT CHUC_VU, MAT_KHAU FROM NHAN_VIEN WHERE ID_NV = ?";
        try (PreparedStatement statement = ketNoi.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("MAT_KHAU");
                String chucvu = resultSet.getString("CHUC_VU");

                if (AutoPasswordEncryption.checkPassword(password, storedPassword)) {
                    // Nếu mật khẩu đã mã hóa và hợp lệ
                    handleSuccessfulLogin(chucvu);
                } else {
                    // Mật khẩu chưa được mã hóa, so sánh mật khẩu trực tiếp
                    if (password.equals(storedPassword)) {
                        handleSuccessfulLogin(chucvu);

                        // Mã hóa mật khẩu và cập nhật vào cơ sở dữ liệu
                        String hashedPassword = AutoPasswordEncryption.hashPassword(password);
                        String updateQuery = "UPDATE NHAN_VIEN SET MAT_KHAU = ? WHERE ID_NV = ?";
                        try (PreparedStatement updateStatement = ketNoi.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, hashedPassword);
                            updateStatement.setString(2, username);
                            updateStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật mật khẩu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Tài khoản và mật khẩu không hợp lệ!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!");
            }
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}



private void handleSuccessfulLogin(String chucvu) {
    if (chucvu.equals("Quản lý")) {
        JOptionPane.showMessageDialog(this, "Bạn đã đăng nhập với tư cách Quản Lý!");
        this.dispose();
        new HomeFrame().setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Bạn đã đăng nhập với tư cách Nhân Viên!");
        this.dispose();
        new HomeFrameStaff().setVisible(true);
    }
}
    public boolean checkVali() {
        if (txtUser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn phải nhập tài khoản !");
            return false;
        }
        if (txtPass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn phải nhập mật khẩu !");
            return false;
        }
        return true;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emailSender1 = new com.email.EmailSender();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        chkRemember = new javax.swing.JCheckBox();
        btnDangNhap = new javax.swing.JButton();
        lblOr = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        lblForgot = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/erp.png"))); // NOI18N

        jPanel2.setForeground(new java.awt.Color(153, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setText("Đăng nhập");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 82, -1, -1));

        jLabel2.setText("Tên đăng nhập");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 119, -1, -1));

        txtUser.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(10, 153, 255)));
        jPanel2.add(txtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 141, 239, 24));

        jLabel3.setText("Mật khẩu");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 177, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Eye.png"))); // NOI18N
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eyeExit.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, -1, -1));

        txtPass.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 153, 255)));
        jPanel2.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 199, 239, 28));

        chkRemember.setText("Remember password ?");
        jPanel2.add(chkRemember, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 233, -1, -1));

        btnDangNhap.setText("Đăng nhập");
        btnDangNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDangNhapMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDangNhapMouseExited(evt);
            }
        });
        btnDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangNhapActionPerformed(evt);
            }
        });
        jPanel2.add(btnDangNhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 271, 151, 32));

        lblOr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/organOvan.png"))); // NOI18N
        lblOr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOrMouseClicked(evt);
            }
        });
        jPanel2.add(lblOr, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 6, -1, -1));

        lblGreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GreenPlusOven.png"))); // NOI18N
        lblGreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGreenMouseClicked(evt);
            }
        });
        jPanel2.add(lblGreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 6, -1, -1));

        lblBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BlueOven.png"))); // NOI18N
        lblBlue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBlueMouseClicked(evt);
            }
        });
        jPanel2.add(lblBlue, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 6, -1, -1));

        lblForgot.setText("Forgot password ?");
        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblForgotMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblForgotMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblForgotMouseExited(evt);
            }
        });
        jPanel2.add(lblForgot, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 394, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangNhapMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangNhapMouseEntered
        btnDangNhap.setBackground(Color.GRAY);
    }//GEN-LAST:event_btnDangNhapMouseEntered

    private void btnDangNhapMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangNhapMouseExited
        btnDangNhap.setBackground(Color.WHITE);
    }//GEN-LAST:event_btnDangNhapMouseExited

    private void lblOrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOrMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblOrMouseClicked

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        this.setExtendedState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_lblGreenMouseClicked

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked
        if(this.getExtendedState() != LoginFrame.MAXIMIZED_BOTH){
            this.setExtendedState(LoginFrame.MAXIMIZED_BOTH);
        }else{
            this.setExtendedState(LoginFrame.NORMAL);
        }
    }//GEN-LAST:event_lblBlueMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        jLabel6.setVisible(true);
        jLabel5.setVisible(false);
        if (evt.getClickCount() == 1) { // Nhấp đúp chuột
                    toggleShowPassword();
                }
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        jLabel6.setVisible(false);
        jLabel5.setVisible(true);
        if (txtPass.getEchoChar() != '\u2022') {
            txtPass.setEchoChar('\u2022'); // Ẩn mật khẩu
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void btnDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangNhapActionPerformed
        // TODO add your handling code here:
        if (checkVali()) {
            checkAccount();
        };
    }//GEN-LAST:event_btnDangNhapActionPerformed

    private void lblForgotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblForgotMouseClicked
        dispose();
        new quenMK().setVisible(true);
    }//GEN-LAST:event_lblForgotMouseClicked

    private void lblForgotMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblForgotMouseEntered
        lblForgot.setText("<html><u>Forgot password ?</u></html>");
        lblForgot.setForeground(Color.GRAY);
    }//GEN-LAST:event_lblForgotMouseEntered

    private void lblForgotMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblForgotMouseExited
        lblForgot.setForeground(Color.BLACK);
    }//GEN-LAST:event_lblForgotMouseExited

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
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FlatMacDarkLaf.setup();
        //FlatIntelliJLaf.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangNhap;
    private javax.swing.JCheckBox chkRemember;
    private com.email.EmailSender emailSender1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblForgot;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

}
