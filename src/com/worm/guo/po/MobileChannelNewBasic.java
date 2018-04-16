package com.worm.guo.po;

import java.util.Date;

public class MobileChannelNewBasic {
	private int mobileNewSiteId; 
	private int newChannelId;
	private long keyHash;
	private Date updateDate;
	private long updateTime;
	private long assetId;
	private String title ;
	private String webUrl = "";	//pc端url
	private String location ;
	private long urlHash  ;
	private String description;
	private Date createDate;
	private String editorUser;
	private String editorCode;
	private String picUrl;
	private int orderIndex;
	private int isDisplay;
	private int isTopImage;
	private String zzSrc;
	private int likeCount; 	//点赞量
	private int visitCount ;  //阅读量
	private int commentCount; 	//评论数
	private String content;
	private String tagName; 	//标签
	private int tag;    		//标签对应value
	private String commentUrl; //评论地址
	private int contentType;    //新闻内容格式  1：表示json格式   2:表示XML格式
	private String channelName;
	private String siteName;
	private String oldLocation;
	
	
	
	
	public int getMobileNewSiteId() {
		return mobileNewSiteId;
	}
	public void setMobileNewSiteId(int mobileNewSiteId) {
		this.mobileNewSiteId = mobileNewSiteId;
	}
	public int getNewChannelId() {
		return newChannelId;
	}
	public void setNewChannelId(int newChannelId) {
		this.newChannelId = newChannelId;
	}
	public long getKeyHash() {
		return keyHash;
	}
	public void setKeyHash(long keyHash) {
		this.keyHash = keyHash;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getAssetId() {
		return assetId;
	}
	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public long getUrlHash() {
		return urlHash;
	}
	public void setUrlHash(long urlHash) {
		this.urlHash = urlHash;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getEditorUser() {
		return editorUser;
	}
	public void setEditorUser(String editorUser) {
		this.editorUser = editorUser;
	}
	public String getEditorCode() {
		return editorCode;
	}
	public void setEditorCode(String editorCode) {
		this.editorCode = editorCode;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	public int getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
	public int getIsTopImage() {
		return isTopImage;
	}
	public void setIsTopImage(int isTopImage) {
		this.isTopImage = isTopImage;
	}
	public String getZzSrc() {
		return zzSrc;
	}
	public void setZzSrc(String zzSrc) {
		this.zzSrc = zzSrc;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public String getCommentUrl() {
		return commentUrl;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getOldLocation() {
		return oldLocation;
	}
	public void setOldLocation(String oldLocation) {
		this.oldLocation = oldLocation;
	}
	
	
}
