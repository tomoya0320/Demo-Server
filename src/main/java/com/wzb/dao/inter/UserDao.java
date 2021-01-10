package com.wzb.dao.inter;

import com.wzb.entity.UserDB;
import net.sf.json.JSONObject;
import java.util.HashMap;

public interface UserDao {
	/**
	 * 检测当前用户ID是否已存在
	 * 
	 * @param account
	 *            用户ID
	 * @return User 不存在则是null
	 * @name isUserExist
	 */
	UserDB queryUser(String account);

	/**
	 * 用户注册
	 * 
	 * @param userDBJson
	 *            新的用户
	 * @return int -1为用户已存在1为注册成功0为注册失败
	 * @name register
	 */
	int register(JSONObject userDBJson);
	
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
	int alterPsd(String account, String oPsd, String nPsd);

	/**
	 * 用户登录验证
	 * 
	 * @param account
	 *            用户ID
	 * @param psd
	 *            用户密码
	 * @return HashMap
	 *         <String,Object> 键"resultCode":0为用户不存在1为登录成功-1为密码错误,键"userDB":返回用户实体
	 * @name login
	 */
	HashMap<String, Object> login(String account, String psd);

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
	int updateUserData(String account, HashMap<String, Object> data);
}