package com.worm.guo.po;

import com.worm.guo.support.JsonSupport;

import net.sf.json.JSONObject;



/**
 * @Title:
 * @Desc:
 * @Company:Bluewit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2015-1-30下午9:04:40
 *
 */
public class TemplateJsonChild implements JsonSupport{

	private String key;
	private String type;
	private String format;
	private String url;
	private String desc;
	private TemplateJsonChild child;
	
	public String toJson() {
		JSONObject jo = JSONObject.fromObject(this);
		return jo.toString();
	}
	
	public TemplateJsonChild toObject(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);
		TemplateJsonChild object = (TemplateJsonChild)JSONObject.toBean(jsonObject, this.getClass());
		return object;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TemplateJsonChild getChild() {
		return child;
	}

	public void setChild(TemplateJsonChild child) {
		this.child = child;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
