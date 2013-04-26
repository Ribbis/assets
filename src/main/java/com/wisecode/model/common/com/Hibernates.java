package com.wisecode.model.common.com;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hibernate 工具类
 * : Administrator
 * Date: 13-4-16
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class Hibernates {

	/**
	 * Initialize the lazy property value, eg.
	 * Hibernates.initLazyProperty(.getRoles());
	 * @param proxyedPropertyValue
	 */
	public static void initLazyProperty(Object proxyedPropertyValue){
		Hibernate.initialize(proxyedPropertyValue);

	}

	/**
	 *  从DataSource中取出Connection，根据Connection的Metadata中的JdbcUrl判断Dialect类型
	 * @param dataSource
	 * @return
	 */
	public static String getDialect(DataSource dataSource){
		// 从DataSource中取出JdbcUrl
		String jdbcUrl = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (connection == null){
				throw new IllegalStateException("Connection returned by DataSource ["+ dataSource+"] was null");
			}
			jdbcUrl = connection.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url",e);
		}finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					connection = null;
				}
			}
		}
		if(StringUtils.contains(jdbcUrl,":h2:")){
			return H2Dialect.class.getName();
		}else if (StringUtils.contains(jdbcUrl,":mysql:")){
			return MySQL5InnoDBDialect.class.getName();
		}else if(StringUtils.contains(jdbcUrl,":oracle:")){
			return Oracle10gDialect.class.getName();
		}else{
			throw new IllegalArgumentException("Unknown Database");
		}
	}
}
