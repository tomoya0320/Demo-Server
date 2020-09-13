package com.wzb.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wzb.dao.UserDao;
import com.wzb.dao.UserDaoByMySql;

import net.sf.json.*;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" }, initParams = {
		@WebInitParam(name = "maxTries", value = "10"), @WebInitParam(name = "autoSave", value = "true"), })

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UserDao userDao = null;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		userDao = new UserDaoByMySql();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String account = request.getHeader("account");
		String password = request.getHeader("password");
		JSONObject result = checkUser(account, password);
		response.getWriter().write(result.toString());
	}

	public JSONObject checkUser(String account, String password) {
		int code;
		JSONObject json = new JSONObject();
		if (!userDao.search(account)) {
			code = 0;
		} else {
			if (password.equals(userDao.getValue(account, "password"))) {
				code = 1;
				//拉取用户信息，后面再改进
				json.element("account", account);
				json.element("password", password);
				json.element("items", JSONArray.fromObject(userDao.getValue(account, "items")));
				json.element("tasks", JSONObject.fromObject(userDao.getValue(account, "tasks")));
			} else {
				code = -1;
			}
		}

		json.element("code", code);
		return json;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
