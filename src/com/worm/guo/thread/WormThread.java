package com.worm.guo.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.worm.guo.apps.base.BaseAppImpl;
import com.worm.guo.enums.Constant;
import com.worm.guo.po.AppChannel;
import com.worm.guo.po.MobileChannelNew;
import com.worm.guo.po.WormTask;
import com.worm.guo.tool.ClassUtil;
import com.worm.guo.tool.DateFunction;
import com.worm.guo.tool.FileUtils;
import com.worm.guo.tool.SystemConfig;

public class WormThread extends TimerTask{

	private static Log log = LogFactory.getLog(WormThread.class);
	private WormTask wormTask;
	private static long radarNum = 0;
	/**
	 * 采集线程
	 */
	@Override
	public void run() {
		try {
			if (wormTask != null) {
				radarNum ++;
				if (log.isInfoEnabled()) {
					log.info("[采集]-["+Thread.currentThread().getName()+"]-["+wormTask.getTemplate().get(0).getTemplateName()+"，第 " + radarNum + " 次]-[启动]");
				}
				List<MobileChannelNew> channelNews = null;
				
				/** 获取具体MainApp **/
				BaseAppImpl app = ClassUtil.getBaseApp(wormTask);
				
				if(app == null || wormTask.getMobileSiteChannels() == null) {
					if(log.isErrorEnabled()) {
						log.error("[采集]-["+wormTask.getSite().getSiteName()+"]-[初始化采集对象异常或采集频道为空]");
					}
					return;
				}
				int count = 0;
				for(AppChannel channel : wormTask.getMobileSiteChannels()) {
					app.setAppChannel(channel);
					long beginTime = System.currentTimeMillis();
					
					count ++;
					if (log.isInfoEnabled()) {
						log.info("[媒体号采集]-["+wormTask.getSite().getSiteName()+ "当前：" + count + " 共：" + wormTask.getMobileSiteChannels().size() + "]-["+channel.getNewChannelId()+":"+channel.getMobileChannelName() + "]-[开始采集]");
					}
					
					/** 新闻采集 **/
					channelNews = app.getChannelNews();
					
					if (channelNews != null && channelNews.size() >0) {
						if (log.isInfoEnabled()) {
							if (log.isInfoEnabled()) {
								log.info("[媒体号采集]-["+wormTask.getSite().getSiteName()+"]-["+channel.getNewChannelId()+":"+channel.getMobileChannelName() + "]-[采集完成]-[新增"+channelNews.size()+"]");
							}
						}
						
						/**************
						 * 媒体号采集结果处理
						 */
						boolean isSave = false;
						//正式生产环境：信息文件存储OSS，MQ通知上载获取文件数据
						if (SystemConfig.TaskModel == Constant.TASKMODEL_PRODUCT.ordinal()) {
							//isSave = saveOssFile(channelNews);
						//开发环境：信息文件存储到本地	
						}else if (SystemConfig.TaskModel == Constant.TASKMODEL_DEVELOP.ordinal()) {
							//信息需要本地存储，(正式环境)建全文索引，(开发环境)看数据
							isSave = saveAssetFile(SystemConfig.uploadFilePath,channelNews);
						}
						
						//文件保存成功更新数据库和redis
						/*if (isSave) {
							//保存到数据库
							saveSiteNews(channelNews);
							//存入redis
							putWebsiteNewsIntoRedis(channelNews);
						}*/
						
						if (log.isInfoEnabled()) {
							log.info("[媒体号采集]-["+wormTask.getSite().getSiteName()+"]-["+channel.getNewChannelId()+":"+channel.getMobileChannelName() + "]-[采集结果处理完成]-[新增:"+channelNews.size()+",用时:"+((System.currentTimeMillis() - beginTime)/1000)+"s]");
						}
					}else {
						if (log.isInfoEnabled()) {
							log.info("[媒体号采集]-["+wormTask.getSite().getSiteName()+"]-["+channel.getNewChannelId()+":"+channel.getMobileChannelName() + "]-[未踩到新信息]");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/***
	 * 
	 * @param channelNews
	 */
	public static boolean saveAssetFile(String filePath,List<MobileChannelNew> channelNews) {
		boolean ret = false;
		if (channelNews != null && channelNews.size() > 0) {
			FileUtils.createDirIfNotExists(filePath);
			String fileName = filePath + "" + DateFunction.getMMDDHHMMSS()+ new Random().nextInt(999999)+ "_" + channelNews.size() + ".txt";
			BufferedWriter writer = null;
			try {
				File file = new File(fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
				for (MobileChannelNew mobileChannelNew : channelNews) {
					if(StringUtils.isNotBlank(mobileChannelNew.getContent())){
						String txt = FileUtils.writeFileTxtString(mobileChannelNew, "201");
						writer.write(txt);
					}else {
						if (log.isInfoEnabled()) {
							log.info("数据为空...." + mobileChannelNew.getTitle() + " siteID:" + mobileChannelNew.getMobileNewSiteId());
						}
					}
				}
				writer.flush();
				if(log.isInfoEnabled()) {
					log.info("["+channelNews.get(0).getSiteName()+"]生成上载文件成功,共：["+channelNews.size()+"]条,输出目录:["+SystemConfig.uploadFilePath+"]");
				}
				ret = true;
			} catch (IOException e) {
				if (log.isErrorEnabled()) {
					log.error("输出上载文件异常..." + fileName);
					log.error(e.getMessage(), e);
				}
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("生成上载文件异常！", e);
				}
			}  finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						if (log.isErrorEnabled()) {
							log.error("关闭输出缓冲buffer异常...");
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	public WormTask getWormTask() {
		return wormTask;
	}
	public void setWormTask(WormTask wormTask) {
		this.wormTask = wormTask;
	}

	
}
