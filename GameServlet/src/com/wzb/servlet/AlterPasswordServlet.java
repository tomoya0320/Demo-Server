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
		String oldPsd = request.getHeader("oldPassword");
		String newPsd = request.getHeader("newPassword");
		JSONObject result = alterPsd(account, oldPsd, newPsd);
		response.getWriter().write(result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public JSONObject alterPsd(String account, String oldPsd, String newPsd) {
		int code;
		JSONObject json = new JSONObject();
		UserDB userDB = new UserDB(account, oldPsd);
		if (!userDao.search(userDB)) {
			code = 0;
		} else {
			if (oldPsd.equals(userDao.getValue(account, "password"))) {
				if(userDao.updateValue(userDB.account, "password", newPsd)){
					code = 1;
				}else{
					code = -2;
				}
			} else {
				code = -1;
			}
		}

		json.element("code", code);
		return json;
	}
}
