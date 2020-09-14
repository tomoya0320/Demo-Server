package com.wzb.util;

import java.sql.*;

public class JdbcUtils {
    private static DataSourceUtils dataSrc = DataSourceUtils.getInstance();

    private JdbcUtils() {
    }


    /**
     * 负责从数据库连接池中获取数据库连接
     *
     * @return java.sql.Connection
     * @name getConnection
     * @notice 数据库连接的数量受到配置文件中最大值的限制
     */
    public static Connection getConnection() {
        return dataSrc.getConnection();
    }

    /**
     * 用于将数据库连接放回连接池中,并释放Statement和ResultSet的资源
     *
     * @param conn 数据库连接
     * @name close
     */
    public static void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            dataSrc.freeConnection(conn);
        }
    }

    /**
     * 用于将一个数据库连接放回连接池中
     *
     * @param conn 数据库连接
     * @name close
     */
    public static void close(Connection conn) {
        if (conn != null) {
            dataSrc.freeConnection(conn);
        }
    }


    /**
     * 负责返回当前池中剩余的空闲连接数
     *
     * @return int 当前空闲连接数
     * @name getfreeCount
     */
    public static int getfreeCount() {
        return dataSrc.getfreeCount();
    }


    /**
     * 负责返回当前已经创建的连接数
     *
     * @return int 当前已经创建的连接数
     * @name getCurrentCount
     */
    public static int getCurrentCount() {
        return dataSrc.getCurrentCount();
    }

    /**
     * 负责根据传入的参数数组给PreparedStatement填入参数
     *
     * @param ps     需要设参的预编译sql语句
     * @param params 参数数组
     * @name setParams
     */
    public static void setParams(PreparedStatement ps, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    ps.setObject(i + 1, params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 返回数据库的插入语法
     *
     * @name getInsertStatement
     */
    public static String getInsertStatement() {
        return dataSrc.getInsertStatement();
    }

    /**
     * 返回数据库的删除语法
     *
     * @name getDeleteStatement
     */
    public static String getDeleteStatement(){
        return dataSrc.getDeleteStatement();
    }

    /**
     * 返回数据库的更新语法
     *
     * @name getUpdateStatement
     */
    public static String getUpdateStatement(){
        return dataSrc.getUpdateStatement();
    }

    /**
     * 返回数据库的查询语法（带条件）
     *
     * @name getQueryStatement
     */
    public static String getQueryStatement(){
        return dataSrc.getQueryStatement();
    }

    /**
     * 返回数据库的查询语法（无条件）
     *
     * @name getQueryNoConditionStatement
     */
    public static String getQueryNoConditionStatement(){
        return dataSrc.getQueryNoConditionStatement();
    }
}
