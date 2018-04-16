package com.worm.guo.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blwit.support.codec.RSAUtils;


/**
 * @Title:数据库连接池
 * @Desc:
 * @Company:Blwit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2014-4-9下午4:55:03
 *
 */
public class ConnectionPool {
	private static final Log LOG = LogFactory.getLog(ConnectionPool.class);

	private static String url = "";
	private static String username = "";
	private static String password = "";

	/** 连接池的大小，也就是连接池中有多少个数据库连接 */
	private int poolSize = 1;
	private Vector<Connection> pool;

	private static ConnectionPool instance = null;

	/**
	 * 私有的构造方法，禁止外部创建本类的对象，要想获得本类的对象，通过<code>getIstance</code>方法。 使用了设计模式中的单子模式。
	 */
	private ConnectionPool() {
		init();
	}

	/**
	 * 读取设置连接池的属性文件
	 */
	private void initConfig() {
		WebsiteConfig.xmlInit("");
		url = WebsiteConfig.DBUrl;
		username = WebsiteConfig.DBUserName;
		password = WebsiteConfig.DBPassword;
		poolSize = WebsiteConfig.PoolSize;
	}

	/**
	 * 连接池初始化方法，读取属性文件的内容 建立连接池中的初始连接
	 */
	private void init() {
		pool = new Vector<Connection>(poolSize);
		initConfig();
		addConnection();
	}

	/**
	 * 返回连接到连接池中
	 */
	public synchronized void release(Connection conn) {
		pool.add(conn);
	}

	/**
	 * 释放Connection回连接池，释放Statement、ResultSet
	 */
	public void freeConn(Connection conn, Statement st, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				if (LOG.isErrorEnabled()) {
					LOG.error(e.getMessage(), e);
				}
			} finally {
				release(conn);
			}
		}

	}

	/**
	 * 关闭连接池中的所有数据库连接
	 */
	public synchronized void closePool() {
		int index = 0;
		while (pool.size() > index) {
			try {
				((Connection) pool.get(index)).close();
				pool.remove(index);
			} catch (SQLException e) {
				if (LOG.isErrorEnabled()) {
					LOG.error(e.getMessage(), e);
				}
				index++;
			}
		}
	}

	/**
	 * 返回当前连接池的一个对象
	 */
	public static ConnectionPool getInstance() {
		if(instance == null){
			instance = new ConnectionPool();
		}
		return instance;
	}

	/**
	 * 返回连接池中第一个数据库连接
	 */
	private Connection getFreeConnection() {
		if (pool.size() > 0) {
			Connection conn = pool.get(0);
			pool.remove(conn);
			return conn;
		} else {
			return null;
		}
	}

	/**
	 * 获得连接池中可用的连接
	 */
	public synchronized Connection getConnection() throws SQLException {

		Connection conn = getFreeConnection();

		// 如果目前没有可以使用的连接，即所有的连接都在使用中
		int retryCount = 0;
		while (conn == null && retryCount <= 100) {
			try {
				wait(250);
			} catch (InterruptedException e) {
				if (LOG.isErrorEnabled()) {
					LOG.error(e.getMessage(), e);
				}
			}
			// 如果连接池中连接均被使用，则重试100次，每次间隔0.25秒
			conn = getFreeConnection();
			retryCount++;
		}

		if (conn == null) {
			if (LOG.isErrorEnabled()) {
				LOG.error("数据库连接池繁忙，没有获得可用的数据库连接....");
			}
		}

		return conn;
	}

	/**
	 * 在连接池中创建初始设置的的数据库连接
	 */
	private void addConnection() {
		Connection conn = null;
		for (int i = 0; i < poolSize; i++) {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = java.sql.DriverManager.getConnection(url, RSAUtils.decryptCode(username), RSAUtils.decryptCode(password));
				pool.add(conn);
			} catch (ClassNotFoundException e) {
				if (LOG.isErrorEnabled()) {
					LOG.error(e.getMessage(), e);
				}
			} catch (SQLException e) {
				if (LOG.isErrorEnabled()) {
					LOG.error(e.getMessage(), e);
				}
			}

		}
	}

}
