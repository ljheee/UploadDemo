package com.test.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * 获取文件上传进度
 * 
 * @author chengqi
 *
 */
public class GetFileProcessServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileUploadPercent = (String)request.getSession().getAttribute("fileUploadProcess");
		Writer writer = null;
		try {
			writer = response.getWriter();
			IOUtils.write(fileUploadPercent == null ? "0%" : fileUploadPercent, writer);
			writer.flush();
			writer.close();
		} catch(Exception e) {
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
