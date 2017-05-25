package com.test.servlet;

import java.text.NumberFormat;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * 文件进度监听器
 * 
 * @author chengqi
 *
 */
public class FileProcessListener implements ProgressListener{


	private HttpSession session;

	public FileProcessListener(HttpSession session) {
		this.session = session;  
	}
	
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		double readByte = pBytesRead;
		double totalSize = pContentLength;
		if(pContentLength == -1) {
			
		} else {
			String p = NumberFormat.getPercentInstance().format(readByte / totalSize);
			session.setAttribute("fileUploadProcess", p);
		}
	}

}
