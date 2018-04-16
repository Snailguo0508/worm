package com.worm.guo.po;

/**
 * 采集模板
 * @Title:
 * @Desc:
 * @Company:Bluewit
 * @Copyright: Copyright (c) 2014 
 * @author liuja
 * @version: 1.0 2015-1-29下午4:31:15
 *
 */
public class Template {
	private int templateId;
	private String templateName; // 模板名称
	private String listHeadType; //列表头图类型
	private int listType; // 列表模板类型,1为json，2为html，3为xml
	private String listFileds; // 列表区域
	private String listhead; // 列表头图
	private String listTitle; // 列表标题
	private String listDescription;// 列表摘要
	private String listTime; // 列表时间
	private String listUrl; // 内容页链接
	private String listImgUrl; // 图片链接
	private String listFavoriteCount;// 列表追番数
	private String listTag;// 列表标签
	private String listNextPage;//列表下一页

	private int mainType; // 内容页模板类型1为json，2为html，3为xml'
	private String mainFileds; // 内容页区域
	private String mainTitle; // 内容页标题
	private String mainTime; // 内容页时间
	private String mainDescription; // 内容页摘要
	private String mainTag; // 内容页标签
	private String mainArea; // 番剧产地
	private String mainAkira; // 声优
	private String mainFavoriteCount;// 内容页追番数
	private String mainPlayCount;// 内容页播放数
	private String mainDanmakuCount; // 内容页弹幕数
	private String mainImgUrl; // 内容页图片地址
	
	private int commentType; // 评论模板类型1为json，2为html，3为xml
	private String commentNextPage; // 评论下一页链接
	private String commentFileds; // 评论区域
	private String commentAuthor; // 评论作者
	private String commentTime; // 评论时间
	private String commentText; // 评论正文
	private String commentProvince; // 评论省份
	private String commentTopNum; // 评论点赞数
	private String mainWebUrl;	//原始地址

	private int subTemplateId; //子模版
	private int isMainTextInList; //文本是否在List中
	
	public int getSubTemplateId() {
		return subTemplateId;
	}

	public void setSubTemplateId(int subTemplateId) {
		this.subTemplateId = subTemplateId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

	public String getListFileds() {
		return listFileds;
	}

	public void setListFileds(String listFileds) {
		this.listFileds = listFileds;
	}

	public String getListhead() {
		return listhead;
	}

	public void setListhead(String listhead) {
		this.listhead = listhead;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}

	public String getListDescription() {
		return listDescription;
	}

	public void setListDescription(String listDescription) {
		this.listDescription = listDescription;
	}

	public String getListTime() {
		return listTime;
	}

	public void setListTime(String listTime) {
		this.listTime = listTime;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public String getListImgUrl() {
		return listImgUrl;
	}

	public void setListImgUrl(String listImgUrl) {
		this.listImgUrl = listImgUrl;
	}

	public int getMainType() {
		return mainType;
	}

	public void setMainType(int mainType) {
		this.mainType = mainType;
	}

	public String getMainFileds() {
		return mainFileds;
	}

	public void setMainFileds(String mainFileds) {
		this.mainFileds = mainFileds;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getMainTime() {
		return mainTime;
	}

	public void setMainTime(String mainTime) {
		this.mainTime = mainTime;
	}


	public String getListFavoriteCount() {
		return listFavoriteCount;
	}

	public void setListFavoriteCount(String listFavoriteCount) {
		this.listFavoriteCount = listFavoriteCount;
	}

	public String getMainDescription() {
		return mainDescription;
	}

	public void setMainDescription(String mainDescription) {
		this.mainDescription = mainDescription;
	}

	public String getMainTag() {
		return mainTag;
	}

	public void setMainTag(String mainTag) {
		this.mainTag = mainTag;
	}

	public String getMainArea() {
		return mainArea;
	}

	public void setMainArea(String mainArea) {
		this.mainArea = mainArea;
	}

	public String getMainAkira() {
		return mainAkira;
	}

	public void setMainAkira(String mainAkira) {
		this.mainAkira = mainAkira;
	}

	public String getMainFavoriteCount() {
		return mainFavoriteCount;
	}

	public void setMainFavoriteCount(String mainFavoriteCount) {
		this.mainFavoriteCount = mainFavoriteCount;
	}

	public String getMainPlayCount() {
		return mainPlayCount;
	}

	public void setMainPlayCount(String mainPlayCount) {
		this.mainPlayCount = mainPlayCount;
	}

	public String getMainDanmakuCount() {
		return mainDanmakuCount;
	}

	public void setMainDanmakuCount(String mainDanmakuCount) {
		this.mainDanmakuCount = mainDanmakuCount;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public int getCommentType() {
		return commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}

	public String getCommentNextPage() {
		return commentNextPage;
	}

	public void setCommentNextPage(String commentNextPage) {
		this.commentNextPage = commentNextPage;
	}

	public String getCommentFileds() {
		return commentFileds;
	}

	public void setCommentFileds(String commentFileds) {
		this.commentFileds = commentFileds;
	}

	public String getCommentAuthor() {
		return commentAuthor;
	}

	public void setCommentAuthor(String commentAuthor) {
		this.commentAuthor = commentAuthor;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getCommentProvince() {
		return commentProvince;
	}

	public void setCommentProvince(String commentProvince) {
		this.commentProvince = commentProvince;
	}

	public String getCommentTopNum() {
		return commentTopNum;
	}

	public void setCommentTopNum(String commentTopNum) {
		this.commentTopNum = commentTopNum;
	}

	public String getListTag() {
		return listTag;
	}

	public void setListTag(String listTag) {
		this.listTag = listTag;
	}

	public String getListNextPage() {
		return listNextPage;
	}

	public void setListNextPage(String listNextPage) {
		this.listNextPage = listNextPage;
	}

	public String getListHeadType() {
		return listHeadType;
	}

	public void setListHeadType(String listHeadType) {
		this.listHeadType = listHeadType;
	}

	public String getMainWebUrl() {
		return mainWebUrl;
	}

	public void setMainWebUrl(String mainWebUrl) {
		this.mainWebUrl = mainWebUrl;
	}

	public int getIsMainTextInList() {
		return isMainTextInList;
	}

	public void setIsMainTextInList(int isMainTextInList) {
		this.isMainTextInList = isMainTextInList;
	}

}

