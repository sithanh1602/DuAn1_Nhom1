/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DuAn.com.UI;

import CheckForm.DateFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author NITRO 5
 */
public class ThongKeFrame extends javax.swing.JFrame {
    ImageIcon icon;
    private String maNhanVien;
    private String fullName;
    private String chucVu;
    public void doiIcon() {
        icon = new ImageIcon("src/main/resources/images/Technology.png");
        setIconImage(icon.getImage());
    }
    String url = "jdbc:sqlserver://localhost:1433;database=DU_AN_1_GROUP1_DIENMAY3;integratedSecurity=false;user=sa;password=123456;encrypt=true;trustServerCertificate=true;";

    /**
     * Creates new form ThongKeFrame
     */
    public ThongKeFrame(String maNV, String fullName, String chucVu) {
        initComponents();
        init();
        this.maNhanVien = maNV;
        this.fullName = fullName;
        this.chucVu = chucVu;
        JFrame frame = new JFrame("Pie Charts Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 3));

        // Add panels to the frame
        frame.add(pnlChartSoLuongBan);
        frame.add(pnlChartKho);
        frame.add(pnlChartDoanhSo);

        pnlChartSoLuongBan.setBorder(BorderFactory.createTitledBorder("Số lượng bán"));
        pnlChartKho.setBorder(BorderFactory.createTitledBorder("Kho"));
        pnlChartDoanhSo.setBorder(BorderFactory.createTitledBorder("Doanh số"));

        

        frame.pack();
        frame.setVisible(true);
        loadTable_Kho();
        txtMaxDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadTable_HDCT();
                    loadTable_Doanhso();
                    loadChart();
                }
            }
        });
        DateFormatter dateFormatter = new DateFormatter(this);
        createChartPanel();
        ChartPanel chartPanel = createChartPanel();
        // Tạo biểu đồ và thêm chúng vào các panel tương ứng
        pnlChartSoLuongBan.setLayout(new BorderLayout());
        pnlChartSoLuongBan.add(createChartPanel(createDatasetSoLuongBan()), BorderLayout.CENTER);

        pnlChartKho.setLayout(new BorderLayout());
        pnlChartKho.add(createChartPanel(createDatasetKho()), BorderLayout.CENTER);

        pnlChartDoanhSo.setLayout(new BorderLayout());
        pnlChartDoanhSo.add(createChartPanel(createDatasetDoanhSo()), BorderLayout.CENTER);

    }

    private ChartPanel createChartPanel(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Biểu đồ", // Tiêu đề biểu đồ
                dataset, // Dữ liệu cho biểu đồ
                true, // Hiển thị chú thích
                true,
                false
        );
        return new ChartPanel(chart);
    }

    private static JPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                if (size.width > 300) {
                    size.width = 300; // Set max width
                }
                if (size.height > 300) {
                    size.height = 300; // Set max height
                }
                return size;
            }
        };
        panel.setPreferredSize(new Dimension(300, 300)); // Set preferred size
        return panel;
    }

    private PieDataset createDatasetKho() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        String minDate = txtMinDate.getText();
        String maxDate = txtMaxDate.getText();
        String query = "SELECT ID_SP, TEN_SP, SUM(SL_TONKHO) AS SO_TON_KHO FROM SAN_PHAM A GROUP BY ID_SP, TEN_SP";

        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("TEN_SP");
                    int stockQuantity = rs.getInt("SO_TON_KHO");
                    dataset.setValue(productName, stockQuantity);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu biểu đồ tồn kho: " + ex.getMessage());
        }

        return dataset;
    }

    private PieDataset createDatasetDoanhSo() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        String minDate = txtMinDate.getText();
        String maxDate = txtMaxDate.getText();
        String query = "SELECT A.ID_NV, TEN_NV, SUM(TONG_TIEN) AS TONG_DOANHTHU "
                + "FROM NHAN_VIEN A INNER JOIN HOA_DON B ON A.ID_NV = B.ID_NV "
                + "INNER JOIN CHI_TIET_HOA_DON C ON C.ID_HOA_DON = B.ID_HOA_DON "
                + "WHERE NGAY_HOA_DON BETWEEN ? AND ? "
                + "GROUP BY A.ID_NV, TEN_NV";

        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, minDate);
            pst.setString(2, maxDate);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String employeeName = rs.getString("TEN_NV");
                    double totalRevenue = rs.getDouble("TONG_DOANHTHU");
                    dataset.setValue(employeeName, totalRevenue);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu biểu đồ doanh thu: " + ex.getMessage());
        }

        return dataset;
    }

    private PieDataset createDatasetSoLuongBan() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        String minDate = txtMinDate.getText();
        String maxDate = txtMaxDate.getText();
        String query = "SELECT A.ID_HOA_DON, SUM(A.SO_LUONG) AS SL_BAN, B.NGAY_HOA_DON, B.TONG_TIEN, TEN_SP  FROM CHI_TIET_HOA_DON A INNER JOIN HOA_DON B ON A.ID_HOA_DON = B.ID_HOA_DON INNER JOIN SAN_PHAM C ON C.ID_SP = C.ID_SP WHERE B.NGAY_HOA_DON BETWEEN ? AND ? GROUP BY A.ID_HOA_DON, B.NGAY_HOA_DON, B.TONG_TIEN, TEN_SP";
        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, minDate);
            pst.setString(2, maxDate);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("TEN_SP");
                    int soldQuantity = rs.getInt("SL_BAN");
                    dataset.setValue(productName, soldQuantity);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu biểu đồ số lượng bán: " + ex.getMessage());
        }

        return dataset;
    }

    private JFreeChart createPieChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart(
                title, // chart title
                dataset, // data
                true, // include legend
                true,
                false);

        // Optional: Customize the chart appearance here
        return chart;
    }

// Usage example:
    private void loadCharts() {
        PieDataset datasetSoLuongBan = createDatasetSoLuongBan();
        JFreeChart chartSoLuongBan = createPieChart(datasetSoLuongBan, "Số lượng bán");
        addChartToPanel(pnlChartSoLuongBan, chartSoLuongBan);
        // Repeat for other charts (Doanh số, Kho) by creating corresponding datasets and adding them to the respective panels
    }

    private void addChartToPanel(JPanel panel, JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.validate();
    }

    private ChartPanel createChartPanel() {
        // Create a dataset
        CategoryDataset dataset = createDataset();

        // Create a chart based on the dataset
        JFreeChart chart = ChartFactory.createBarChart(
                "Sales Statistics", // Chart title
                "Category", // X-axis Label
                "Value", // Y-axis Label
                dataset, // Dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Tooltips
                false // URLs
        );

        // Customize the chart
        chart.setBackgroundPaint(Color.white);

        // Create a chart panel and set its properties
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 500));
        return chartPanel;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Fetch data from the database and add it to the dataset
        String query = "SELECT A.ID_NV, TEN_NV, COUNT(ID_SP) AS TONG_SP_BAN, SUM(SO_LUONG) AS TONG_SL_BAN, SUM(TONG_TIEN) AS TONG_DOANHTHU "
                + "FROM NHAN_VIEN A INNER JOIN HOA_DON B ON A.ID_NV = B.ID_NV INNER JOIN CHI_TIET_HOA_DON C ON C.ID_HOA_DON = B.ID_HOA_DON "
                + "WHERE NGAY_HOA_DON BETWEEN ? AND ? GROUP BY A.ID_NV, TEN_NV";

        try (Connection con = DriverManager.getConnection(url); PreparedStatement pst = con.prepareStatement(query)) {

            // Set the parameters for the query
            pst.setString(1, "2024-01-01"); // Example start date
            pst.setString(2, "2024-12-31"); // Example end date

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String employeeName = rs.getString("TEN_NV");
                    int totalProductsSold = rs.getInt("TONG_SP_BAN");
                    dataset.addValue(totalProductsSold, "Products Sold", employeeName);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dataset;
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

    private void loadChart() {
        PieDataset soLuongBanDataset = createDatasetSoLuongBan();
        addChartToPanel(pnlChartSoLuongBan, createPieChart(soLuongBanDataset, "Số lượng bán"));

        PieDataset khoDataset = createDatasetKho();
        addChartToPanel(pnlChartKho, createPieChart(khoDataset, "Kho"));

        PieDataset doanhSoDataset = createDatasetDoanhSo();
        addChartToPanel(pnlChartDoanhSo, createPieChart(doanhSoDataset, "Doanh số"));
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
        pnlChart = new javax.swing.JPanel();
        pnlChartSoLuongBan = new javax.swing.JPanel();
        pnlChartKho = new javax.swing.JPanel();
        pnlChartDoanhSo = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setLayout(new java.awt.BorderLayout());

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Kho)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Kho, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlChartSoLuongBanLayout = new javax.swing.GroupLayout(pnlChartSoLuongBan);
        pnlChartSoLuongBan.setLayout(pnlChartSoLuongBanLayout);
        pnlChartSoLuongBanLayout.setHorizontalGroup(
            pnlChartSoLuongBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
        );
        pnlChartSoLuongBanLayout.setVerticalGroup(
            pnlChartSoLuongBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlChartKhoLayout = new javax.swing.GroupLayout(pnlChartKho);
        pnlChartKho.setLayout(pnlChartKhoLayout);
        pnlChartKhoLayout.setHorizontalGroup(
            pnlChartKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        pnlChartKhoLayout.setVerticalGroup(
            pnlChartKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlChartDoanhSoLayout = new javax.swing.GroupLayout(pnlChartDoanhSo);
        pnlChartDoanhSo.setLayout(pnlChartDoanhSoLayout);
        pnlChartDoanhSoLayout.setHorizontalGroup(
            pnlChartDoanhSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlChartDoanhSoLayout.setVerticalGroup(
            pnlChartDoanhSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlChartLayout = new javax.swing.GroupLayout(pnlChart);
        pnlChart.setLayout(pnlChartLayout);
        pnlChartLayout.setHorizontalGroup(
            pnlChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlChartSoLuongBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlChartKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlChartDoanhSo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlChartLayout.setVerticalGroup(
            pnlChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartLayout.createSequentialGroup()
                .addGroup(pnlChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlChartKho, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlChartSoLuongBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlChartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlChartDoanhSo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pnlChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBlue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGreen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOr))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

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

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
         dispose();
        new HomeFrame(maNhanVien, fullName, chucVu).setVisible(true);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void lblBlueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlueMouseClicked
        if (this.getExtendedState() != LoginFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(LoginFrame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(LoginFrame.NORMAL);
        }
    }//GEN-LAST:event_lblBlueMouseClicked

    private void lblOrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOrMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblOrMouseClicked

    private void lblGreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGreenMouseClicked
        this.setExtendedState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_lblGreenMouseClicked

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
                new ThongKeFrame("Mã nhân viên", "Họ và Tên","Chức vụ").setVisible(true);
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
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlChartDoanhSo;
    private javax.swing.JPanel pnlChartKho;
    private javax.swing.JPanel pnlChartSoLuongBan;
    private javax.swing.JTable tblDoanhSo;
    private javax.swing.JTable tblHDCT_TK;
    private javax.swing.JTable tblKho_TK;
    private javax.swing.JTextField txtMaxDate;
    private javax.swing.JTextField txtMinDate;
    // End of variables declaration//GEN-END:variables
}
