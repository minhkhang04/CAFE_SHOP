/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pc
 */
package DAO;
import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiCSDL {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QL_CAPHE1;encrypt=false";
            String user = "sa";
            String pass = "123";

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}