package com.panda;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.schema.SchemaManager;

/**
 * @Program: VirtualLdapDemo
 * @Description:
 * @Author: JinHu
 * @Create: 2019-09-09 14:52
 */
public class LdapUtil {
    public static Entry innerGetUserInfoByCn(SchemaManager schemaManager, String user) {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        try {
            Entry entry = new DefaultEntry(schemaManager);
            String cn = propertiesUtil.getUserCn(user);
            String dn = propertiesUtil.getUserDn(user);
            String email = propertiesUtil.getUserEmail(user);
            String password = propertiesUtil.getUserPassword(user);

            entry.setDn(dn);
            entry.add("objectClass", "top", "inetOrgPerson", "organizationalPerson", "person");
            entry.add("cn", cn);
            entry.add("mail", email);
            entry.add("userPassword", password);

            return entry;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Entry innerGetUserInfoByDn(SchemaManager schemaManager, String dn) {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        try {
            Entry entry = new DefaultEntry(schemaManager);
            String user = getUserCnByDn(dn);
            String cn = propertiesUtil.getUserCn(user);
            String email = propertiesUtil.getUserEmail(user);
            String password = propertiesUtil.getUserPassword(user);

            entry.setDn(dn);
            entry.add("objectClass", "top", "inetOrgPerson", "organizationalPerson", "person");
            entry.add("cn", cn);
            entry.add("mail", email);
            entry.add("userPassword", password);

            return entry;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String getUserCnByDn(String dn) {
        String []strings = dn.split(",");
        for (String s : strings) {
            String []kv = s.split("=");
            if (kv[0].equals("cn")) {
                return kv[1];
            }
        }

        return null;
    }

    public static String getAdminCnByDn(String dn) {
        String []strings = dn.split(",");
        for (String s : strings) {
            String []kv = s.split("=");
            if (kv[0].equals("uid")) {
                return kv[1];
            }
        }

        return null;
    }

}
