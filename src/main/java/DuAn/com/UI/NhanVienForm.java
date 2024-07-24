/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
import CheckForm.AddMK_Auto;
import CheckForm.Hide_Password;
import CheckForm.ResetForm;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NITRO 5 dsd xccxxc
 */
public class NhanVienForm extends javax.swing.JFrame {

    String url = "jdbc:sqlserver://localhost:1433;database=DU_AN_1_GROUP1_DIENMAY;integratedSecurity=false;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";

    public NhanVienForm() {
        initComponents();
        init();
        AddID_Auto addID_Auto = new AddID_Auto();
        addID_Auto.initTextFieldMap(this); // Khởi tạo các JTextField từ lớp NhanVienForm
        addID_Auto.setTextFieldValues(); // Đặt giá trị và tạo mã tự động nếu có
        AddMK_Auto addMK_Auto = new AddMK_Auto();
        addMK_Auto.initTextFieldMap(this);
        addMK_Auto.setTextFieldValues();
        ResetForm resetForm = new ResetForm();
        resetForm.resetComponents();
        loadTable_Staff();
        tblStaff.getColumnModel().getColumn(6).setCellRenderer(new Hide_Password());

        tblStaff.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Kiểm tra xem có hàng nào được chọn không
                if (!e.getValueIsAdjusting() && tblStaff.getSelectedRow() != -1) {
                    // Lấy chỉ số hàng được chọn
                    int row = tblStaff.getSelectedRow();

                    // Lấy dữ liệu từ hàng được chọn
                    String id = tblStaff.getValueAt(row, 0).toString();
                    String name = tblStaff.getValueAt(row, 1).toString();
                    String gender = tblStaff.getValueAt(row, 2).toString();
                    String date = tblStaff.getValueAt(row, 3).toString();
                    String role = tblStaff.getValueAt(row, 4).toString();
                    String salary = tblStaff.getValueAt(row, 5).toString();
                    String password = tblStaff.getValueAt(row, 6).toString();

                    // Cập nhật các ô nhập liệu
                    txtID_NV.setText(id);
                    txtNameNV.setText(name);
                    if (gender.equals("Nam")) {
                        rdoMale.setSelected(true);
                    } else {
                        rdoFemale.setSelected(true);
                    }
                    txtDate.setText(date);
                    txtChucVu.setText(role);
                    txtLuong.setText(salary);

                    // Cập nhật ô mật khẩu với dấu sao
                    String maskedPassword = "*".repeat(password.length());
                    txtMK_NV.setText(maskedPassword);
                }
            }
        });
    }

    private void loadTable_Staff() {
        DefaultTableModel model = (DefaultTableModel) tblStaff.getModel();
        model.setRowCount(0); // Clear all rows in the table before filling it with new data
        String query = "SELECT * FROM NHAN_VIEN";
        try ( Connection con = DriverManager.getConnection(url);  PreparedStatement pst = con.prepareStatement(query)) {
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String ID = rs.getString("ID_NV");
                    String name = rs.getString("TEN_NV");
                    String Gender = rs.getString("GIOI_TINH");
                    String Date = rs.getString("NGAY_VAO_LAM");
                    String Role = rs.getString("CHUC_VU");
                    String Salary = rs.getString("LUONG");
                    String password = rs.getString("MAT_KHAU");

                    // Add a new row to the table with the course information
                    model.addRow(new Object[]{ID, name, Gender, Date, Role, Salary, password});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị người học thống kê: " + ex.getMessage());
        }

        // Apply the password renderer to the password column (assuming it's column 6)
        tblStaff.getColumnModel().getColumn(6).setCellRenderer(new Hide_Password());
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
    
    public void fillBtn() {
        int selectedRow = tblStaff.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblStaff.getModel();
        txtID_NV.setText(String.valueOf(model.getValueAt(selectedRow, 0)));
        txtNameNV.setText(String.valueOf(model.getValueAt(selectedRow, 1)));
        txtChucVu.setText(String.valueOf(model.getValueAt(selectedRow, 2)));
        String gioiTinh = model.getValueAt(selectedRow, 3).toString();
        if(gioiTinh.equals("Nam")){
            rdoMale.setSelected(true);
        }else if(gioiTinh.equals("Nữ")){
            rdoFemale.setSelected(true);
        } 
        txtDate.setText(String.valueOf(model.getValueAt(selectedRow, 4)));
        txtLuong.setText(String.valueOf(model.getValueAt(selectedRow, 5)));
        txtMK_NV.setText(String.valueOf(model.getValueAt(selectedRow, 6)));
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
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnAdd_NV = new javax.swing.JButton();
        btnUpdate_NV = new javax.swing.JButton();
        btnDelete_NV = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblThoat = new javax.swing.JLabel();
        btnFirts = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtID_NV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNameNV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtChucVu = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdoMale = new javax.swing.JRadioButton();
        rdoFemale = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtLuong = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtMK_NV = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStaff = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/account.png"))); // NOI18N
        jLabel1.setText("Nhân viên");

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

        btnAdd_NV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add.png"))); // NOI18N
        btnAdd_NV.setText("Thêm");
        btnAdd_NV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_NVActionPerformed(evt);
            }
        });

        btnUpdate_NV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fix.png"))); // NOI18N
        btnUpdate_NV.setText("Sửa");
        btnUpdate_NV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_NVActionPerformed(evt);
            }
        });

        btnDelete_NV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/remove.png"))); // NOI18N
        btnDelete_NV.setText("Xoá");
        btnDelete_NV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_NVActionPerformed(evt);
            }
        });

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reset.png"))); // NOI18N
        btnReset.setText("Mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        btnFirts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/first.png"))); // NOI18N
        btnFirts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirtsActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/prev.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/last.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnAdd_NV)
                .addGap(5, 5, 5)
                .addComponent(btnUpdate_NV)
                .addGap(5, 5, 5)
                .addComponent(btnDelete_NV)
                .addGap(5, 5, 5)
                .addComponent(btnReset)
                .addGap(84, 84, 84)
                .addComponent(btnFirts)
                .addGap(5, 5, 5)
                .addComponent(btnPrev)
                .addGap(5, 5, 5)
                .addComponent(btnNext)
                .addGap(5, 5, 5)
                .addComponent(btnLast))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThoat))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnAdd_NV))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnUpdate_NV))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnDelete_NV))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnReset))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnFirts))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnPrev))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnNext))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnLast)))
                .addGap(18, 18, 18)
                .addComponent(lblThoat)
                .addContainerGap())
        );

        jLabel2.setText("Mã nhân viên:");

        jLabel3.setText("tên nhân viên:");

        txtNameNV.setToolTipText("");

        jLabel4.setText("Chức vụ");

        txtChucVu.setToolTipText("");

        jLabel5.setText("Giới tính:");

        buttonGroup1.add(rdoMale);
        rdoMale.setText("Nam");
        rdoMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMaleActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoFemale);
        rdoFemale.setText("Nữ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtID_NV)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNameNV)
                    .addComponent(txtChucVu)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(rdoMale, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtID_NV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoFemale, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(rdoMale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel6.setText("Lương:");

        txtLuong.setToolTipText("");

        jLabel7.setText("Mật khẩu:");

        txtMK_NV.setToolTipText("");

        jLabel8.setText("Ngày vào làm:");

        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDate)
                    .addComponent(txtLuong)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMK_NV)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMK_NV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setLayout(new java.awt.BorderLayout());

        tblStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "GIỚI TÍNH", "NGÀY VÀO LÀM", "CHỨC VỤ", "LƯƠNG", "MẬT KHẨU"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStaffMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblStaff);
        if (tblStaff.getColumnModel().getColumnCount() > 0) {
            tblStaff.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE))))
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
                        .addGap(7, 7, 7)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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

    private void rdoMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMaleActionPerformed

    private boolean validateForm() {
        if (txtID_NV.getText().trim().isEmpty() || txtNameNV.getText().trim().isEmpty()
                || txtChucVu.getText().trim().isEmpty() || txtLuong.getText().trim().isEmpty()
                || txtMK_NV.getText().trim().isEmpty() || txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double salary = Double.parseDouble(txtLuong.getText().trim());
            if (salary < 10000000) {
                JOptionPane.showMessageDialog(this, "Lương phải lớn hơn 10 triệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.setLenient(false);
            sdf.parse(txtDate.getText().trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm không hợp lệ. Định dạng đúng là yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void btnAdd_NVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_NVActionPerformed
        if (validateForm()) {
            String ID = txtID_NV.getText().trim();
            String name = txtNameNV.getText().trim();
            String Gender = rdoMale.isSelected() ? "Nam" : "Nữ";
            String Date = txtDate.getText().trim();
            String Role = txtChucVu.getText().trim();
            double Salary = Double.parseDouble(txtLuong.getText().trim());
            String password = txtMK_NV.getText().trim();

            String query = "INSERT INTO NHAN_VIEN (ID_NV, TEN_NV, GIOI_TINH, NGAY_VAO_LAM, CHUC_VU, LUONG, MAT_KHAU) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try ( Connection con = DriverManager.getConnection(url);  PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, ID);
                pst.setString(2, name);
                pst.setString(3, Gender);
                pst.setString(4, Date);
                pst.setString(5, Role);
                pst.setDouble(6, Salary);
                pst.setString(7, password);

                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công.");
                    loadTable_Staff();
                    // Làm sạch các ô nhập liệu sau khi thêm thành công
                    txtID_NV.setText("");
                    txtNameNV.setText("");
                    rdoMale.setSelected(false);
                    rdoFemale.setSelected(false);
                    txtDate.setText("");
                    txtChucVu.setText("");
                    txtLuong.setText("");
                    txtMK_NV.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAdd_NVActionPerformed

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame().setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void btnUpdate_NVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_NVActionPerformed
        // Kiểm tra tính hợp lệ của dữ liệu
        if (validateForm()) {
            String ID = txtID_NV.getText().trim();
            String name = txtNameNV.getText().trim();
            String Gender = rdoMale.isSelected() ? "Nam" : "Nữ";
            String Date = txtDate.getText().trim();
            String Role = txtChucVu.getText().trim();
            double Salary = Double.parseDouble(txtLuong.getText().trim());
            String password = txtMK_NV.getText().trim();

            // Câu lệnh SQL cập nhật thông tin nhân viên
            String query = "UPDATE NHAN_VIEN SET TEN_NV = ?, GIOI_TINH = ?, NGAY_VAO_LAM = ?, CHUC_VU = ?, LUONG = ?, MAT_KHAU = ? WHERE ID_NV = ?";

            try ( Connection con = DriverManager.getConnection(url);  PreparedStatement pst = con.prepareStatement(query)) {
                // Thiết lập các tham số cho câu lệnh SQL
                pst.setString(1, name);
                pst.setString(2, Gender);
                pst.setString(3, Date);
                pst.setString(4, Role);
                pst.setDouble(5, Salary);
                pst.setString(6, password);
                pst.setString(7, ID);

                // Thực hiện cập nhật
                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công.");
                    loadTable_Staff();
                    // Cập nhật bảng dữ liệu sau khi cập nhật thành công
                    // Làm sạch các ô nhập liệu sau khi thêm thành công
                    txtID_NV.setText("");
                    txtNameNV.setText("");
                    rdoMale.setSelected(false);
                    rdoFemale.setSelected(false);
                    txtDate.setText("");
                    txtChucVu.setText("");
                    txtLuong.setText("");
                    txtMK_NV.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhân viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }    }//GEN-LAST:event_btnUpdate_NVActionPerformed

    private void btnDelete_NVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_NVActionPerformed
        // Lấy mã chuyên đề từ trường nhập liệu
        String ID = txtID_NV.getText();
        // Xác nhận xóa
        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Connection con = DriverManager.getConnection(url);

                // Kiểm tra xem chuyên đề có đang được sử dụng trong bảng KHOA_HOC hoặc HOC_VIEN không
                String checkQuery = "SELECT * FROM NHAN_VIEN WHERE ID_NV = ?";
                PreparedStatement checkStatement = con.prepareStatement(checkQuery);
                checkStatement.setString(1, ID);
                ResultSet resultSet = checkStatement.executeQuery();

                String sql = "DELETE FROM NHAN_VIEN WHERE ID_NV = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, ID);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công.");
                    loadTable_Staff();
                    // Làm sạch các ô nhập liệu sau khi thêm thành công
                    txtID_NV.setText("");
                    txtNameNV.setText("");
                    rdoMale.setSelected(false);
                    rdoFemale.setSelected(false);
                    txtDate.setText("");
                    txtChucVu.setText("");
                    txtLuong.setText("");
                    txtMK_NV.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Không có nhân viên nào được xóa.");

                }
                con.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa thông tin nhân viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }    }//GEN-LAST:event_btnDelete_NVActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtID_NV.setText("");
        txtNameNV.setText("");
        rdoMale.setSelected(false);
        rdoFemale.setSelected(false);
        txtDate.setText("");
        txtChucVu.setText("");
        txtLuong.setText("");
        txtMK_NV.setText("");    }//GEN-LAST:event_btnResetActionPerformed

    private void btnFirtsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirtsActionPerformed
        int ViTri = 0;
        tblStaff.setRowSelectionInterval(ViTri, ViTri);
        fillBtn();
    }//GEN-LAST:event_btnFirtsActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        int ViTri = tblStaff.getSelectedRow();
        if (ViTri == 0) {
            btnPrev.setEnabled(false);
            btnFirts.setEnabled(false);
        } else {
            ViTri = ViTri - 1;
        }
        tblStaff.setRowSelectionInterval(ViTri, ViTri);
        btnNext.setEnabled(true);
        btnLast.setEnabled(true);
        fillBtn();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        int ViTri = tblStaff.getSelectedRow();
        if (ViTri < 0) {
            JOptionPane.showMessageDialog(this, "Không có hàng nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ViTri++;
        if (ViTri >= tblStaff.getRowCount()) {
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Bạn đang ở vị trí cuối!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        } else {
            tblStaff.setRowSelectionInterval(ViTri, ViTri);
            btnPrev.setEnabled(true);
            btnFirts.setEnabled(true);
        }
        fillBtn();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        int ViTri = tblStaff.getRowCount()-1;
        tblStaff.setRowSelectionInterval(ViTri, ViTri);
        fillBtn();
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblStaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStaffMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblStaffMouseClicked

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
            java.util.logging.Logger.getLogger(NhanVienForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhanVienForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhanVienForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhanVienForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NhanVienForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd_NV;
    private javax.swing.JButton btnDelete_NV;
    private javax.swing.JButton btnFirts;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate_NV;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JRadioButton rdoFemale;
    private javax.swing.JRadioButton rdoMale;
    private javax.swing.JTable tblStaff;
    private javax.swing.JTextField txtChucVu;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtID_NV;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMK_NV;
    private javax.swing.JTextField txtNameNV;
    // End of variables declaration//GEN-END:variables
}
