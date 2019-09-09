package com.panda;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Program: VirtualLdapDemo
 * @Description:
 * @Author: JinHu
 * @Create: 2019-09-09 14:53
 */
public class PropertiesUtil {
    private static volatile PropertiesUtil utils;
    private Properties properties;

    private PropertiesUtil() {
        initProperties();
    }

    private void initProperties() {
        try {
            Properties properties = new Properties();
            File file = new File(PropertiesUtil.class.getClassLoader().getResource("test.properties").getFile());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            properties.load(inputStream);
            this.properties = properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PropertiesUtil getInstance() {
        if (utils == null) {
            synchronized (PropertiesUtil.class) {
                if (utils == null) {
                    utils = new PropertiesUtil();
                }
            }
        }
        return utils;
    }

    public String getPropertyValue(String key) {
        return (String)properties.get(key);
    }

    public String getUserCn(String user) {
        return (String) properties.get(user + ".cn");
    }

    public String getUserDn(String user) {
        return (String) properties.get(user + ".dn");
    }

    public String getUserEmail(String user) {
        return (String) properties.get(user + ".email");
    }

    public String getUserPassword(String user) {
        return (String) properties.get(user + ".password");
    }

    public static void main(String []args) {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        System.out.println( propertiesUtil.getUserCn("user01"));
    }
}
