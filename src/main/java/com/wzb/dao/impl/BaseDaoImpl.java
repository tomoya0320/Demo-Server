package com.wzb.dao.impl;

import com.wzb.dao.inter.BaseDao;
import com.wzb.util.JdbcUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class BaseDaoImpl implements BaseDao {

	/**
	 * 把一个对象插入一张表
	 *
	 * @param obj 要插入的对象
	 * @return int 0为操作失败1为操作成功
	 * @name insert
	 */
	@Override
	public int insert(Object obj) {
		String tableName = obj.getClass().getSimpleName(); // 获取数据库里的表格名字
		Field[] fields = obj.getClass().getDeclaredFields(); // 获取所有的属性
		StringBuilder fieldNames = new StringBuilder();
		StringBuilder values = new StringBuilder();
		LinkedList fieldVals = new LinkedList();
		for (Field field : fields) {
			try {
				fieldNames.append(field.getName() + ",");
				values.append("?,");
				fieldVals.add(field.get(obj));
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		String sql = String.format(JdbcUtils.getInsertStatement(), tableName, fieldNames.substring(0, fieldNames.length() - 1),
				values.substring(0, values.length() - 1));

		JdbcUtils.execute(sql, fieldVals.toArray());

		return 1;
	}

	/**
	 * 把一个对象从一张表中删除
	 *
	 * @param obj 要删除的对象
	 * @return int 0为操作失败1为操作成功
	 * @name delete
	 */
	@Override
	public int delete(Object obj) {
		String tableName = obj.getClass().getSimpleName(); // 获取数据库里的表格名字
		Field[] fields = obj.getClass().getDeclaredFields(); // 获取所有的属性
		StringBuilder fieldNames = new StringBuilder();
		LinkedList fieldVals = new LinkedList();
		for (Field field : fields) {
			try {
				fieldNames.append(field.getName() + "=? and ");
				fieldVals.add(field.get(obj));
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		String sql = String.format(JdbcUtils.getDeleteStatement(), tableName,
				fieldNames.substring(0, fieldNames.length() - 5));

		JdbcUtils.execute(sql, fieldVals.toArray());

		return 1;
	}

	/**
	 * 删除符合条件的对象
	 *
	 * @param classType 要删除的对象的类型
	 * @param condition 判断的属性域和值
	 * @return int 0为操作失败1为操作成功
	 * @name delete
	 */
	@Override
	public int delete(Class<?> classType, HashMap<String, Object> condition) {
		String tableName = classType.getSimpleName(); // 获取数据库里的表格名字
		StringBuilder fieldNames = new StringBuilder();
		for (String field : condition.keySet()) {
			fieldNames.append(field + "=? and ");
		}
		String sql = String.format(JdbcUtils.getDeleteStatement(), tableName,
				fieldNames.substring(0, fieldNames.length() - 5));

		JdbcUtils.execute(sql, condition.values().toArray());

		return 1;
	}

	/**
	 * 更新一张表中的某个对象
	 *
	 * @param classType 要更新的对象的类型
	 * @param condition 更新的条件的属性域和值
	 * @param data      更新的数据的属性域和值
	 * @return int 0为操作失败1为操作成功
	 * @name update
	 */
	@Override
	public int update(Class<?> classType, HashMap<String, Object> condition, HashMap<String, Object> data) {
		String tableName = classType.getSimpleName(); // 获取数据库里的表格名字
		StringBuilder setNames = new StringBuilder();
		StringBuilder whereNames = new StringBuilder();
		try {
			for (String set : data.keySet()) {
				setNames.append(set + "=?,");
			}
			for (String where : condition.keySet()) {
				whereNames.append(where + "=? and ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		String sql = String.format(JdbcUtils.getUpdateStatement(), tableName,
				setNames.substring(0, setNames.length() - 1), whereNames.substring(0, whereNames.length() - 5));

		LinkedList<Object> setAndWhereVals = new LinkedList<>();
		setAndWhereVals.addAll(data.values());
		setAndWhereVals.addAll(condition.values());
		JdbcUtils.execute(sql, setAndWhereVals.toArray());

		return 1;
	}

	/**
	 * 执行输入的sql语句，并且将结果以LinkedList的形式返回
	 *
	 * @param classType 类
	 * @param condition 判断的属性域和值
	 * @return 查询对象结果列表
	 * @name queryList
	 */
	@Override
	public LinkedList query(Class<?> classType, HashMap<String, Object> condition) {
		String tableName = classType.getSimpleName();

		String sql;
		if (condition != null) {
			StringBuilder fieldNames = new StringBuilder();
			for (String field : condition.keySet()) {
				fieldNames.append(field + " regexp ? and ");
			}
			sql = String.format(JdbcUtils.getQueryStatement(), tableName,
					fieldNames.substring(0, fieldNames.length() - 5));

		} else {
			sql = String.format(JdbcUtils.getQueryNoConditionStatement(), tableName);
		}

		return JdbcUtils.executeQuery(classType, sql, condition.values().toArray());
	}
}