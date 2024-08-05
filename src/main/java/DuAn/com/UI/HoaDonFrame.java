/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.AddID_Auto;
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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author NITRO 5
 */
public class HoaDonFrame extends javax.swing.JFrame {
    ImageIcon icon;
    private String maNhanVien;
    private String fullName;
    private String chucVu;
    public void doiIcon() {
        icon = new ImageIcon("src/main/resources/images/Technology.png");
        setIconImage(icon.getImage());
    }
    DefaultTableModel model = new NonEditableTableModel();
    DefaultTableModel model1 = new NonEditableTableModel();
    Connection ketNoi;

    public HoaDonFrame(String maNV, String fullName, String chucVu) throws ClassNotFoundException, SQLException {
        initComponents();
        init();
        this.maNhanVien = maNV;
        this.fullName = fullName;
        this.chucVu = chucVu;
        AddID_Auto addID_Auto = new AddID_Auto();
        addID_Auto.initTextFieldMap(this); // Khởi tạo các JTextField từ lớp NhanVienForm
        addID_Auto.setTextFieldValues();
        addMouseClickListener();
        tenCotBang1();
        tenCotBangChiTiet();
        TaiDulieuVaoBang();
        formatCurrencyColumns(); // Format currency columns

        // Add action listener for button sự kiện click nút cap nhập
        btnCapNhap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateInvoiceStatus();// hàm cập nhập trạng thái
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(HoaDonFrame.this, "Lỗi khi cập nhật trạng thái hóa đơn.");
                }
            }
        });

        btnInHoaDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblHoaDon.getSelectedRow();

                // Kiểm tra nếu có chọn hóa đơn
                if (selectedRow != -1) {
                    // Hiển thị hộp thoại xác nhận trước khi in
                    int response = JOptionPane.showConfirmDialog(
                            HoaDonFrame.this,
                            "Bạn có muốn in hóa đơn không?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION
                    );

                    // Nếu người dùng chọn Yes
                    if (response == JOptionPane.YES_OPTION) {
                        String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 0);
                        String[] details = new String[10];
                        details[0] = (String) tblHoaDon.getValueAt(selectedRow, 0);
                        details[1] = (String) tblHoaDon.getValueAt(selectedRow, 1);
                        details[2] = (String) tblHoaDon.getValueAt(selectedRow, 2);
                        details[3] = (String) tblHoaDon.getValueAt(selectedRow, 3);

                        // Định dạng tiền tệ
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        details[4] = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 4));
                        details[5] = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 5));
                        details[6] = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 6));

                        details[7] = (String) tblHoaDon.getValueAt(selectedRow, 7);
                        details[8] = (String) tblHoaDon.getValueAt(selectedRow, 8);
                        details[9] = "Additional detail"; // Placeholder for any additional detail

                        try {
                            String productDetails = laySanPhamDaMua(idHoaDon);
                            InHoaDon inHoaDon = new InHoaDon();
                            inHoaDon.setInvoiceDetails(details, productDetails);
                            inHoaDon.setVisible(true);
                        } catch (ClassNotFoundException | SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(HoaDonFrame.this, "Lỗi khi hiển thị hóa đơn.");
                        }
                    } else {
                        // Nếu người dùng chọn No, không làm gì cả
                        JOptionPane.showMessageDialog(HoaDonFrame.this, "In hóa đơn bị hủy.");
                    }
                } else {
                    JOptionPane.showMessageDialog(HoaDonFrame.this, "Vui lòng chọn hóa đơn để in.");
                }
            }
        });

    }

    //hàm lấy sản phẩm để in ra trong txtSanPhamDaMua
    private String laySanPhamDaMua(String idHoaDon) throws ClassNotFoundException, SQLException {
        ketNoiCsdl();
        String sql = "SELECT SP.TEN_SP, CT.SO_LUONG "
                + "FROM CHI_TIET_HOA_DON CT "
                + "JOIN SAN_PHAM SP ON CT.ID_SP = SP.ID_SP "
                + "WHERE CT.ID_HOA_DON = ?";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        cauLenh.setString(1, idHoaDon);
        ResultSet ketQua = cauLenh.executeQuery();
        StringBuilder sb = new StringBuilder();
        while (ketQua.next()) {
            String tenSanPham = ketQua.getString("TEN_SP");
            int soLuong = ketQua.getInt("SO_LUONG");
            sb.append("Sản phẩm: ").append(tenSanPham).append(" - Số lượng: ").append(soLuong).append("\n");
        }
        ketNoi.close();
        return sb.toString();
    }

    // hàm cập nhập trạng thái
    private void updateInvoiceStatus() throws ClassNotFoundException, SQLException {
        int selectedRow = tblHoaDon.getSelectedRow();

        // Kiểm tra nếu chưa chọn dòng trên bảng
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(HoaDonFrame.this, "Vui lòng chọn hóa đơn để cập nhật.");
            return; // Dừng thực thi nếu không chọn hóa đơn
        }

        // Kiểm tra nếu chưa chọn trạng thái
        if (!rdoChuaThanhToan.isSelected() && !rdoDaThanhToan.isSelected() && !rdoDaThanhToanGiaoHang.isSelected()) {
            JOptionPane.showMessageDialog(HoaDonFrame.this, "Bạn cần chọn trạng thái cho hóa đơn.");
            return; // Dừng thực thi nếu chưa chọn trạng thái
        }

        // Lấy ID hóa đơn được chọn
        String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 0);
        String trangThai = "";

        // Xác định trạng thái dựa trên radio button được chọn
        if (rdoChuaThanhToan.isSelected()) {
            trangThai = "Chưa thanh toán";
        } else if (rdoDaThanhToan.isSelected()) {
            trangThai = "Đã thanh toán";
        } else if (rdoDaThanhToanGiaoHang.isSelected()) {
            trangThai = "Đã thanh toán, đang giao hàng";
        }

        // Cập nhật trạng thái hóa đơn trong cơ sở dữ liệu
        ketNoiCsdl();
        String sql = "UPDATE HOA_DON SET TRANG_THAI = ? WHERE ID_HOA_DON = ?";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        cauLenh.setString(1, trangThai);
        cauLenh.setString(2, idHoaDon);
        int rowsUpdated = cauLenh.executeUpdate();

        // Thông báo kết quả cập nhật
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(HoaDonFrame.this, "Cập nhật trạng thái hóa đơn thành công.");
            TaiDulieuVaoBang(); // Refresh data
        } else {
            JOptionPane.showMessageDialog(HoaDonFrame.this, "Không tìm thấy hóa đơn.");
        }

        ketNoi.close();
    }

    private void formatCurrencyColumns() {
        //MoneyRenderer cho các cột có giá trị tiền tệ
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
        txtSpDaMua.setEditable(false); // Make it read-only
        lblOr.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblBlue.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblGreen.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
        lblThoat.setCursor(new Cursor(Cursor.HAND_CURSOR) {
        });
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
            model.addRow(new Object[]{idHD, idNV, idKH, idDate, tongTien, tienKhachDua, tienThua, GhiChu, trangThai});
        }
        tblHoaDon.setModel(model);
        ketNoi.close();
    }

    public void addMouseClickListener() {
        tblHoaDon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
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

        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = tblHoaDon.getSelectedRow();
                    if (selectedRow != -1) {
                        String idHoaDon = (String) tblHoaDon.getValueAt(selectedRow, 0);
                        label1.setText(idHoaDon);
                        label2.setText((String) tblHoaDon.getValueAt(selectedRow, 1));
                        label3.setText((String) tblHoaDon.getValueAt(selectedRow, 2));
                        label4.setText((String) tblHoaDon.getValueAt(selectedRow, 3));

                        // Định dạng tiền tệ
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String giaFormatted = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 4));
                        String tongTienFormatted = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 5));
                        String tienKhachDuaFormatted = currencyFormat.format(tblHoaDon.getValueAt(selectedRow, 6));

                        label5.setText(giaFormatted);
                        label6.setText(tongTienFormatted);
                        label7.setText(tienKhachDuaFormatted);

                        label8.setText((String) tblHoaDon.getValueAt(selectedRow, 7));
                        label9.setText((String) tblHoaDon.getValueAt(selectedRow, 8));

                        try {
                            hienThiChiTietHoaDon(idHoaDon);
                            hienThiSanPhamDaMua(idHoaDon); // Hiển thị tên sản phẩm đã mua và số lượng
                        } catch (ClassNotFoundException | SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(HoaDonFrame.this, "Lỗi khi hiển thị chi tiết hóa đơn.");
                        }
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

    private void hienThiSanPhamDaMua(String idHoaDon) throws ClassNotFoundException, SQLException {
        ketNoiCsdl();
        String sql = "SELECT SP.TEN_SP, CT.SO_LUONG "
                + "FROM CHI_TIET_HOA_DON CT "
                + "JOIN SAN_PHAM SP ON CT.ID_SP = SP.ID_SP "
                + "WHERE CT.ID_HOA_DON = ?";
        PreparedStatement cauLenh = ketNoi.prepareStatement(sql);
        cauLenh.setString(1, idHoaDon);
        ResultSet ketQua = cauLenh.executeQuery();
        StringBuilder sb = new StringBuilder();
        while (ketQua.next()) {
            String tenSanPham = ketQua.getString("TEN_SP");
            int soLuong = ketQua.getInt("SO_LUONG");
            sb.append("Sản phẩm: ").append(tenSanPham).append("  SL: ").append(soLuong).append("\n");
        }
        if (sb.length() > 0) {
            txtSpDaMua.setText(sb.toString());
        } else {
            txtSpDaMua.setText("Không tìm thấy sản phẩm.");
        }
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
        btnCapNhap = new javax.swing.JButton();
        rdoChuaThanhToan = new javax.swing.JRadioButton();
        rdoDaThanhToan = new javax.swing.JRadioButton();
        rdoDaThanhToanGiaoHang = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label5 = new javax.swing.JLabel();
        label6 = new javax.swing.JLabel();
        label7 = new javax.swing.JLabel();
        label8 = new javax.swing.JLabel();
        label9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSpDaMua = new javax.swing.JTextArea();
        btnInHoaDon = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblThoat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
        );

        btnCapNhap.setText("Cập nhập ");

        buttonGroup1.add(rdoChuaThanhToan);
        rdoChuaThanhToan.setText("Chưa thanh toán");

        buttonGroup1.add(rdoDaThanhToan);
        rdoDaThanhToan.setText("Đã thanh toán");

        buttonGroup1.add(rdoDaThanhToanGiaoHang);
        rdoDaThanhToanGiaoHang.setText("Đã thanh toán, đang giao hàng");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Mã hoá đơn :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Mã nhân viên :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Mã khách hàng :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Ngày tạo :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Tổng tiền :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Tiền khách đưa :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Tiền thừa :");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Ghi chú :");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Trạng thái :");

        label1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label2.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label3.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label4.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label5.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label6.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label7.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label8.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        label9.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Sản phẩm đã mua :");

        txtSpDaMua.setColumns(20);
        txtSpDaMua.setRows(5);
        txtSpDaMua.setBorder(null);
        jScrollPane3.setViewportView(txtSpDaMua);

        btnInHoaDon.setText("In hoá đơn");
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(label9, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                        .addComponent(label8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(btnInHoaDon)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnInHoaDon)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(label1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(label2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(label3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(label4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(label5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(label6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(label8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(label9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rdoDaThanhToanGiaoHang)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoDaThanhToan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoChuaThanhToan)
                                .addGap(18, 18, 18)
                                .addComponent(btnCapNhap))
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addGap(14, 14, 14)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoDaThanhToanGiaoHang)
                            .addComponent(rdoDaThanhToan)
                            .addComponent(rdoChuaThanhToan)
                            .addComponent(btnCapNhap))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

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
                .addComponent(lblThoat)
                .addGap(0, 277, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(lblThoat))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
        new HomeFrame(maNhanVien,fullName, chucVu).setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInHoaDonActionPerformed

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
                    new HoaDonFrame("Mã nhân viên", "Họ và Tên","Chức vụ").setVisible(true);
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
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblOr;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JRadioButton rdoChuaThanhToan;
    private javax.swing.JRadioButton rdoDaThanhToan;
    private javax.swing.JRadioButton rdoDaThanhToanGiaoHang;
    private javax.swing.JTable tblHdct;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextArea txtSpDaMua;
    // End of variables declaration//GEN-END:variables
}
