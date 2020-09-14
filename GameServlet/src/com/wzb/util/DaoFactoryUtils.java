package com.wzb.util;


import java.io.IOException;
import java.util.Properties;

public class DaoFactoryUtils {
    /**
     * 配置文件
     */
    private static final String PROPERTIES = "factory.properties";

    private DaoFactoryUtils() {

    }

    public enum DaoType {
        /**
         * Dao类型
         */
         BaseDao,UserDao
    }
    public static Object getDao(DaoType daoType){
        String className;
        Object bean;
        /**
         * 加载配置文件
         */
        Properties prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES));
            className = prop.getProperty(daoType.name());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            bean = Class.forName(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }
}
