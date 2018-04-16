package com.worm.guo.apps.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.worm.guo.po.MobileChannelNew;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * 功能描述 采集接口类
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017年8月7日 上午6:59:54
 */
public interface BaseApp {
	
	/** 
	 * 获取list源码
	 * @param url
	 * @return
	 */
	public Object getListWebCode(String url);
	
	/**
	 * 获取正文的原始码
	 * @param location
	 * @return
	 */
	public Map<String,String> getMainWebResource(String location);
	
	/**
	 * 获取JSONList的源码
	 * @param url
	 * @return
	 */
	public String getJsonListCode(String url);
	
	/**
	 *  获取HtmlList的源码
	 * @param url
	 * @return
	 */
	public String getHtmlListCode(String url);
	
	/**
	 * jsonList转JsonArray
	 * @param obj
	 * @return
	 */
	public JSONArray getJsonList(Object obj);
	
	/**
	 * 单条新闻对象处理
	 * @param jobj
	 * @return
	 */
	public JSONObject getJsonListObj(JSONObject jobj);
	
	/**
	 * 获取jsonList中的标题
	 * @param obj
	 * @return
	 */
	public String getJsonListTitle(Object obj);
	/**
	 * json列表里的时间
	 * @param obj
	 * @return
	 */
	public Date getJsonListCreateDate(Object obj);
	
	/**
	 * json 列表中的摘要
	 * @param jobj
	 * @return
	 */
	public String getJsonListSummary(JSONObject jobj);
	
	/**
	 * json列表里的列表图片
	 * @param obj
	 * @return
	 */
	public String getJsonListImg(Object obj);
	/**
	 * json列表追番数
	 * @param obj
	 * @return
	 */
	public int getJsonListFavoriteCpunt(Object obj);
	
	/**
	 * json列表标签
	 * @param obj
	 * @return
	 */
	public String getJsonListTag(Object obj);
	
	
	/**
	 * 正文为json格式的采集链接
	 * @param obj
	 * @return
	 */
	public String getJsonMainLocation(Object obj);
	/**
	 * json正文的原文链接
	 * @param obj
	 * @return
	 */
	public String getJsonMainWebUrl(Object obj);
	
	
	
	/**
	 * json list的下一页
	 * @param pageNo
	 * @param source
	 * @param news
	 * @return
	 */
	public String nextJsonPageUrl(int pageNo, Object source, List<MobileChannelNew> news); 
	
	/**
	 * json Main正文 -> jsonObj
	 * @param webCode
	 * @return
	 */
	public JSONObject getMainJsonObject(String webCode);
	
	/**
	 * json main -> webUrl
	 * @param obj
	 * @return
	 */
	public String getMainJsonWebUrl(Object obj);

	/**
	 * jsonMain -> createDate
	 * @param obj
	 * @return
	 */
	public Date getMainJsonCreateDate(Object obj);

	/**
	 * 拼接urlHashLink
	 * @param location
	 * @return
	 */
	public String getUrlHashLink(String location);
	
	/**
	 * json正文摘要
	 * @param obj
	 * @return
	 */
	public String getMainJsonSummary(Object obj);
	
	/**
	 * json正文标签
	 * @param obj
	 * @return
	 */
	public String getMainJsonTag(Object obj);
	
	/**
	 * json正文番剧产地
	 * @param obj
	 * @return
	 */
	public String getMainJsonArea(Object obj);
	/**
	 * json正文声优信息
	 * @param obj
	 * @return
	 */
	public String getMainJsonAkira(Object obj);
	/**
	 * json正文追番数
	 * @param obj
	 * @return
	 */
	public int getMainJsonFavoriteCount(Object obj);
	
	/**
	 * json正文播放数
	 * @param obj
	 * @return
	 */
	public int getMainJsonPlayCount(Object obj);
	/**
	 * json正文弹幕数
	 * @param obj
	 * @return
	 */
	public int getMainJsonDanmakuCount(Object obj);
	/**
	 * html格式的列表
	 * @param obj
	 * @return
	 */
	public List<String> getHtmlList(Object obj);
	
	/**
	 * html格式的列表标题
	 * @param field
	 * @return
	 */
	public String getHtmlListTitle(String filed);
	
	/**
	 * html列表的时间
	 * @param field
	 * @return
	 */
	public Date getHtmlListCreateDate(String field); 
	
	/**
	 * html列表中的摘要
	 * @param field
	 * @return
	 */
	public String getHtmlListSummary(String field);

	/**
	 * html列表图片
	 * @param field
	 * @return
	 */
	public String getHtmlListImg(String field);

	/**
	 * html列表的正文的采集链接
	 * @param field
	 * @return
	 */
	public String getHtmlMainLocation(String field);


	/**
	 * html格式的正文主题
	 * @param webCode
	 * @return
	 */
	public String getMainHtmlStr(String webCode);


	/**
	 * html格式正文时间
	 * @param str
	 * @return
	 */
	public Date getMainHtmlCreateDate(String str);

	/**
	 * html格式正文摘要
	 * @param str
	 * @return
	 */
	public String getMainHtmlSummary(String str);

	/**
	 * html格式正文原文链接
	 * @param str
	 * @return
	 */
	public String getMainHtmlWebUrl(String str);


	/**
	 * html正文阅读数
	 * @return
	 */
	public int getMainHtmlRead(String str);
	
	
}

