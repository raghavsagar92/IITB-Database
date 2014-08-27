/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iitb_database;

import com.novell.ldap.LDAPException;
import db.DbConfig;
import db.IitbInfo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import utils.Config;

/**
 *
 * @author raghav
 */
public class IITB_Database {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
            throws LDAPException, FileNotFoundException, IOException, SQLException, ClassNotFoundException {
        
        System.out.println("Searching in " + Config.LDAP_HOST);
        LdapSearch ldapSearch = new LdapSearch();
        ldapSearch.search();
        System.out.println("Ldap Search complete.");
        
        System.out.println("Dumping in Database.");
        DbConfig dbConfig = new DbConfig();
        IitbInfo studentInfo = new IitbInfo();
        studentInfo.createTable( dbConfig.getConnection() );
        studentInfo.clearTable(dbConfig.getConnection() );
        studentInfo.populateTable( dbConfig.getConnection() );
        studentInfo.createIndexes( dbConfig.getConnection() );
        dbConfig.close();
        System.out.println("Database dump complete.");
    }
    
}
