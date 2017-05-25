package com.test.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.servlet.entity.User;
import com.test.servlet.util.DBUtil;

/**
 * 处理验证用户登录
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/** 上传目录名*/
	private static final String uploadFolderName = "uploadFiles";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String n = request.getParameter("userName");
		String p = request.getParameter("password");
		
		int result = DBUtil.checkUser(new User(n, p));
		response.setCharacterEncoding("UTF-8");
		switch (result) {
		case 0://登录成功
			request.getSession().setAttribute("curUser", n);
			request.getSession().setAttribute("userLevel", DBUtil.getUserLevel(n));
			String curProjectPath = this.getServletContext().getRealPath("/");
			
			String saveDirectoryPath = curProjectPath + "/" + uploadFolderName+ "/" + n;
			request.getSession().setAttribute("savePath",saveDirectoryPath);
			response.sendRedirect("upload.html");
			break;
		case -1://用户名不存在
			response.getWriter().print("用户名不存在");
			break;
		case 1://密码错误
			response.getWriter().print("密码错误");
			break;

		default://fail
			break;
		}
	
	}

}
