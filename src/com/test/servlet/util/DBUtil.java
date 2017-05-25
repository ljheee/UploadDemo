package com.test.servlet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.test.servlet.entity.User;
/**
 * 
 *数据库连接工具
 */
public class DBUtil {
	
	private static Connection con;
	private static PreparedStatement ps;
	private static ResultSet rs;
	
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/first","lkd","root");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取注册用户列表
	 * @return
	 */
	public static List<User> getAllUsers(){
		
		List<User> list = null;
		User user = null;
		String sql = "select * from users";
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(list == null){
					list = new ArrayList<>();
				}
				user = new User(
						rs.getString("name"), 
						rs.getString("pass"), 
						rs.getInt("level"));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 检查用户登录状态
	 * @param uu
	 * @return
	 */
	public static int checkUser(User uu){
		int result = -1;//fail
		
		List<User> list = DBUtil.getAllUsers();
		for (User user : list) {
			
			if(user.getName().equals(uu.getName())){
				if(user.getPass().equals(uu.getPass())){
					result = 0;//ok
					break;
				}else{
					result = 1;//pass error
				}
				
			}else{
				result = -1;//"用户名不存在，请确认"
			}
		}
		return result;
	}
	
	/**
	 * 根据用户名获得level
	 * @param name
	 * @return
	 */
	public static int getUserLevel(String name){
		int result = -1;
		List<User> list = DBUtil.getAllUsers();
		for (User user : list) {
			
			if(user.getName().equals(name)){
				result = user.getLevel();
				break;
			}else{
				result = -1;//用户名不存在
			}
		}
		
		return result;
	}
	
	/**
	 * 根据文件名，获取数据库UUID值
	 * @param name
	 * @return
	 */
	public static String getUuidFromDb(String name){
		String result = null;
		String sql = "select * from filemap where file_name=?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			
			while(rs.next()){
				result = rs.getString(4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public static String getFileFromUuid(String uuid){
		String result = null;
		String sql = "select * from filemap where uuid=?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, uuid);
			rs = ps.executeQuery();
			
			while(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public static Boolean addUuidFromFile(String name){
		Boolean result = null;
		String sql = "insert into  filemap values(?,1,null,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, UUID.randomUUID().toString());
			result = ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return !result;
	}
	
	
}
