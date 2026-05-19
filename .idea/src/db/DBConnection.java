import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // 개발할 때 heatwave로 URL 변경
    private static final String URL = "jdbc:mysql://your-host:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("DB 연결 성공!");
            } catch (SQLException e) {
                System.out.println("DB 연결 실패: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("DB 연결 종료");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}