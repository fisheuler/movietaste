package com.ibm.taste.example.movie.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtil {
	private static DataSource ds;

	public static void createDataSource() throws Exception {
		try {
			Context context = new InitialContext();
			Context envContext = (Context)context.lookup("java:comp/env");
			ds = (DataSource) envContext.lookup("jdbc/movie");
			if (ds == null) {
				Thread.sleep(2000);
				ds = (DataSource) envContext.lookup("jdbc/movie");
				if (ds == null) {
					throw new Exception("get datasource failed!");
				}
			}
		} catch (NamingException ne) {
			throw ne;
		} catch (Exception e) {
			throw e;
		}
	}

	public static DataSource getDataSource() {
		try {
			if (ds == null) {
				createDataSource();
			}
			return ds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Connection getConnection(){
		DataSource dataSource = getDataSource();
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnectionFromDataSource() {
		try {
			Connection con = null;
			if (ds == null) {
				createDataSource();
			}
			con = ds.getConnection();
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet execuateQuery(String sql){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnectionFromDataSource();
			pstmt = conn.prepareStatement(sql);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
	
	public static  Connection getJDBCConnection()
    {
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/movie";
        String username = "root";     //修改为你自己用户名
        String password = "123456";      //修改为你自己密码

        Connection conn = null;

        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn)
    {
        if(conn!=null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
