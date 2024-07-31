package CheckForm;

import org.mindrot.jbcrypt.BCrypt;

public class AutoPasswordEncryption {

    // Phương thức mã hóa mật khẩu
    public static String hashPassword(String plainPassword) {
        // Tạo salt và mã hóa mật khẩu
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
      // Hàm kiểm tra xem chuỗi mật khẩu đã mã hóa hay chưa
    public static boolean isHashed(String password) {
        // Kiểm tra định dạng chuỗi mật khẩu để xác định xem nó đã mã hóa hay chưa
        // Bcrypt hash luôn bắt đầu với $2a$ hoặc $2b$
        return password.startsWith("$2a$") || password.startsWith("$2b$");
    }

    // Phương thức kiểm tra mật khẩu
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // So sánh mật khẩu đã mã hóa với mật khẩu nhập vào
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Ví dụ về cách sử dụng lớp này
    public static void main(String[] args) {
        // Mã hóa mật khẩu
        String plainPassword = "mySecurePassword";
        String hashedPassword = hashPassword(plainPassword);
        System.out.println("Mật khẩu đã mã hóa: " + hashedPassword);

        // Kiểm tra mật khẩu
        boolean isPasswordCorrect = checkPassword(plainPassword, hashedPassword);
        System.out.println("Mật khẩu chính xác: " + isPasswordCorrect);
    }

   
}
