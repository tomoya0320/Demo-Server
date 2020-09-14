package com.wzb.dao.inter;

import java.util.HashMap;
import java.util.LinkedList;

public interface BaseDao {
	/**
	 * 把一个对象插入一张表
	 * 
	 * @param obj
	 *            要插入的对象
	 * @return int 0为操作失败1为操作成功
	 * @name insert
	 */
	int insert(Object obj);

	/**
	 * 把一个对象从一张表中删除
	 * 
	 * @param obj
	 *            要删除的对象
	 * @return int 0为操作失败1为操作成功
	 * @name delete
	 */
	int delete(Object obj);

	/**
	 * 删除符合条件的对象
	 * 
	 * @param classType
	 *            要删除的对象的类型
	 * @param condition
	 *            判断的属性域和值
	 * @return int 0为操作失败1为操作成功
	 * @name delete
	 */
	int delete(Class<?> classType, HashMap<String, Object> condition);

	/**
	 * 更新一张表中的某个对象
	 * 
	 * @param classType
	 *            要更新的对象的类型
	 * @param condition
	 *            更新的条件的属性域和值
	 * @param data
	 *            更新的数据的属性域和值
	 * @return int 0为操作失败1为操作成功
	 * @name update
	 */
	int update(Class<?> classType, HashMap<String, Object> condition, HashMap<String, Object> data);

	/**
	 * 执行输入的sql语句，并且将结果以LinkedList的形式返回
	 *
	 * @param classType
	 *            类
	 * @param condition
	 *            判断的属性域和值
	 * @return java.util.LinkedList
	 * @name queryList
	 */
	LinkedList<?> query(Class<?> classType, HashMap<String, Object> condition);
}
