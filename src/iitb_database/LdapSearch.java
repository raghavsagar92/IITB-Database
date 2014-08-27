/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iitb_database;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;
import utils.Config;

/**
 *
 * @author raghav
 */
public class LdapSearch {
    public static String fillFilter(String filter, String start, String end) {
        if(start != null)
            filter = filter.replaceFirst(Pattern.quote("?"), start);
        if(end != null)
            filter = filter.replaceFirst(Pattern.quote("?"), end);
        return filter;
    }
    
    public static String getDepartment(String homeDirectory) {
        String[] fields = homeDirectory.split(Pattern.quote("/"));
        if(fields.length < 2)
            return "";
        return fields[fields.length - 2];
    }
    
    public void searchRange() throws LDAPException, UnsupportedEncodingException, FileNotFoundException, IOException {
        LDAPConnection lc  = new LDAPConnection();
        lc.connect(Config.LDAP_HOST, Config.LDAP_PORT);
        lc.bind(LDAPConnection.LDAP_V3, Config.LDAP_BASE_DN, (byte[])null);
        
        LDAPSearchResults results = null;
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                                            new FileOutputStream(Config.LDAP_DUMP_FILE), "UTF-8"));
        int uidStart = 0;
        while(uidStart < Config.UID_CAP) {
            try {
                String filter = fillFilter(Config.LDAP_FILTER_RANGE, Integer.toString(uidStart), Integer.toString(uidStart + Config.QUERY_MAX_RESULTS - 1));
                results = lc.search(Config.LDAP_BASE_DN, LDAPConnection.SCOPE_SUB, filter, Config.LDAP_ATTRIBUTES, false);
                while (results.hasMore()) {
                    LDAPEntry ldapEntry = results.next();
                    String nextDN = ldapEntry.getDN();
                    //System.out.println(nextDN);
                    for(String attribute : Config.LDAP_ATTRIBUTES) {
                        LDAPAttribute attributeVal = ldapEntry.getAttribute(attribute);
                        if(attributeVal == null) {
                            writer.write("\t");
                            continue;
                        }
                        Enumeration enumeration = attributeVal.getStringValues();
                        if(enumeration.hasMoreElements()) {
                            writer.write(enumeration.nextElement().toString());
                        }
                        writer.write("\t");
                    }
                    writer.write("\n");
                }
                Date date = new Date();
                System.out.println("Uid : " + uidStart + "\t" + date.toString());
                uidStart += Config.QUERY_MAX_RESULTS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writer.close();
        lc.disconnect();
    }
    
    public void search() throws LDAPException, UnsupportedEncodingException, FileNotFoundException, IOException {
        LDAPConnection lc  = new LDAPConnection();
        lc.connect(Config.LDAP_HOST, Config.LDAP_PORT);
        lc.bind(LDAPConnection.LDAP_V3, Config.LDAP_BASE_DN, (byte[])null);
        
        LDAPSearchResults results = null;
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                                            new FileOutputStream(Config.LDAP_DUMP_FILE), "UTF-8"));
        int uidStart = 0;
        while(uidStart < Config.UID_CAP) {
            try {
                String filter = fillFilter(Config.LDAP_FILTER_EQUAL, Integer.toString(uidStart), null);
                results = lc.search(Config.LDAP_BASE_DN, LDAPConnection.SCOPE_SUB, filter, Config.LDAP_ATTRIBUTES, false);
                while(results.hasMore()) {
                    LDAPEntry ldapEntry = results.next();
                    String nextDN = ldapEntry.getDN();
                    //System.out.println(nextDN);
                    for(String attribute : Config.LDAP_ATTRIBUTES) {
                        LDAPAttribute attributeVal = ldapEntry.getAttribute(attribute);
                        if(attributeVal == null) {
                            writer.write("\t");
                            continue;
                        }
                        Enumeration enumeration = attributeVal.getStringValues();
                        if(enumeration.hasMoreElements()) {
                            writer.write(enumeration.nextElement().toString());
                        }
                        writer.write("\t");
                    }
                    writer.write("\n");
                }
                Date date = new Date();
                System.out.println("Uid : " + uidStart + "\t" + date.toString());
                ++uidStart;
            } catch (Exception e) {
                System.out.println("Error in : " + uidStart);
                e.printStackTrace();
                ++uidStart;
            }
        }
        writer.close();
        lc.disconnect();
    }
}


