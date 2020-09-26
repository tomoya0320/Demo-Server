package com.wzb.servlet;

import net.sf.json.*;
import com.wzb.constant.Methods;
import com.wzb.dao.inter.UserDao;
import com.wzb.entity.UserDB;
import com.wzb.util.DaoFactoryUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.wzb.util.ControllerUtils.getMethod;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String, String> hashCodes = new HashMap<String, String>();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		Methods method = getMethod(request);
		UserDao userDao = (UserDao) DaoFactoryUtils.getDao(DaoFactoryUtils.DaoType.UserDao);
		switch (method) {
		case LOGIN:
			login(userDao, request, response);
			break;
		case ALTERPSD:
			alterPsd(userDao, request, response);
			break;
		case REGISTER:
			register(userDao, request, response);
			break;
		case UPDATE:
			update(userDao, request, response);
			break;
		default:
			break;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	private void register(UserDao userDao, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		JSONObject userDBJson = JSONObject.fromObject(request.getHeader("userDB"));
		int resultCode = userDao.register(userDBJson);
		JSONObject json = new JSONObject();
		if (resultCode == 1) {
			json.element("userDB", userDBJson);
		}
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void login(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String account = request.getHeader("account");
		String password = request.getHeader("password");
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
			json.element("hashCode", request.getHeader("hashCode"));
			hashCodes.put(account, request.getHeader("hashCode"));
		}

		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void alterPsd(UserDao userDao, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String account = request.getHeader("account");
		String oldPassword = request.getHeader("oldPassword");
		String newPassword = request.getHeader("newPassword");
		int resultCode = userDao.alterPsd(account, oldPassword, newPassword);
		JSONObject json = new JSONObject();
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void update(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String account = request.getHeader("account");
		String hashCode = request.getHeader("hashCode");
		JSONArray types = JSONArray.fromObject(request.getHeader("types"));
		int resultCode;
		if (!hashCodes.containsKey(account) || !hashCodes.get(account).equals(hashCode)) {
			resultCode = -2; // hashCode验证失败
		} else {
			HashMap<String, Object> data = new HashMap<String, Object>();
			for (int i = 0; i < types.size(); i++) {
				data.put(types.get(i).toString(), request.getHeader(types.get(i).toString()));
			}
			resultCode = userDao.updateUserData(account, data);
		}
		JSONObject json = new JSONObject();
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}
}
