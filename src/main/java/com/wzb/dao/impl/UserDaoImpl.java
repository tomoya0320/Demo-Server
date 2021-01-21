package com.wzb.dao.impl;
import com.wzb.dao.inter.UserDao;
import com.wzb.entity.UserData;
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
	 * @name queryUser
	 */
	@Override
	public UserData queryUser(String account) {
		HashMap<String, Object> condition = new HashMap();
		condition.put("account", "^" + account + "$");
		LinkedList existedUser = query(UserData.class, condition);
		return existedUser.size() > 0 ? (UserData) existedUser.element() : null;
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
		UserData existedUser = queryUser(userDBJson.getString("account"));
		if (existedUser != null) {
			return -1;
		} else {
			UserData userData = new UserData();
			try {
				for (Field field : UserData.class.getDeclaredFields()) {
					if(field.getType().getSimpleName().equals("String")){
						field.set(userData, userDBJson.getString(field.getName()));
					}else{
						field.set(userData, userDBJson.get(field.getName()));
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return insert(userData);
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
	 * @return int 0为用户不存在1为修改成功-1为原密码错误-2为其他异常
	 * @name alterPsd
	 */
	@Override
	public int alterPsd(String account, String oPsd, String nPsd) {
		UserData existedUser = queryUser(account);
		if (existedUser != null) {
			if (oPsd.equals(existedUser.password)) {
				HashMap<String, Object> condition = new HashMap();
				HashMap<String, Object> data = new HashMap();
				condition.put("account", account);
				data.put("password", nPsd);
				return update(UserData.class, condition, data) == 1 ? 1 : -2;
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
	 *         <String,Object> 键"resultCode":0为用户不存在1为登录成功-1为密码错误,键"user":
	 *         返回用户实体
	 * @name login
	 */
	@Override
	public HashMap<String, Object> login(String account, String psd) {
		HashMap<String, Object> result = new HashMap<>();
		UserData existedUser = queryUser(account);
		if (existedUser == null) {
			result.put("resultCode", 0);
			result.put("user", null);
			return result;
		}
		if (existedUser.password.equals(psd)) {
			result.put("resultCode", 1);
			result.put("user", existedUser);
		} else {
			result.put("resultCode", -1);
			result.put("user", null);
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
		UserData existedUser = queryUser(account);
		if (existedUser != null) {
			HashMap<String, Object> condition = new HashMap();
			condition.put("account", account);
			return update(UserData.class, condition, data) == 1 ? 1 : -1;
		} else {
			return 0;
		}
	}
}