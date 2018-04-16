package com.worm.guo.tool;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringFunction {

	private static final Log LOG = LogFactory.getLog(StringFunction.class);


	/**
	 * 取临时输入的高亮词	 * 
	 * @param strData
	 *            String
	 */
	public static String getHiliteWords(String data) {
		if (data == null) {
			return "";
		}
		String highLightKey = data.trim();
		// ( ) and & or | not ! ~ **
//		String regex = "\\(|\\)|( and )|( AND )|&|( or )|( OR )|(\\|)|( not )|!|~|\\*\\*|\\*|\\\\*";
//		highLightKey = Pattern.compile(regex).matcher(highLightKey).replaceAll(" ");
//		highLightKey = highLightKey.trim();
//
//		if (highLightKey.indexOf("\"^") > -1 && highLightKey.indexOf("\"") == 0) {// 样本训练来的
//			highLightKey = highLightKey.replaceAll("\"", "");
//			highLightKey = highLightKey + " ";
//			highLightKey = highLightKey.replaceAll("\\^", "<");
//			highLightKey = highLightKey.replaceAll(" ", ">");
//			Matcher tag = Pattern.compile("<(.*?)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(highLightKey);
//			if (tag.find()) {
//				highLightKey = tag.replaceAll(" ");
//			}
//		} else {
			highLightKey = highLightKey.replaceAll("\"", "");
			highLightKey = highLightKey.replaceAll("'", "");
			highLightKey = highLightKey.replaceAll("‘", "");
			highLightKey = highLightKey.replaceAll("’", "");
			highLightKey = highLightKey.replaceAll("\\.\\*", " ");
			highLightKey = highLightKey.replaceAll("[*]", " ");		
					
//		}
		return highLightKey;
	}

	/**
	 * 高亮
	 * 
	 * @param content
	 * @param highLightKey
	 * @param classId
	 *            1-18
	 * @return
	 */
	public static String highLight(String content, String highLightKey, int classId, boolean all) {
		if (StringUtils.isNotBlank(highLightKey)) {
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("\\+", "[+]");
			highLightKey = highLightKey.replace("|.|", "|");


			Matcher m = null;
			StringBuffer sb = new StringBuffer();
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			while (m.find()) {
				m.appendReplacement(sb, "<span class='hilite" + classId + "'>" + m.group() + "</span>");
			}
			m.appendTail(sb);
			content = sb.toString();

		}
		return content;
	}
	
	public static String highLightApp(String content, String highLightKey, int classId, boolean all) {
		if (StringUtils.isNotBlank(highLightKey)) {
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("\\+", "[+]");
			highLightKey = highLightKey.replace("|.|", "|");


			Matcher m = null;
			StringBuffer sb = new StringBuffer();
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			while (m.find()) {
				m.appendReplacement(sb, "<span style=\"color:red;\">" + m.group() + "</span>");
			}
			m.appendTail(sb);
			content = sb.toString();

		}
		return content;
	}
	
	public static String highLightContent(String content, String highLightKey, int classId, boolean all) {
		if (StringUtils.isNotBlank(highLightKey)) {
			Matcher m = null;
			m = Pattern.compile("and|or|not",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(highLightKey);
			if (m.find()) {
				highLightKey = m.replaceAll("|");
			}
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replace("(", "|").replace("（", "|");
			highLightKey = highLightKey.replace(")", "|").replace("）", "|");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("[　]+", "|");
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			String[] strs = highLightKey.split("&|\\||\\!|！|＆|｜");
			highLightKey = "";
			for(String str : strs){
				if(StringUtils.isNotBlank(str)){
					highLightKey += (StringUtils.isNotBlank(highLightKey) ? "|" : "") + str.replace(" ", "");
				}
			}
			
//			highLightKey = highLightKey.replaceAll("&", " ");
//			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
//			highLightKey = highLightKey.replaceAll("[\\\\]", "");
//			highLightKey = highLightKey.replaceAll("\\*", " ");
//			highLightKey = highLightKey.replaceAll(",", " ");
//			highLightKey = highLightKey.replaceAll("\"", "").trim();
//			highLightKey = highLightKey.replaceAll("\\s+", "|");
//			highLightKey = highLightKey.replaceAll("\\+", "[+]");
//			highLightKey = highLightKey.replaceAll("|.|", "|");
			
			boolean find = false;
			
			StringBuffer sb = new StringBuffer();
			if(!all){
				m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
				while (m.find()) {
					content = content.length() > 130 ? (content.indexOf(m.group()) < 80 ? content.substring(0,130) : content.substring(content.indexOf(m.group())-80,(content.indexOf(m.group())+80) > content.length() ? content.length() : content.indexOf(m.group())+80)) : content;
					break;
				}
			}
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			while (m.find()) {
				find = true;
				m.appendReplacement(sb, "<span class=\"highlight\">" + m.group() + "</span>");
			}
			if(!find && !all){
				return content.length() > 130 ? content.substring(0,130) : content;
			}
			m.appendTail(sb);
			content = sb.toString();
		}
		return content;
	}
	
	/**
	 * 获取移动客户端高亮摘要
	 */
	public static String getMobileDescription(String content, String highLightKey) {
		if (StringUtils.isNotBlank(highLightKey)) {
			Matcher m = null;
			m = Pattern.compile("and|or|not",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(highLightKey);
			if (m.find()) {
				highLightKey = m.replaceAll("|");
			}
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replace("(", "|").replace("（", "|");
			highLightKey = highLightKey.replace(")", "|").replace("）", "|");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("[　]+", "|");
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			String[] strs = highLightKey.split("&|\\||\\!|！|＆|｜");
			highLightKey = "";
			for(String str : strs){
				if(StringUtils.isNotBlank(str)){
					highLightKey += (StringUtils.isNotBlank(highLightKey) ? "|" : "") + str.replace(" ", "");
				}
			}
			
//			highLightKey = highLightKey.replaceAll("&", " ");
//			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
//			highLightKey = highLightKey.replaceAll("[\\\\]", "");
//			highLightKey = highLightKey.replaceAll("\\*", " ");
//			highLightKey = highLightKey.replaceAll(",", " ");
//			highLightKey = highLightKey.replaceAll("\"", "").trim();
//			highLightKey = highLightKey.replaceAll("\\s+", "|");
//			highLightKey = highLightKey.replaceAll("\\+", "[+]");
//			highLightKey = highLightKey.replaceAll("|.|", "|");
			
			boolean find = false;
			
			StringBuffer sb = new StringBuffer();
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			while (m.find()) {
				content = content.length() > 54 ? (content.indexOf(m.group()) < 30 ? content.substring(0,54) : content.substring(content.indexOf(m.group())-30,(content.indexOf(m.group())+30) > content.length() ? content.length() : content.indexOf(m.group())+30)) : content;
				break;
			}
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			if (m.find()) {
				find = true;
			}
			if(!find){
				return content.length() > 54 ? content.substring(0,54) : content;
			}
			m.appendTail(sb);
			content = sb.toString();
		}
		return content;
	}
	
	/**
	 * 对含有html标签的文本进行高亮处理
	 * @param content
	 * @param highLightKey
	 * @return
	 */
	public static String highLightHtml(String content, String highLightKey) {
		if (StringUtils.isNotBlank(highLightKey)) {
			/** 清洗高亮关键词 **/
			Matcher m = Pattern.compile("and|or|not",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(highLightKey);
			if (m.find()) {
				highLightKey = m.replaceAll("|");
			}
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replace("(", "|").replace("（", "|");
			highLightKey = highLightKey.replace(")", "|").replace("）", "|");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("[　]+", "|");
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			String[] strs = highLightKey.split("&|\\||\\!|！|＆|｜");
			highLightKey = "";
			for(String str : strs){
				if(StringUtils.isNotBlank(str)){
					highLightKey += (StringUtils.isNotBlank(highLightKey) ? "|" : "") + str.replace(" ", "");
				}
			}
			
			List<String> tagList = new ArrayList<String>();
			m = Pattern.compile("<(.*?)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			int start = 0;
			int end = 0;
			while (m.find()) {
				end = m.start();
				if (end > start) {
					String text = content.substring(start, end);
					tagList.add(StringFunction.highLightText(text, highLightKey));
				}else {
					end = 0;
				}
				tagList.add(m.group());
				start = m.end();
			}
			if (start < content.length()) {
				String text = content.substring(start);
				tagList.add(StringFunction.highLightText(text, highLightKey));
			}
			
			content = "";
			for (String text : tagList) {
				content += text;
			}			
		}
		
		return content;
	}
	
	public static String highLightText(String text, String highLightKey) {
		Matcher m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "<span class=\"highlight\">" + m.group() + "</span>");
		}
		m.appendTail(sb);
		text = sb.toString();
		return text;
	}
	
	/**
	 * 移动客户端使用，可与前面方法合并
	 * 对含有html标签的文本进行高亮处理
	 * @param content
	 * @param highLightKey
	 * @return
	 */
	public static String highLightAppHtml(String content, String highLightKey) {
		if (StringUtils.isNotBlank(highLightKey)) {
			Matcher m = null;
			m = Pattern.compile("and|or|not",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(highLightKey);
			if (m.find()) {
				highLightKey = m.replaceAll("|");
			}
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replace("(", "|").replace("（", "|");
			highLightKey = highLightKey.replace(")", "|").replace("）", "|");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("[　]+", "|");
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			String[] strs = highLightKey.split("&|\\||\\!|！|＆|｜");
			highLightKey = "";
			for(String str : strs){
				if(StringUtils.isNotBlank(str)){
					highLightKey += (StringUtils.isNotBlank(highLightKey) ? "|" : "") + str.replace(" ", "");
				}
			}
			
//			highLightKey = highLightKey.replaceAll("&", " ");
//			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
//			highLightKey = highLightKey.replaceAll("[\\\\]", "");
//			highLightKey = highLightKey.replaceAll("\\*", " ");
//			highLightKey = highLightKey.replaceAll(",", " ");
//			highLightKey = highLightKey.replaceAll("\"", "").trim();
//			highLightKey = highLightKey.replaceAll("\\s+", "|");
//			highLightKey = highLightKey.replaceAll("\\+", "[+]");
//			highLightKey = highLightKey.replaceAll("|.|", "|");
			
			List<String> tagList = new ArrayList<String>();
			m = Pattern.compile("<(.*?)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			int start = 0;
			int end = 0;
			while (m.find()) {
				end = m.start();
				if (end > start) {
					String text = content.substring(start, end);
					tagList.add(StringFunction.highLightAppText(text, highLightKey));
				}else {
					end = 0;
				}
				tagList.add(m.group());
				start = m.end();
			}
			if (start < content.length()) {
				String text = content.substring(start);
				tagList.add(StringFunction.highLightAppText(text, highLightKey));
			}
			
			content = "";
			for (String text : tagList) {
				content += text;
			}	
			
		}
		return content;
	}
	
	public static String highLightAppText(String text, String highLightKey) {
		Matcher m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "<span style=\"color:red;\">" + m.group() + "</span>");
		}
		m.appendTail(sb);
		text = sb.toString();
		return text;
	}
	
	public static String highLightAppContent(String content, String highLightKey, int classId, boolean all) {
		if (StringUtils.isNotBlank(highLightKey)) {
			Matcher m = null;
			m = Pattern.compile("and|or|not",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(highLightKey);
			if (m.find()) {
				highLightKey = m.replaceAll("|");
			}
			// * ? + \ |
//			if (highLightKey.contains("*")) {
//				highLightKey = getHiliteWords(highLightKey);
//			}
			highLightKey = highLightKey.replaceAll(",", " ");
			highLightKey = highLightKey.replaceAll("\\*", " ");
			highLightKey = highLightKey.replaceAll("[\\\\]", "");
			highLightKey = highLightKey.replace("(", "|").replace("（", "|");
			highLightKey = highLightKey.replace(")", "|").replace("）", "|");
			highLightKey = highLightKey.replaceAll("\\s+", "|");
			highLightKey = highLightKey.replaceAll("[　]+", "|");
			highLightKey = highLightKey.replaceAll("\"", "").trim();
			String[] strs = highLightKey.split("&|\\||\\!|！|＆|｜");
			highLightKey = "";
			for(String str : strs){
				if(StringUtils.isNotBlank(str)){
					highLightKey += (StringUtils.isNotBlank(highLightKey) ? "|" : "") + str.replace(" ", "");
				}
			}
			
//			highLightKey = highLightKey.replaceAll("&", " ");
//			highLightKey = highLightKey.replaceAll("\\?", "(.?)");
//			highLightKey = highLightKey.replaceAll("[\\\\]", "");
//			highLightKey = highLightKey.replaceAll("\\*", " ");
//			highLightKey = highLightKey.replaceAll(",", " ");
//			highLightKey = highLightKey.replaceAll("\"", "").trim();
//			highLightKey = highLightKey.replaceAll("\\s+", "|");
//			highLightKey = highLightKey.replaceAll("\\+", "[+]");
//			highLightKey = highLightKey.replaceAll("|.|", "|");
			
			boolean find = false;
			
			StringBuffer sb = new StringBuffer();
			if(!all){
				m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
				while (m.find()) {
					content = content.length() > 100 ? (content.indexOf(m.group()) < 50 ? content.substring(0,100) : content.substring(content.indexOf(m.group())-50,(content.indexOf(m.group())+50) > content.length() ? content.length() : content.indexOf(m.group())+50)) : content;
					break;
				}
			}
			m = Pattern.compile(highLightKey, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
			while (m.find()) {
				find = true;
				m.appendReplacement(sb, "<span style=\"color:red;\">" + m.group() + "</span>");
			}
			if(!find && !all){
				return content.length() > 100 ? content.substring(0,100) : content;
			}
			m.appendTail(sb);
			content = sb.toString();
		}
		return content;
	}

	/**
	 * 返回字符串s的MD5码

	 * 
	 * @param s
	 *            String
	 * @return BigInteger
	 */
	public static BigInteger getMD5(String s) {
		if (s == null)
			return null;
		try {
			byte[] bTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(bTemp);
			byte[] md = mdTemp.digest();
			return new BigInteger(1, md);
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 为URL添加80端口
	 */
	public static String changeUrlValueUtil(String url){
		
	    String url80=url;
	    try {
	    	if (StringUtils.isNotBlank(url)) {
	    		URL locationUrl = new URL(url);
				if (url.contains("http://") && locationUrl.getPort() == -1) {
					// 不包含80端口，执行以下代码
				    int pos = url.indexOf("/", 7);
				    if (pos != -1 && !url.substring(0, pos).endsWith(":80")) {// 存在'/'，且'/'之前不以':80'结束
					url80 = url.substring(0, pos) + ":80" + url.substring(pos);
				    }
				}
			}
			
		} catch (MalformedURLException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("转化url异常..." + url);
				LOG.error(e.getMessage(), e);
			}
			
		}
	    return url80;
	}

	/**
	 * 该功能主要是为了去掉内容中的高亮样式
	 * <p>
	 * 将内容中的jsp页面标签等替换为 '目标字符窜';
	 * 
	 * @param content
	 *            含有页面标签的内容

	 * @param replacement
	 *            要替换成的 '目标字符窜'
	 * @return
	 */
	public static String replaceHtmlTag(String content, String replacement) {
		String rtn = content;
		if (StringUtils.isNotBlank(rtn) && replacement != null) {
			Matcher htmlTag = Pattern.compile("<script [^>]*>(.*?)</script>",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(rtn);
			if (htmlTag.find()) {
				rtn = htmlTag.replaceAll(replacement);
			}
			htmlTag = Pattern.compile("<style [^>]*>(.*?)</style>",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(rtn);
			if (htmlTag.find()) {
				rtn = htmlTag.replaceAll(replacement);
			}
			htmlTag = Pattern.compile("<link [^>]*>(.*?)</link>",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(rtn);
			if (htmlTag.find()) {
				rtn = htmlTag.replaceAll(replacement);
			}
			htmlTag = Pattern.compile("<([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(rtn);
			if (htmlTag.find()) {
				rtn = htmlTag.replaceAll(replacement);
			}
		}
		return rtn;
	}


	/** 
     *  MurMurHash算法，是非加密HASH算法，性能很高， 
     *  比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免） 
     *  等HASH算法要快很多，而且据说这个算法的碰撞率很低. 
     */  
	public static Long getMurMurHash(String key){
		byte[] bytes = null;
		try {
			bytes = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}
	
	public static void main(String[] args){
//		String content = " 7月5日，杭州公交发生一起惨痛的纵火事件，在16位重危病人中，14位实施了气管切开手术，其中8位的烧伤面积超过60％，部分重症患者仍然面临时时存在的死亡威胁。这已经是今年第三起造成严重伤亡的公交纵火事件。车上的监控录像记录了纵火的全程，从有人开始察觉纵火者的行动，到香蕉水被打火机点燃，大约有5秒的时间——但这5秒内，乘客们只是在躲避。换作是你，这5秒，会采取行动阻止纵火吗？“又有公交车被纵火了？”这是许多人在听说本次杭州公交纵火的第一反应。这起事件，已经是继2月27日贵阳公交纵火、5月12日宜宾公交纵火后，今年第三起严重的公交纵火事件。而更恶劣的2009年成都公交纵火、2013年厦门公交纵火，还未从人们脑海里远去。除此之外，近年包括北上广深等一线城市在内的许多地方，都发生过纵火事件。公交纵火，是近年公共安全事件中一个突出的现象。即便意识到“制止纵火是求生的唯一办法”，如果没有经过演习训练，“挺身而出”也有可能停留在纸上谈兵。只有不断反复强化“应该制止纵火”的认知，以及进行模拟演练，才更有可能把认知转化到行动上。而这方面正是我国应急管理的弱项，民政部救灾司的官员访问美国后，总结的一个经验就是，中国社会危机警觉性较差，公民缺乏自救、救护的防灾意识和能力，而这是需要政府进行大力投入的。美国为公共安全应急管理专门成立了一个联邦紧急事务管理机构（FEMA），就致力于提高公众的防灾意识和采取自救措施的能力。在公民自救方面，做得最出色的国家是以色列。由于整个国家都长期处于反恐前线，以色列民众反恐意识很强，他们非常留意可疑的包裹、举止可疑的人以及可能危及公众安全的行为，并会毫不犹豫地向警方报告。据统计，以色列超过80％的未遂恐怖袭击是被普通民众制止的，每年以色列警方根据民众报告检查的可疑包裹多达数千个。民众的警觉和勇为，有办法阻止一起起潜在的重大恶性事件。好社会呼唤勇敢、有判断力、经常锻炼的公民目前的中国，正处在社会转型的攻坚阶段，各种不稳定因子可能处在高发期，整个社会的风气也变得较为乖戾。“扶老奶奶”成为需要小心翼翼才能做的事，“见义勇为”则往往被评价为“超出了行为能力的范围”，以至于许多人做好事也不敢做、制止坏人也不敢做，等到真出事情的时候，普通人根本无用武之地，乃至于连自救的能力都极其缺乏。这无疑不是一个好现象。一个让人感到安全、舒心的社会，即便不必做到“路不拾遗，门不闭户”的地步，至少也该让人感到，不管出了什么样的事故，都有相对可靠安全的人能够进行处理。人的素质如果达不到，那就尽量提高，一个好社会，需要勇敢、有判断力、经常锻炼的公民，这样的人可以成为社会的支柱。而如果没有这样的人呢？以公交纵火这件事来说，人们会越来越对公共交通失去信心，部分人转而使用私家车——而众所周知，私家车的平均出事频率和死亡人数，是远远高于公共交通的。如果真形成这样的局面，那就是最可悲的结果。结果";
//		//String key = "浙江|杭州|宁波|温州|绍兴|湖州|嘉兴|金华|衢州|舟山|台州|丽水|西湖|钱塘江|会稽山|雁荡山|普陀山|雪窦山|天目山|京杭运河|西湖|西塘|乌镇|富春江|千岛湖|雁荡山|西溪";
//		//System.out.println(highLightContent(content, key, 0, false));
////		System.out.println(content.lastIndexOf("。|，"));
//		
//		Matcher m = null;
//		int pos = 0;
//		m = Pattern.compile("。|，",Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(content);
//		if (m.find()) {
//			pos = m.end();
//		}
		System.out.println(getMurMurHash("http://bbs.lh168.net/forum.php?mod=viewthread&tid=12211826&extra=page%3D1%26filter%3Dauthor%26orderby%3Ddateline"));
	}
}
