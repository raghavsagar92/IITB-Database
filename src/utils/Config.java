/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author raghav
 */
public class Config {
    public static final String LDAP_HOST = "ldap.iitb.ac.in";
    public static final int LDAP_PORT = 389;
    public static final String LDAP_BASE_DN = "dc=iitb,dc=ac,dc=in";
    public static final String[] LDAP_ATTRIBUTES = {"uidNumber", "uid", "employeeNumber", "employeeType", "cn", "homeDirectory"};
    public static final String LDAP_FILTER_RANGE = "(&(uidNumber>=?)(uidNumber<=?))"; 
    public static final String LDAP_FILTER_EQUAL = "(uidNumber=?)"; 
    
    public static final int QUERY_MAX_RESULTS = 50;
    public static final int UID_CAP = 60001;
    
    /**************** File Paths **************/
    public static String LDAP_DUMP_FILE = "data/dumps/iitb_info.dat";       
}
