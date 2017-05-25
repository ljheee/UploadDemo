package com.test.servlet;

import java.io.File;  
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
public class DeleteServlet extends HttpServlet {      
	private static final long serialVersionUID = -7407671490229820583L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        String fileName = request.getParameter("filename");  
        String path=(String) request.getSession().getAttribute("savePath");  
        String filename= path + "/" + fileName;  
        //System.out.println(filename);  
        //fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");  
        File file = new File(filename);  
        if(file.exists()) {  
            file.delete();  
            request.setAttribute("message", "您要删除的资源已被删除！！");  
            request.getRequestDispatcher("/message.jsp").forward(request, response);  
        } else {  
            request.setAttribute("message", "没有找到你要删除的资源，可能是以前删除了！！");  
            request.getRequestDispatcher("/message.jsp").forward(request, response);  
        }  
    }  
      
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	
    	 String fileName = request.getParameter("filename");  
         String path=(String) request.getSession().getAttribute("savePath");  
         String filename= path + "/" + fileName;  
         //System.out.println(filename);  
         //fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");  
         File file = new File(filename);
         
         response.setCharacterEncoding("UTF-8");
         response.setContentType("text/plain;charset=utf-8");
         response.setHeader("Pragma", "No-Cache");
         response.setDateHeader("Expires", 0);
         
         PrintWriter out = response.getWriter();
         
         if(file.exists()) {  
             file.delete();  
             request.setAttribute("message", "您要删除的资源已被删除！！");  
             out.print(true);
             out.flush();
             out.close();
         } else {  
        	 out.print(false);
        	 out.flush();
             out.close();
             request.setAttribute("message", "没有找到你要删除的资源，可能是以前删除了！！");  
         }
         
    	
    }  
}
