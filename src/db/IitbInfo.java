/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import iitb_database.LdapSearch;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang3.text.WordUtils;
import utils.Config;

/**
 *
 * @author raghav
 */
public class IitbInfo {
    static final int BATCH_MAX_SIZE = 3000;
    static String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + DbConfig.IITB_INFO_TABLE;
    static String CLEAR_TABLE_SQL = "DELETE FROM " + DbConfig.IITB_INFO_TABLE;
    static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + DbConfig.IITB_INFO_TABLE + "(" +
                                    "iitb_id int NOT NULL,\n" +
                                    "iitb_ldap_id varchar(63) NOT NULL DEFAULT '',\n" +
                                    "iitb_roll_no varchar(30) NOT NULL DEFAULT '',\n" +
                                    "iitb_type varchar(15) NOT NULL DEFAULT '',\n" +                        
                                    "iitb_name varchar(100) NOT NULL DEFAULT '',\n" +
                                    "iitb_dept varchar(15) NOT NULL DEFAULT ''\n" +
                                    ");";
    
    static String CREATE_INDEX_SQL = "CREATE INDEX iitb_id_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_id);" +
                                    "CREATE INDEX iitb_ldap_id_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_ldap_id);" +
                                    "CREATE INDEX iitb_roll_no_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_roll_no);" +
                                    "CREATE INDEX iitb_type_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_type);" +
                                    "CREATE INDEX iitb_name_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_name);" +
                                    "CREATE INDEX iitb_dept_key ON " + DbConfig.IITB_INFO_TABLE + "(iitb_dept);";
    
    static String INSERT_TABLE_SQL = "INSERT INTO " + DbConfig.IITB_INFO_TABLE +
                                        " (iitb_id, iitb_ldap_id, iitb_roll_no, iitb_type, iitb_name, iitb_dept) VALUES " +
                                        " (?,?,?,?,?,?); ";
    
    public static String SELECT_TABLE_ID_SQL = "SELECT iitb_id, iitb_ldap_id, iitb_roll_no, iitb_type, iitb_name, iitb_dept FROM " + 
                                        DbConfig.IITB_INFO_TABLE +
                                        " WHERE iitb_id=? ; ";
    
    public IitbInfo() {}
    
    public void createTable(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(DROP_TABLE_SQL);
        stmt.executeUpdate(CREATE_TABLE_SQL);
    }
    public void createIndexes(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(CREATE_INDEX_SQL);
    }
    public void clearTable(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(CLEAR_TABLE_SQL);
    }
    
    public void populateTable(Connection connection) 
            throws SQLException, UnsupportedEncodingException, FileNotFoundException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TABLE_SQL);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                                            new FileInputStream(Config.LDAP_DUMP_FILE), "UTF-8"));
        int batchCounter = 0;
        String line;
        while((line = br.readLine()) != null) {
            String[] fields = line.split("\\t");
            Integer id = Integer.parseInt(fields[0]);
            String ldapId = fields[1];
            String rollNo = fields[2];
            String employeeType = fields[3];
            String name = WordUtils.capitalizeFully(fields[4]);
            String department = LdapSearch.getDepartment(fields[5]);
            
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, ldapId);
            preparedStatement.setString(3, rollNo);
            preparedStatement.setString(4, employeeType);
            preparedStatement.setString(5, name);
            preparedStatement.setString(6, department);
            preparedStatement.addBatch();
            ++batchCounter;
            if(batchCounter >= BATCH_MAX_SIZE) {
                batchCounter = 0;
                preparedStatement.executeBatch();
            }
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        br.close();
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        DbConfig dbConfig = new DbConfig();
        IitbInfo studentInfo = new IitbInfo();
        studentInfo.createTable( dbConfig.getConnection() );
        studentInfo.clearTable(dbConfig.getConnection() );
        studentInfo.populateTable( dbConfig.getConnection() );
        studentInfo.createIndexes( dbConfig.getConnection() );
        dbConfig.close();
    }
}
