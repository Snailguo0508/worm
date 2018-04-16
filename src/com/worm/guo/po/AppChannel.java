package com.worm.guo.po;

public class AppChannel {

	private int newChannelId ;				//频道id，主键
	private int mobileNewSiteId; 			//站点id
	private String mobileChannelName ; 		//频道名称
	private String mobileChannelUrl ;		//频道url
	private int isDisplay ;				//该频道是否显示

	private int seqNo ;					//频道的显示顺序
	private int isHead;					//频道是否有头图：0：没有；1：有
	private int pageCount;					//该频道采集页数
	private int subTemplateId; 			//频道模板ID 
	
	public int getNewChannelId() {
		return newChannelId;
	}
	public void setNewChannelId(int newChannelId) {
		this.newChannelId = newChannelId;
	}
	public int getMobileNewSiteId() {
		return mobileNewSiteId;
	}
	public void setMobileNewSiteId(int mobileNewSiteId) {
		this.mobileNewSiteId = mobileNewSiteId;
	}
	public String getMobileChannelName() {
		return mobileChannelName;
	}
	public void setMobileChannelName(String mobileChannelName) {
		this.mobileChannelName = mobileChannelName;
	}
	public String getMobileChannelUrl() {
		return mobileChannelUrl;
	}
	public void setMobileChannelUrl(String mobileChannelUrl) {
		this.mobileChannelUrl = mobileChannelUrl;
	}
	public int getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getIsHead() {
		return isHead;
	}
	public void setIsHead(int isHead) {
		this.isHead = isHead;
	}
	public int getSubTemplateId() {
		return subTemplateId;
	}
	public void setSubTemplateId(int subTemplateId) {
		this.subTemplateId = subTemplateId;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
