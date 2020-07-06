import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class ConnectionBD {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/telega";
        String user = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM users WHERE id = 1";

            try (PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    System.out.println(rs.getInt("id"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}