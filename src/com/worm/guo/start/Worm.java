package com.worm.guo.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.worm.guo.constant.WormConstant;
import com.worm.guo.po.WormTask;
import com.worm.guo.thread.WormThread;
import com.worm.guo.tool.RadarQueue;
import com.worm.guo.tool.SystemConfig;
import com.worm.guo.tool.TimeTaskQuartz;


public class Worm {
	private static Log log = LogFactory.getLog(Worm.class); 
	
	
	public static boolean initConfig(){
		if(!SystemConfig.init()){
			if (log.isErrorEnabled()) {
				log.error("[加载系统配置文件异常]");
			}
			return false;
		}
		if(!SystemConfig.initTask()){
			if (log.isErrorEnabled()) {
				log.error("[加载采集任务模板数据异常]");
			}
			return false;
		}
		return 	true;
	} 
	
	public static void main(String[] args) {
		if (log.isInfoEnabled()) {
			log.info("[采集启动]-[等待系统初始化]");
		}
		if(!initConfig()) {
			return;
		}
		if(RadarQueue.taskMap != null && RadarQueue.taskMap.keySet().size() > 0){
			TimeTaskQuartz quartz = new TimeTaskQuartz();
			int timeInterval = SystemConfig.TaskInterval;
			//每个app设置单独的定时采集数据任务线程
			for(int taskId : RadarQueue.taskMap.keySet()){
				WormTask task = RadarQueue.taskMap.get(taskId);
				if (SystemConfig.TASK_TIME.keySet().size() >0 && SystemConfig.TASK_TIME.containsKey(taskId)) {
					timeInterval = SystemConfig.TASK_TIME.get(taskId);
					WormThread thread = new WormThread();
					thread.setWormTask(task);
					/** 优先采集  **/
					if(WormConstant.PRIORITYSITESET.contains(taskId)) {
						quartz.scheduleTask(thread, -2, timeInterval);
					}else { //延时采集
						quartz.scheduleTask(thread, -1, timeInterval);
					}
				}
			}
		}else {
			if (log.isErrorEnabled()) {
				log.error("[媒体号采集]-[未配置采集任务]");
			}
		}
	}
}


