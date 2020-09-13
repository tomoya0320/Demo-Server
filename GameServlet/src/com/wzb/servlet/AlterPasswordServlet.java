package com.wzb.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wzb.dao.UserDao;
import com.wzb.dao.UserDaoByMySql;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class AlterPasswordServlet
 */
@WebServlet("/AlterPasswordServlet")
public class AlterPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UserDao userDao = null;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		userDao = new UserDaoByMySql();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String account = request.getHeader("account");
		String oldPassword = request.getHeader("oldPassword");
		String newPassword = request.getHeader("newPassword");
		JSONObject result = alterPsd(account, oldPassword, newPassword);
		response.getWriter().write(result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public JSONObject alterPsd(String account, String oldPassword, String newPassword) {
		int code;
		if (!userDao.search(account)) {
			code = 0;
		} else {
			if (oldPassword.equals(userDao.getValue(account, "password"))) {
				if(userDao.updateValue(account, "password", newPassword)){
					code = 1;
				}else{
					code = -2;
				}
			} else {
				code = -1;
			}
		}
		JSONObject json = new JSONObject();
		json.element("code", code);
		return json;
	}
}
