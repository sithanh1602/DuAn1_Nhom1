/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
import CheckForm.CurrencyRenderer;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author NITRO 5
 */
public class HoaDonFrame extends javax.swing.JFrame {
    DefaultTableModel model = new NonEditableTableModel();
    DefaultTableModel model1 = new NonEditableTableModel();
    Connection ketNoi;

    public HoaDonFrame() throws ClassNotFoundException, SQLException {
        initComponents();
        init();
        AddID_Auto addID_Auto = new AddID_Auto();
        addID_Auto.initTextFieldMap(this); // Khởi tạo các JTextField từ lớp NhanVienForm
        addID_Auto.setTextFieldValues();
        addMouseClickListener();
        tenCotBang1();
        tenCotBangChiTiet();
        TaiDulieuVaoBang();
        formatCurrencyColumns(); // Format currency columns
        
        // Add action listener for button
        btnCapNhap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateInvoiceStatus();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(HoaDonFrame.this, "Lỗi khi cập nhật trạng thái hóa đơn.");
                }
            }
        });
    }
    
     private void updateInvoiceStatus() throws ClassNotFoundException, SQLException {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow != -1) {
            String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 0);
            String trangThai = "";
            if (rdoChuaThanhToan.isSelected()) {
                trangThai = "Chưa thanh toán";
            } else if (rdoDaThanhToan.isSelected()) {
                trangThai = "Đã thanh toán";
            } else if (rdoDaThanhToanGiaoHang.isSelected()) {
                trangThai = "Đã thanh toán, đang giao hàng";
            }
            
            ketNoiCsdl();
            String sql = "UPDATE HOA_DON SET TRANG_THAI = ? WHERE ID_HOA_DON = ?";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            cauLenh.setString(1, trangThai);
            cauLenh.setString(2, idHoaDon);
            int rowsUpdated = cauLenh.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(HoaDonFrame.this, "Cập nhật trạng thái hóa đơn thành công.");
                TaiDulieuVaoBang(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(HoaDonFrame.this, "Không tìm thấy hóa đơn.");
            }
            ketNoi.close();
        } else {
            JOptionPane.showMessageDialog(HoaDonFrame.this, "Vui lòng chọn hóa đơn để cập nhật.");
        }
    }
    
     private void formatCurrencyColumns() {
        // Apply CurrencyRenderer to columns with currency values
        tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer()); // Tổng tiền
        tblHoaDon.getColumnModel().getColumn(5).setCellRenderer(new CurrencyRenderer()); // Tiền khách đưa
        tblHoaDon.getColumnModel().getColumn(6).setCellRenderer(new CurrencyRenderer()); // Tiền thừa

        tblHdct.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer()); // Đơn giá
        tblHdct.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer()); // Thành tiền
    }

    public static class NonEditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho phép chỉnh sửa ô
        }
    }

    public void init() {
        setLocationRelativeTo(null);
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {});
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {});
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {});
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {});
    }

    public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }

    public void TaiDulieuVaoBang() throws ClassNotFoundException, SQLException {
        model.setRowCount(0);
        ketNoiCsdl();
        String sql = "SELECT * FROM HOA_DON";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        ResultSet ketQua = cauLenh.executeQuery();
        while (ketQua.next()) {
            String idHD = ketQua.getString(1);
            String idNV = ketQua.getString(2);
            String idKH = ketQua.getString(3);
            String idDate = ketQua.getString(4);
            int tongTien = ketQua.getInt(5);
            int tienKhachDua = ketQua.getInt(6);
            int tienThua = ketQua.getInt(7);
            String GhiChu = ketQua.getString(8);
            String trangThai = ketQua.getString(9);           
            model.addRow(new Object[]{idHD, idNV, idKH, idDate, tongTien, tienKhachDua, tienThua,GhiChu,trangThai});
        }
        tblHoaDon.setModel(model);
        ketNoi.close();
    }

    public void addMouseClickListener() {
        tblHoaDon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tblHoaDon.getSelectedRow();
                    String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 0);
                    try {
                        hienThiChiTietHoaDon(idHoaDon);
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(HoaDonFrame.this, "Lỗi khi hiển thị chi tiết hóa đơn.");
                    }
                }
            }
        });
    }

    public void hienThiChiTietHoaDon(String idHoaDon) throws ClassNotFoundException, SQLException {
        model1.setRowCount(0);
        ketNoiCsdl();
        String sql = "SELECT * FROM CHI_TIET_HOA_DON WHERE ID_HOA_DON = ?";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        cauLenh.setString(1, idHoaDon);
        ResultSet ketQua = cauLenh.executeQuery();
        while (ketQua.next()) {
            String maHD = ketQua.getString(1);
            String maSP = ketQua.getString(2);
            int soLuong = ketQua.getInt(3);
            int donGia = ketQua.getInt(4);
            int thanhTien = ketQua.getInt(5);
            model1.addRow(new Object[]{maHD, maSP, soLuong, donGia, thanhTien});
        }
        tblHdct.setModel(model1);
        ketNoi.close();
    }


    public void tenCotBang1() {
        model.addColumn("Mã hoá đơn");
        model.addColumn("Mã nhân viên");
        model.addColumn("Mã khách hàng");
        model.addColumn("Ngày tạo");
        model.addColumn("Tổng tiền");
        model.addColumn("Tiền khách đưa");
        model.addColumn("Tiền thừa");
        model.addColumn("Ghi chú");
        model.addColumn("Trạng Thái");
        tblHoaDon.setModel(model);

    }

    public void tenCotBangChiTiet() {
        model1.addColumn("Mã hoá đơn");
        model1.addColumn("Mã sản phẩm");
        model1.addColumn("Số lượng");
        model1.addColumn("Đơn giá");
        model1.addColumn("Thành tiền");
        tblHdct.setModel(model1);
    }
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblThoat = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHdct = new javax.swing.JTable();
        rdoChuaThanhToan = new javax.swing.JRadioButton();
        rdoDaThanhToan = new javax.swing.JRadioButton();
        rdoDaThanhToanGiaoHang = new javax.swing.JRadioButton();
        btnCapNhap = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThoat)
                .addContainerGap(271, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblThoat)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Mã khách hàng", "Tên sản phẩm", "Ngày tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(0).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(1).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(2).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Bill.png"))); // NOI18N
        jLabel1.setText("Hoá đơn");

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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tblHdct.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblHdct);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(237, 237, 237))
        );

        buttonGroup1.add(rdoChuaThanhToan);
        rdoChuaThanhToan.setText("Chưa thanh toán");

        buttonGroup1.add(rdoDaThanhToan);
        rdoDaThanhToan.setText("Đã thanh toán");

        buttonGroup1.add(rdoDaThanhToanGiaoHang);
        rdoDaThanhToanGiaoHang.setText("Đã thanh toán, đang giao hàng");

        btnCapNhap.setText("Cập nhập ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCapNhap)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdoChuaThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoDaThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoDaThanhToanGiaoHang)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblOr)
                        .addComponent(lblGreen)
                        .addComponent(lblBlue)))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 268, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoChuaThanhToan)
                    .addComponent(rdoDaThanhToan)
                    .addComponent(rdoDaThanhToanGiaoHang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCapNhap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        
    }//GEN-LAST:event_lblGreenMouseClicked

    private void lblOrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOrMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblOrMouseClicked

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked

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
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new HoaDonFrame().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(HoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(HoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhap;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JRadioButton rdoChuaThanhToan;
    private javax.swing.JRadioButton rdoDaThanhToan;
    private javax.swing.JRadioButton rdoDaThanhToanGiaoHang;
    private javax.swing.JTable tblHdct;
    private javax.swing.JTable tblHoaDon;
    // End of variables declaration//GEN-END:variables
}
