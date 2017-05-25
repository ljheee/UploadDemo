package com.test.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.test.servlet.util.DBUtil;

/**
 * 文件上传数据接收类
 * 
 * @author chengqi
 *
 */
public class UploadFileServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;


	/** 上传临时文件存储目录*/
	private static final String tempFolderName = "tempFiles";

	/** 上传文件最大为30M*/ 
	private static final Long fileMaxSize = 30000000L; 

	/** 允许上传的扩展名*/
	private static final String [] extensionPermit = {"txt", "xls", "zip", "ppt", "doc", "xml", "cpp", "png", "jpg", "jpeg", "jar", "jsp", "java", "rar"};

	/** 统一的编码格式*/
	private static final String encode = "UTF-8";
	
	private String curUser;
	private int userLevel;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			curUser = (String) request.getSession().getAttribute("curUser");
			userLevel = (int) request.getSession().getAttribute("userLevel");
			String curProjectPath = this.getServletContext().getRealPath("/");
			String saveDirectoryPath =  (String) request.getSession().getAttribute("savePath");
			
			String tempDirectoryPath = curProjectPath + "/" + tempFolderName+ "/" + curUser;
			File saveDirectory = new File(saveDirectoryPath);
			File tempDirectory = new File(tempDirectoryPath);
			
			//Android端上传文件
			String fileN = request.getParameter("fileName");
			if(fileN!=null&&!fileN.equals("")){
				String fileContext = request.getParameter("fileContext");
				File fff = new File(saveDirectoryPath+"/"+fileN);
				FileWriter fw = new FileWriter(fff);
				fw.write(fileContext);
				fw.close();
			}
			
			
			
			//上传时产生的临时文件的默认保存目录
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//DiskFileItemFactory中DEFAULT_SIZE_THRESHOLD=10240表示如果上传文件大于10K则会产生上传临时文件
			//上传临时文件的默认目录为java.io.tmpdir中保存的路径，根据操作系统的不同会有区别
			
			if(!tempDirectory.exists()) {
				tempDirectory.mkdir();
			}
			if(!saveDirectory.exists()) {
				saveDirectory.mkdir();
			}
			//重新设置临时文件保存目录
			factory.setRepository(tempDirectory);

			//设置文件清除追踪器，文件上传过程中产生的临时文件
			FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(this.getServletContext());
			factory.setFileCleaningTracker(fileCleaningTracker);

			ServletFileUpload upload = new ServletFileUpload(factory);

			//设置文件上传进度监听器
			FileProcessListener processListener = new FileProcessListener(request.getSession());
			upload.setProgressListener(processListener);

			// 设置文件上传的大小限制
			upload.setFileSizeMax(fileMaxSize * userLevel);

			// 设置文件上传的头编码，如果需要正确接收中文文件路径或者文件名
			// 这里需要设置对应的字符编码，为了通用这里设置为UTF-8
			upload.setHeaderEncoding(encode);

			//解析请求数据包
			List<FileItem> fileItems = upload.parseRequest(request);
			//遍历解析完成后的Form数据和上传文件数据
			for (Iterator<FileItem> iterator = fileItems.iterator(); iterator.hasNext(); ) {
				FileItem fileItem = iterator.next();
				String fieldName = fileItem.getFieldName();
				String name = fileItem.getName();
				
				//如果为上传文件数据
				if(!fileItem.isFormField()) {
					if(fileItem.getSize() > 0) {
						String fileExtension = FilenameUtils.getExtension(name);
						if(!ArrayUtils.contains(extensionPermit, fileExtension)) {
							throw new NoSupportExtensionException("No Support extension.");
						}
						String fileName = FilenameUtils.getName(name);
						DBUtil.addUuidFromFile(fileName);//TODO添加文件上传者id
						FileUtils.copyInputStreamToFile(fileItem.getInputStream(), 
								new File(saveDirectory, fileName));
					}
				} else { //Form表单数据
//					String value = fileItem.getString(encode);
				}
			}
			request.setAttribute("message",State.OK.getMessage());
			
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			
			System.out.println("upload ok.");
			
		} catch(FileSizeLimitExceededException e) { 
			request.setAttribute("message",State.OVER_FILE_LIMIT.getMessage());
			responseMessage(request,response, State.OVER_FILE_LIMIT);
		} catch(NoSupportExtensionException e) { 
			request.setAttribute("message",State.NO_SUPPORT_EXTENSION.getMessage());
			responseMessage(request,response, State.NO_SUPPORT_EXTENSION);
		} catch(Exception e) {
			request.setAttribute("message",State.ERROR.getMessage());  
			responseMessage(request,response, State.ERROR);
		} finally {
			//清除上传进度信息
			request.getSession().removeAttribute("fileUploadProcess");
		}
	}

	/**
	 * 返回状态
	 */
	public enum State {
		
		OK(200, "上传成功"),
		ERROR(500, "上传失败"),
		OVER_FILE_LIMIT(501, "超过上传大小限制"),
		NO_SUPPORT_EXTENSION(502, "不支持的扩展名");

		private int code;
		private String message;
		
		private State(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}
		public String getMessage() {
			return message;
		}

	}
	

	/**
	 * 文件上传完（服务器接受文件，并保存到指定目录）
	 * 返回结果函数---执行脚本，清除定时器
	 * @param response
	 * @param state
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void responseMessage(HttpServletRequest request,HttpServletResponse response, State state) throws ServletException, IOException {
		
		response.setCharacterEncoding(encode);
		response.setContentType("text/html; charset=" + encode);
		Writer writer = null;
		
		try {
			writer = response.getWriter();
			writer.write("<script>");
			writer.write("window.parent.fileUploadCallBack({\"code\":" + state.getCode() +",\"message\":\"" + state.getMessage()+ "\"});");
			writer.write("</script>");
			writer.flush();
			writer.close();
		} catch(Exception e) {
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}


}
