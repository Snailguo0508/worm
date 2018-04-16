package com.worm.guo.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTaskQuartz {

	/** 一天有多少个分钟 */
	public static final int DayMinCount = 24 * 60;
	
	
	/**
	 * 定时启动任务，task为所启动的任务（继承TimerTask），startHour 0~23表示整点启动且表示几点启动；-1表示定时启动，如每4分钟，interval表示与下次的间隔时间，单位为分
	 * @param task
	 * @param startHour
	 * @param interval
	 */
	public void scheduleTask(TimerTask task, int startHour, int interval) {
		Timer timer = new Timer();
		if (startHour >= 0 && startHour <= 23) {//整点启动
			String nextTimeStr = RadarUtil.getSystemDate() + " " + (startHour >= 10 ? startHour : "0" + startHour) + ":00:00";
			Date nextTime = RadarUtil.stringDate(nextTimeStr, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new Date());
			Date date = new Date();
			
			long waitTime = 0;
			if (nextTime.after(date)) {

			}else {
				nextTime = RadarUtil.getDateBeforeDay(nextTime, -1);
			}
			
			waitTime = nextTime.getTime() - date.getTime();
			
			timer.schedule(task, waitTime, interval * 60 * 1000);
		
		}else {//定时启动
			if(startHour == -2){  //调试模式  2s后启动
				timer.schedule(task, 2 * 1000 ,interval * 60 * 1000);
			}else{
				timer.schedule(task, interval * 60 * 1000, interval * 60 * 1000);
			}
			
		}
		
	}
}
