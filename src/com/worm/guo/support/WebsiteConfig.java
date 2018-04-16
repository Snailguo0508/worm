package com.worm.guo.support;

import java.io.File;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @Title:
 * @Desc:
 * @Company:Blwit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2014-5-4下午8:14:03
 *
 */
public class WebsiteConfig {
	
	private static Log LOG = LogFactory.getLog(WebsiteConfig.class);
	
	public static String WEBSITE_PATH = "";
	
	/** 数据库连接池大小 */
	public static int PoolSize = 1;
	/** 数据库地址 */
	public static String DBUrl = "";
	/** 数据库驱动名称 **/
	public static String DriverName = "";
	/** 数据库用户名 */
	public static String DBUserName = "";
	/** 数据库密码 */
	public static String DBPassword = "";
	
	public static boolean ISPUBNET = true;
	
	public static boolean xmlInit(String databaseName) {
		
		boolean rtn = false;

		SAXReader reader = new SAXReader();
		File file = new File("DatabaseConfig.xml");
		try {
			Document document = reader.read(file);
			Element eroot = document.getRootElement();
			List nodes = eroot.elements();
			
			for (int i = 0; i < nodes.size(); i++) {
				Element element = (Element) nodes.get(i);
				
				if (element.getName().equalsIgnoreCase("WebsitePath")) {
					WebsiteConfig.WEBSITE_PATH = element.getData().toString().trim();
				}
				if (element.getName().equalsIgnoreCase("IsPubNet")) {
					int net = Integer.parseInt(element.getData().toString().trim());
					if (net == 1) {
						WebsiteConfig.ISPUBNET = true;
					}else {
						WebsiteConfig.ISPUBNET = false;
					}
					
				}
				if (element.getName().equalsIgnoreCase("DBConfig")) {
					List dbNodes = element.elements();
					if (dbNodes != null && dbNodes.size() >0) {
						for(int j=0;j<dbNodes.size();j++){
							Element dbElement = (Element) dbNodes.get(j);
							if (dbElement.getName().equalsIgnoreCase("PoolSize")) {
								WebsiteConfig.PoolSize = Integer.parseInt(dbElement.getData().toString().trim());
							}
							if (dbElement.getName().equalsIgnoreCase("DriverName")) {
								WebsiteConfig.DriverName = dbElement.getData().toString().trim();
							}
							if (dbElement.getName().equalsIgnoreCase("DBUrl")) {
								WebsiteConfig.DBUrl = dbElement.getData().toString().trim();
							}
							if (dbElement.getName().equalsIgnoreCase("DBUserName")) {
								WebsiteConfig.DBUserName = dbElement.getData().toString().trim();
							}
							if (dbElement.getName().equalsIgnoreCase("DBPassword")) {
								WebsiteConfig.DBPassword = dbElement.getData().toString().trim();
							}
						}
					}
				}
				rtn = true;
			}	
		}catch(Exception e){
			rtn = false;
			if (LOG.isErrorEnabled()) {
				LOG.error("解析配置文件信息异常");
				LOG.error(e.getMessage(),e);
			}
		}
		return rtn;
	}
}
