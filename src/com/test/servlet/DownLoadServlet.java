package com.test.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.CharBuffer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class DownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 	String fileName = request.getParameter("filename");  
	        //System.out.println(fileName);  
	        //fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");  
	        String path=(String) request.getSession().getAttribute("savePath"); 
	        File file = new File(path + "/" + fileName);  
	        //System.out.println(file);  
	        if(!file.exists()){  
	            request.setAttribute("message", "您要下载的资源已被删除！！");  
	            request.getRequestDispatcher("/message.jsp").forward(request, response);  
	            return;  
	        }  
	        String realname = fileName.substring(fileName.indexOf("_")+1);  
	        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));  
	        FileInputStream in = new FileInputStream(path + "/" + fileName);  
	        OutputStream out = response.getOutputStream();  
	        byte buffer[] = new byte[1024];  
	        int len = 0;  
	        while((len=in.read(buffer))>0){  
	            out.write(buffer, 0, len);  
	        }  
	        in.close();  
	        out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("filename");  
        //System.out.println(fileName);  
        //fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");  
        String path=(String) request.getSession().getAttribute("savePath"); 
        File file = new File(path + "/" + fileName);  
        //System.out.println(file);  
        if(!file.exists()){  
            request.setAttribute("message", "您要下载的资源已被删除！！");  
            return;  
        }  
        String downloadFilename = file.getName();
        downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8"); 
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-Cache");
        response.setDateHeader("Expires", 0);  
        
        PrintWriter out = response.getWriter();
        out.print(getByte(file));
        out.flush();
        out.close();
        
	}
	
	private char[] getByte(File f){
		char[] result = new char[(int) f.length()+1];
		try {
			FileReader fr = new FileReader(f);
			fr.read(result);
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
