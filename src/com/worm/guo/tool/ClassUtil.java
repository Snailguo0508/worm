package com.worm.guo.tool;

import com.worm.guo.apps.base.BaseAppImpl;
import com.worm.guo.apps.bilibili.BiliBili;
import com.worm.guo.po.WormTask;

/**
 * 
 * 功能描述
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017-8-17 上午11:34:56
 */
public class ClassUtil {
	
	public static BaseAppImpl getBaseApp(WormTask wormTask){
		BaseAppImpl appImpl = null;
		//1.个性化自定义实现类
		int id = wormTask.getTemplate().get(0).getTemplateId();
		if(id == 1) {
			appImpl = new BiliBili();
		}else if(id == 2) {
			
		}
		
		//2.每个实现类必要初始化
		if(appImpl != null) {
			appImpl.setTemplate(wormTask.getTemplate().get(0));
			appImpl.setSite(wormTask.getSite());
			/* 网站频道信息放在每次启动任务从数据库中查询,修改频道后不用重启项目 */
			String filter="AND websiteNewSiteId="+wormTask.getSite().getWebsiteNewSiteId();
			/*List<MobileSiteChannel> channels= siteDao.getSiteChannel(filter);
			if(channels != null && channels.size()>0) {
				wormTask.setMobileSiteChannels(channels);
			}*/
		}
		return appImpl;
	}
}
