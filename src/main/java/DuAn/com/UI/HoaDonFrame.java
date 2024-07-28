/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author NITRO 5
 */
public class HoaDonFrame extends javax.swing.JFrame {

    String url = "jdbc:sqlserver://localhost:1433;database=DU_AN_1_GROUP1_DIENMAY;integratedSecurity=false;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";
    private String lastIdHoaDon = "";

    /**
     * Creates new form HoaDonFrame
     */
    public HoaDonFrame() {
        initComponents();
        init();
        AddID_Auto addID_Auto = new AddID_Auto();
        addID_Auto.initTextFieldMap(this); // Khởi tạo các JTextField từ lớp NhanVienForm
        addID_Auto.setTextFieldValues();
        loadTable();
        loadCboNameSP();
        setCurrentDate();
        txtGia.setEnabled(false);
        txtTongTien.setEnabled(false);
        txtDate.setEnabled(false);

        cboNameSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrice();
                updateTotal();
            }
        });
        txtSoLuong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotal();
            }
        });

        // Khởi tạo và cấu hình Timer để kiểm tra mỗi 3 giây
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAndSetDate();
            }
        });
        timer.start(); // Bắt đầu Timer
        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 4) {
                    loadSelectedRowData2();
                }
            }
        });
        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    loadSelectedRowData();
                }
            }
        });
    }

    private void checkAndSetDate() {
        // Kiểm tra nếu txtDate rỗng
        if (txtDate.getText().isEmpty()) {
            setCurrentDate();
        }
    }

    private void setCurrentDate() {
        // Lấy ngày hiện tại và định dạng nó
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        // Đặt ngày hiện tại vào txtDate
        txtDate.setText(formattedDate);
    }

    private void updateTotal() {
        try {
            // Lấy giá từ ô nhập liệu txtPrice
            String priceText = txtGia.getText();
            double price = Double.parseDouble(priceText.isEmpty() ? "0" : priceText);

            // Lấy số lượng từ ô nhập liệu txtQuantity
            String quantityText = txtSoLuong.getText();
            int quantity = Integer.parseInt(quantityText.isEmpty() ? "0" : quantityText);

            // Tính toán tổng và cập nhật ô nhập liệu txtTotal
            double total = price * quantity;
            txtTongTien.setText(String.format("%.0f", total));
        } catch (NumberFormatException e) {
            // Nếu có lỗi định dạng số, đặt tổng là 0
            txtTongTien.setText("0.00");
        }
    }

    private void updatePrice() {
        String selectedItem = (String) cboNameSP.getSelectedItem();
        if (selectedItem != null) {
            // Chuỗi truy vấn SQL để lấy giá sản phẩm theo tên
            String sql = "SELECT GIA FROM SAN_PHAM WHERE TEN_SP = ?";

            // Kết nối với cơ sở dữ liệu và thực hiện truy vấn
            try ( Connection conn = DriverManager.getConnection(url);  PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, selectedItem);

                try ( ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Lấy giá sản phẩm từ kết quả truy vấn
                        String price = rs.getString("GIA");
                        txtGia.setText(price);
                    } else {
                        txtGia.setText("Price not found");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error retrieving product price: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getSelectedMaSP() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow != -1) {
            // Giả sử cột mã sản phẩm là cột thứ 0
            return tblHoaDon.getValueAt(selectedRow, 0).toString();
        }
        return null;
    }

// Phương thức để tải dữ liệu lên JComboBox
    private void loadCboNameSP() {
        // Xóa tất cả các mục hiện có trong JComboBox
        cboNameSP.removeAllItems();

        // Chuỗi truy vấn SQL để lấy danh sách sản phẩm
        String sql = "SELECT TEN_SP FROM SAN_PHAM";

        // Kết nối với cơ sở dữ liệu và thực hiện truy vấn
        try ( Connection conn = DriverManager.getConnection(url);  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            // Duyệt qua kết quả truy vấn và thêm từng sản phẩm vào JComboBox
            while (rs.next()) {
                String tenSanPham = rs.getString("TEN_SP");
                cboNameSP.addItem(tenSanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAndLoadData() {
        SwingUtilities.invokeLater(() -> {
            String currentIdHoaDon = txtIDHoaDon.getText();
            if (!currentIdHoaDon.equals(lastIdHoaDon)) {
                lastIdHoaDon = currentIdHoaDon;
                loadData(currentIdHoaDon);
            }
        });
    }

    private void loadData(String idHoaDon) {
        String sql = "SELECT * FROM HOA_DON WHERE ID_HOA_DON = ?";
        try ( Connection conn = DriverManager.getConnection(url);  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtIDHoaDon.setText(rs.getString("ID_HOA_DON"));
                txtMa_KH.setText(rs.getString("ID_KH"));
                cboNameSP.setSelectedItem(rs.getString("ID_SP"));
                txtDate.setText(rs.getString("NGAYBAN_HANG"));
                txtSoLuong.setText(rs.getString("SOLUONG"));
                txtGia.setText(rs.getString("GIA"));
                txtTongTien.setText(rs.getString("TONGTIEN"));
            } else {
                clearTextFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTextFields() {
        txtIDHoaDon.setText("");
        txtMa_KH.setText("");
        cboNameSP.setSelectedItem("");
        txtDate.setText("");
        txtSoLuong.setText("");
        txtGia.setText("");
        txtTongTien.setText("");
    }

    private void loadSelectedRowData() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow != -1) {
            String idHoaDon = tblHoaDon.getValueAt(selectedRow, 0).toString();
            txtIDHoaDon.setText(idHoaDon);
            txtMa_KH.setText(tblHoaDon.getValueAt(selectedRow, 1).toString());
            cboNameSP.setSelectedItem(tblHoaDon.getValueAt(selectedRow, 2).toString());
            txtDate.setText(tblHoaDon.getValueAt(selectedRow, 3).toString());

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to view details.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadSelectedRowData2() {
        int selectedRow = tblHoaDon.getSelectedRow();

        String idHoaDon = tblHoaDon.getValueAt(selectedRow, 0).toString();
        txtIDHoaDon.setText(idHoaDon);

        // Mở form để hiển thị thông tin chi tiết
        FormHDCT detailsForm = new FormHDCT();
        detailsForm.setIdHoaDon(idHoaDon);
        detailsForm.setVisible(true);

    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);

        try ( Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT ID_HOA_DON, ID_KH, ID_SP, NGAYBAN_HANG FROM HOA_DON";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("ID_HOA_DON"), rs.getString("ID_KH"), rs.getString("ID_SP"), rs.getDate("NGAYBAN_HANG")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        jLabel2 = new javax.swing.JLabel();
        txtIDHoaDon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMa_KH = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cboNameSP = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        lblThoat = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        txtID_HDCT = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel2.setText("Mã hoá đơn");

        jLabel3.setText("Mã khách hàng");

        jLabel4.setText("Tên sản phẩm");

        cboNameSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Số lượng");

        jLabel6.setText("Giá");

        jLabel7.setText("Ngày tạo");

        jLabel8.setText("Tổng tiền");

        lblThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/recycle_32.png"))); // NOI18N
        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/first.png"))); // NOI18N

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/prev.png"))); // NOI18N

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next.png"))); // NOI18N

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/last.png"))); // NOI18N

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

        jLabel9.setText("Mã HĐCT:");

        jLabel10.setText("Mã nhân viện");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGia)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(138, 138, 138))
                                    .addComponent(txtSoLuong))
                                .addGap(37, 37, 37)))
                        .addContainerGap(196, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblThoat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addGap(26, 26, 26)
                        .addComponent(btnXoa)
                        .addGap(22, 22, 22)
                        .addComponent(btnMoi))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(cboNameSP, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(145, 145, 145)
                                .addComponent(jLabel8))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGap(123, 123, 123))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtIDHoaDon)
                                        .addGap(36, 36, 36)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtID_HDCT))))
                            .addComponent(jLabel4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtMa_KH, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(txtMaNV)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID_HDCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMa_KH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNameSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblThoat, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private boolean validateFields() {
        if (txtMa_KH.getText().isEmpty() || cboNameSP.getSelectedItem() == null || txtSoLuong.getText().isEmpty()
                || txtGia.getText().isEmpty() || txtDate.getText().isEmpty() || txtTongTien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Additional validation can be added here (e.g., numeric checks)
        return true;
    }

private void addHoaDon() {
    String tenSanPham = (String) cboNameSP.getSelectedItem();
    String maSanPham = getMaSPFromTenSP(tenSanPham);

    if (maSanPham == null) {
        JOptionPane.showMessageDialog(this, "Unable to find the product code for the selected product.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sqlHoaDon = "INSERT INTO HOA_DON (ID_HOA_DON, ID_KH, ID_SP, ID_NV,NGAYBAN_HANG, SO_LUONG, GIA, TONG_TIEN) VALUES (?, ?,?, ?, ?, ?, ?, ?)";
    String sqlHoaDonChiTiet = "INSERT INTO HOA_DON_CHI_TIET (ID_HOA_DON_CT, ID_HOA_DON, ID_SP,SO_LUONG,GIA,TONG_TIEN) VALUES (?, ?, ?, ?, ? ,?)";

    Connection conn = null;
    PreparedStatement stmtHoaDon = null;
    PreparedStatement stmtHoaDonChiTiet = null;

    try {
        conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);  // Start transaction

        // Insert into HOA_DON
        stmtHoaDon = conn.prepareStatement(sqlHoaDon);
        stmtHoaDon.setString(1, txtIDHoaDon.getText());
        stmtHoaDon.setString(2, txtMa_KH.getText());
        stmtHoaDon.setString(3, maSanPham);
        stmtHoaDon.setString(4, txtMaNV.getText());
        stmtHoaDon.setString(5, txtDate.getText());
        stmtHoaDon.setString(6, txtSoLuong.getText());
        stmtHoaDon.setString(7, txtGia.getText());
        stmtHoaDon.setString(8, txtTongTien.getText());
        stmtHoaDon.executeUpdate();

        // Insert into HOA_DON_CHI_TIET
        stmtHoaDonChiTiet = conn.prepareStatement(sqlHoaDonChiTiet);
        stmtHoaDonChiTiet.setString(1, txtID_HDCT.getText());  // Assuming you have a text field for ID_HOA_DON_CT
        stmtHoaDonChiTiet.setString(2, txtIDHoaDon.getText());
         stmtHoaDonChiTiet.setString(3, maSanPham);
        stmtHoaDonChiTiet.setString(4, txtSoLuong.getText());
        stmtHoaDonChiTiet.setString(5, txtGia.getText());
        stmtHoaDonChiTiet.setString(6, txtTongTien.getText());
        stmtHoaDonChiTiet.executeUpdate();

        conn.commit();  // Commit transaction

        loadTable();
        clearTextFields();
        JOptionPane.showMessageDialog(this, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();  // Rollback transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        if (stmtHoaDon != null) {
            try {
                stmtHoaDon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (stmtHoaDonChiTiet != null) {
            try {
                stmtHoaDonChiTiet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);  // Reset auto-commit
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

    private String getMaSPFromTenSP(String tenSanPham) {
        String sql = "SELECT ID_SP FROM SAN_PHAM WHERE TEN_SP = ?";
        try ( Connection conn = DriverManager.getConnection(url);  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenSanPham);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ID_SP");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving product code: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }


    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (validateFields()) {
            addHoaDon();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        new HomeFrame().setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

private void updateHoaDon() {
    String tenSanPham = (String) cboNameSP.getSelectedItem();
    String maSanPham = getMaSPFromTenSP(tenSanPham);

    if (maSanPham == null) {
        JOptionPane.showMessageDialog(this, "Unable to find the product code for the selected product.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sqlUpdateHoaDon = "UPDATE HOA_DON SET ID_KH = ?, ID_SP = ?, ID_NV = ?, NGAYBAN_HANG = ?, SO_LUONG = ?, GIA = ?, TONG_TIEN = ? WHERE ID_HOA_DON = ?";
    String sqlUpdateHoaDonChiTiet = "UPDATE HOA_DON_CHI_TIET SET ID_SP = ?, SO_LUONG = ?, GIA = ?, TONG_TIEN = ? WHERE ID_HOA_DON_CT = ? AND ID_HOA_DON = ?";

    Connection conn = null;
    PreparedStatement stmtUpdateHoaDon = null;
    PreparedStatement stmtUpdateHoaDonChiTiet = null;

    try {
        conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);  // Start transaction

        // Update HOA_DON
        stmtUpdateHoaDon = conn.prepareStatement(sqlUpdateHoaDon);
        stmtUpdateHoaDon.setString(1, txtMa_KH.getText());
        stmtUpdateHoaDon.setString(2, maSanPham);
        stmtUpdateHoaDon.setString(3, txtMaNV.getText());
        stmtUpdateHoaDon.setString(4, txtDate.getText());
        stmtUpdateHoaDon.setString(5, txtSoLuong.getText());
        stmtUpdateHoaDon.setString(6, txtGia.getText());
        stmtUpdateHoaDon.setString(7, txtTongTien.getText());
        stmtUpdateHoaDon.setString(8, txtIDHoaDon.getText());
        stmtUpdateHoaDon.executeUpdate();

        // Update HOA_DON_CHI_TIET
        stmtUpdateHoaDonChiTiet = conn.prepareStatement(sqlUpdateHoaDonChiTiet);
        stmtUpdateHoaDonChiTiet.setString(1, maSanPham);
        stmtUpdateHoaDonChiTiet.setString(2, txtSoLuong.getText());
        stmtUpdateHoaDonChiTiet.setString(3, txtGia.getText());
        stmtUpdateHoaDonChiTiet.setString(4, txtTongTien.getText());
        stmtUpdateHoaDonChiTiet.setString(5, txtID_HDCT.getText());  // Assuming you have a text field for ID_HOA_DON_CT
        stmtUpdateHoaDonChiTiet.setString(6, txtIDHoaDon.getText());
        stmtUpdateHoaDonChiTiet.executeUpdate();

        conn.commit();  // Commit transaction

        loadTable();
        clearTextFields();
        JOptionPane.showMessageDialog(this, "Record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();  // Rollback transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        if (stmtUpdateHoaDon != null) {
            try {
                stmtUpdateHoaDon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (stmtUpdateHoaDonChiTiet != null) {
            try {
                stmtUpdateHoaDonChiTiet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);  // Reset auto-commit
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

    private String getTenSPFromMaSP(String maSanPham) {
        String sql = "SELECT TEN_SP FROM SAN_PHAM WHERE ID_SP = ?";
        try ( Connection conn = DriverManager.getConnection(url);  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TEN_SP");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving product name: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }


    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (validateFields()) {
            updateHoaDon();
        }    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        deleteHoaDon(txtIDHoaDon.getText());
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        clearTextFields();
    }//GEN-LAST:event_btnMoiActionPerformed

private void deleteHoaDon(String idHoaDon) {
    String sqlDeleteHoaDon = "DELETE FROM HOA_DON WHERE ID_HOA_DON = ?";
    String sqlDeleteHoaDonChiTiet = "DELETE FROM HOA_DON_CHI_TIET WHERE ID_HOA_DON = ?";

    Connection conn = null;
    PreparedStatement stmtDeleteHoaDon = null;
    PreparedStatement stmtDeleteHoaDonChiTiet = null;

    try {
        conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);  // Start transaction

        // Delete from HOA_DON_CHI_TIET
        stmtDeleteHoaDonChiTiet = conn.prepareStatement(sqlDeleteHoaDonChiTiet);
        stmtDeleteHoaDonChiTiet.setString(1, idHoaDon);
        stmtDeleteHoaDonChiTiet.executeUpdate();

        // Delete from HOA_DON
        stmtDeleteHoaDon = conn.prepareStatement(sqlDeleteHoaDon);
        stmtDeleteHoaDon.setString(1, idHoaDon);
        stmtDeleteHoaDon.executeUpdate();

        conn.commit();  // Commit transaction

        loadTable();
        JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();  // Rollback transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        if (stmtDeleteHoaDon != null) {
            try {
                stmtDeleteHoaDon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (stmtDeleteHoaDonChiTiet != null) {
            try {
                stmtDeleteHoaDonChiTiet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);  // Reset auto-commit
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
                new HoaDonFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboNameSP;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtIDHoaDon;
    private javax.swing.JTextField txtID_HDCT;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMa_KH;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
