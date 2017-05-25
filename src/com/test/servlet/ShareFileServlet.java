package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.servlet.util.DBUtil;

@WebServlet("/shareFileServlet")
public class ShareFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = request.getParameter("name");
		String uuid = request.getParameter("uuid");
		String fileName = DBUtil.getFileFromUuid(uuid);
		String str = "http://172.23.80.81:8080/UploadDemo/uploadFiles/"+user+"/"+fileName;
		response.sendRedirect(str);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
		String fileName = request.getParameter("filename");
		String curUser = (String) request.getSession().getAttribute("curUser");
		String uuid = "?name="+curUser+"&uuid="+DBUtil.getUuidFromDb(fileName);
		String result = "http://172.23.80.81:8080/UploadDemo/shareFileServlet"+uuid;
		
		//String("http://172.23.80.81:8080/UploadDemo/shareFileServlet?name=ljh&uuid=0xcftt5q9m")
		
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-Cache");
        response.setDateHeader("Expires", 0);
        
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
	
	}

}
