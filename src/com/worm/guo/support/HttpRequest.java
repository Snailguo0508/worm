package com.worm.guo.support;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URLEncodedUtils;

import com.sun.xml.internal.bind.v2.runtime.output.Encoded;

/**
 * 
 * <p>
 * 封装的http请求类


 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author lrf
 * @version 1.0
 */
public class HttpRequest {
	public final static String REQ_POST = "POST";
	public final static String REQ_GET = "GET";

	private final static Pattern CHAR_PATTERN = Pattern.compile("<meta[^>]+charset=(.+?)\"");
	private final static Pattern CHARSET_PATTERN = Pattern.compile("<meta charset=(.+?)>");
	
	private static int request_timeout = 120000; // 连接超时设置 10秒	private static int read_timeout = 120000; // 读取设置 5秒
	
	private static String proxy_userName = "";
	private static String proxy_userPwd = "";
	private static String proxy_ip = "";
	private static int proxy_port = 80;
	private static String host = "";
	private static String domain = "hold";
	private static final Log log = LogFactory.getLog(HttpRequest.class);
	private final static String ESCAPE_REG = "(%[a-fA-F0-9]{2})";
	private final static String GBK_UNICODE_REG = "[\u4e00-\u9fa5]+";
	private final static String CHARSET_REGX = "<meta [^>](.*?)charset=(.*?)>";
	
	
	public static String[] UserAgent = {
			"Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.2",
			"Mozilla/5.0 (iPad; U; CPU OS 3_2_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B500 Safari/531.21.11",			
			"Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/989",
			"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; rv:26.0) Gecko/20100101 Firefox/26.0" };

	
	private static Cookie[] cookies;

	private HttpRequest() {
	}
	
	/**
	 * 发送http请求，返回请求内容

	 * 
	 * @param url
	 *            地址
	 * @param reqMethed
	 *            请求方法 POST GET 现在get未加其他处理
	 * @param isRedirectory
	 *            是否重定向

	 * @param encode
	 *            返回内容的编码方式

	 * @param params
	 *            POST或GET方式的参数

	 * @parm isRedirectory 是否重定向

	 * @param userName
	 *            认证用户名与密码；若无认证，置null
	 * @param authenticationHost
	 *            认证HOST地址,如 www.example.com; 若无认证，置null
	 * @return map 返回内容 key stcode:返回码 stinfo:返回码信息 content:内容
	 */
	public static Map<String, String> getWebPageCode(String url, String reqMethod, String requestBody, boolean isRedirectory, String encode,List<NameValuePair> params, String userName, String password, String authenticationHost, String contentType) {
		HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        //HttpConnectionManagerParams httpparams = httpConnectionManager.getParams();
        //httpparams.setDefaultMaxConnectionsPerHost(32);//very important!! 默认只有两个
        //httpparams.setMaxTotalConnections(256);//very important!! 
		HttpClient client = new HttpClient(httpConnectionManager);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(request_timeout);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		int retryCount = 1;//重试次数
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(retryCount, false));
		
		boolean isAuthentication = false;
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
			client.getState().setCredentials(new AuthScope(authenticationHost, 80, null),new UsernamePasswordCredentials(userName, password));
			isAuthentication = true;
		}
		Map<String, String> ret = null;
		try {
			ret = getWebPageContent(client, url, reqMethod, requestBody, isRedirectory, encode, params, isAuthentication, contentType);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("请求信息异常...url:" + url);
				log.error(e);
			}
		}
		return ret;
	}
	
	
	private static Map<String, String> getWebPageContent(HttpClient client, String url, String reqMethod, String requestBody, boolean isRedirectory, String encode, List<NameValuePair> params, boolean isAuthentication, String contentType)throws Exception {
		//url = getNewUrl(url);
		// 设置代理
		HostConfiguration hcf = setProxy(client);
		
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;

		if (!isEscapeUrl(url)) {
			url = URIUtil.encodePathQuery(url, "utf-8");
		} else {
			url = escapeChineseChar(url);
		}

		StringBuffer html = new StringBuffer();
		URL tempUrl = new URL(url);
		if (reqMethod.equals(REQ_POST)) {
			post = new PostMethod(url);
			post.setDoAuthentication(isAuthentication);
			if(params != null){
				for (NameValuePair p : params) {
					post.addParameter(p);
				}
			}
			InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes());
			post.setRequestBody(requestBodyStream);
			if(StringUtils.isNotBlank(contentType)){
				post.addRequestHeader("Content-Type", contentType);// 
			}else{
				//西藏网
				if (tempUrl.getHost().equals("news.iuoooo.com")) {
					post.addRequestHeader("Content-Type", "application/json; charset=utf-8");
					post.addRequestHeader("ApplicationContext", "eyJBcHBQYWNrQWNjVG9rZW4iOm51bGwsIkVtcGxveWVlSWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJJRCI6bnVsbCwiTGF0aXR1ZGUiOm51bGwsIkxvZ2luQ3VycmVudEN1bHR1cmUiOjAsIkxvZ2luRGVwYXJ0bWVudCI6IjAwMDAwMDAwLTAwMDAtMDAwMC0wMDAwLTAwMDAwMDAwMDAwMCIsIkxvZ2luRGVwYXJ0bWVudE5hbWUiOm51bGwsIkxvZ2luSVAiOm51bGwsIkxvZ2luT3JnIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwiTG9naW5PcmdDb2RlIjpudWxsLCJMb2dpbk9yZ05hbWUiOm51bGwsIkxvZ2luVGVuYW50SWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJMb2dpblRlbmFudE5hbWUiOm51bGwsIkxvZ2luVGltZSI6IlwvRGF0ZSgxNDE1NjA2NjAzOTQzKzA4MDApXC8iLCJMb2dpblVzZXJDb2RlIjoiMDMxRjU0RTQtN0JFNS00MjIxLTk2RUQtOEQ1NDA3QzAzQ0JCQDAyOjAwOjAwOjAwOjAwOjAwIiwiTG9naW5Vc2VySUQiOiJkNzg2OTdjMS04MjFlLTQ3NTQtYTAxZC03YzQ0NTc3ZjdkZmMiLCJMb2dpblVzZXJOYW1lIjpudWxsLCJMb25naXR1ZGUiOm51bGwsIlNlc3Npb25JRCI6IjI1YjU4Y2ZiLThiOTQtNDVlNC1hNzE4LTllMzcxNDBkY2M1YyJ9");
					post.addRequestHeader("Cookie", "ASP.NET_SessionId=jg0e1ifzcak31r0upfop32m3");
				}else if(tempUrl.getHost().contains("www.mingjingnews.com")){
					post.addRequestHeader("Accept-Encoding", "identity");
					post.addRequestHeader("X-Requested-With", "XMLHttpRequest");
					post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					post.addRequestHeader("Connection", "Keep-Alive");
					post.addRequestHeader("User-Agent", "Appcelerator Titanium/5.5.1 (R831T; Android API Level: 19; zh-CN;)");
					post.addRequestHeader("X-Titanium-Id", "ac49e1e8-4747-401a-9999-b4d1935e8a5a");
				}else {
					post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");
				}
			}
			httpMethod = post;
		} else {
			get = new GetMethod(url);
			if (isAuthentication) {
				get.setDoAuthentication(isAuthentication);
			} else {
				if ("".equals(proxy_ip) && "无".equals(proxy_ip)) {
					get.setDoAuthentication(isAuthentication);
				} else {
					get.setDoAuthentication(true); // 有代理的话，要设置成true
				}
			}

			get.setFollowRedirects(isRedirectory);
			get.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*;q=0.8");
			get.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			get.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			get.addRequestHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12 GTB7.1");
			get.addRequestHeader("Cache-Control", "max-age=0");
			get.addRequestHeader("Connection", "keep-alive");
			//TODO 今日头条
			if(tempUrl.getHost().equals("ic.snssdk.com")){
				get = new GetMethod(url);
				get.addRequestHeader("Accept-Encoding","gzip");
				get.addRequestHeader("User-Agent","Dalvik/2.1.0 (Linux; U; Android 7.0; EVA-AL00 Build/HUAWEIEVA-AL00) NewsArticle/6.0.1 okhttp/3.4.1");
				get.addRequestHeader("Cookie","install_id=8159422438; ttreq=1$b29d068516fccba2b56ce1cb5c4ddf59f737f080; qh[360]=1");
				get.addRequestHeader("X-SS-REQ-TICKET",String.valueOf(System.currentTimeMillis()));
				get.addRequestHeader("Connection","Keep-Alive");
				get.addRequestHeader("Host"," ic.snssdk.com");
			}else if (tempUrl.getHost().equals("a1.go2yd.com")) {	//一点资讯
				get = new GetMethod(url);
				get.addRequestHeader("Cookie","JSESSIONID=m1ZEzZeXFSQgNvAOAnpheQ");
			}else if(tempUrl.getHost().equals("r.inews.qq.com")){
				get.addRequestHeader("User-Agent", "腾讯新闻5470(android)");
				get.addRequestHeader("Cookie", "lskey=;luin=;skey=;uin=; logintype=0; main_login=;");
				get.addRequestHeader("Accept-Encoding","gzip,deflate");	
			}
			httpMethod = get;
		}
		Map<String, String> ret = new HashMap<String, String>();
		int stCode = HttpStatus.SC_BAD_REQUEST;
		try {
			if (hcf != null) {
				stCode = client.executeMethod(hcf, httpMethod);
			} else {
				stCode = client.executeMethod(httpMethod);
			}
			if (stCode == HttpStatus.SC_OK) {
				byte[] bytes = null;
				Header contentEncodingHeader = httpMethod.getResponseHeader("Content-Encoding");
				if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
					bytes = unzip(httpMethod.getResponseBodyAsStream());
				} else {
					bytes = streamToByte(httpMethod.getResponseBodyAsStream());
				}
				if (StringUtils.isBlank(encode)) {
					encode = getCharset(encode, bytes);
				}
				try {
					
					ret.put("host", tempUrl.getProtocol() + "://" + tempUrl.getHost() + (StringUtils.isNotBlank(tempUrl.getPath()) ? "/" + tempUrl.getPath().split("/")[1] : ""));
					if(bytes != null && bytes.length > 3 && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65){ //BOM,代表UTF-8编码
						byte[] temp = new byte[bytes.length-3];
						for(int i=3;i<bytes.length;i++){
							temp[i-3] = bytes[i];
						}
						bytes = temp;
					}
					if(StringUtils.isBlank(encode)){
						encode = "UTF-8";
					}
					InputStream input = new ByteArrayInputStream(bytes);
					
					if(tempUrl.getHost().equals("dlhsp85jeriqp.cloudfront.net")){
						html.append(readWeiXinDumpValue(input));
					}else{
						BufferedReader reader = new BufferedReader(new InputStreamReader(input, encode));
						String line = null;
						while ((line = reader.readLine()) != null) {
							html.append(line);
						}
					}
				} catch (UnsupportedEncodingException ue) {
					html.append(new String(bytes, "ISO-8859-1"));
				}
				ret.put("content", html.toString());
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("获取URL源码失败，url:"+url, e);
			}
		} finally {
			ret.put("stcode", stCode + "");
			if (get != null) {
				StatusLine statusLine = httpMethod.getStatusLine();
				if (statusLine != null) {
					ret.put("stinfo", statusLine.toString());
				} else {
					ret.put("stinfo", "connect error!");
				}
			} else {
				ret.put("stinfo", "connect error!");
			}
			httpMethod.releaseConnection();
		}
		
		return ret;

	}
	
	
	 public static String readWeiXinDumpValue(InputStream in){
    	String rtn = "";
        byte[] buffer = new byte[1024];
        try {
	        BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
	        StringBuffer sb = new StringBuffer();
	        while(bzIn.read(buffer)!= -1){
	             String str = new String(buffer, "utf-8");
	             sb.append(str);
	        }
	        rtn = sb.toString(); 
	        rtn = rtn.substring(0, rtn.lastIndexOf("}]}")) + "}]}";
	        if (bzIn != null) {
				bzIn.close();
			}
	        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rtn;
    }
	//URL加随机数
	public static String getNewUrl(String url){
		if(url.indexOf("?")<0) url += "?";
		else url += "&";
		url += "___="+Math.random();
		return url;
	}

	/**
	 * 发送http请求，返回请求内容


	 * 
	 * @param url
	 *            地址
	 * @param reqMethed
	 *            请求方法 POST GET 现在get未加其他处理
	 * @param isRedirectory
	 *            是否重定向


	 * @param encode
	 *            返回内容的编码方式


	 * @param params
	 *            POST或GET方式的参数


	 * @parm isRedirectory 是否重定向


	 * @param userName
	 *            认证用户名与密码；若无认证，置null
	 * @param authenticationHost
	 *            认证HOST地址,如 www.example.com; 若无认证，置null
	 * @return map 返回内容 key stcode:返回码 stinfo:返回码信息 content:内容
	 */
	public static Map<String, String> readHTML(String url, String reqMethod, boolean isRedirectory, String encode,
			List<NameValuePair> params, String userName, String password, String authenticationHost) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		boolean isAuthentication = false;
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
			client.getState().setCredentials(new AuthScope(authenticationHost, 80, null),
					new UsernamePasswordCredentials(userName,password));
			isAuthentication = true;
		}
		Map<String, String> ret = null;
		try {
			ret = getUrlContent(client, url, reqMethod, isRedirectory, encode, params, isAuthentication);
			if (ret.get("stcode").equals(HttpStatus.SC_OK + "")) {
				List<String> frames = ParseFrame.getFramesUrl(ret.get("content"));
				if (frames != null && frames.size() >0) {
					Map<String, String> retFrame = null;
					URL hostUrl = new URL(url);
					for (String frameUrl : frames) {
						retFrame = getUrlContent(client, "http://" + hostUrl.getHost()
								+ (hostUrl.getPort() == -1 ? "" : ":" + hostUrl.getPort()) + "/" + frameUrl, reqMethod,
								isRedirectory, encode, params, isAuthentication);
						if (retFrame.get("stcode").equals(HttpStatus.SC_OK + "")) {
							ret.put("content", url + "\n\n" + ret.get("content") + "\n\n" + frameUrl
									+ retFrame.get("content"));
						}
					}
				}
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
		}
		return ret;
	}
	
	public static Map<String, String> readHTML(String url, String reqMethod, boolean isRedirectory, String encode,
			List<NameValuePair> params, String userName, String password, String authenticationHost,int readTimeout) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		if (readTimeout>0) { //设置读超时时间


			client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
		}
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		boolean isAuthentication = false;
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
			client.getState().setCredentials(new AuthScope(authenticationHost, 80, null),
					new UsernamePasswordCredentials(userName, password));
			isAuthentication = true;
		}
		Map<String, String> ret = null;
		try {
			ret = getUrlContent(client, url, reqMethod, isRedirectory, encode, params, isAuthentication);
			if (ret.get("stcode").equals(HttpStatus.SC_OK + "")) {
				List<String> frames = ParseFrame.getFramesUrl(ret.get("content"));
				if (frames != null && frames.size() >0) {
					Map<String, String> retFrame = null;
					URL hostUrl = new URL(url);
					for (String frameUrl : frames) {
						retFrame = getUrlContent(client, "http://" + hostUrl.getHost()
								+ (hostUrl.getPort() == -1 ? "" : ":" + hostUrl.getPort()) + "/" + frameUrl, reqMethod,
								isRedirectory, encode, params, isAuthentication);
						if (retFrame.get("stcode").equals(HttpStatus.SC_OK + "")) {
							ret.put("content", url + "\n\n" + ret.get("content") + "\n\n" + frameUrl
									+ retFrame.get("content"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	} 
	
	private static Map<String, String> readXml(String url, String reqMethod, boolean isRedirectory, String encode,
			List<NameValuePair> params, String postXml, String xmlCharset,int readTimeout) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		if (readTimeout>0) { //设置读超时时间


			client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
		}
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置代理
		HostConfiguration hcf =setProxy(client);
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;
		try {
			if (!isEscapeUrl(url)) {
				url = URIUtil.encodePathQuery(url, "utf-8");
			} else {
				url = escapeChineseChar(url);
			}
		} catch (URIException e1) {
		}
		if (reqMethod.equals(REQ_POST) && params != null) {
			post = new PostMethod(url);
			for (NameValuePair p : params) {
				post.addParameter(p);
			}
			post.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			post.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httpMethod = post;
		} else if (reqMethod.equals(REQ_POST) && postXml != null) {
			post = new PostMethod(url);
			RequestEntity entity = null;
			try {
				entity = new StringRequestEntity(postXml, null, "utf-8");
			} catch (UnsupportedEncodingException e) {
				if (log.isErrorEnabled()) {
					log.error(e);
				}
			}
			post.setRequestEntity(entity);
			httpMethod = post;
		} else if (reqMethod.equals(REQ_GET)) {
			get = new GetMethod(url);
			get.setFollowRedirects(isRedirectory);
			get.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			get.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httpMethod = get;
		}

		Map<String, String> ret = new HashMap<String, String>();
		int stCode = HttpStatus.SC_BAD_REQUEST;
		try {
			if (hcf != null) {
				stCode = client.executeMethod(hcf, httpMethod);
			} else {
				stCode = client.executeMethod(httpMethod);
			}

			if (stCode == HttpStatus.SC_OK) {
				byte[] bytes = httpMethod.getResponseBody();
				encode = getCharset(encode, bytes);

				String html = "";
				try {
					html = new String(bytes, encode);
				} catch (UnsupportedEncodingException ue) {
					html = new String(bytes, "ISO-8859-1");
				}
				ret.put("content", html);
			}
		} catch (java.net.SocketTimeoutException ioe) {
			ioe.printStackTrace();
			stCode=HttpStatus.SC_REQUEST_TIMEOUT; // 读超时。


		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e);
				log.error("URL: " + url);
			}
		} finally {
			ret.put("stcode", stCode + "");
			if (get != null) {
				StatusLine statusLine = httpMethod.getStatusLine();
				if (statusLine != null) {
					ret.put("stinfo", statusLine.toString());
				} else {
					ret.put("stinfo", "connect error!");
				}
			} else {
				ret.put("stinfo", "connect error!");
			}
			httpMethod.releaseConnection();
		}
		
		return ret;

	}
	
	
	
	/**
	 * 发送http请求，发送内容为xml的


	 * 
	 * @param url
	 *            地址
	 * @param reqMethed
	 *            请求方法 POST GET 现在get未加其他处理
	 * @param isRedirectory
	 *            是否重定向


	 * @param encode
	 *            返回内容的编码方式


	 * @param postXml
	 *            发送请求的xml字符串


	 * @param xmlCharsetxml编码
	 * @return map 返回内容 key stcode:返回码 stinfo:返回码信息 content:内容
	 */
	public static Map<String, String> postXml(String url, String reqMethod, boolean isRedirectory, String encode,
			String postXml, String xmlCharset) {
		return readXml(url, reqMethod, isRedirectory, encode, null, postXml, xmlCharset,0);
	}

	/**
	 * 发送http请求，发送内容为xml的


	 * 
	 * @param url
	 *            地址
	 * @param reqMethed
	 *            请求方法 POST GET 现在get未加其他处理
	 * @param isRedirectory
	 *            是否重定向


	 * @param encode
	 *            返回内容的编码方式


	 * @param postXml
	 *            发送请求的xml字符串


	 * @param xmlCharsetxml编码
	 * @return map 返回内容 key stcode:返回码 stinfo:返回码信息 content:内容
	 */
	public static Map<String, String> postXml(String url, String reqMethod, boolean isRedirectory, String encode,
			String postXml, String xmlCharset,int readTimeout) {
		return readXml(url, reqMethod, isRedirectory, encode, null, postXml, xmlCharset,readTimeout);
	}

	/**
	 * 用于和底层服务获取xml信息
	 * 发送http请求，发送内容为xml的


	 * 
	 * @param url
	 *            地址
	 * @param postXml
	 *            发送请求的xml字符串


	 * @param xmlCharsetxml编码
	 * @return map 返回内容 key stcode:返回码 stinfo:返回码信息 content:内容
	 */
	public static Map<String, String> postSupportXml(String url,String postXml, String xmlCharset,int readTimeout) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		if (readTimeout>0) { //设置读超时时间


			client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
		}
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置代理
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;
		String reqMethod=REQ_POST;
		if (reqMethod.equals(REQ_POST) && postXml != null) {
			post = new PostMethod(url);
			RequestEntity entity = null;
			try {
				entity = new StringRequestEntity(postXml, null, "utf-8");
			} catch (UnsupportedEncodingException e) {
				if (log.isErrorEnabled()) {
					log.error(e);
				}
			}
			post.setRequestEntity(entity);
			httpMethod = post;
		}

		Map<String, String> ret = new HashMap<String, String>();
		int stCode = HttpStatus.SC_BAD_REQUEST;
		try {
			stCode = client.executeMethod(httpMethod);

			if (stCode == HttpStatus.SC_OK) {
				byte[] bytes = httpMethod.getResponseBody();
				String html = "";
				try {
					html = new String(bytes, xmlCharset);
				} catch (UnsupportedEncodingException ue) {
					html = new String(bytes, "ISO-8859-1");
				}
				ret.put("content", html);
			}
		} catch (java.net.SocketTimeoutException ioe) {
			ioe.printStackTrace();
			stCode=HttpStatus.SC_REQUEST_TIMEOUT; // 读超时。


		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e);
				log.error("URL: " + url);
			}
		} finally {
			ret.put("stcode", stCode + "");
			if (get != null) {
				StatusLine statusLine = httpMethod.getStatusLine();
				if (statusLine != null) {
					ret.put("stinfo", statusLine.toString());
				} else {
					ret.put("stinfo", "connect error!");
				}
			} else {
				ret.put("stinfo", "connect error!");
			}
			httpMethod.releaseConnection();
		}
		return ret;
	}
	
	private static Map<String, String> getUrlContent(HttpClient client, String url, String reqMethod,
			boolean isRedirectory, String encode, List<NameValuePair> params, boolean isAuthentication)
			throws Exception {
		// 设置代理
		HostConfiguration hcf = setProxy(client);
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;

		if (!isEscapeUrl(url)) {
			url = URIUtil.encodePathQuery(url, "utf-8");
		} else {
			url = escapeChineseChar(url);
		}

		String html = "";

		if (reqMethod.equals(REQ_POST) && params != null) {
			post = new PostMethod(url);
			post.setDoAuthentication(isAuthentication);

			for (NameValuePair p : params) {
				post.addParameter(p);
			}
			post.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			post.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httpMethod = post;
		} else {
			get = new GetMethod(url);
			if (isAuthentication) {
				get.setDoAuthentication(isAuthentication);
			} else {
				if ("".equals(proxy_ip) && "无".equals(proxy_ip)) {
					get.setDoAuthentication(isAuthentication);
				} else {
					get.setDoAuthentication(true); // 有代理的话，要设置成true
				}
			}

			get.setFollowRedirects(isRedirectory);
			get.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			get.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			get.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			get
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12 GTB7.1");
			get.addRequestHeader("Cache-Control", "max-age=0");
			get.addRequestHeader("Connection", "keep-alive");
			get.addRequestHeader("Keep-Alive", "115");
			httpMethod = get;
		}
		
		if ( cookies != null && cookies.length > 0 ){
			client.getState().addCookies(cookies);
		}
		Map<String, String> ret = new HashMap<String, String>();
		int stCode = HttpStatus.SC_BAD_REQUEST;
		try {
			if (hcf != null) {
				stCode = client.executeMethod(hcf, httpMethod);
			} else {
				stCode = client.executeMethod(httpMethod);
			}
			if (stCode == HttpStatus.SC_OK) {
				byte[] bytes = null;
				Header contentEncodingHeader = httpMethod.getResponseHeader("Content-Encoding");
				if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
					bytes = unzip(httpMethod.getResponseBodyAsStream());
				} else {
					bytes = httpMethod.getResponseBody();
				}
				encode = getCharset(encode, bytes);
				try {
					html = new String(bytes, encode);
				} catch (UnsupportedEncodingException ue) {
					html = new String(bytes, "ISO-8859-1");
				}
				html = StringEscapeUtils.unescapeHtml(html);
				ret.put("content", html);
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("获取URL源码失败，url:"+url, e);
			}
		} finally {
			ret.put("stcode", stCode + "");
			if (get != null) {
				StatusLine statusLine = httpMethod.getStatusLine();
				if (statusLine != null) {
					ret.put("stinfo", statusLine.toString());
				} else {
					ret.put("stinfo", "connect error!");
				}
			} else {
				ret.put("stinfo", "connect error!");
			}
			httpMethod.releaseConnection();
		}
		
		return ret;

	}

	/**
	 * 依据返回内容判断内容编码
	 * 
	 * @param reqEncode
	 *            请求编码
	 * @param httpMethod
	 *            get post
	 * @return
	 */
	public static String getCharset(String reqEncode, byte[] bytes) throws Exception {
		String charset = reqEncode;
		InputStream is = new ByteArrayInputStream(bytes);
		charset = getCharset(is);
		return charset;
	}
	
	public static String getCharsetFirst(String reqEncode, byte[] bytes) throws Exception {
		String charset = reqEncode;
		if (StringUtils.isBlank(reqEncode)) {
			if (StringUtils.isBlank(charset) || charset.equals("ISO-8859-1")) {
				String body = new String(bytes, "ISO-8859-1");
				Matcher m = Pattern.compile(CHARSET_REGX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(body);
				if (m.find()) {
					String buffer = m.group().toLowerCase();
					int pos = buffer.indexOf("charset");
					if (pos != -1) {
						buffer = buffer.substring(pos);
						buffer = buffer.substring("charset".length() + 1);
						m = Pattern.compile("[\\w|-]+").matcher(buffer);
						if (m.find()) {
							charset = m.group();
						}
					}
				}
			}
		}
		return charset;
	}
	
	private static boolean isEscapeUrl(String url) {
		boolean ret = false;
		if (StringUtils.isNotBlank(url)) {
			Matcher m = Pattern.compile(ESCAPE_REG).matcher(url);
			if (m.find()) {
				ret = true;
			}
		}
		return ret;
	}
	
	public static byte[] readStream(String url, String reqMethod){
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(request_timeout);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		HostConfiguration hcf = setProxy(client);
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;


		if (reqMethod.equals(REQ_POST)) {
			post = new PostMethod(url);
			post.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			post.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httpMethod = post;
		} else {
			get = new GetMethod(url);
			get.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			get.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			get.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			get
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12 GTB7.1");
			get.addRequestHeader("Cache-Control", "max-age=0");
			get.addRequestHeader("Connection", "keep-alive");
			get.addRequestHeader("Keep-Alive", "115");
			httpMethod = get;
		}
		int stCode = HttpStatus.SC_BAD_REQUEST;
		byte[] bytes = null;
		try {
			if (hcf != null) {
				stCode = client.executeMethod(hcf, httpMethod);
			} else {
				stCode = client.executeMethod(httpMethod);
			}
			if (stCode == HttpStatus.SC_OK) {
				Header contentEncodingHeader = httpMethod.getResponseHeader("Content-Encoding");
				if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
					bytes = unzip(httpMethod.getResponseBodyAsStream());
				} else {
					bytes = httpMethod.getResponseBody();
				}
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e);
				log.error("URL: " + url);
			}
		} finally {
			httpMethod.releaseConnection();
		}
		return bytes;
		
	}
	
	public static boolean getWebsiteCookie(String url,String reqMethod){
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(request_timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(request_timeout);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		HostConfiguration hcf = setProxy(client);
		PostMethod post = null;
		GetMethod get = null;
		HttpMethodBase httpMethod = null;
		boolean ret = false;

		if (reqMethod.equals(REQ_POST)) {
			post = new PostMethod(url);
			post.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			post.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httpMethod = post;
		} else {
			get = new GetMethod(url);
			get.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			get.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
			get.addRequestHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			get
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12 GTB7.1");
			get.addRequestHeader("Cache-Control", "max-age=0");
			get.addRequestHeader("Connection", "keep-alive");
			get.addRequestHeader("Keep-Alive", "115");
			httpMethod = get;
		}

		int stCode = HttpStatus.SC_BAD_REQUEST;
		try {
			if (hcf != null) {
				stCode = client.executeMethod(hcf, httpMethod);
			} else {
				stCode = client.executeMethod(httpMethod);
			}
			if (stCode == HttpStatus.SC_OK) {
				cookies = client.getState().getCookies();
				ret = (cookies == null | cookies.length == 0 );
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e);
				log.error("URL: " + url);
			}
		} finally {
			httpMethod.releaseConnection();
		}
		
		return ret;
	}

	/**
	 * 若URL中存在中文字符则进行URL编码
	 * 
	 * @param url
	 * @return
	 */
	private static String escapeChineseChar(String url) {
		String chineseChar = "";
		String escChineseChars = "";
		// 判断是否包含中文字符
		try {
			for (int i = 0; i < url.length(); i++) {
				if (url.substring(i, i + 1).matches(GBK_UNICODE_REG)) {
					chineseChar = url.substring(i, i + 1);
					escChineseChars = URIUtil.encodeAll(chineseChar);
					url = url.replace(chineseChar, escChineseChars);
				}
			}
		} catch (URIException e) {
			if (log.isErrorEnabled())
				log.error(e);
		}
		return url;
	}

	/**
	 * gzip解压
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] unzip(InputStream in) throws IOException {
		GZIPInputStream gin = new GZIPInputStream(in);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[255];
		int len;
		while ((len = gin.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		gin.close();
		out.close();
		return out.toByteArray();
	}
	
	public static byte[] streamToByte(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[255];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		return out.toByteArray();
	}

	/**
	 * 设置代理信息
	 */
	private static HostConfiguration setProxy(HttpClient client) {
		HostConfiguration hcf = null;
		//TODO 多个代理服务IP
		HttpClientObj obj = HttpClientConfig.getHttpClientObj();
		if(obj != null){
			proxy_ip = obj.getProxy_ip();
			proxy_port = obj.getProxy_port();
			proxy_userName = obj.getProxy_userName();
			proxy_userPwd = obj.getProxy_userPwd();
			domain = obj.getDomain();
		}
		if (proxy_ip != null && proxy_ip.length() > 0 && !"无".equals(proxy_ip)) {
			// 设置代理
			client.getState().setProxyCredentials(AuthScope.ANY,
					new NTCredentials(proxy_userName, proxy_userPwd, host, domain));
			hcf = new HostConfiguration();
			hcf.setProxy(proxy_ip, proxy_port);
		}
		return hcf;
	}

	public static void setProxy_ip(String proxy_ip) {
		HttpRequest.proxy_ip = proxy_ip;
	}

	public static void setProxy_port(int proxy_port) {
		HttpRequest.proxy_port = proxy_port;
	}

	public static void setProxy_userName(String proxy_userName) {
		HttpRequest.proxy_userName = proxy_userName;
	}

	public static void setProxy_userPwd(String proxy_userPwd) {
		HttpRequest.proxy_userPwd = proxy_userPwd;
	}

	public static void setHost(String host) {
		HttpRequest.host = host;
	}

	public static void setDomain(String domain) {
		HttpRequest.domain = domain;
	}

	/**
	 * 依据url地址获取抓取返回的内容	 * @param urlstr
	 * @return  
	 */
	public static String getCustomContent(String  urlstr){
		CookieManager cm = new CookieManager();
		cm.setCookiePolicy(java.net.CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cm);
		URL url;
		StringBuffer sb = new StringBuffer();
		URLConnection conn = null;
		try {
			url = new URL(urlstr);
			if (proxy_ip != null && proxy_ip.length() > 0 && !"无".equals(proxy_ip)) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_ip, proxy_port));
				conn = url.openConnection(proxy);
			}else{
				conn = url.openConnection();
			}
			BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

			String temp;
			while ((temp = bf.readLine()) != null) {
				sb.append(temp + "\n");
			}
			bf.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	// 获取网页编码
	public static String getCharset(InputStream inStream) {
		String strencoding = null;
		BufferedReader in = null;
		try {
			Matcher char_matcher = null;
			String line = null;
			in = new BufferedReader(new InputStreamReader(inStream));
			while ((line = in.readLine()) != null) {
				char_matcher = CHAR_PATTERN.matcher(line);
				if (char_matcher.find()) {
					String str = char_matcher.group(1);
					str = str.trim();
					strencoding = str;
					return strencoding.replace("\"", "").replace("\\", "").trim();
				} else {
					char_matcher = CHARSET_PATTERN.matcher(line);
					if (char_matcher.find()) {
						String str = char_matcher.group(1);
						str = str.trim();
						strencoding = str;
						return strencoding.replace("\"", "").replace("\\", "").trim();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (strencoding == null) {
			strencoding = "UTF-8";
		}
		return strencoding;
	}
	
	public static void setRequest_timeout(int requestTimeout) {
		request_timeout = requestTimeout;
	}
	public static void setRead_timeout(int readTimeout) {
		read_timeout = readTimeout;
	}
	
	public static Cookie[] getCookies() {
		return cookies;
	}

	public static void setCookies(Cookie[] cookies) {
		HttpRequest.cookies = cookies;
	}
	public static void main(String[] args) {
		//
		//https://a1.go2yd.com/Website/channel/search-channel?group_fromid=g181&appid=yidian&word=%E6%96%B0%E6%B0%91%E6%99%9A%E6%8A%A5
		String downloadUrl = "https://bangumi.bilibili.com/web_api/season/index_global?page=2&page_size=20&version=0&is_finish=0&start_year=0&tag_id=5&index_type=1&index_sort=0&quarter=0";
		Map<String, String> downMap = HttpRequest.getWebPageCode(downloadUrl, "GET","",false, "", null, "", "", "","");
		System.out.println(downMap.get("content"));
	}
}
