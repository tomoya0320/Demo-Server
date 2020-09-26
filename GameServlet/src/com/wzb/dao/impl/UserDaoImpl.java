package com.wzb.dao.impl;

import com.wzb.dao.inter.UserDao;
import com.wzb.entity.UserDB;

import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {
	/**
	 * 检测当前用户ID是否已存在
	 *
	 * @param account
	 *            用户ID
	 * @return boolean 是否存在
	 * @name isUserExist
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserDB isUserExist(String account) {
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("account", "^" + account + "$");
		LinkedList<UserDB> existedUser = (LinkedList<UserDB>) query(UserDB.class, condition);
		return existedUser.size() > 0 ? existedUser.element() : null;
	}

	/**
	 * 用户注册
	 * 
	 * @param userDBJson
	 *            新的用户
	 * @return int -1为用户已存在1为注册成功0为注册失败
	 * @name register
	 */
	@Override
	public int register(JSONObject userDBJson) {
		UserDB existedUser = isUserExist(userDBJson.getString("account"));
		if (existedUser != null) {
			return -1;
		} else {
			UserDB userDB = new UserDB();
			try {
				for (Field field : UserDB.class.getDeclaredFields()) {
					if(field.getType().getSimpleName().equals("String")){
						field.set(userDB, userDBJson.getString(field.getName()));
					}else{
						field.set(userDB, userDBJson.get(field.getName()));
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return insert(userDB);
		}
	}

	/**
	 * 修改某个用户的密码
	 *
	 * @param account
	 *            用户ID
	 * @param oPsd
	 *            原密码
	 * @param nPsd
	 *            新密码
	 * @return int 0为用户不存在1为修改成功-1为原密码错误-2为修改失败
	 * @name alterPsd
	 */
	@Override
	public int alterPsd(String account, String oPsd, String nPsd) {
		UserDB existedUser = isUserExist(account);
		if (existedUser != null) {
			if (oPsd.equals(existedUser.password)) {
				HashMap<String, Object> condition = new HashMap<String, Object>();
				HashMap<String, Object> data = new HashMap<String, Object>();
				condition.put("account", account);
				data.put("password", nPsd);
				return update(UserDB.class, condition, data) == 1 ? 1 : -2;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 用户登录验证
	 *
	 * @param account
	 *            用户ID
	 * @param psd
	 *            用户密码
	 * @return HashMap
	 *         <String,Object> 键"resultCode":0为用户不存在1为登录成功-1为密码错误,键"userDB":
	 *         返回用户实体
	 * @name login
	 */
	@Override
	public HashMap<String, Object> login(String account, String psd) {
		HashMap<String, Object> result = new HashMap<>();
		UserDB existedUser = isUserExist(account);
		if (existedUser == null) {
			result.put("resultCode", 0);
			result.put("userDB", null);
			return result;
		}
		if (existedUser.password.equals(psd)) {
			result.put("resultCode", 1);
			result.put("userDB", existedUser);
		} else {
			result.put("resultCode", -1);
			result.put("userDB", null);
		}
		return result;
	}

	/**
	 * 更新某个用户的数据
	 * 
	 * @param account
	 *            用户ID
	 * @param data
	 *            更新的数据的属性域和值
	 * @return int 1为更新成功-1为更新失败0为用户不存在
	 * @name updateUserData
	 */
	@Override
	public int updateUserData(String account, HashMap<String, Object> data) {
		// TODO Auto-generated method stub
		UserDB existedUser = isUserExist(account);
		if (existedUser != null) {
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("account", account);
			return update(UserDB.class, condition, data) == 1 ? 1 : -1;
		} else {
			return 0;
		}
	}
}
