package com.test.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/listFileServlet")
public class ListFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	List<String> listNames = new ArrayList<>();
	List<Long> sizeList = new ArrayList<>();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = (String) request.getSession().getAttribute("savePath"); 
		
        Map<String,String> fileNameMap = new HashMap<String,String>();  
        listfile(new File(path),fileNameMap);  
        request.setAttribute("fileNameMap", fileNameMap);  
        request.getRequestDispatcher("/listfile.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String,String> fileNameMap = new HashMap<String,String>();  
		listNames = new ArrayList<>();
		sizeList = new ArrayList<>();
		
		String path = (String) request.getSession().getAttribute("savePath"); 
        listfile(new File(path),fileNameMap);
        
        JSONObject json = new JSONObject();
        json.put("size", listNames.size());
        
        for (int i = 0; i < listNames.size(); i++) {
        	json.put(""+i, listNames.get(i));
		}

        JSONArray jsonArr = JSONArray.fromObject(sizeList);
        
        JSONObject jsonResp = new JSONObject();
        jsonResp.put("json1", json);
        jsonResp.put("json2", jsonArr);
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-Cache");
        response.setDateHeader("Expires", 0);
        
        
        PrintWriter out = response.getWriter();
        out.print(jsonResp);
        out.flush();
        out.close();
	}
	
	/**
	 * 把当前用户文件目录，文件全名--文件名做映射
	 * @param file  当前用户文件目录
	 * @param map
	 */
	public void listfile(File file,Map<String,String> map){  
		
        if(!file.isFile()){  
            File files[] = file.listFiles();  
            for(File f : files){  
                listfile(f,map);  
            }  
        }else{  
        	String name = file.getName();
        	sizeList.add(file.length());
            String realName = name.substring(file.getName().indexOf("_")+1);  
            //System.out.println(realName);  
            map.put(name, realName);  
            listNames.add(name);
        }  
    }
	

}
