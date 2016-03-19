package muyanmoyang.utils;

import java.sql.*;

public class DBUtil {
	public static Connection getConnection() {
		try {
			// 第一步：加载MySQL的JDBC的驱�?
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("类com.mysql.jdbc.Driver没有发现!");  
			e.printStackTrace();
		} 
 
		// 取得连接的url,能访问MySQL数据库的用户�?密码  
		// String url = "jdbc:mysql://localhost/copynews";
		String url = "jdbc:mysql://localhost:3306/sogou?useUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "123456";

		// 第二步：创建与MySQL数据库的连接类的实例  
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失�?");
			e.printStackTrace();
		}
		return con;
	}

	public static Connection getConnection(String url) {

		try {
			// 第一步：加载MySQL的JDBC的驱�?
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("类com.mysql.jdbc.Driver没有发现!");
			e.printStackTrace();
		}
		// 取得能访问MySQL数据库的用户�?密码�?
		String username = "root";
		String password = "123456";
		Connection con = null;
		try {
			// 第二步：创建与MySQL数据库的连接类的实例
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失�?");
			e.printStackTrace();
		}
		return con;
	}

	public static Connection getConnection(String url, String username,
			String password) {
		try {
			// 第一步：加载MySQL的JDBC的驱�?
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("类com.mysql.jdbc.Driver没有发现!");
			e.printStackTrace();
		}
		// 取得能访问MySQL数据库的用户�?密码�?
		Connection con = null;
		try {
			// 第二步：创建与MySQL数据库的连接类的实例
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失�?");
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 根据传入的SQL语句返回�?��结果集合.
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static ResultSet select(String sql) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException sqle) {
			throw new SQLException("select data exception:" + sqle.getMessage());
		} catch (Exception e) {
			throw new Exception("System e exception: " + e.getMessage());
		}
	}

	/**
	 * 根据传入的SQL语句和数据库连接字符串返回一个查询结果集�?
	 * 
	 * @param sql
	 *            sql语句.
	 * @param constring
	 *            数据库链接字符串.
	 * @return ResultSet 结果�?
	 * @throws Exception
	 */
	public static ResultSet select(String sql, String constring)
			throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection(constring);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException sqle) {
			throw new SQLException("select data exception:" + sqle.getMessage());
		} catch (Exception e) {
			throw new Exception("System e exception: " + e.getMessage());
		}
	}

	/**
	 * 根据传入的SQL语句向数据库增加�?��记录
	 * 
	 * @param sql
	 *            sql语句.
	 * @throws Exception
	 */
	public static void insert(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("insert data exception:" + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new Exception("connection close exception: " + e.getMessage());
		}
	}
	/**
	 * 根据传入的SQL语句和链接函数向数据库增加一条记�?
	 * 
	 * @param sql
	 *            sql语句.
	 * @throws Exception
	 */
	public static void insert(Connection conn,String sql) throws Exception {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("insert data exception:" + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
		}
	}
	/**
	 * 根据传入的SQL语句更新数据库记�?
	 * 
	 * @param sql
	 *            sql语句.
	 * @throws Exception
	 */
	public static void update(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("update data exception:" + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new Exception("connection close exception: " + e.getMessage());
		}
	}

	/**
	 * 根据传入的SQL语句删除�?��数据库记�?
	 * 
	 * @param sql
	 *            sql语句.
	 * @throws Exception
	 */
	public static void delete(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("delete data exception:" + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new Exception("connection close exception: " + e.getMessage());
		}
	}
}
