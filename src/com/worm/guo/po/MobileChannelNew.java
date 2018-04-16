package com.worm.guo.po;

import java.util.List;

import com.worm.guo.support.JsonSupport;

import net.sf.json.JSONObject;

public class MobileChannelNew extends MobileChannelNewBasic implements JsonSupport{
	private long mbChannelNewId;
	
	private int favorite;
	private int playCount;
	private int danmakuCount;
	private String year;
	private String month;
	private String area;
	private String akira;
	
	
	
	
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public int getDanmakuCount() {
		return danmakuCount;
	}
	public void setDanmakuCount(int danmakuCount) {
		this.danmakuCount = danmakuCount;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAkira() {
		return akira;
	}
	public void setAkira(String akira) {
		this.akira = akira;
	}
	public long getMbChannelNewId() {
		return mbChannelNewId;
	}
	public void setMbChannelNewId(long mbChannelNewId) {
		this.mbChannelNewId = mbChannelNewId;
	}
	public List<MobileChannelNew> getMobileChannelNews() {
		return mobileChannelNews;
	}
	public void setMobileChannelNews(List<MobileChannelNew> mobileChannelNews) {
		this.mobileChannelNews = mobileChannelNews;
	}
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	private List<MobileChannelNew> mobileChannelNews;
	private String nextPage;
	@Override
	public String toJson() {
		JSONObject jo = JSONObject.fromObject(this);
		return jo.toString();
	}
}
