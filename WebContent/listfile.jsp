<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE HTML>  
<html>  
  <head>  
    <title>下载文件显示页面</title>  
  </head>  
    
  <body>  
      <!-- 遍历Map集合 -->  
    <h2>可下载列表：</h2>
    <c:forEach var="me" items="${fileNameMap}">  
        <c:url value="/downLoadServlet" var="downurl">  
            <c:param name="filename" value="${me.key}"></c:param>  
        </c:url>  
        ${me.value}<a href="${downurl}">下载</a>  
        <br/>  
    </c:forEach>  
    <br/>  
    <br/>  
    
    <hr>
    <h2>可删除列表：</h2>
    <c:forEach var="de" items="${fileNameMap}">  
        <c:url value="/deleteServlet" var="delurl">  
            <c:param name="filename" value="${de.key}"></c:param>  
        </c:url>  
        ${de.value}<a href="${delurl}">删除</a>  
        <br/>  
    </c:forEach>  
    <br/>  
    <a href="/UploadDemo/apacheUploadDemo.html">返回继续上传</a>  
  </body>  
</html>  