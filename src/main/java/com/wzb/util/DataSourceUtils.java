package com.wzb.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @description 负责提供数据库连接池，向dao实现类提供数据库连接
 */
public class DataSourceUtils {
    /**
     * 配置文件路径
     */
    private static String PROP_PATH = "databaseConfig.properties";
    /**
     * 测试数据库连接的等待时长
     */
    private static int TIMEOUT;
    private static String url;
    private static String user;
    private static String password;
    private static DataSourceUtils instance;
    private static Properties prop;
    /**
     * 数据库连接池
     */
    private LinkedList<Connection> connPool = new LinkedList<>();

    static {
        try {
            /**
             * 加载配置文件
             */
            prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROP_PATH));
            /**
             * 驱动类名称
             */
            String driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            TIMEOUT = Integer.parseInt(prop.getProperty("TIMEOUT"));
            /**
             * 注册驱动
             */
            Class.forName(driver);
            instance = new DataSourceUtils();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * 负责提供数据库连接池实例
     *
     * @return DataSourceUtils
     * @name getInstance
     */
    public static DataSourceUtils getInstance() {
        return instance;
    }

    /**
     * 负责从数据库连接池中获取数据库连接
     *
     * @return java.sql.Connection
     * @name getConnection
     */
    public Connection getConnection() {
        if (connPool.size() > 0) {
            /**
             * 先检查连接是否可用，如果不可用，关闭该连接，返回一个新连接
             */
            Connection conn = connPool.removeLast();
            try {
                if (conn.isValid(TIMEOUT)) {
                    return conn;
                } else {
                    destroyConnection(conn);
                    return createConnection();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return createConnection();
        }
    }


    /**
     * 用于将数据库连接放回连接池中
     *
     * @param conn 数据库连接
     * @name freeConnection
     */
    public void freeConnection(Connection conn) {
        this.connPool.addLast(conn);
    }

    /**
     * 返回数据库的插入语法
     *
     * @name getInsertStatement
     */
    public String getInsertStatement() {
        return prop.getProperty("insert");
    }

    /**
     * 返回数据库的删除语法
     *
     * @name getDeleteStatement
     */
    public String getDeleteStatement() {
        return prop.getProperty("delete");
    }

    /**
     * 返回数据库的更新语法
     *
     * @name getUpdateStatement
     */
    public String getUpdateStatement() {
        return prop.getProperty("update");
    }

    /**
     * 返回数据库的查询语法（带条件）
     *
     * @name getQueryStatement
     */
    public String getQueryStatement() {
        return prop.getProperty("query");
    }

    /**
     * 返回数据库的查询语法（无条件）
     *
     * @name getQueryNoConditionStatement
     */
    public String getQueryNoConditionStatement() {
        return prop.getProperty("queryNoCondition");
    }

    /**
     * 创建连接("协议+访问的数据库名”，“用户名”，“密码”）
     *
     * @return java.sql.Connection
     * @name createConnection
     */
    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 关闭数据库连接
     *
     * @return java.sql.Connection
     * @name createConnection
     */
    private void destroyConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}