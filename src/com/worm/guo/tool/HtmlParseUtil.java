package com.worm.guo.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.worm.guo.po.Template;
import com.worm.guo.po.TemplateJson;



/**
 * html 解析 工具
 * 功能描述
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017年8月7日 上午7:52:21
 */
public class HtmlParseUtil {
	private static Log LOG = LogFactory.getLog(HtmlParseUtil.class);
	
	
	
	public static List<String> getParseResultList(String code, String regexs) {
		List<String> resultList = new ArrayList<String>();
		String rtn = "";
		try {
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getRegexs() != null && templateJson.getRegexs().size() > 0) {
					for (String regex : templateJson.getRegexs()) {
						Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(code);// 解析后的html内容
						while (m.find()) {
							rtn = m.group(1);
							resultList.add(rtn);
						}
						if (StringUtils.isNotBlank(rtn)) {
							return resultList;
						}
					}
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("正则匹配单个结果异常", e);
			}
		}
		return resultList;
	}



	/**
	 * 
	 * @param filed
	 * @param listhead
	 * @return
	 */
	public static String getParseResult(String code, String regexs) {
		// 正则匹配单个结果
		String rtn = "";
		try {
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getRegexs() != null && templateJson.getRegexs().size() > 0) {
					for (String regex : templateJson.getRegexs()) {
						Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(code);// 解析后的html内容
						while (m.find()) {
							rtn = m.group(1);
							break;
						}
						if (StringUtils.isNotBlank(rtn)) {
							return rtn;
						}
					}
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("正则匹配单个结果异常", e);
			}
		}
		return rtn;
	}

	/**
	 * html文本中的时间提取
	 * @param field
	 * @param template
	 * @return
	 */
	public static Date getParseDate(String code, Template template) {
		String regexs = template.getMainTime();
		Date date = null;
		try {
			if(template.getTemplateId() == 5 || template.getTemplateId() == 8|| template.getTemplateId() == 16 || template.getTemplateId() == 24 || template.getTemplateId() == 26 || template.getTemplateId() ==27){
				if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(regexs)) {
					TemplateJson templateJson = new TemplateJson().toObject(regexs);
					if (templateJson != null && templateJson.getRegexs() != null && templateJson.getRegexs().size() > 0) {
						for (int i = 0; i < templateJson.getRegexs().size(); i++) {
							String regex = templateJson.getRegexs().get(i);
							String format = templateJson.getFormats().get(i);
							Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
							Matcher m = p.matcher(code);// 解析后的html内容
							while (m.find()) {
								String time = m.group(1);
								if(template.getTemplateId() == 26){
									time = time.replace("最后更新：", "").trim();
								}else if(template.getTemplateId() == 16 || template.getTemplateId() == 27){
									time = time.replace("年", "-").replace("月", "-").replace("日", " ").replace("时", ":00:00");
								}else{
									time = time.replace("年", "-").replace("月", "-").replace("日", "");
								}
								
								SimpleDateFormat sdf = new SimpleDateFormat(format);
								date = sdf.parse(time);
								break;
							}
							if (date != null) {
								return date;
							}
						}
					}
				}
			}else{
				date = getParseResultDate(code,regexs);
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("正则匹配单个结果异常", e);
			}
		}
		return date;
	}
	
	// 正则匹配时间
	public static Date getParseResultDate(String code, String regexs) {
		Date date = null;
		try {
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getRegexs() != null && templateJson.getRegexs().size() > 0) {
					for (int i = 0; i < templateJson.getRegexs().size(); i++) {
						String regex = templateJson.getRegexs().get(i);
						String format = templateJson.getFormats().get(i);
						Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(code);// 解析后的html内容
						while (m.find()) {
							String time = m.group(1).trim();
							SimpleDateFormat sdf = new SimpleDateFormat(format);
							date = sdf.parse(time);
							break;
						}
						if (date != null) {
							return date;
						}
					}
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("正则匹配单个结果异常", e);
			}
		}
		return date;
	}
	
	// 去除基本的标签
	public static String replaceHtmlBasicTag(String content, String replacement) {
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
			rtn = rtn.replaceAll(" +", " ").replaceAll("\\t+", " ").replaceAll("　+", " ").replaceAll("(&nbsp;){1,}", " ").replace("|0", "");
			if(rtn.contains("您的浏览器不支持video标签。")){
				rtn = rtn.replaceAll("您的浏览器不支持video标签。", "");
			}
			if (rtn.contains("您不支持音频播放")) {
				rtn = rtn.replaceAll("您不支持音频播放", "");
			}
			if (rtn.contains("您不支持视频播放")) {
				rtn = rtn.replaceAll("您不支持视频播放", "");
			}
		}
		return rtn;
	}
}
