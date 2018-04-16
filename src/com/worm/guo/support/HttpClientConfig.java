package com.worm.guo.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class HttpClientConfig {
	private static Properties props = new Properties();
	private static int count = 0;
	
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("httpclient-agency.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static void updateProperties(String key, String value) {
		props.setProperty(key, value);
	}
	
	public static HttpClientObj getHttpClientObj(){
		try{
			HttpClientObj obj = new HttpClientObj();
			int objCount = Integer.valueOf(getValue("httpclient.count")) - 1;
			if(objCount >= 0){
				String proxyIp = getValue("httpclient.proxy_ip").split(";")[count];
				String proxyPort = getValue("httpclient.proxy_port").split(";")[count];
				String proxyUserName = "";
				String proxyUserPwd = "";
				String domain = "";
				if(StringUtils.isNotBlank(getValue("httpclient.proxy_userName"))){
					proxyUserName = getValue("httpclient.proxy_userName").split(";")[count];
				}
				if(StringUtils.isNotBlank(getValue("httpclient.proxy_userPwd"))){
					proxyUserPwd = getValue("httpclient.proxy_userPwd").split(";")[count];
				}
				if(StringUtils.isNotBlank(getValue("httpclient.domain"))){
					domain = getValue("httpclient.domain").split(";")[count];
				}
				obj.setProxy_ip(proxyIp);
				obj.setProxy_port(Integer.valueOf(proxyPort));
				obj.setProxy_userName(proxyUserName);
				obj.setProxy_userPwd(proxyUserPwd);
				obj.setDomain(domain);
				
				if(count >= objCount){
					count = 0;
				}else{
					count++;
				}
				return obj;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
