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
		String account = request.getHeader("account");
		String password = request.getHeader("password");
		String tasks = request.getHeader("tasks");
		String items = request.getHeader("items");
		String mails = request.getHeader("mails");
		UserDB user = new UserDB("", account, password, items, tasks, mails);
		int resultCode = userDao.register(user);
		JSONObject json;
		if (resultCode == 1) {
			json = JSONObject.fromObject(user);
		} else {
			json = new JSONObject();
		}
		json.element("resultCode", resultCode);
		response.getWriter().write(json.toString());
	}

	private void login(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String account = request.getHeader("account");
		String password = request.getHeader("password");
		String hashCode = request.getHeader("hashCode");
		HashMap<String, Object> result = userDao.login(account, password);
		int resultCode = (int) result.get("resultCode");
		UserDB user = (UserDB) result.get("user");
		JSONObject json = new JSONObject();
		if (resultCode == 1) {
			json.element("hashCode", hashCode);
			// 拉取用户数据
			json.element("name", user.name);
			json.element("account", user.account);
			json.element("items", user.items);
			json.element("tasks", user.tasks);
			json.element("mails", user.mails);
			hashCodes.put(account, hashCode);
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
