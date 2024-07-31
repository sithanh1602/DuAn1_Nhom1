/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
import CheckForm.ImageRenderer;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author NITRO 5
 */
public class SanPhamForm extends javax.swing.JFrame {

    DefaultTableModel model = new DefaultTableModel();
    DefaultComboBoxModel model_box = new DefaultComboBoxModel();
    DefaultComboBoxModel model_box1 = new DefaultComboBoxModel();
    Connection ketNoi;
    String link;

    /**
     * Creates new form SanPhamForm
     */
    public SanPhamForm() throws ClassNotFoundException, SQLException {
        initComponents();
        init();
        listenCBo();
        AddID_Auto addID_Auto = new AddID_Auto();
        addID_Auto.initTextFieldMap(this); // Khởi tạo các JTextField từ lớp NhanVienForm
        addID_Auto.setTextFieldValues(); // Đặt giá trị và tạo mã tự động nếu có
        // Thiết lập model và renderer cho bảng
        model = (DefaultTableModel) tblSp.getModel();
        tblSp.getColumn("Images").setCellRenderer(new ImageRenderer());

        // Thiết lập model và renderer cho bảng
        tblSp.getColumn("Images").setCellRenderer(new ImageRenderer());

        // Thiết lập kích thước cột
        setColumnWidth(tblSp.getColumn("Images"), 400); // Cập nhật kích thước cột hình ảnh
        setColumnWidth(tblSp.getColumn("Mã sãn phẩm"), 70);
        setColumnWidth(tblSp.getColumn("Tên sản phẩm"), 160);
        setColumnWidth(tblSp.getColumn("Mã loại sản phẩm"), 70);
        setColumnWidth(tblSp.getColumn("Mã nhà cung cấp"), 70);
        setColumnWidth(tblSp.getColumn("Giá tiền"), 70);
        setColumnWidth(tblSp.getColumn("Số lượng tồn kho"), 70);

        // Thiết lập kích thước hàng và font chữ
        tblSp.setRowHeight(100); // Thay đổi chiều cao của hàng
    }

    public void init() throws ClassNotFoundException, SQLException {
        setLocationRelativeTo(null);
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        txtMancc.setEnabled(false);
        txtMalsp.setEnabled(false);
        datTenCot();
        TaiDulieuVaoBang();
        DoDuLieuLenComboBox();
        DoDuLieuLenComboBoxNcc();

    }
    
     private void setColumnWidth(TableColumn column, int width) {
        if (column != null) {
            column.setMinWidth(width);
            column.setMaxWidth(width);
            column.setPreferredWidth(width);
        }
    }

    public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433; databaseName = DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true";// them doan cuoi vao url
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }

    public void datTenCot() {
        //model.setColumnCount(0);
        model.addColumn("Mã sãn phẩm");
        model.addColumn("Tên sản phẩm");
        model.addColumn("Mã loại sản phẩm");
        model.addColumn("Mã nhà cung cấp");
        model.addColumn("Giá tiền");
        model.addColumn("Số lượng tồn kho");
        model.addColumn("Images");
        tblSp.setModel(model);
    }

    public void DoDuLieuLenComboBox() {
        model_box.removeAllElements();
        try {
            ketNoiCsdl();
            String sql = "SELECT TEN_LOAI_SP FROM LOAI_SP";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            ResultSet ketQua = cauLenh.executeQuery();
            while (ketQua.next() == true) {
                String id = ketQua.getString(1);
                model_box.addElement(id);
            }
            cboLoaiSP.setModel(model_box);
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DoDuLieuLenComboBoxNcc() {
        model_box1.removeAllElements();
        try {
            ketNoiCsdl();
            String sql = "SELECT TEN_NHA_CC FROM NHA_CUNG_CAP";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            ResultSet ketQua = cauLenh.executeQuery();
            while (ketQua.next() == true) {
                String id = ketQua.getString(1);
                model_box1.addElement(id);
            }
            cboNhaCC.setModel(model_box1);
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TaiDulieuVaoBang() throws ClassNotFoundException, SQLException {
        model.setRowCount(0);
        ketNoiCsdl();
        String sql = "SELECT * FROM SAN_PHAM";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        ResultSet ketQua = cauLenh.executeQuery();
        while (ketQua.next()) {
            String idSP = ketQua.getString(1);
            String tenSp = ketQua.getString(2);
            String idLsp = ketQua.getString(3);
            String idNcc = ketQua.getString(4);
            int giaTien = ketQua.getInt(5);
            int soLuong = ketQua.getInt(6);
            String link = ketQua.getString(7); // Đường dẫn đến hình ảnh
            model.addRow(new Object[]{idSP, tenSp, idLsp, idNcc, giaTien, soLuong, link});
        }
        tblSp.setModel(model);
        ketNoi.close();
    }

    private void displayImage(String imagePath) {
        // Clear lblIMG to refresh
        lblPic.setIcon(null);
        // Check if the image path is not empty
        if (!imagePath.isEmpty()) {
            // Create ImageIcon from the image path
            ImageIcon icon = new ImageIcon(imagePath);
            icon.setImage(icon.getImage().getScaledInstance(lblPic.getWidth(), lblPic.getHeight(), Image.SCALE_SMOOTH));
            // Set the ImageIcon on lblIMG
            lblPic.setIcon(icon);
        }
    }

    public void clear() {
        txtIDSP.setText("");
        txtTenSP.setText("");
        txtMalsp.setText("");
        txtMancc.setText("");
        txtSoLuong.setText("");
        txtGiaTien.setText("");
        lblPic.setIcon(null);
    }

    public void listenCBo() {
        tblSp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectedRow = tblSp.getSelectedRow();
                if (selectedRow < 0) {
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) tblSp.getModel();
                txtIDSP.setText(String.valueOf(model.getValueAt(selectedRow, 0)));
                txtTenSP.setText(String.valueOf(model.getValueAt(selectedRow, 1)));
                txtMalsp.setText(String.valueOf(model.getValueAt(selectedRow, 2)));
                txtMancc.setText(String.valueOf(model.getValueAt(selectedRow, 3)));
                txtGiaTien.setText(String.valueOf(model.getValueAt(selectedRow, 4)));
                txtSoLuong.setText(String.valueOf(model.getValueAt(selectedRow, 5)));
                String imagePath = tblSp.getValueAt(selectedRow, 6).toString();
                displayImage(imagePath);

                // Get the selected supplier ID
                String selectedSupplierID = String.valueOf(model.getValueAt(selectedRow, 3));

                try {
                    ketNoiCsdl();
                    String sql = "SELECT TEN_NHA_CC FROM NHA_CUNG_CAP WHERE ID_NHA_CC = ?";
                    PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
                    cauLenh.setString(1, selectedSupplierID);
                    ResultSet ketQua = cauLenh.executeQuery();
                    if (ketQua.next()) {
                        String supplierName = ketQua.getString(1);
                        cboNhaCC.setSelectedItem(supplierName);
                    }
                    ketNoi.close();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public boolean kiemTraRong() {
        if (txtMalsp.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Lấy mã loại sản phẩm bằng cách chọn tên loại sản phẩm!");
            return false;
        }
        if (txtTenSP.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Chưa nhập tên sản phẩm!");
            return false;
        }
        if (txtMancc.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Lấy mã nhà cung cấp bằng cách chọn tên nhà cung cấp!");
            return false;
        }

        if (txtSoLuong.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá tiền!");
            return false;
        }
        try {
            Integer.parseInt(txtGiaTien.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số!");
            return false;
        }
        if (txtSoLuong.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Chưa nhập số lượng kho!");
            return false;
        }
        try {
            Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số!");
            return false;
        }
        return true;
    }

    public void fillBtn() {
        int selectedRow = tblSp.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblSp.getModel();
        txtIDSP.setText(String.valueOf(model.getValueAt(selectedRow, 0)));
        txtTenSP.setText(String.valueOf(model.getValueAt(selectedRow, 1)));
        txtMalsp.setText(String.valueOf(model.getValueAt(selectedRow, 2)));
        txtMancc.setText(String.valueOf(model.getValueAt(selectedRow, 3)));
        txtGiaTien.setText(String.valueOf(model.getValueAt(selectedRow, 4)));
        txtSoLuong.setText(String.valueOf(model.getValueAt(selectedRow, 5)));
        String imagePath = tblSp.getValueAt(selectedRow, 6).toString();
        displayImage(imagePath);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboLoaiSP = new javax.swing.JComboBox<>();
        btnThemLoaiSP = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblPic = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboNhaCC = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtGiaTien = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtIDSP = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtMancc = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnFirts = new javax.swing.JButton();
        lblThoat = new javax.swing.JLabel();
        txtMalsp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSp = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/product - Copy.png"))); // NOI18N
        jLabel1.setText("Sản phẩm");

        lblBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BlueOven.png"))); // NOI18N
        lblBlue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBlueMouseClicked(evt);
            }
        });

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

        jLabel2.setText("Loại sản phẩm");

        cboLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiSPActionPerformed(evt);
            }
        });

        btnThemLoaiSP.setText("Thêm");
        btnThemLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaiSPActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ảnh"));

        lblPic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPicMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPic, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setText("Nhà cung cấp");

        cboNhaCC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNhaCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNhaCCActionPerformed(evt);
            }
        });

        jLabel3.setText("Tên sản phẩm");

        jLabel5.setText("Giá tiền");

        jLabel6.setText("Số lượng tồn kho");

        jLabel8.setText("Mã sản phẩm");

        jLabel9.setText("Mã nhà cung cấp");

        txtMancc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManccKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtManccKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenSP)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addGap(29, 29, 29))
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(txtIDSP, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaTien)
                            .addComponent(txtSoLuong))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cboNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMancc)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIDSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMancc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiaTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fix.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/remove.png"))); // NOI18N
        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reset.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/last.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/prev.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnFirts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/first.png"))); // NOI18N
        btnFirts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirtsActionPerformed(evt);
            }
        });

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.setText("jLabel7");
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btnThem))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 27, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnFirts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnSua)
                        .addGap(35, 35, 35)
                        .addComponent(btnXoa)
                        .addGap(36, 36, 36)
                        .addComponent(btnMoi)
                        .addGap(0, 27, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnFirts, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLast)
                            .addComponent(btnNext)
                            .addComponent(btnPrev)))
                    .addComponent(lblThoat, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jLabel10.setText("Mã loại sản phẩm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMalsp))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(162, 162, 162)
                                .addComponent(jLabel10)))
                        .addGap(18, 18, 18)
                        .addComponent(btnThemLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemLoaiSP)
                    .addComponent(txtMalsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tblSp.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSp);

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblOr)
                        .addComponent(lblGreen)
                        .addComponent(lblBlue))
                    .addComponent(jLabel1))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked
        if (this.getExtendedState() != LoginFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(LoginFrame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(LoginFrame.NORMAL);
        }
    }//GEN-LAST:event_lblBlueMouseClicked

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        this.setExtendedState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_lblGreenMouseClicked

    private void lblOrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOrMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblOrMouseClicked

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        int ViTri = tblSp.getSelectedRow();
        if (ViTri == 0) {
            btnPrev.setEnabled(false);
            btnFirts.setEnabled(false);
        } else {
            ViTri = ViTri - 1;
        }
        tblSp.setRowSelectionInterval(ViTri, ViTri);
        btnNext.setEnabled(true);
        btnLast.setEnabled(true);
        fillBtn();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame().setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void btnThemLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiSPActionPerformed
        try {
            dispose();
            new LoaiSPFrame().setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnThemLoaiSPActionPerformed

    private void lblPicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPicMouseClicked
        JFileChooser chonFile = new JFileChooser("src\\HinhAnh\\");
        chonFile.showOpenDialog(null);
        File f = chonFile.getSelectedFile();
        if (f != null) {
            link = "src\\HinhAnh\\" + f.getName();
            ImageIcon hinhAnh = new ImageIcon(new ImageIcon(link).getImage().getScaledInstance(lblPic.getWidth(), lblPic.getHeight(), Image.SCALE_SMOOTH));
            lblPic.setIcon(hinhAnh);

        }
    }//GEN-LAST:event_lblPicMouseClicked

    private void cboNhaCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNhaCCActionPerformed
        try {
            ketNoiCsdl();
            String sql = "SELECT ID_NHA_CC FROM NHA_CUNG_CAP WHERE TEN_NHA_CC = ?";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            cauLenh.setString(1, cboNhaCC.getSelectedItem().toString());
            ResultSet ketQua = cauLenh.executeQuery();
            while (ketQua.next() == true) {
                txtMancc.setText(ketQua.getString(1));
            }
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cboNhaCCActionPerformed

    private void tblSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSpMouseClicked
        int selectedRow = tblSp.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblSp.getModel();
        txtIDSP.setText(String.valueOf(model.getValueAt(selectedRow, 0)));
        txtTenSP.setText(String.valueOf(model.getValueAt(selectedRow, 1)));
        txtMalsp.setText(String.valueOf(model.getValueAt(selectedRow, 2)));
        txtMancc.setText(String.valueOf(model.getValueAt(selectedRow, 3)));
        txtGiaTien.setText(String.valueOf(model.getValueAt(selectedRow, 4)));
        txtSoLuong.setText(String.valueOf(model.getValueAt(selectedRow, 5)));
        String imagePath = tblSp.getValueAt(selectedRow, 6).toString();
        displayImage(imagePath);
    }//GEN-LAST:event_tblSpMouseClicked

    private void cboLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiSPActionPerformed
        try {
            ketNoiCsdl();
            String sql = "SELECT ID_LSP FROM LOAI_SP WHERE TEN_LOAI_SP = ?";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            cauLenh.setString(1, cboLoaiSP.getSelectedItem().toString());
            ResultSet ketQua = cauLenh.executeQuery();
            while (ketQua.next() == true) {
                txtMalsp.setText(ketQua.getString(1));
            }
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cboLoaiSPActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        try {
            ketNoiCsdl();
            String sql = "SELECT * FROM SAN_PHAM WHERE TEN_SP like N'%" + txtSearch.getText() + "%' ";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            ResultSet ketQua = cauLenh.executeQuery();
            model.setRowCount(0);
            while (ketQua.next() == true) {
                String idSP = ketQua.getString(1);
                String tenSp = ketQua.getString(2);
                String idLsp = ketQua.getString(3);
                String idNcc = ketQua.getString(4);
                int giaTien = ketQua.getInt(5);
                int soLuong = ketQua.getInt(6);
                String link = ketQua.getString(7);
                model.addRow(new Object[]{idSP, tenSp, idLsp, idNcc, giaTien, soLuong, link});
            }
            tblSp.setModel(model);
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoaiSPFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSPFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            //kiểm tra mã không trùng, kiểm tra rổnng
            if (kiemTraRong() == true) {
                ketNoiCsdl();
                String sql = "INSERT INTO SAN_PHAM(ID_SP,TEN_SP,ID_LSP,ID_NHA_CC,GIA,SL_TONKHO,HINH) VALUES(?, ?, ?, ?, ?,?,?)";
                PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
                cauLenh.setString(1, txtIDSP.getText());
                cauLenh.setString(2, txtTenSP.getText());
                cauLenh.setString(3, txtMalsp.getText());
                cauLenh.setString(4, txtMancc.getText());
                cauLenh.setInt(5, Integer.parseInt(txtGiaTien.getText()));
                cauLenh.setInt(6, Integer.parseInt(txtSoLuong.getText()));
                cauLenh.setString(7, link);

                cauLenh.executeUpdate();// có thay đổi thì dùng excuteUpdate (thêm sửa xoá)
                TaiDulieuVaoBang();
                ketNoi.close();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        try {
            ketNoiCsdl();
            String sql = "UPDATE SAN_PHAM SET TEN_SP=?, ID_LSP=?, ID_NHA_CC=?, GIA=?, SL_TONKHO=?, HINH=? WHERE ID_SP=?";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            cauLenh.setString(1, txtTenSP.getText());
            cauLenh.setString(2, txtMalsp.getText());
            cauLenh.setString(3, txtMancc.getText());
            cauLenh.setInt(4, Integer.parseInt(txtGiaTien.getText()));
            cauLenh.setInt(5, Integer.parseInt(txtSoLuong.getText()));
            cauLenh.setString(6, link);
            cauLenh.setString(7, txtIDSP.getText()); // Tham số ID_SP là tham số cuối cùng

            cauLenh.executeUpdate(); // Có thay đổi thì dùng executeUpdate (thêm, sửa, xoá)
            TaiDulieuVaoBang();
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtManccKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManccKeyReleased

    }//GEN-LAST:event_txtManccKeyReleased

    private void txtManccKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManccKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManccKeyPressed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int ViTri = tblSp.getSelectedRowCount();
        try {
            ketNoiCsdl();
            String sql = "DELETE SAN_PHAM WHERE ID_SP=?";
            PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
            cauLenh.setString(1, txtIDSP.getText());
            cauLenh.executeUpdate();// có thay đổi thì dùng excuteUpdate (thêm sửa xoá)
            TaiDulieuVaoBang();
            JOptionPane.showMessageDialog(this, "Xoá thành công!");
            clear();
            ketNoi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoaiSPFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSPFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        clear();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnFirtsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirtsActionPerformed
        int ViTri = 0;
        tblSp.setRowSelectionInterval(ViTri, ViTri);
        fillBtn();
    }//GEN-LAST:event_btnFirtsActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        int ViTri = tblSp.getSelectedRow();
        if (ViTri < 0) {
            JOptionPane.showMessageDialog(this, "Không có hàng nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ViTri++;
        if (ViTri >= tblSp.getRowCount()) {
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Bạn đang ở vị trí cuối!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        } else {
            tblSp.setRowSelectionInterval(ViTri, ViTri);
            btnPrev.setEnabled(true);
            btnFirts.setEnabled(true);
        }
        fillBtn();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        int ViTri = tblSp.getRowCount() - 1;
        tblSp.setRowSelectionInterval(ViTri, ViTri);
        fillBtn();
    }//GEN-LAST:event_btnLastActionPerformed

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
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new SanPhamForm().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SanPhamForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirts;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemLoaiSP;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiSP;
    private javax.swing.JComboBox<String> cboNhaCC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblPic;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTable tblSp;
    private javax.swing.JTextField txtGiaTien;
    private javax.swing.JTextField txtIDSP;
    private javax.swing.JTextField txtMalsp;
    private javax.swing.JTextField txtMancc;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables
}
