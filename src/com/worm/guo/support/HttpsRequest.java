package com.worm.guo.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;

import com.worm.guo.urlCode.UrlDecoding;

/**
 * @Title:
 * @Desc:
 * @Company:Bluewit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2015-9-11下午4:02:49
 *
 */
public class HttpsRequest {
	
	private final static String ESCAPE_REG = "(%[a-fA-F0-9]{2})";
	private final static String GBK_UNICODE_REG = "[\u4e00-\u9fa5]+";
	private final static String CHARSET_REGX = "<meta [^>](.*?)charset=(.*?)>";

	/**
	 * 忽视证书HostName
	 */
	private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslsession) {
			System.out.println("WARNING: Hostname is not matched for cert.");
			return true;
		}
	};

	/**
	 * Ignore Certification
	 */
	private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

		private X509Certificate[] certificates;

		public void checkClientTrusted(X509Certificate certificates[],
				String authType) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = certificates;
//				System.out.println("init at checkClientTrusted");
			}
		}

		public void checkServerTrusted(X509Certificate[] ax509certificate,
				String s) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = ax509certificate;
//				System.out.println("init at checkServerTrusted");
			}
			// for (int c = 0; c < certificates.length; c++) {
			// X509Certificate cert = certificates[c];
			// System.out.println(" Server certificate " + (c + 1) + ":");
			// System.out.println("  Subject DN: " + cert.getSubjectDN());
			// System.out.println("  Signature Algorithm: "
			// + cert.getSigAlgName());
			// System.out.println("  Valid from: " + cert.getNotBefore());
			// System.out.println("  Valid until: " + cert.getNotAfter());
			// System.out.println("  Issuer: " + cert.getIssuerDN());
			// }

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

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
		}
		return url;
	}
	public static String getHttpsContent(String urlString) {

		//设置代理
		HttpClientObj obj = HttpClientConfig.getHttpClientObj();
		if (obj != null) {
			String proxy_ip = obj.getProxy_ip();
			String proxy_port = obj.getProxy_port()+"";
			if (proxy_ip != null && proxy_ip.length() > 0 && !"无".equals(proxy_ip)) {
				// 设置代理
				System.getProperties().put("proxySet", "true");
				System.getProperties().put("proxyHost", proxy_ip);
				System.getProperties().put("proxyPort", proxy_port);
			}
		}
//		ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
		StringBuffer html = new StringBuffer();
		try {
			long a = System.currentTimeMillis();
			if (!isEscapeUrl(urlString)) {
				urlString = URIUtil.encodePathQuery(urlString);
			} else {
				urlString = escapeChineseChar(urlString);
			}
			URL url = new URL(urlString);
			/*
			 * use ignore host name verifier
			 */
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			if(url.getHost().contains("nytimes.com")) {
				long t = System.currentTimeMillis() / 1000;
				connection.addRequestProperty("Cookie", UrlDecoding.NYAuthor(t));
				connection.addRequestProperty("User-Agent", "okhttp/2.7.5 nyt-android/6.10.7");
				connection.addRequestProperty("NYT-App-Type", "NYT-Phoenix");
				connection.addRequestProperty("NYT-Device-Model", "LGE Nexus 5");
				connection.addRequestProperty("NYT-App-Version", "6.10.7");
				connection.addRequestProperty("NYT-Sprinkle", "embed");
				connection.addRequestProperty("NYT-Timestamp", t+"");
				connection.addRequestProperty("NYT-Local-Timezone", "+0800");
				connection.addRequestProperty("NYT-Local-Timezone", "en, zh");
				connection.addRequestProperty("NYT-OS-Version", "4.4.4");
				connection.addRequestProperty("NYT-Build-Type", "release");
				connection.addRequestProperty("Accept-Encoding", "gzip");
				connection.addRequestProperty("If-Modified-Since", "Tue, 16 May 2017 12:24:04 GMT");
			}
			// Prepare SSL Context
			TrustManager[] tm = { ignoreCertificationTrustManger };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			connection.setSSLSocketFactory(ssf);
			BufferedReader reader = null;
			if(url.getHost().contains("nytimes.com")) {
				GZIPInputStream gzip = new GZIPInputStream(connection.getInputStream());  
				reader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));  
			}else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			}
			String line = null;
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
			reader.close();
			connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		String repString = html.toString();
		return repString;
	}

	
	public String getHttpContent(String url) {
		 try {
			// 创建URL对象
			URL myURL = new URL("https://www.sun.com");
			// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
			// 取得该连接的输入流，以读取响应内容
			InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
			// 读取服务器的响应内容并显示
			int respInt = insr.read();
			while (respInt != -1) {
				System.out.print((char) respInt);
				respInt = insr.read();
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	
	/**
	 * https请求返回Map
	 * @Description: 
	 * @param url
	 * @return Map<String,String>   
	 * @throws
	 */
	public static Map<String,String> getHttpsContentMap(String url) {
		Map<String,String> downMap = new HashMap<String, String>();
		String output = new String(HttpsRequest.getHttpsContent(url));
		if(StringUtils.isNotBlank(output)) {
			downMap.put("content", output);
		}
		return downMap;
	}
	
	/**
	 * jdk1.6扩展TLS
	 * @param urlPath
	 * @param method
	 * @param data
	 * @param charSet
	 * @param header
	 * @param contentType
	 * @param accpect
	 * @return
	 */
	public static Map<String,String> getHttpsMapCode(String urlPath,String method, String data, String charSet, String[] header, String contentType, String accpect) {
        Map<String,String> result = null;
        URL url = null;
        HttpsURLConnection httpurlconnection = null;
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            url = new URL(urlPath);
            httpurlconnection = (HttpsURLConnection) url.openConnection();
            httpurlconnection.setSSLSocketFactory(new TLSSocketConnectionFactory());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);

            charSet = StringUtils.isBlank(charSet) ? "UTF-8":charSet;
            
            if (header != null) {
                for (int i = 0; i < header.length; i++) {
                    String[] content = header[i].split(":");
                    httpurlconnection.setRequestProperty(content[0], content[1]);
                }
            }
            if(method.trim().equals("POST") || method.trim().equals("post")) {
            	httpurlconnection.setRequestMethod("POST");
            }else {
            	httpurlconnection.setRequestMethod("GET");
            }
            httpurlconnection.setRequestProperty("Content-Type", contentType);
            if (null != accpect) {
                httpurlconnection.setRequestProperty("Accpect", accpect);
            }

            httpurlconnection.connect();
            out = new OutputStreamWriter(httpurlconnection.getOutputStream(), charSet); // utf-8编码
            out.append(data);
            out.flush();
            out.close();

            int code = httpurlconnection.getResponseCode();

            if (code == 200) {
                int length = (int) httpurlconnection.getContentLength();// 获取长度
                InputStream is = httpurlconnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                StringBuilder builder = new StringBuilder();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                String rc = builder.toString();
                if(StringUtils.isNotBlank(rc)) {
                	result = new HashMap<String, String>();
                	result.put("content", rc);
                }
            } else {
            	System.out.println("请求失败,url:"+urlPath+",code:"+code);
            }
        } catch (Exception e) {
        	System.err.println("获取https请求异常\n"+e);
        } finally {
            url = null;
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            	System.err.println("关闭https数据流异常...");
            }
        }
        return result;
    }
	
	public static void main(String[] args) {
		//设置代理上外网
		/*System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "192.168.1.119");
		System.getProperties().put("proxyPort", "8888");*/
		String urlString = "https://www.shine.cn/news/";
		Map<String,String> downMap = getHttpsMapCode(urlString, "GET", "", "", null, "", "");
		System.out.println(downMap.get("content"));
	}
}