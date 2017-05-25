<%@ page language="java" pageEncoding="UTF-8"%>  
<!DOCTYPE HTML>  
<html>  
  <head>  
    <title>消息提示</title>  
  </head>  
    
  <body>  
  
<%--   		<%request.getAttribute("message"); %> --%>
  		<h1>${message}</h1>
        <br/>  
        <a href="listFileServlet">显示所有文件</a>  
  </body>  
</html>  