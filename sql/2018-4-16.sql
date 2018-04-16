
DROP TABLE IF EXISTS `ana_media_channel_news`;
CREATE TABLE `ana_media_channel_news` (
`recordId`  int(11) NOT NULL AUTO_INCREMENT ,
`assetId`  mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`mobileNewSiteId`  int(11) NOT NULL ,
`newChannelId`  int(11) NOT NULL ,
`siteName`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`channelName`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`tags`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`area`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`akira`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`favorites`  int(11) NOT NULL DEFAULT 0 ,
`playCount`  int(11) NOT NULL DEFAULT 0 ,
`danmakuCount`  int(11) NOT NULL DEFAULT 0 ,
`year`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`month`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`createDate`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`location`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`updateDate`  timestamp NULL DEFAULT NULL ,
PRIMARY KEY (`recordId`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=41679

;


DROP TABLE IF EXISTS `ana_media_site`;
CREATE TABLE `ana_media_site` (
`websiteNewSiteId`  int(11) NOT NULL ,
`siteName`  varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`siteUrl`  varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`siteLogo`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`AppFolder`  int(2) NULL DEFAULT NULL ,
PRIMARY KEY (`websiteNewSiteId`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;


DROP TABLE IF EXISTS `ana_media_site_channel`;
CREATE TABLE `ana_media_site_channel` (
`websiteChannelId`  int(11) NOT NULL AUTO_INCREMENT ,
`websiteNewSiteId`  int(11) NULL DEFAULT NULL ,
`websiteChannelName`  varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`websiteChannelUrl`  varchar(12000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`isDisplay`  int(11) NULL DEFAULT NULL ,
`seqNo`  int(11) NULL DEFAULT NULL ,
`pageCount`  int(1) NOT NULL DEFAULT 1 ,
`subTemplateId`  int(2) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`websiteChannelId`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=663

;


DROP TABLE IF EXISTS `ana_media_site_template`;
CREATE TABLE `ana_media_site_template` (
`templateId`  bigint(20) NOT NULL ,
`templateName`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`listHeadType`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listType`  int(6) NULL DEFAULT 0 ,
`listFileds`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listhead`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listTitle`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listDescription`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listTime`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listImgUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listCommentsCount`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listTag`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`listNextPage`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainType`  int(6) NULL DEFAULT NULL ,
`mainFileds`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainTitle`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainTime`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainFrom`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainAuthor`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainText`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainNextPage`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainCommentCount`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainCommentsUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainVedioUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`mainImgUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`createDate`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`updateDate`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`updateUserId`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`mainWebUrl`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`subTemplateId`  int(10) NULL DEFAULT 0 ,
`isMainTextInList`  int(11) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`templateId`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;