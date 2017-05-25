<%@ page contentType="text/json; charset=utf-8"%><%@ page
	language="java"%><%@ page import="java.util.*,java.io.*,net.sf.json.*"%>
<%
	StringBuffer json = new StringBuffer();
	String line = null;
	try {
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			json.append(line);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	JSONObject jsonTemp = JSONObject.fromObject(json.toString());
	JSONObject jsonObj = jsonTemp.getJSONObject("json");
	System.out.println("receive:" + jsonTemp.toString());

	String user = jsonObj.getString("user");
	String password = jsonObj.getString("password");
	System.out.println("user:" + user + ",password:" + password);

	JSONObject resp = new JSONObject();
	resp.accumulate("serverCode", 200);
	resp.accumulate("serverMsg", "your name=" + user + ",and password=" + password);

	out.print(resp.toString());
%>
