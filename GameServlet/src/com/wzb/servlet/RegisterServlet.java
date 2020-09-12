package com.wzb.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wzb.dao.UserDao;
import com.wzb.dao.UserDaoByMySql;
import com.wzb.entity.UserDB;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
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
		String psd = request.getHeader("password");
		String tasks = request.getHeader("tasks");
		JSONObject result = addUser(account, psd, tasks);
		response.getWriter().write(result.toString());
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

	public JSONObject addUser(String account, String psd, String tasks) {
		int code;
		JSONObject json = new JSONObject();
		UserDB userDB = new UserDB(account, psd);
		if (userDao.search(userDB)) {
			code = -1;
		} else {
			if (userDao.insert(userDB) && userDao.updateValue(account, "tasks", tasks) &&
				userDao.updateValue(account, "items", "[]")) {
				code = 1;
			} else {
				code = -2;
			}
		}
		json.element("code", code);
		return json;
	}
}
