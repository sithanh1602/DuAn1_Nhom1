package CheckForm;

import java.sql.*;

public class RetrievePassword {
      private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DU_AN_1_GROUP1_DIENMAY3;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    public static void main(String[] args) {
        String query = "SELECT MAT_KHAU FROM NHAN_VIEN WHERE ID_NV = 'NV5212'";
        
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
             
            if (rs.next()) {
                String hashedPassword = rs.getString("MAT_KHAU");
                System.out.println("Hashed Password: " + hashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
