package com.worm.guo.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.worm.guo.po.TemplateJson;
import com.worm.guo.po.TemplateJsonChild;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 解析类
 * 功能描述
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017年8月7日 上午7:43:42
 */
public class JsonParseUtil {

	private static Log LOG = LogFactory.getLog(JsonParseUtil.class);
	/***
	 * 计算列表数据
	 * @param code
	 * @param regexs
	 * @return
	 */
	public static JSONArray getJsonResultList(String code, String regexs) {
		try {
			if(StringUtils.isNotBlank(regexs)){
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if(templateJson != null && templateJson.getJsons() != null && templateJson.getJsons().size() > 0){
					JSONArray array = new JSONArray();
					List<TemplateJsonChild> jsons = templateJson.getJsons();

					TemplateJsonChild root = null;
					for(int i=0;i<jsons.size();i++){
						root = jsons.get(i);
						if(root.getType().equalsIgnoreCase("obj")){
							JSONObject jsonObj = JSONObject.fromObject(code);
							array.add(jsonObj);
						}else if(root.getType().equalsIgnoreCase("array")){
							array = JSONArray.fromObject(code);
						}else if(root.getType().contains("array:")){	//列表返回多个列表集合，但第一个包含所有，取第一个---待完善
							int index = Integer.parseInt((root.getType().split(":"))[1]);
							array = JSONArray.fromObject(code);
							JSONObject jsonObject = array.getJSONObject(index);
							root = root.getChild();
							if(root.getType().equalsIgnoreCase("array")){
								array = jsonObject.getJSONArray(root.getKey());
							}
						}
						if(array.size() > 0){
							while(root.getChild() != null){
								array = getJosnArray(array, root.getChild());
								root = root.getChild();
							}
						}
						if(array != null && array.size() > 0){
							break;
						}
					}
					return array;
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("JSON匹配多个结果异常", e);
			}
		}
		return null;
	}
	
	private static JSONArray getJosnArray(JSONArray array , TemplateJsonChild child){
		JSONArray temp = new JSONArray();
		try{
			if(array != null && array.size() > 0){
				for(int i=0;i<array.size();i++){
					JSONObject jsonObj = array.getJSONObject(i);
					if(child.getType().equalsIgnoreCase("obj")){
						if(child.getKey().equalsIgnoreCase("undefiend")){
							Iterator<String> it = jsonObj.keys();
							while (it.hasNext()) { // 遍历JSONObject
								String key = (String) it.next().toString();
								temp.add(jsonObj.getJSONObject(key));
							}
						}else{
							jsonObj = jsonObj.getJSONObject(child.getKey());
							temp.add(jsonObj);
						}
					}else if(child.getType().equalsIgnoreCase("array")){
						if(child.getKey().equalsIgnoreCase("undefiend")){
							Iterator<String> it = jsonObj.keys();
							while (it.hasNext()) { // 遍历JSONObject
								String key = (String) it.next().toString();
								temp.addAll(jsonObj.getJSONArray(key));
							}
						}else{
							if(jsonObj != null && child != null && jsonObj.has(child.getKey()) && StringUtils.isNotBlank(jsonObj.getString(child.getKey()))){
								try{
									temp.addAll(jsonObj.getJSONArray(child.getKey()));
								}catch(Exception e){
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return temp; 
	}

	/**
	 * json匹配单个结果
	 * @param obj
	 * @param listTitle
	 */
	public static String getJsonResult(JSONObject jsonObj, String regexs) {
		String rtn = "";
		//TODO 变更
		try {
			if (jsonObj != null && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getJsons() != null && templateJson.getJsons().size() > 0) {
					for(TemplateJsonChild root : templateJson.getJsons()){//模板循环
						if(root.getType().equalsIgnoreCase("string")){
							if(jsonObj.containsKey(root.getKey())){
								return jsonObj.getString(root.getKey());
							}
						}else if(root.getType().equalsIgnoreCase("obj")){
							jsonObj = jsonObj.getJSONObject(root.getKey());
							TemplateJsonChild tempChild = root.getChild();
							while(tempChild != null){
								if(tempChild.getType().equalsIgnoreCase("obj")){
									jsonObj = jsonObj.getJSONObject(tempChild.getKey());
									tempChild = tempChild.getChild();
									continue;
								}else if(tempChild.getType().equalsIgnoreCase("array")){
									break;							
								}else if(tempChild.getType().contains("array:")){							
									return getJsonResultArray(jsonObj, regexs, root);
								}else if(tempChild.getType().equalsIgnoreCase("string")){
									if(jsonObj.containsKey(tempChild.getKey())){
										return jsonObj.getString(tempChild.getKey());
									}else {
										break;
									}
								}
							}
						}else if(root.getType().equalsIgnoreCase("array")){
							if (jsonObj.containsKey(root.getKey())) {
								JSONArray array = jsonObj.getJSONArray(root.getKey());
								if(array != null && array.size() > 0){
									if(array != null && array.size() > 0){
										return array.getString(0); //腾讯
									}
								}
							}
						}else if(root.getType().contains("array:")){	//匹配结果array中的一个
							return getJsonResultArray(jsonObj, regexs, root);
						}
					}//模板循环
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("JSON匹配单个结果异常", e);
			}
		}
		return rtn;
	}
	
	/**
	 * 结果有多个array获取其中一个的方法
	 * json书写规则 array:0-9
	 * @param jsonObj
	 * @param regexs
	 * @return
	 */
	private static String getJsonResultArray(JSONObject jsonObj, String regexs, TemplateJsonChild root){
		if(!jsonObj.containsKey(root.getKey())){
			if(root.getType().contains("array:")){	//不包含对应的key 类型确实array表示json对象没有这个字段，跳出
				return "";
			}else{
				root = root.getChild();
			}		
		}
		if(jsonObj.containsKey(root.getKey())){
			int index = 0;
			try {
				index = Integer.parseInt((root.getType().split(":"))[1]);
			} catch (Exception e) {
				if(LOG.isErrorEnabled()){
					LOG.error("JSON转化异常" + e + "jsonKey:" +root.getKey() + "jsonObj:" +jsonObj);
				}
			}			
			JSONArray jsonArray = jsonObj.getJSONArray(root.getKey());
			if( jsonArray != null && jsonArray.size() > 0){
				if(jsonArray.size() < index){
					jsonObj = jsonArray.getJSONObject(0);
				}else{
					jsonObj = jsonArray.getJSONObject(index);
				}			
			}else{
				return "";
			}
			TemplateJsonChild tempChild = root.getChild();
			while (tempChild != null) {
				if(tempChild.getType().equalsIgnoreCase("obj")){
					jsonObj = jsonObj.getJSONObject(tempChild.getKey());
					tempChild = tempChild.getChild();
					continue;
				}else if(tempChild.getType().equalsIgnoreCase("string")){
					if(jsonObj.containsKey(tempChild.getKey())){
						return jsonObj.getString(tempChild.getKey());
					}else {
						break;
					}
				}								
			}
		}
		return "";
	}

	/***
	 * json时间解析
	 * @param jsonObj
	 * @param regexs
	 * @return
	 */
	public static Date getJsonResultDate(JSONObject jsonObj, String regexs) {
		Date date = null;
		try {
			if (jsonObj != null && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getJsons() != null && templateJson.getJsons().size() > 0) {
					for(TemplateJsonChild root : templateJson.getJsons()){
						if(jsonObj.containsKey(root.getKey())){
							String time = jsonObj.getString(root.getKey());
							if (root.getFormat() != null) {
								String format = root.getFormat();
								try{
									SimpleDateFormat sdf = new SimpleDateFormat(format);
									if (time.contains("+0800")) {
										sdf = new SimpleDateFormat(format, Locale.ENGLISH);
									}
									if(time.contains("T") && !(time.contains("Tue") || time.contains("Thu") || time.contains("Oct"))){
										time = time.replace("T", " ");//路透社
									}
									if(time.contains("+00:00")) {
										time = time.replaceAll("\\+00:00", "");
									}
									
									//华尔街日报:两个频道，其中"热门文章"包括am/pm :同一个app时间格式竟然不一样
									boolean isPM = false;
									if (time.contains("AM") || time.contains("PM")) {
										if (time.contains("PM")) {
											isPM = true;
										}
										sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										time = time.substring(0, time.lastIndexOf(" "));
									}else if(time.length()>19 && !time.contains(",")){
										time = time.substring(0, 19);
									}
									
									date = sdf.parse(time);
									if (isPM) {
										date.setHours(date.getHours() + 12);
									}
								}catch(Exception e){		
								}
							} else {
								if (time.contains("Date(") && time.contains("+")) {//西藏网
									time = time.substring(time.indexOf("(") + 1, time.indexOf("+"));
								}
								date = DateFunction.parseTimeStrToDate(time);
							}
							if (date != null) {
								return date;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("JSON匹配单个结果异常", e);
			}
		}
		return date;
	}

	/**
	 * 获取JSON正文>JSONObject对象
	 * @param code
	 * @param regexs
	 * @return
	 */
	public static JSONObject getJsonMainObj(String code, String regexs) {
		if(StringUtils.isNotBlank(code)) {
			JSONObject jsonObj = JSONObject.fromObject(code);
			try {			
				if (jsonObj != null && StringUtils.isNotBlank(regexs)) {
					TemplateJson templateJson = new TemplateJson().toObject(regexs);
					if(templateJson != null && templateJson.getJsons() != null && templateJson.getJsons().size() > 0){
						List<TemplateJsonChild> jsons = templateJson.getJsons();
						for(TemplateJsonChild root : jsons){
							 if(root.getKey() == null){
									continue;
							}else if(root.getKey().equalsIgnoreCase("")){
								continue;
							}else if(root.getKey().equalsIgnoreCase("undefiend")){
								Iterator<String> it = jsonObj.keys();
								while (it.hasNext()) { // 遍历JSONObject
									String key = (String) it.next().toString();
									if(key.equalsIgnoreCase("postid")){
										break;
									}
									JSONObject rtn = (JSONObject) jsonObj.get(key);
									if (rtn != null && rtn.size() > 0) {
										return rtn;
									}
								}
							}else{
								if(root.getType().equalsIgnoreCase("obj")){
									jsonObj = jsonObj.getJSONObject(root.getKey());
									TemplateJsonChild tempChild = root.getChild();
									while(tempChild != null){
										if(tempChild.getType().equalsIgnoreCase("obj")){
											jsonObj = jsonObj.getJSONObject(tempChild.getKey());
											tempChild = tempChild.getChild();
											continue;
										}else if(tempChild.getType().equalsIgnoreCase("array")){
											//TODO 
											continue;
										}
									}
								}else if(root.getType().equalsIgnoreCase("array")){
									
									JSONArray array = jsonObj.getJSONArray(root.getKey());
									if(array != null && array.size() > 0){
										if(array != null && array.size() > 0){
											return (JSONObject)array.getJSONObject(0);
										}
									}
								}
							}
						}
						
					}
				}
			} catch (Exception e) {
				if (LOG.isErrorEnabled()) {
					LOG.error("JSON匹配多个结果异常", e);
				}
			}
			return jsonObj;
		}else {
			if(LOG.isErrorEnabled()) {
				LOG.error("[媒体号采集]-[获取JSON格式正文异常]-[正文源码为空]");
			}
		}
		return null;
	}
	
	public static String getJsonResultUrl(JSONObject jsonObj, String regexs) {
		String url = "";
		String isUrl = "";
		try {
			if (jsonObj != null && StringUtils.isNotBlank(regexs)) {
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				if (templateJson != null && templateJson.getPrefix() != null) {
					url = templateJson.getPrefix();
					isUrl = url;
					if (templateJson.getParameters() != null && templateJson.getParameters().size() > 0) {
						for (int i = 0; i < templateJson.getParameters().size(); i++) {
							String[] parameters = templateJson.getParameters().get(i).split(":");
							if(jsonObj.containsKey(parameters[1])){
								url += (url.endsWith("?") ? "" : "&") + parameters[0] + "="
								+ jsonObj.getString(parameters[1]);
							}
						}
					}
					if (templateJson.getReplacements() != null && templateJson.getReplacements().size() > 0) {
						for (int i = 0; i < templateJson.getReplacements().size(); i++) {
							String[] replacements = templateJson.getReplacements().get(i).split(":");
							if(jsonObj.containsKey(replacements[1])){
								url = url.replace(replacements[0], jsonObj.getString(replacements[1]));
							}
						}
					}
				}else if(templateJson != null && templateJson.getJsons() != null){
					url = getJsonResult(jsonObj, regexs);
				}
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("JSON匹配单个结果异常", e);
			}
		}
		if(isUrl.equals(url)){
			return "";
		}
		return url;
	}
	/**
	 * json获取列表下一页链接
	 */
	public static String getJsonResultNextPage(String nowUrl, String regexs, int pageNo){
		String nextPage = "";
		try{
			if(StringUtils.isNotBlank(nowUrl) && StringUtils.isNotBlank(regexs)){
				TemplateJson templateJson = new TemplateJson().toObject(regexs);
				String preUrl = templateJson.getPrefix();
				int oldNum = templateJson.getStartPageNum();
				int newNum = templateJson.getStartPageNum() + (pageNo + 1)*templateJson.getPerPageNum();
				String oldStr = templateJson.getReplacePage();
				String newStr = templateJson.getReplacePage();
				if(oldStr.startsWith("-")){
					oldStr = oldNum + oldStr;
					newStr = newNum + newStr;
				}else{
					oldStr += oldNum;
					newStr += newNum;
				}
				if(!StringUtils.isBlank(preUrl)){
					nextPage = preUrl + newNum;
				}else{
					nextPage = nowUrl.replace(oldStr, newStr);
				}
			}
		}catch(Exception e){
			if (LOG.isErrorEnabled()) {
				LOG.error("JSON匹配单个结果异常", e);
			}
		}
		return nextPage;
	}
}
