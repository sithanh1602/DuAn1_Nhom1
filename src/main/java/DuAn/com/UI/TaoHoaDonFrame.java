/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.CurrencyFormatter;
import CheckForm.CurrencyRenderer;
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
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author NITRO 5
 */
public class TaoHoaDonFrame extends javax.swing.JFrame {

    ImageIcon icon;
    private String maNhanVien;
    private String fullName;
    private String chucVu;

    public void doiIcon() {
        icon = new ImageIcon("src/main/resources/images/Technology.png");
        setIconImage(icon.getImage());
    }
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model1 = new DefaultTableModel();
    DefaultTableModel model2 = new DefaultTableModel();
    DefaultTableModel model3 = new DefaultTableModel();
    Connection ketNoi;

    String url = "jdbc:sqlserver://localhost:1433;database=DU_AN_1_GROUP1_DIENMAY3;integratedSecurity=false;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";

    /**
     * Creates new form TaoHoaDonFrame
     */
    public TaoHoaDonFrame(String maNV, String fullName, String chucVu) throws ClassNotFoundException, SQLException {
        initComponents();
        init();
        txtMaNV.setText(maNV);
        this.maNhanVien = maNV;
        this.fullName = fullName;
        this.chucVu = chucVu;
        model = new NonEditableTableModel(); // Sử dụng mô hình không thể chỉnh sửa
        model1 = new NonEditableTableModel(); // Sử dụng mô hình không thể chỉnh sửa
        model2 = new NonEditableTableModel(); // Sử dụng mô hình không thể chỉnh sửa
        model3 = new NonEditableTableModel(); // Sử dụng mô hình không thể chỉnh sửa
        CurrencyFormatter currencyFormatter = new CurrencyFormatter(this);
        // Khởi tạo model cho JTable
        tenCotBang1();
        tenCotBang2();
        tenCotBangHoaDon();
        tenCotBangHoaDonCT();
        TaiDulieuVaoBang();
        updateInvoiceList();
        tblSp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    int selectedRow = tblSp.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get data from the selected row
                        String maSP = (String) model.getValueAt(selectedRow, 0);
                        String tenSP = (String) model.getValueAt(selectedRow, 1);
                        int donGia = (Integer) model.getValueAt(selectedRow, 4);

                        // Prompt user to input quantity
                        String soLuongStr = JOptionPane.showInputDialog("Nhập số lượng:");
                        if (soLuongStr != null && !soLuongStr.trim().isEmpty()) {
                            try {
                                int soLuong = Integer.parseInt(soLuongStr);

                                // Check stock quantity
                                int availableStock = getStockQuantity(maSP); // Method to get stock quantity

                                if (soLuong > availableStock) {
                                    JOptionPane.showMessageDialog(null, "Số sản phẩm còn lại là (" + availableStock + ").", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    int thanhTien = soLuong * donGia; // Calculate total amount

                                    // Prepare the row data to be added to tblGh
                                    Object[] rowData = new Object[]{
                                        model1.getRowCount() + 1, // STT (serial number), incremented for each new row
                                        maSP,
                                        tenSP,
                                        soLuong,
                                        donGia,
                                        thanhTien,
                                        "" // Default status, or you can adjust based on your logic
                                    };

                                    // Add row data to tblGh
                                    model1.addRow(rowData);

                                    calculateTotalAmount();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Vui lòng nhập một số hợp lệ cho số lượng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        txtTenKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy mã nhân viên từ txtTenKH
                String maNV = txtTenKH.getText();
                // Truy vấn cơ sở dữ liệu để lấy tên, sdt, và dia chi tương ứng với mã nhân viên
                Map<String, String> khachHangDetails = getTenNhanVienFromMaNhanVien(maNV);

                if (khachHangDetails != null) {
                    // Hiển thị thông tin khách hàng trong các trường tương ứng
                    txtTenKH.setText(khachHangDetails.get("tenNV"));
                    txtSdt.setText(khachHangDetails.get("sdt"));
                    txtDiaChi.setText(khachHangDetails.get("diaChi"));
                } else {
                    // Xử lý nếu không tìm thấy thông tin khách hàng
                    txtTenKH.setText("");
                    txtSdt.setText("");
                    txtDiaChi.setText("");
                }
            }
        });

        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Single click
                    int selectedRow = tblHoaDon.getSelectedRow();
                    if (selectedRow != -1) {
                        String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 1);
                        showInvoiceDetail(idHoaDon);
                    }
                }
            }
        });

        txtKhachDuaMoney.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                calculateTienThua();
            }

            public void removeUpdate(DocumentEvent e) {
                calculateTienThua();
            }

            public void insertUpdate(DocumentEvent e) {
                calculateTienThua();
            }
        });

        btnTaoHD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createInvoice();
            }
        });
    }

    // Method to get stock quantity of a product
    private int getStockQuantity(String maSP) {
        Connection ketNoi = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int stockQuantity = 0;

        try {
            ketNoi = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true",
                    "sa", "123456");

            String sql = "SELECT SL_TONKHO FROM SAN_PHAM WHERE ID_SP = ?";
            stmt = ketNoi.prepareStatement(sql);
            stmt.setString(1, maSP);
            rs = stmt.executeQuery();

            if (rs.next()) {
                stockQuantity = rs.getInt("SL_TONKHO");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (ketNoi != null) {
                    ketNoi.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return stockQuantity;
    }

    private void calculateTienThua() {
        try {
            // Ensure txtTongTien_Money has valid text and is parsed correctly
            String tongTienText = txtTongTien_Money.getText().replaceAll("[^\\d]", "");
            int tongTien = tongTienText.isEmpty() ? 0 : Integer.parseInt(tongTienText);

            // Ensure txtKhachDuaMoney has valid text and is parsed correctly
            String khachDuaText = txtKhachDuaMoney.getText().replaceAll("[^\\d]", "");
            int tienKhachDua = khachDuaText.isEmpty() ? 0 : Integer.parseInt(khachDuaText);

            // Calculate change
            int tienThua = tienKhachDua - tongTien;

            // Set result to txtTienThua_Money
            txtTienThua_Money.setText(String.valueOf(tienThua));
        } catch (NumberFormatException e) {
            // Handle unexpected formatting issues
            txtTienThua_Money.setText("0"); // Default to 0 if there is an issue
        }
    }

    private void showInvoiceDetail(String idHoaDon) {
        DefaultTableModel modelChiTiet = (DefaultTableModel) tblHdct.getModel();
        modelChiTiet.setRowCount(0); // Xóa dữ liệu trước đó

        try {
            // Kết nối đến cơ sở dữ liệu
            Connection ketNoi = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true",
                    "sa", "123456");

            // Truy vấn để lấy chi tiết hóa đơn
            String sqlSelect = "SELECT ID_SP, SO_LUONG, DON_GIA FROM CHI_TIET_HOA_DON WHERE ID_HOA_DON = ?";
            PreparedStatement stmt = ketNoi.prepareStatement(sqlSelect);
            stmt.setString(1, idHoaDon);
            ResultSet rs = stmt.executeQuery();

            // Truy vấn để lấy tên sản phẩm
            String sqlProduct = "SELECT TEN_SP FROM SAN_PHAM WHERE ID_SP = ?";
            PreparedStatement stmtProduct = ketNoi.prepareStatement(sqlProduct);

            while (rs.next()) {
                String maSP = rs.getString("ID_SP");
                int soLuong = rs.getInt("SO_LUONG");
                int donGia = rs.getInt("DON_GIA");
                int thanhTien = soLuong * donGia; // Tính toán thành tiền

                // Lấy tên sản phẩm
                stmtProduct.setString(1, maSP);
                ResultSet rsProduct = stmtProduct.executeQuery();
                String tenSP = "";
                if (rsProduct.next()) {
                    tenSP = rsProduct.getString("TEN_SP");
                }
                rsProduct.close();

                // Thêm hàng vào bảng chi tiết hóa đơn
                modelChiTiet.addRow(new Object[]{maSP, tenSP, soLuong, donGia, thanhTien});
            }

            rs.close();
            stmt.close();
            stmtProduct.close();
            ketNoi.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu.");
        }
    }

    private void createInvoice() {
        Connection ketNoi = null;
        PreparedStatement stmtInsertHoaDon = null;
        PreparedStatement stmtInsertChiTiet = null;
        PreparedStatement stmtUpdateStock = null;
        PreparedStatement stmtCheckExistence = null;

        try {
            // Collect information from input fields
            String tenKH = txtTenKH.getText();
            String sdt = txtSdt.getText();
            String diaChi = txtDiaChi.getText();

            // Remove currency formatting before parsing
            String tongTienString = removeCurrencyFormatting(txtTongTien_Money.getText());
            String tienKhachString = removeCurrencyFormatting(txtKhachDuaMoney.getText());
            String tienThuaString = removeCurrencyFormatting(txtTienThua_Money.getText());

            // Handle possible empty strings or parse exceptions
            int tongTien = parseInteger(tongTienString);
            int tienKhach = parseInteger(tienKhachString);
            int tienThua = parseInteger(tienThuaString);

            // Check if employee ID is provided
            String idNV = txtMaNV.getText().trim();
            if (idNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ghiChu = txtGhiChu.getText();

            // Get payment method
            String paymentMethod = (String) cboThanhToan.getSelectedItem();
            if (paymentMethod == null || paymentMethod.trim().isEmpty() || paymentMethod.equals("Phương thức thanh toán")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get delivery status
            String status = rdoGiaoHang.isSelected() ? "Đã thanh toán, Đang giao hàng" : "Đã thanh toán";

            // Validation checks
            if (!paymentMethod.equals("Chuyển khoản")) {
                if (tienKhach < tongTien) {
                    JOptionPane.showMessageDialog(this, "Tiền khách đưa phải lớn hơn hoặc bằng số tiền phải trả.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tienThua < 0) {
                    JOptionPane.showMessageDialog(this, "Tiền thừa không được âm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (tongTien == 0 && model1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tổng tiền không được bằng 0 và phải có ít nhất một sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate invoice ID
            String idHoaDon = generateInvoiceId();

            // Connect to the database
            ketNoi = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true",
                    "sa", "123456");
            ketNoi.setAutoCommit(false); // Start transaction

            // Get customer ID
            String idKH = getCustomerIdByName(tenKH, ketNoi);
            if (idKH == null) {
                JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại.");
                return;
            }

            // Insert invoice
            String sqlInsertHoaDon = "INSERT INTO HOA_DON (ID_HOA_DON, ID_NV, ID_KH, NGAY_HOA_DON, TONG_TIEN, TIEN_KHACH_DUA, TIEN_THUA, GHICHU, TRANG_THAI) "
                    + "VALUES (?, ?, ?, GETDATE(), ?, ?, ?, ?, ?)";
            stmtInsertHoaDon = ketNoi.prepareStatement(sqlInsertHoaDon);
            stmtInsertHoaDon.setString(1, idHoaDon);
            stmtInsertHoaDon.setString(2, idNV); // Use the ID from txtMaNV
            stmtInsertHoaDon.setString(3, idKH);
            stmtInsertHoaDon.setInt(4, tongTien);
            stmtInsertHoaDon.setInt(5, tienKhach);
            stmtInsertHoaDon.setInt(6, tienThua);
            stmtInsertHoaDon.setString(7, ghiChu);
            stmtInsertHoaDon.setString(8, status);
            stmtInsertHoaDon.executeUpdate();

            // Check if invoice detail already exists
            String sqlCheckExistence = "SELECT COUNT(*) FROM CHI_TIET_HOA_DON WHERE ID_HOA_DON = ? AND ID_SP = ?";
            stmtCheckExistence = ketNoi.prepareStatement(sqlCheckExistence);

            // Insert invoice details
            String sqlInsertChiTiet = "INSERT INTO CHI_TIET_HOA_DON (ID_HOA_DON, ID_SP, SO_LUONG, DON_GIA, THANH_TIEN) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stmtInsertChiTiet = ketNoi.prepareStatement(sqlInsertChiTiet);

            // Update stock for each product
            String sqlUpdateStock = "UPDATE SAN_PHAM SET SL_TONKHO = SL_TONKHO - ? WHERE ID_SP = ?";
            stmtUpdateStock = ketNoi.prepareStatement(sqlUpdateStock);

            for (int i = 0; i < model1.getRowCount(); i++) {
                String maSP = (String) model1.getValueAt(i, 1);
                int soLuong = (Integer) model1.getValueAt(i, 3);
                int donGia = (Integer) model1.getValueAt(i, 4);
                int thanhTien = (Integer) model1.getValueAt(i, 5);

                // Check if detail already exists
                stmtCheckExistence.setString(1, idHoaDon);
                stmtCheckExistence.setString(2, maSP);
                ResultSet rs = stmtCheckExistence.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // If exists, you might choose to update instead of insert
                    // Skip insertion or handle update logic here
                    continue;
                }

                // Insert invoice details
                stmtInsertChiTiet.setString(1, idHoaDon);
                stmtInsertChiTiet.setString(2, maSP);
                stmtInsertChiTiet.setInt(3, soLuong);
                stmtInsertChiTiet.setInt(4, donGia);
                stmtInsertChiTiet.setInt(5, thanhTien);
                stmtInsertChiTiet.addBatch();

                // Update stock
                stmtUpdateStock.setInt(1, soLuong);
                stmtUpdateStock.setString(2, maSP);
                stmtUpdateStock.addBatch();
            }

            stmtInsertChiTiet.executeBatch();
            stmtUpdateStock.executeBatch();

            // Commit transaction
            ketNoi.commit();

            // Update invoice list
            updateInvoiceList();

            // Clear tblGh after saving
            model1.setRowCount(0);
            calculateTotalAmount();

            // Show success message
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được tạo thành công.");
            txtTienThua_Money.setText("0");
            txtKhachDuaMoney.setText("0");
            txtTongTien_Money.setText("0");
        } catch (SQLException ex) {
            // Rollback transaction if error occurs
            if (ketNoi != null) {
                try {
                    ketNoi.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số.");
        } finally {
            // Close connections
            try {
                if (stmtInsertHoaDon != null) {
                    stmtInsertHoaDon.close();
                }
                if (stmtInsertChiTiet != null) {
                    stmtInsertChiTiet.close();
                }
                if (stmtUpdateStock != null) {
                    stmtUpdateStock.close();
                }
                if (stmtCheckExistence != null) {
                    stmtCheckExistence.close();
                }
                if (ketNoi != null) {
                    ketNoi.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        clearProductTable();
    }

    private String removeCurrencyFormatting(String formattedCurrency) {
        if (formattedCurrency == null || formattedCurrency.isEmpty()) {
            return "0";
        }
        // Remove commas, periods, and "VND"
        return formattedCurrency.replaceAll("[^\\d]", "");
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            // Handle invalid input gracefully
            return 0;
        }
    }

    private void clearProductTable() {
        model1.setRowCount(0);
        txtTongTien_Money.setText("0");
        calculateTotalAmount();
    }

    private String getCustomerIdByName(String tenKH, Connection ketNoi) throws SQLException {
        String sql = "SELECT ID_KH FROM KHACH_HANG WHERE TEN_KH = ?";
        PreparedStatement stmt = ketNoi.prepareStatement(sql);
        stmt.setString(1, tenKH);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("ID_KH");
        }
        return null;
    }

    private String generateInvoiceId() {
        // Tạo mã hóa đơn tự động, ví dụ sử dụng timestamp hoặc số tự động tăng
        return "HD" + System.currentTimeMillis();
    }

    private void updateInvoiceList() {
        try {
            model2.setRowCount(0); // Xóa các dòng hiện tại

            Connection ketNoi = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true",
                    "sa", "123456");

            // Update SQL query to include TRANG_THAI
            String sqlSelectHoaDon = "SELECT h.ID_HOA_DON, nv.TEN_NV, kh.TEN_KH, h.NGAY_HOA_DON, h.TRANG_THAI "
                    + "FROM HOA_DON h "
                    + "JOIN NHAN_VIEN nv ON h.ID_NV = nv.ID_NV "
                    + "JOIN KHACH_HANG kh ON h.ID_KH = kh.ID_KH";

            PreparedStatement stmtSelectHoaDon = ketNoi.prepareStatement(sqlSelectHoaDon);
            ResultSet rs = stmtSelectHoaDon.executeQuery();

            int stt = 1; // Số thứ tự hóa đơn
            while (rs.next()) {
                String idHoaDon = rs.getString("ID_HOA_DON");
                String tenNV = rs.getString("TEN_NV");
                String tenKH = rs.getString("TEN_KH");
                Date ngayHoaDon = rs.getDate("NGAY_HOA_DON");
                String trangThai = rs.getString("TRANG_THAI"); // Retrieve TRANG_THAI from the result set
                model2.addRow(new Object[]{stt++, idHoaDon, tenNV, tenKH, trangThai, ngayHoaDon});
            }

            rs.close();
            stmtSelectHoaDon.close();
            ketNoi.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void init() {
        setLocationRelativeTo(null);
        txtTienThua_Money.setEnabled(false);
        txtMaNV.setEnabled(false);
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        txtTongTien_Money.setEnabled(false);
        txtPhaiTra_Money.setEnabled(false);
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblGh.getSelectedRow();
                if (selectedRow != -1) {
                    // Remove the selected row from tblGh
                    model1.removeRow(selectedRow);

                    // Recalculate and update the total amount
                    calculateTotalAmount();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }
        });
    }

    public void ketNoiCsdl() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlserver://localhost:1433; databaseName = DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true";// them doan cuoi vao url
        String user = "sa";
        String pass = "123456";
        ketNoi = DriverManager.getConnection(url, user, pass);
    }

    public void tenCotBang1() {
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

    public void tenCotBang2() {
        //model.setColumnCount(0);
        model1.addColumn("STT");
        model1.addColumn("Mã sản phẩm");
        model1.addColumn("Tên sản phẩm");
        model1.addColumn("Số lượng");
        model1.addColumn("Đơn giá");
        model1.addColumn("Thành tiền");
        tblGh.setModel(model1);
    }

    public void tenCotBangHoaDon() {
        model2.addColumn("STT");
        model2.addColumn("Mã hóa đơn");
        model2.addColumn("Tên NV");
        model2.addColumn("Tên KH");
        model2.addColumn("Trạng thái");
        model2.addColumn("Ngày tạo");
        tblHoaDon.setModel(model2);

        // Set column widths
        TableColumnModel columnModel = tblHoaDon.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); // Set width for "STT" column
        columnModel.getColumn(1).setPreferredWidth(100); // Set width for "Mã hóa đơn" column
        columnModel.getColumn(2).setPreferredWidth(150); // Set width for "Tên NV" column
        columnModel.getColumn(3).setPreferredWidth(150); // Set width for "Tên KH" column
        columnModel.getColumn(4).setPreferredWidth(100); // Set width for "Trạng thái" column
        columnModel.getColumn(5).setPreferredWidth(100); // Set width for "Ngày tạo" column
    }

    public void tenCotBangHoaDonCT() {
        model3.addColumn("Mã sản phẩm");
        model3.addColumn("Tên sản phẩm");
        model3.addColumn("Số lượng");
        model3.addColumn("Đơn giá");
        tblHdct.setModel(model3);
    }

    private void calculateTotalAmount() {
        int totalAmount = 0;
        for (int i = 0; i < model1.getRowCount(); i++) {
            int thanhTien = (Integer) model1.getValueAt(i, 5);
            totalAmount += thanhTien;
        }
        txtTongTien_Money.setText(String.valueOf(totalAmount));
        txtPhaiTra_Money.setText(String.valueOf(totalAmount));
    }

    private Map<String, String> getTenNhanVienFromMaNhanVien(String maNV) {
        Map<String, String> khachHangDetails = new HashMap<>();

        // Câu truy vấn SQL để lấy thông tin khách hàng từ mã khách hàng
        String query = "SELECT TEN_KH, DIA_CHI, SDT FROM KHACH_HANG WHERE ID_KH = ?";

        try (
                // Kết nối đến cơ sở dữ liệu
                Connection con = DriverManager.getConnection(url); // Chuẩn bị câu truy vấn
                 PreparedStatement pst = con.prepareStatement(query);) {
            // Đặt giá trị cho tham số trong câu truy vấn
            pst.setString(1, maNV);

            // Thực thi truy vấn
            try (ResultSet rs = pst.executeQuery()) {
                // Nếu có kết quả trả về
                if (rs.next()) {
                    khachHangDetails.put("tenNV", rs.getString("TEN_KH"));
                    khachHangDetails.put("diaChi", rs.getString("DIA_CHI"));
                    khachHangDetails.put("sdt", rs.getString("SDT"));
                }
            }
        } catch (SQLException ex) {
            // Xử lý ngoại lệ nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
            ex.printStackTrace();
        }

        return khachHangDetails.isEmpty() ? null : khachHangDetails;
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

    // Mô hình bảng không thể chỉnh sửa
    public static class NonEditableTableModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho phép chỉnh sửa ô
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSp = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGh = new javax.swing.JTable();
        btnXoa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        txtTongTien_Money = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPhaiTra_Money = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtKhachDuaMoney = new javax.swing.JTextField();
        txtTienThua_Money = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnTaoHD = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        cboThanhToan = new javax.swing.JComboBox<>();
        rdoGiaoHang = new javax.swing.JRadioButton();
        txtMaNV = new javax.swing.JTextField();
        btnThemKH = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHdct = new javax.swing.JTable();
        lblBlue = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        lblOr = new javax.swing.JLabel();
        lblThoat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Bill.png"))); // NOI18N
        jLabel1.setText("Thanh toán");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

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
        jScrollPane2.setViewportView(tblSp);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnReset)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách đã chọn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tblGh.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tblGh);

        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnXoa)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblHoaDon);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tạo hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel3.setText("Tên khách hàng ");

        txtTenKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setText("Số điện thoại");

        txtSdt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setText("Địa chỉ");

        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setText("Tổng tiền");

        txtTongTien_Money.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTongTien_Money.setDisabledTextColor(new java.awt.Color(204, 0, 51));

        jLabel6.setText("Tiền  phải trả");

        txtPhaiTra_Money.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPhaiTra_Money.setDisabledTextColor(new java.awt.Color(51, 51, 255));

        jLabel7.setText("Khách đưa");

        txtKhachDuaMoney.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtKhachDuaMoney.setForeground(new java.awt.Color(255, 153, 0));

        txtTienThua_Money.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel8.setText("Tiền thừa");

        btnTaoHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Create.png"))); // NOI18N
        btnTaoHD.setText("Tạo hoá đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        txtGhiChu.setBorder(javax.swing.BorderFactory.createTitledBorder("Ghi chú"));
        jScrollPane4.setViewportView(txtGhiChu);

        cboThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phương thức thanh toán", "Tiền mặt", "Chuyển Khoản" }));

        rdoGiaoHang.setText("Giao hàng");

        txtMaNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtMaNV.setForeground(new java.awt.Color(153, 0, 255));
        txtMaNV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaNV.setDisabledTextColor(new java.awt.Color(204, 0, 255));

        btnThemKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Plus Math.png"))); // NOI18N
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSdt, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                .addComponent(txtDiaChi))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTenKH)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtKhachDuaMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(txtTienThua_Money, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtTongTien_Money, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtPhaiTra_Money, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoGiaoHang)
                                .addComponent(cboThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTongTien_Money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnThemKH))
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhaiTra_Money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKhachDuaMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTienThua_Money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoGiaoHang)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTaoHD)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        jPanel6.setLayout(new java.awt.BorderLayout());

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
        jScrollPane5.setViewportView(tblHdct);

        jPanel6.add(jScrollPane5, java.awt.BorderLayout.CENTER);

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThoat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblOr)
                        .addComponent(lblGreen)
                        .addComponent(lblBlue)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblThoat)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed

    }//GEN-LAST:event_btnTaoHDActionPerformed

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

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        dispose();
        HomeFrame homeFrame = new HomeFrame(maNhanVien, fullName, chucVu);
        homeFrame.setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        try {
            TaiDulieuVaoBang();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TaoHoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaoHoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKHActionPerformed
         try {
                    // Create and open TaoHoaDonFrame with the employee ID
                    dispose();
                    new KhachHangFormHoaDon(maNhanVien,fullName, chucVu).setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
    }//GEN-LAST:event_btnThemKHActionPerformed

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
            java.util.logging.Logger.getLogger(TaoHoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaoHoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaoHoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaoHoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TaoHoaDonFrame("Mã nhân viên", "Họ và Tên", "Chức vụ").setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TaoHoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TaoHoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThemKH;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboThanhToan;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JRadioButton rdoGiaoHang;
    private javax.swing.JTable tblGh;
    private javax.swing.JTable tblHdct;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblSp;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtKhachDuaMoney;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtPhaiTra_Money;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTienThua_Money;
    private javax.swing.JTextField txtTongTien_Money;
    // End of variables declaration//GEN-END:variables
}
