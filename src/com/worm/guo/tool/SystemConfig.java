package com.worm.guo.tool;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.worm.guo.po.AppChannel;
import com.worm.guo.po.Template;
import com.worm.guo.po.WebSite;
import com.worm.guo.po.WormTask;

public class SystemConfig {

	private static Log log = LogFactory.getLog(SystemConfig.class);
	
	public static String RadarEncoding = "UTF-8";
	public static int TaskInterval = 60;
	public static int TaskModel = 0;
	public static String uploadFilePath; //上载文件存储位置
	public static Map<Integer,Integer> TASK_TIME = new HashMap<Integer, Integer>();
	
	/***
	 * 加载配置信息
	 * @return
	 */
	public static boolean init(){
		boolean  result = false;
			SAXReader reader = new SAXReader();
			File file = new File("WormConfig.xml");
			try {
				Document document = reader.read(file);
				Element root = document.getRootElement();
				List nodes = root.elements();
				for (int i = 0; i < nodes.size(); i++) {
					Element element = (Element) nodes.get(i);
					if(element.getName().equals("BaseConfig")){
						List baseNodes = element.elements();
						for (int j = 0; j < baseNodes.size(); j++) {
							Element baseElement = (Element) baseNodes.get(j);
							if(baseElement.getName().equals("RadarEncoding")){
								SystemConfig.RadarEncoding = baseElement.getData().toString().trim();
							}
							if (baseElement.getName().equalsIgnoreCase("TaskInterval")) {
								SystemConfig.TaskInterval = Integer.parseInt(baseElement.getData().toString().trim());
							}
							if(baseElement.getName().equalsIgnoreCase("TaskModel")){
								SystemConfig.TaskModel = Integer.parseInt(baseElement.getData().toString().trim());
							}
							if(baseElement.getName().equalsIgnoreCase("UploadFilePath")){
								SystemConfig.uploadFilePath = baseElement.getData().toString().trim();
							}
						}
					}
					if(element.getName().equalsIgnoreCase("TaskConfig")){
						List requestSites = element.elements("Task");
						for (int j = 0; j < requestSites.size(); j++) {
							Element siteTask = (Element) requestSites.get(j);
							if (siteTask != null) {
								int taskId = Integer.parseInt(siteTask.element("TaskId").getData().toString());
								int time = Integer.parseInt(siteTask.elementText("QuartzTime"));
								TASK_TIME.put(taskId,time);
							}
						}
					}
				}
				result = true;
			} catch (MalformedURLException e) {
				if (log.isErrorEnabled()) {
					log.error("[配置文件读取错误]");
					log.error(e.getMessage(), e);
				}
				result = false;
			} catch (DocumentException e) {
				if (log.isErrorEnabled()) {
					log.error("[采集配置文件解析错误]");
					log.error(e.getMessage(), e);
				}
				result = false;
			}		
		return result;
	}
	public static boolean initTask(){
		boolean result = false;
		File file = new File("worm");
		File[] taskFiles = file.listFiles();
		if (taskFiles != null && taskFiles.length >0) {
			for(File taskFile : taskFiles){
				SAXReader reader = new SAXReader();
				try {
					Document document = reader.read(taskFile);
					Element eroot = document.getRootElement();
					List nodes = eroot.elements();
					WormTask task = new WormTask();
					Template template = new Template();
					List<AppChannel> channels = new ArrayList<AppChannel>();
					WebSite site = new WebSite();
					for (int i = 0; i < nodes.size(); i++) {
						Element element = (Element) nodes.get(i);
						if (element.getName().equalsIgnoreCase("AppTemplate")) {
							List baseConfigNodes = element.elements();
							for (int j = 0; j < baseConfigNodes.size(); j++) {
								Element baseConfigElement = (Element) baseConfigNodes.get(j);
								if (baseConfigElement.getData() != null && StringUtils.isNotBlank(baseConfigElement.getData().toString())) {
									if (baseConfigElement.getName().equalsIgnoreCase("templateId")) {
										template.setTemplateId(Integer.parseInt((baseConfigElement.getData().toString().trim())));
									}
									if (baseConfigElement.getName().equalsIgnoreCase("templateName")) {
										template.setTemplateName(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listType")) {
										template.setListType(Integer.parseInt((baseConfigElement.getData().toString().trim())));
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listFileds")) {
										template.setListFileds((baseConfigElement.getData().toString().trim()));
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listhead")) {
										template.setListhead(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listTitle")) {
										template.setListTitle(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listDescription")) {
										template.setListDescription(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listTime")) {
										template.setListTime(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listUrl")) {
										template.setListUrl(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listImgUrl")) {
										template.setListImgUrl(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listFavoriteCount")) {
										template.setListFavoriteCount(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listTag")) {
										template.setListTag(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("listNextPage")) {
										template.setListNextPage(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainType")) {
										template.setMainType(Integer.parseInt(baseConfigElement.getData().toString().trim()));
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainFileds")) {
										template.setMainFileds(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainTitle")) {
										template.setMainTitle(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainTime")) {
										template.setMainTime(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainDescription")) {
										template.setMainDescription(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainTag")) {
										template.setMainTag(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainArea")) {
										template.setMainArea(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainAkira")) {
										template.setMainAkira(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainFavoriteCount")) {
										template.setMainFavoriteCount(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainPlayCount")) {
										template.setMainPlayCount(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainDanmakuCount")) {
										template.setMainDanmakuCount(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainImgUrl")) {
										template.setMainImgUrl(baseConfigElement.getData().toString().trim());
									}
									/*if (baseConfigElement.getName().equalsIgnoreCase("commentType")) {
										template.setCommentType(Integer.parseInt(baseConfigElement.getData().toString().trim()));
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentNextPage")) {
										template.setCommentNextPage(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentFileds")) {
										template.setCommentFileds(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentAuthor")) {
										template.setCommentAuthor(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentTime")) {
										template.setCommentTime(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentText")) {
										template.setCommentText(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentProvince")) {
										template.setCommentProvince(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentTopNum")) {
										template.setCommentTopNum(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("mainWebUrl")) {
										template.setMainWebUrl(baseConfigElement.getData().toString().trim());
									}
									if (baseConfigElement.getName().equalsIgnoreCase("commentFileds")) {
										template.setCommentFileds(baseConfigElement.getData().toString().trim());
									}*/
									template.setSubTemplateId(0);
								}
							}
						}
						if (element.getName().equalsIgnoreCase("AppChannels")) {
							List appChannels = element.elements("AppChannel");
							for (int j = 0; j < appChannels.size(); j++) {
								Element channelNode = (Element) appChannels.get(j);
								
								if (channelNode != null) {
									AppChannel channel = new AppChannel();
									channel.setNewChannelId(Integer.parseInt(channelNode.element("newChannelId").getData().toString()));
									channel.setMobileChannelName(channelNode.elementText("mobileChannelName").trim());
									channel.setMobileChannelUrl(channelNode.elementText("mobileChannelUrl"));
									channel.setPageCount(Integer.parseInt(channelNode.elementTextTrim("pageCount")));
									channel.setIsHead(Integer.parseInt(channelNode.elementText("isHead")));
									channel.setSubTemplateId(0);
									channels.add(channel);
								}
							}
						}
						if (element.getName().equalsIgnoreCase("AppSite")) {
							List sites = element.elements();
							for (int j = 0; j < sites.size(); j++) {
								Element appNode = (Element) sites.get(j);
								if (appNode.getName().equalsIgnoreCase("WebsiteNewSiteId")) {
									site.setWebsiteNewSiteId(Integer.parseInt(appNode.getData().toString()));
								}
								if(appNode.getName().equalsIgnoreCase("SiteName")){	
									site.setSiteName(appNode.getData().toString().trim());
								}
							}
						}
					}
					
					if (template.getTemplateId() >0 && channels.size() >0) {
						List<Template> templates = new ArrayList<Template>();
						templates.add(template);
						task.setTemplate(templates);
						task.setSite(site);
						task.setMobileSiteChannels(channels);
						RadarQueue.taskMap.put(template.getTemplateId(), task);
					}
					result = true;
				} catch (MalformedURLException e) {
					if (log.isErrorEnabled()) {
						log.error("加载采集任务配置文件异常..." + taskFile.getName());
						log.error(e.getMessage(), e);
					}
					result = false;
				} catch (DocumentException e) {
					if (log.isErrorEnabled()) {
						log.error("加载采集任务配置文件异常..." + taskFile.getName());
						log.error(e.getMessage(), e);
					}
					result = false;
				}
			}
		}else {
			if (log.isErrorEnabled()) {
				log.error("未发现采集任务模板，请检查是否配置模板文件");
			}
		}
		return result;
	}
}
