package com.worm.guo.support;

public class HttpClientObj {

	private String proxy_ip;
	private int proxy_port;
	private String proxy_userName;
	private String proxy_userPwd;
	private String domain;
	
	public String getProxy_ip() {
		return proxy_ip;
	}
	public void setProxy_ip(String proxyIp) {
		proxy_ip = proxyIp;
	}
	public int getProxy_port() {
		return proxy_port;
	}
	public void setProxy_port(int proxyPort) {
		proxy_port = proxyPort;
	}
	public String getProxy_userName() {
		return proxy_userName;
	}
	public void setProxy_userName(String proxyUserName) {
		proxy_userName = proxyUserName;
	}
	public String getProxy_userPwd() {
		return proxy_userPwd;
	}
	public void setProxy_userPwd(String proxyUserPwd) {
		proxy_userPwd = proxyUserPwd;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
