package com.wzb.servlet;

import com.wzb.dao.inter.UserDao;
import com.wzb.entity.UserData;
import com.wzb.util.ControllerUtils;
import com.wzb.util.DaoFactoryUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static Base64 base64 = new Base64();
	private static HashMap<String, String> tokens = new HashMap();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		UserDao userDao = (UserDao) DaoFactoryUtils.getDao(DaoFactoryUtils.DaoType.UserDao);
		JSONObject requestData = JSONObject.fromObject(new String(base64.decode(request.getReader().readLine()), StandardCharsets.UTF_8));
		switch (ControllerUtils.getMethod(requestData)) {
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
		JSONObject userDBJson = JSONObject.fromObject(requestData.get("user"));
		int resultCode = userDao.register(userDBJson);
		JSONObject json = new JSONObject();
		if (resultCode == 1) {
			json.element("user", userDBJson);
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
			UserData userData = (UserData) result.get("user");
			JSONObject userDBJson = new JSONObject();
			try {
				for (Field field : UserData.class.getDeclaredFields()) {
					userDBJson.element(field.getName(), field.get(userData));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json.element("user", userDBJson);
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