/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author raghav
 */
public class DbConfig {
    public static String driverName = "org.postgresql.Driver";
    public static String url = "jdbc:postgresql://127.0.0.1:5432/iitbdb";
    public static String username = "raghav";
    public static String password = "****";
    
    public static String IITB_INFO_TABLE = "iitb_info";
    
    Connection connection;
    
    public DbConfig() throws SQLException, ClassNotFoundException {
        System.out.println("Connecting to : " + DbConfig.url);
        Class.forName(DbConfig.driverName);
        connection = DriverManager.getConnection(DbConfig.url, DbConfig.username, DbConfig.password);
        System.out.println("[DB] Opened connection.");
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
        System.out.println("[DB] connection closed.");
    }
    
}
