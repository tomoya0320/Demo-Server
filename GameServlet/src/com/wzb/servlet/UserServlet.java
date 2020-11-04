package com.wzb.servlet;

import net.sf.json.*;
import com.wzb.dao.inter.UserDao;
import com.wzb.entity.UserDB;
import com.wzb.util.DaoFactoryUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.wzb.util.ControllerUtils.getMethod;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String, String> tokens = new HashMap<String, String>();
	private static Base64 base64 = new Base64();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		UserDao userDao = (UserDao) DaoFactoryUtils.getDao(DaoFactoryUtils.DaoType.UserDao);
		JSONObject requestData = JSONObject.fromObject(new String(base64.decode(request.getHeader("data"))));
		switch (getMethod(requestData)) {
		case LOGIN:
			login(userDao, requestData, response);
			break;
		case ALTERPSD:
			alterPsd(userDao, requestData, response);
			break;
		case REGISTER:
			register(userDao, requestData, response);
			break;
		case UPDATE:
			update(userDao, requestData, response);
			break;
		default:
			break;
		}
	}

	private void register(UserDao userDao, JSONObject requestData, HttpServletResponse response) throws IOException {
		JSONObject userDBJson = JSONObject.fromObject(requestData.get("userDB"));
		int resultCode = userDao.register(userDBJson);
		JSONObject json = new JSONObject();
		if (resultCode == 1) {
			json.element("userDB", userDBJson);
		}
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void login(UserDao userDao, JSONObject requestData, HttpServletResponse response) throws IOException {
		String account = requestData.getString("account");
		String password = requestData.getString("password");
		HashMap<String, Object> result = userDao.login(account, password);
		int resultCode = (int) result.get("resultCode");
		JSONObject json = new JSONObject();
		if (resultCode == 1) {
			UserDB userDB = (UserDB) result.get("userDB");
			JSONObject userDBJson = new JSONObject();
			try {
				for (Field field : UserDB.class.getDeclaredFields()) {
					userDBJson.element(field.getName(), field.get(userDB));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json.element("userDB", userDBJson);
			json.element("token", requestData.getString("token"));
			tokens.put(account, requestData.getString("token"));
		}

		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void alterPsd(UserDao userDao, JSONObject requestData, HttpServletResponse response) throws IOException {
		String account = requestData.getString("account");
		String oldPassword = requestData.getString("oldPassword");
		String newPassword = requestData.getString("newPassword");
		int resultCode = userDao.alterPsd(account, oldPassword, newPassword);
		JSONObject json = new JSONObject();
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void update(UserDao userDao, JSONObject requestData, HttpServletResponse response) throws IOException {
		String account = requestData.getString("account");
		String password = requestData.getString("password");
		String token = requestData.getString("token");
		HashMap<String, Object> result = userDao.login(account, password);
		JSONArray types = JSONArray.fromObject(requestData.getString("types"));
		int resultCode;
		if ((int) result.get("resultCode") != 1 || !tokens.containsKey(account) || !tokens.get(account).equals(token)) {
			resultCode = -2; // hashCode验证失败
		} else {
			HashMap<String, Object> data = new HashMap<String, Object>();
			for (int i = 0; i < types.size(); i++) {
				data.put(types.get(i).toString(), requestData.getString(types.get(i).toString()));
			}
			resultCode = userDao.updateUserData(account, data);
		}
		JSONObject json = new JSONObject();
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}
}
