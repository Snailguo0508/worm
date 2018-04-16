package com.worm.guo.apps.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.worm.guo.po.AppChannel;
import com.worm.guo.po.MobileChannelNew;
import com.worm.guo.po.Template;
import com.worm.guo.po.WebSite;
import com.worm.guo.support.HttpRequest;
import com.worm.guo.tool.HtmlParseUtil;
import com.worm.guo.tool.JsonParseUtil;
import com.worm.guo.tool.StringFunction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 功能描述 采集基类实现
 * 
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017年8月7日 上午7:08:30
 */
public class BaseAppImpl implements BaseApp {

	private static Log LOG = LogFactory.getLog(BaseAppImpl.class); 
	private static final String THREEDAYNEWCHANNELNEW = "threeday_new_channelnew";
	private Template template;
	private AppChannel appChannel;
	private WebSite site;
	private int listType = 1;
	
	@Override
	public Object getListWebCode(String listUrl) {
		listType = template.getListType();
		
		if(listType == 1) {
			String source = this.getJsonListCode(listUrl);
			JSONArray array = getJsonList(source);
			return array;
		}else if(listType == 2){
			String source = this.getHtmlListCode(listUrl);
			List<String> htmlList = getHtmlList(source);
			return htmlList;
		}else if(listType == 3) {
			
		}
		return null;
	}
	
	@Override
	public String getJsonListCode(String listUrl) {
		String source = null;
		Map<String, String> retMap = HttpRequest.getWebPageCode(listUrl, "get", null, false, null, null, null, null, null, null);
		if(retMap != null && StringUtils.isNotBlank(retMap.get("content"))){
			source = retMap.get("content");
		}
		return source;
	}

	@Override
	public String getHtmlListCode(String listUrl) {
		String source = null;
		Map<String, String> retMap = HttpRequest.getWebPageCode(listUrl, "get", null, false, null, null, null, null, null, null);
		if(retMap != null && StringUtils.isNotBlank(retMap.get("content"))){
			source = retMap.get("content");
		}
		return source;
	}

	@Override
	public JSONArray getJsonList(Object obj) {
		JSONArray array = JsonParseUtil.getJsonResultList((String) obj, template.getListFileds());
		return array;
	}

	@Override
	public List<String> getHtmlList(Object obj) {
		List<String> list = HtmlParseUtil.getParseResultList((String) obj,template.getListFileds());
		return list;
	}
	/**
	 * 获取json翻页
	 * @param pageNo
	 * @param source
	 * @param news
	 * @return
	 */
	@Override
	public String nextJsonPageUrl(int pageNo, Object source, List<MobileChannelNew> news) {
		String nextPage = JsonParseUtil.getJsonResultNextPage(this.appChannel.getMobileChannelUrl(), this.template.getListNextPage(), pageNo);
		return nextPage;
	}

	/**
	 * json -list 解析
	 * @param source
	 */
	private List<MobileChannelNew> getJsonListParse(Object source) {
		List<MobileChannelNew> channelNews = null;
		JSONArray field = (JSONArray) source;
		if(field != null && field.size()>0) {
			channelNews = new ArrayList<MobileChannelNew>();
			for (int i = 0; i < field.size(); i++) {
				try {
					JSONObject jobj = field.getJSONObject(i);
					jobj = this.getJsonListObj(jobj);
					MobileChannelNew channelNew =  new MobileChannelNew();
					channelNew.setTitle(this.getJsonListTitle(jobj));
					channelNew.setCreateDate(this.getJsonListCreateDate(jobj));
					channelNew.setPicUrl(this.getJsonListImg(jobj));
					channelNew.setLocation(this.getJsonMainLocation(jobj));
					channelNew.setUrlHash(this.calculateUrlHash(channelNew.getLocation()));
					channelNew.setDescription(this.getJsonListSummary(jobj));
					channelNew.setWebUrl(this.getJsonMainWebUrl(jobj));
					channelNews.add(channelNew);
				} catch (Exception e) {
					if(LOG.isErrorEnabled()) {
						LOG.error("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[解析JSON列表异常...]");
					}
				}
			}
		}
		return channelNews;
	}

	/**
	 * Json 列表中的 summary
	 * @param jobj
	 * @return
	 */
	@Override
	public String getJsonListSummary(JSONObject jobj) {
		return JsonParseUtil.getJsonResult(jobj, template.getListDescription());
	}

	/**
	 *location 计算 urlHash 用于消重
	 * 特殊情况中，由于location存在变量，时间等造成文章重复
	 * 重写此方法中的 getUrlHashLink
	 * @param location
	 * @return
	 */
	private long calculateUrlHash(String location) {
		String loc = this.getUrlHashLink(location);
		if(StringUtils.isNotBlank(loc)) {
			return StringFunction.getMurMurHash(StringFunction.changeUrlValueUtil(loc));
		}
		return 0;
	}

	/**
	 * 获取能用来消重的链接
	 * @param location
	 * @return
	 */
	@Override
	public String getUrlHashLink(String location) {
		return location;
	}



	/**
	 * 解析网页列表
	 * @param source
	 */
	private List<MobileChannelNew> getHtmlListParse(Object source) {
		List<MobileChannelNew> channelNews = null;
		List<String> fields = (List<String>) source;
		if(fields != null && fields.size()>0) {
			channelNews = new ArrayList<MobileChannelNew>();
			for (String field : fields) {
				MobileChannelNew channelNew =  new MobileChannelNew();
				channelNew.setTitle(this.getHtmlListTitle(field));
				channelNew.setCreateDate(this.getHtmlListCreateDate(field));
				
				channelNew.setPicUrl(this.getHtmlListImg(field));
				channelNew.setLocation(this.getHtmlMainLocation(field));
				channelNew.setUrlHash(this.calculateUrlHash(channelNew.getLocation()));
				channelNew.setDescription(this.getHtmlListSummary(field));
				channelNews.add(channelNew);
			}
		}
		return channelNews;
	}
	
	/**
	 * JSONList 单条新闻处理
	 * @param jobj
	 * @return
	 */
	@Override
	public JSONObject getJsonListObj(JSONObject jobj) {
		return jobj;
	}
	
	@Override
	public String getJsonListTitle(Object obj) {
		String title = JsonParseUtil.getJsonResult((JSONObject)obj,template.getListTitle());
		return title;
	}

	@Override
	public Date getJsonListCreateDate(Object obj) {
		Date createDate = JsonParseUtil.getJsonResultDate((JSONObject)obj, template.getListTime());
		return createDate;
	}


	@Override
	public String getJsonListImg(Object obj) {
		String imgSrc = JsonParseUtil.getJsonResult((JSONObject)obj,template.getListImgUrl());
		return imgSrc;
	}
	
	@Override
	public String getJsonMainLocation(Object obj) {
		String location = JsonParseUtil.getJsonResultUrl((JSONObject)obj,template.getListUrl());
		return location;
	}


	@Override
	public String getJsonMainWebUrl(Object obj) {
		String MainWebUrl = JsonParseUtil.getJsonResult((JSONObject)obj,template.getMainWebUrl());
		return MainWebUrl;
	}

	
	/**
	 * 
	 * @param f
	 * @return
	 */
	@Override
	public String getHtmlListTitle(String filed) {
		String title = HtmlParseUtil.getParseResult(filed, template.getListTitle());
		return title;
	}

	/**
	 * html列表中的摘要
	 * @param field
	 * @return
	 */
	public String getHtmlListSummary(String field) {
		String summary = HtmlParseUtil.getParseResult(field, template.getListDescription());
		return summary;
	}

	
	/**
	 * html列表的正文的采集链接
	 * @param field
	 * @return
	 */
	public String getHtmlMainLocation(String field) {
		String location = HtmlParseUtil.getParseResult(field, template.getListUrl());
		return location;
	}

	/**
	 * html列表图片
	 * @param field
	 * @return
	 */
	public String getHtmlListImg(String field) {
		String pic = HtmlParseUtil.getParseResult(field, template.getListImgUrl());
		return pic;
	}

	


	/**
	 * html列表的时间
	 * @param field
	 * @return
	 */
	public Date getHtmlListCreateDate(String field) {
		Date createDate = HtmlParseUtil.getParseDate(field, template);
		return createDate;
	}
	
	
	
	/**************************************************************
	   ******** 采集Main ***********
	 **********************/
	
	public List<MobileChannelNew> getChannelNews() {
		/** 采集列表 **/
		List<MobileChannelNew> listChannelNews = this.getListChannelNews();
		/** 列表消重 **/
		List<MobileChannelNew> repateChannelNews = this.removeRepateChannelNews(listChannelNews);
		/** 采集正文 **/
		List<MobileChannelNew> channelNews = this.getMainChannelNews(repateChannelNews);
		
		/** 评论采集 **/
		//TODO
		
		return channelNews;
	}

	/************************
	 * 列表采集
	 * @return
	 */
	public List<MobileChannelNew> getListChannelNews() {
		List<MobileChannelNew> channelNews = new ArrayList<MobileChannelNew>();
		List<MobileChannelNew> mobileChannelNews = new ArrayList<MobileChannelNew>();
		String url = this.appChannel.getMobileChannelUrl();
		int pageCount = this.appChannel.getPageCount();
		
		if(StringUtils.isNotBlank(url)) {
			for(int pageNo =0; pageNo < pageCount; pageNo++) {
				Object source = this.getListWebCode(url);
				/** 开始解析列表页 **/
				if(listType == 1) {
					channelNews = this.getJsonListParse(source);
				}else if(listType == 2) {
					channelNews = this.getHtmlListParse(source);
				}else {
					//TODO 
				}
				
				if(channelNews != null && channelNews.size()>0) {
					mobileChannelNews.addAll(channelNews);
				}
				
				url = this.nextJsonPageUrl(pageNo,source,channelNews);
				if (StringUtils.isBlank(url)) {
					break;
				}
			}
		}
		return mobileChannelNews;
	}
	
	/********************************
	 * 正文采集
	 * @param channelNews
	 */
	private List<MobileChannelNew> getMainChannelNews(List<MobileChannelNew> channelNews) {
		List<MobileChannelNew> mainNewsList = new ArrayList<MobileChannelNew>();
		if(channelNews != null && channelNews.size()>0 && template.getIsMainTextInList() == 0) {
			for (MobileChannelNew mobileChannelNew : channelNews) {
				//获取正文地址源码
				if(StringUtils.isNotBlank(mobileChannelNew.getLocation())) {
					String webCode = this.getMainWebCode(mobileChannelNew.getLocation());
					if(template.getMainType() == 1) { //json正文页
						this.getMainJsonNews(webCode,mobileChannelNew);
					}else if(template.getMainType() ==2) { //html正文页
						this.getMainHtmlNews(webCode,mobileChannelNew);
					}else {
						//TODO other
					}
				}
			}
			/** 正文去空 设置新闻站点属性 **/
			if(channelNews != null && channelNews.size()>0) {
				for (MobileChannelNew channelNew : channelNews) {
					if(StringUtils.isNotBlank(channelNew.getContent())){
						channelNew.setMobileNewSiteId(template.getTemplateId());
						channelNew.setNewChannelId(appChannel.getNewChannelId());
						channelNew.setMobileChannelNews(channelNews);
						channelNew.setSiteName(site.getSiteName());
						channelNew.setChannelName(appChannel.getMobileChannelName());
						mainNewsList.add(channelNew);
					}else{
						if(LOG.isErrorEnabled()) {
							LOG.info("[媒体号采集]-["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-["+channelNew.getTitle()+"]-[正文为空]"+"采集链接："+channelNew.getLocation());
						}
					}
				}
			}
			
			if(LOG.isInfoEnabled()) {
				LOG.info("[媒体号采集]-["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[去除空正文信息完成]-[剩余"+mainNewsList.size()+"]");
			}
		}
		return mainNewsList;
	}

	/**
	 * 获取正文的地址
	 * @param location
	 * @return
	 */
	private String getMainWebCode(String location) {
		String code = null;
		Map<String,String> webCode = this.getMainWebResource(location);
		if(webCode != null && StringUtils.isNotBlank(webCode.get("content"))) {
			code= webCode.get("content");
		}
		return code;
	}
	
	/**
	 * 获取正文链接原始码
	 * @param location
	 */
	@Override
	public Map<String,String> getMainWebResource(String location) {
		return HttpRequest.getWebPageCode(location, "get", null, false, "", null, null, null, null, null);
	}

	/**
	 * 获取json格式的正文
	 * @param webCode
	 * @param mobileChannelNew
	 * @return
	 */
	private void getMainJsonNews(String webCode, MobileChannelNew mobileChannelNew) {
		//解析json格式正文
		try {
			Object obj = this.getMainJsonObject(webCode);
			//1 摘要
			if(mobileChannelNew.getDescription() == null){
				String description = this.getMainJsonSummary(obj);
				mobileChannelNew.setDescription(description);
			}
			//3 时间
			if(mobileChannelNew.getCreateDate() == null) {
				Date createDate = this.getMainJsonCreateDate(obj);
				mobileChannelNew.setCreateDate(createDate);
			}
			//追番数
			if(mobileChannelNew.getFavorite() == 0){
				int favorite = this.getMainJsonFavoriteCount(obj);
				mobileChannelNew.setFavorite(favorite);
			}
			//声优
			if(mobileChannelNew.getAkira() == null){
				String akira = this.getMainJsonAkira(obj);
				mobileChannelNew.setAkira(akira);
			}
			//番剧产地
			if(mobileChannelNew.getArea() == null){
				String area = this.getMainJsonArea(obj);
				mobileChannelNew.setArea(area);
			}
			//播放数
			if(mobileChannelNew.getPlayCount() == 0){
				int playCount = this.getMainJsonPlayCount(obj);
				mobileChannelNew.setPlayCount(playCount);
			}
			//弹幕数
			if(mobileChannelNew.getDanmakuCount() == 0){
				int danmakuCount = this.getMainJsonDanmakuCount(obj);
				mobileChannelNew.setDanmakuCount(danmakuCount);
			}
			//9 摘要
			if(StringUtils.isBlank(mobileChannelNew.getDescription())) {
				String summary = this.getMainJsonSummary(obj);
				mobileChannelNew.setDescription(summary);
			}
		} catch (Exception e) {
			if(LOG.isErrorEnabled()) {
				LOG.info("["+site.getSiteName()+"]-["+mobileChannelNew.getTitle()+"]-[解析JSON格式正文异常...]");
			}
		}
		
	}

	/**
	 * Json格式的正文摘要
	 * @param obj
	 * @return
	 */
	@Override
	public String getMainJsonSummary(Object obj) {
		return JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainDescription());
	}

	/**
	 * Html格式的正文解析
	 * @param webCode
	 * @param mobileChannelNew
	 */
	private void getMainHtmlNews(String webCode, MobileChannelNew mobileChannelNew) {

		//解析html格式正文
		try {
			String str = this.getMainHtmlStr(webCode);
			if(StringUtils.isBlank(str)) { 
				if(LOG.isInfoEnabled()) {
					LOG.info("["+site.getSiteName()+"]-["+mobileChannelNew.getTitle()+"]-[解析HTML格式正文异常,检查MainField...]"+"[采集链接]："+mobileChannelNew.getLocation());
				}
				return;
			}
			
			
			
			//3 时间
			if(mobileChannelNew.getCreateDate() == null) {
				Date createDate = this.getMainHtmlCreateDate(str);
				mobileChannelNew.setCreateDate(createDate);
			}
			
			//7 阅读量
			mobileChannelNew.setVisitCount(this.getMainHtmlRead(str));
			
			//8 阅读原文
			if(StringUtils.isBlank(mobileChannelNew.getWebUrl())) {
				String webUrl = this.getMainHtmlWebUrl(str);
				mobileChannelNew.setWebUrl(webUrl);
			}
			
			//9 摘要   html格式没有正文摘要
//			if(StringUtils.isBlank(mobileChannelNew.getDescription())) {
//				String summary = this.getMainHtmlSummary(str);
//				mobileChannelNew.setDescription(summary);
//			}
		} catch (Exception e) {
			if(LOG.isErrorEnabled()) {
				LOG.error("["+site.getSiteName()+"]-["+mobileChannelNew.getTitle()+"]-[解析HTML格式正文异常]"+"[采集链接]："+mobileChannelNew.getLocation());
			}
		}
	}

	/**
	 * json正文对象
	 */
	@Override
	public JSONObject getMainJsonObject(String webCode) {
		JSONObject obj = JsonParseUtil.getJsonMainObj(webCode,template.getMainFileds());
		return obj;
	}

	
	
	/**
	 * json正文中的发布时间
	 */
	@Override
	public Date getMainJsonCreateDate(Object obj) {
		Date creteDate = JsonParseUtil.getJsonResultDate((JSONObject)obj, template.getMainTime());
		return creteDate;
	}	
	

	
	/**
	 * json原文链接
	 */
	@Override
	public String getMainJsonWebUrl(Object obj) {
		String MainWebUrl = JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainWebUrl());
		return MainWebUrl;
	}
	
	/**
	 * html格式的正文主体
	 * @param webCode
	 * @return
	 */
	@Override
	public String getMainHtmlStr(String webCode) {
		String field = HtmlParseUtil.getParseResult(webCode, template.getMainFileds());
		return field;
	}

	/**
	 * html格式正文时间
	 * @param str
	 * @return
	 */
	@Override
	public Date getMainHtmlCreateDate(String str) {
		Date createDate = HtmlParseUtil.getParseDate(str, template);
		return createDate;
	}

	/**
	 * html格式正文摘要
	 * @param str
	 * @return
	 */
	@Override
	public String getMainHtmlSummary(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * html格式正文原文链接
	 * @param str
	 * @return
	 */
	@Override
	public String getMainHtmlWebUrl(String str) {
		String webUrl = HtmlParseUtil.getParseResult(str, template.getMainWebUrl());
		return webUrl;
	}
	
	/**
	 * html格式阅读数
	 */
	@Override
	public int getMainHtmlRead(String str) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 列表新闻消重
	 * @param listChannelNews
	 * @return
	 */
	private List<MobileChannelNew> removeRepateChannelNews(List<MobileChannelNew> listChannelNews) {
		//1 频道内消重
		List<MobileChannelNew> mapList = this.channelRemoveRepate(listChannelNews);
		//2 redis消重
		List<MobileChannelNew> redisList = this.removeForRedis(mapList);
		
		return redisList;
	}

	/**
	 * 频道内消重
	 * @param listChannelNews
	 */
	private List<MobileChannelNew> channelRemoveRepate(List<MobileChannelNew> listChannelNews) {
		if(LOG.isInfoEnabled()) {
			LOG.info("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[频道内消重开始...]");
		}
		List<MobileChannelNew> repateList = null;
		if(listChannelNews != null && listChannelNews.size() > 0) {
			repateList = new ArrayList<MobileChannelNew>();
			Map<Long,String> mapRepate = new HashMap<Long, String>();
			for (MobileChannelNew mobileChannelNew : listChannelNews) {
				if(!mapRepate.containsKey(mobileChannelNew.getUrlHash())) {
					mapRepate.put(mobileChannelNew.getUrlHash(), "");
					repateList.add(mobileChannelNew);
				}else{
					if(LOG.isInfoEnabled()) {
						LOG.info("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[频道内出现重复数据],title:"+mobileChannelNew.getTitle());
					}
				}
			}
			mapRepate.clear();
			if(LOG.isInfoEnabled()) {
				LOG.info("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[频道内消重完成]-[消重前:"+listChannelNews.size()+"]-[消重后:"+repateList.size()+"]");
			}
		}
		return repateList;
	}

	/**
	 * redis消重
	 * @param mapList
	 * @return
	 */
	private List<MobileChannelNew> removeForRedis(List<MobileChannelNew> mapList) {
		List<MobileChannelNew> news = null;
		int removeCount = 0;
		if(LOG.isInfoEnabled()){
			LOG.info("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[redis消重开始]-[消重前总数："+(mapList == null ? "0":mapList.size())+"]");
		}
		if(mapList != null && mapList.size() > 0) {
			news = new ArrayList<MobileChannelNew>();
			for(int i = 0; i < mapList.size(); i++) {
				MobileChannelNew mobileChannelNew = mapList.get(i);
				if (StringUtils.isNotBlank(mobileChannelNew.getLocation())) {
					long assetId = StringFunction.getMurMurHash(StringFunction.changeUrlValueUtil(mobileChannelNew.getLocation()));
					mobileChannelNew.setAssetId(assetId);
					String isExists = "";
					//isExists = RedisUtil.getMbChannelNewId(RedisConstants.MEDIA_ASSET_REDIS + mobileChannelNew.getUrlHash());
					mobileChannelNew.setUpdateDate(new Date());
					mobileChannelNew.setKeyHash(new Date().getTime());
					mobileChannelNew.setOrderIndex(i+1);
					mobileChannelNew.setNewChannelId(appChannel.getNewChannelId());
					mobileChannelNew.setMobileNewSiteId(template.getTemplateId());
					if(StringUtils.isNotBlank(isExists)) {
						removeCount++;
					}else {
						news.add(mobileChannelNew);
					}
			}
			}
			if(LOG.isInfoEnabled()) {
				LOG.info("["+site.getSiteName()+"]-["+appChannel.getMobileChannelName()+"]-[redis消重完成]-[消重后："+news.size()+"个,去重："+ removeCount +"个]");
			}
		}
		return news;
	}
	
	
	
	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public AppChannel getappChannel() {
		return appChannel;
	}

	public void setAppChannel(AppChannel appChannel) {
		this.appChannel = appChannel;
	}

	public WebSite getSite() {
		return site;
	}

	public void setSite(WebSite site) {
		this.site = site;
	}

	@Override
	public int getJsonListFavoriteCpunt(Object obj) {
		int favorite = Integer.parseInt(JsonParseUtil.getJsonResult((JSONObject)obj, template.getListFavoriteCount()));
		return favorite;
	}

	@Override
	public String getJsonListTag(Object obj) {
		String tag = JsonParseUtil.getJsonResult((JSONObject)obj, template.getListTag());
		return tag;
	}

	@Override
	public String getMainJsonTag(Object obj) {
		String tag = JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainTag());
		return tag;
	}

	@Override
	public String getMainJsonArea(Object obj) {
		String Area = JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainArea());
		return Area;
	}

	@Override
	public String getMainJsonAkira(Object obj) {
		String Akira = JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainAkira());
		return Akira;
	}

	@Override
	public int getMainJsonFavoriteCount(Object obj) {
		int favorite = Integer.parseInt(JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainFavoriteCount()));
		return favorite;
	}

	@Override
	public int getMainJsonPlayCount(Object obj) {
		int playCount = Integer.parseInt(JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainPlayCount()));
		return playCount;
	}

	@Override
	public int getMainJsonDanmakuCount(Object obj) {
		int danmakuCount = Integer.parseInt(JsonParseUtil.getJsonResult((JSONObject)obj, template.getMainDanmakuCount()));
		return danmakuCount;
	}







}
