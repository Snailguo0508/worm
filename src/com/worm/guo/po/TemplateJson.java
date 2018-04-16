package com.worm.guo.po;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.worm.guo.support.JsonSupport;

import net.sf.json.JSONObject;



/**
 * @Title:
 * @Desc:
 * @Company:Bluewit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2015-1-30下午9:03:40
 *
 */
public class TemplateJson implements JsonSupport{

	private List<String> regexs;//匹配的正则或者json标签
	private List<String> clears;//删除的正则或者json标签
	private List<String> replacements;//替换参数
	private List<String> formats;//时间格式
	private int type;//类型,1对象，0数组 or 列表头图类型 0：在列表中，1：在json里，2：其他URL
	private int level;//层次
	private String prefix;//内容页前缀
	private List<String> parameters;//内容页参数	private String replacePage;//下一页替换参数	private int startPageNum;//起始页	private int perPageNum;//每页数量
	private String videos;//视频
	private String pics;//图片
	private String pic;//
	private String url;//视频地址or列表头图地址
	private String alt;//图片简介
	private List<String> videosImg; //网站视频地址列表

	private String htmlListArea;//htmllist区域

	private List<TemplateJsonChild> jsons;

	
	public List<String> getRegexs() {
		return regexs;
	}
	public void setRegexs(List<String> regexs) {
		this.regexs = regexs;
	}
	public List<String> getClears() {
		return clears;
	}
	public void setClears(List<String> clears) {
		this.clears = clears;
	}
	
	public String toJson() {
		JSONObject jo = JSONObject.fromObject(this);
		return jo.toString();
	}
	
	public TemplateJson toObject(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String,Class> maps = new HashMap<String,Class>();
		maps.put("jsons",TemplateJsonChild.class);
		TemplateJson object = (TemplateJson)JSONObject.toBean(jsonObject, this.getClass(),maps);
		return object;
	}
	public List<String> getFormats() {
		return formats;
	}
	public void setFormats(List<String> formats) {
		this.formats = formats;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getVideos() {
		return videos;
	}
	public void setVideos(String videos) {
		this.videos = videos;
	}
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
	public List<String> getReplacements() {
		return replacements;
	}
	public void setReplacements(List<String> replacements) {
		this.replacements = replacements;
	}
	public String getReplacePage() {
		return replacePage;
	}
	public void setReplacePage(String replacePage) {
		this.replacePage = replacePage;
	}
	public int getStartPageNum() {
		return startPageNum;
	}
	public void setStartPageNum(int startPageNum) {
		this.startPageNum = startPageNum;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<TemplateJsonChild> getJsons() {
		return jsons;
	}
	public void setJsons(List<TemplateJsonChild> jsons) {
		this.jsons = jsons;
	}
	public String getHtmlListArea() {
		return htmlListArea;
	}
	public void setHtmlListArea(String htmlListArea) {
		this.htmlListArea = htmlListArea;
	}
	public List<String> getVideosImg() {
		return videosImg;
	}
	public void setVideosImg(List<String> videosImg) {
		this.videosImg = videosImg;
	}
}
