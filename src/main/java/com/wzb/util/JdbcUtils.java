package com.wzb.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.LinkedList;

public class JdbcUtils {
    private static DataSourceUtils dataSrc = DataSourceUtils.getInstance();

    private JdbcUtils() { }

    /**
     * 执行sql语句
     * @param sql sql语句
     * @param params 参数列表
     */
    public static void execute(String sql, Object[] params) {
        try {
            Connection conn = dataSrc.getConnection();
            PreparedStatement preStmt = conn.prepareStatement(sql);
            JdbcUtils.setParams(preStmt, params);
            preStmt.execute();
            preStmt.close();
            dataSrc.freeConnection(conn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static LinkedList executeQuery(Class<?> classType, String sql, Object[] params) {
        try {
            LinkedList resultList = new LinkedList();
            Connection conn = dataSrc.getConnection();
            PreparedStatement preStmt = conn.prepareStatement(sql);
            JdbcUtils.setParams(preStmt, params);
            ResultSet result = preStmt.executeQuery();

            while ((result.next())) {
                Object obj = classType.newInstance();
                for (Field field : classType.getDeclaredFields()) {
                    field.set(obj, result.getObject(field.getName().toLowerCase()));
                }
                resultList.add(obj);
            }

            result.close();
            preStmt.close();
            dataSrc.freeConnection(conn);

            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 负责根据传入的参数数组给PreparedStatement填入参数
     *
     * @param ps     需要设参的预编译sql语句
     * @param params 参数数组
     * @name setParams
     */
    private static void setParams(PreparedStatement ps, Object[] params) {
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
    public static String getDeleteStatement() {
        return dataSrc.getDeleteStatement();
    }

    /**
     * 返回数据库的更新语法
     *
     * @name getUpdateStatement
     */
    public static String getUpdateStatement() {
        return dataSrc.getUpdateStatement();
    }

    /**
     * 返回数据库的查询语法（带条件）
     *
     * @name getQueryStatement
     */
    public static String getQueryStatement() {
        return dataSrc.getQueryStatement();
    }

    /**
     * 返回数据库的查询语法（无条件）
     *
     * @name getQueryNoConditionStatement
     */
    public static String getQueryNoConditionStatement() {
        return dataSrc.getQueryNoConditionStatement();
    }
}