package com.worm.guo.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateFunction {

	private static Log log = LogFactory.getLog(DateFunction.class);

	public static final long MILLIS_PER_MINUTE = 60 * 1000;
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	/**
	 * 根据字符串时间 转换为相应的Date
	 * 
	 * @param time
	 *            如：X分钟前，X小时前
	 * 
	 * @return date
	 */
	public static Date parseStringToDate(String time) {
		Date date = null;
		Calendar c = Calendar.getInstance();
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher m = pattern.matcher(time);
		int num = 0;
		if (m.find()) {
			num = Integer.valueOf(m.replaceAll("").trim());
		}
		if (time.indexOf("秒") > 0) {
			c.add(Calendar.SECOND, 0 - num);
		} else if (time.indexOf("分") > 0) {
			c.add(Calendar.MINUTE, 0 - num);
		} else if (time.indexOf("时") > 0) {
			c.add(Calendar.HOUR, 0 - num);
		} else if (time.indexOf("天") > 0) {
			c.add(Calendar.DATE, 0 - num);
		}
		date = c.getTime();

		return date;
	}

	/**
	 * time Long值转为date
	 * 
	 * @param time
	 *            ,如"1389701337000"
	 * @return date
	 */
	public static Date parseTimeStrToDate(String time) {
		Date date = null;
		if (time.length() == 10) {
			time = time + "000";
		}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.valueOf(time));
		date = c.getTime();
		return date;
	}

	/**
	 * 返回日期类型 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static Date StringDate(String str) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(str);
		} catch (ParseException e) {

		}

		return date;
	}

	/**
	 * 返回日期类型 自动检测格式
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static Date StringDateClever(String str) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if (str.length() == 19) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (str.length() == 17) {
				sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
			} else if (str.length() == 16) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (str.length() == 13) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			} else if (str.length() == 10) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			}
			date = sdf.parse(str);
		} catch (ParseException e) {
			log.error(e);
		}
		return date;
	}

	/**
	 * 根据日期格式 返回相应日期格式的日期
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 */
	public static Date String2DateBasePattern(String datetime, String pattern) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			date = sdf.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回日期字符串(YYYY-MM-DD)，如果是空值返回特殊日期。
	 * 
	 * 
	 * @param input
	 *            Date
	 * @return String
	 */
	public static String getString(Date input) {
		String strReturn = "";
		if (input == null) {
			strReturn = "3000-1-1";
		} else {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			strReturn = fmt.format(input.getTime());
		}
		return strReturn;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：20050828143158
	 * <p>
	 * 
	 * @return String
	 */
	public static String getYYYYMMDDHHMMSS() {
		Calendar c = Calendar.getInstance();
		int y, m, d, h, mi, s;
		String year, month, day, hour, minute, second;
		String returnValue = "";

		y = c.get(Calendar.YEAR);
		m = c.get(Calendar.MONTH) + 1;
		d = c.get(Calendar.DATE);
		h = c.get(Calendar.HOUR_OF_DAY);
		mi = c.get(Calendar.MINUTE);
		s = c.get(Calendar.SECOND);

		year = String.valueOf(y);

		if (m <= 9) {
			month = "0" + String.valueOf(m);
		} else {
			month = String.valueOf(m);

		}
		if (d <= 9) {
			day = "0" + String.valueOf(d);
		} else {
			day = String.valueOf(d);

		}
		if (h <= 9) {
			hour = "0" + String.valueOf(h);
		} else {
			hour = String.valueOf(h);

		}
		if (mi <= 9) {
			minute = "0" + String.valueOf(mi);
		} else {
			minute = String.valueOf(mi);

		}
		if (s <= 9) {
			second = "0" + String.valueOf(s);
		} else {
			second = String.valueOf(s);

		}
		returnValue = year + month + day + hour + minute + second;

		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：0828143158
	 * <p>
	 * 
	 * @return String
	 */
	public static String getMMDDHHMMSS() {
		Calendar c = Calendar.getInstance();
		int m, d, h, mi, s;
		String month, day, hour, minute, second;
		String returnValue = "";

		m = c.get(Calendar.MONTH) + 1;
		d = c.get(Calendar.DATE);
		h = c.get(Calendar.HOUR_OF_DAY);
		mi = c.get(Calendar.MINUTE);
		s = c.get(Calendar.SECOND);

		if (m <= 9) {
			month = "0" + String.valueOf(m);
		} else {
			month = String.valueOf(m);

		}
		if (d <= 9) {
			day = "0" + String.valueOf(d);
		} else {
			day = String.valueOf(d);

		}
		if (h <= 9) {
			hour = "0" + String.valueOf(h);
		} else {
			hour = String.valueOf(h);

		}
		if (mi <= 9) {
			minute = "0" + String.valueOf(mi);
		} else {
			minute = String.valueOf(mi);

		}
		if (s <= 9) {
			second = "0" + String.valueOf(s);
		} else {
			second = String.valueOf(s);

		}
		returnValue = month + day + hour + minute + second;

		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：20050828
	 * <p>
	 * 
	 * @return String
	 */
	public static String getYYYYMMDD() {
		Calendar c = Calendar.getInstance();
		int y, m, d;
		String year, month, day;
		String returnValue = "";

		y = c.get(Calendar.YEAR);
		m = c.get(Calendar.MONTH) + 1;
		d = c.get(Calendar.DATE);

		year = String.valueOf(y);

		if (m <= 9) {
			month = "0" + String.valueOf(m);
		} else {
			month = String.valueOf(m);

		}
		if (d <= 9) {
			day = "0" + String.valueOf(d);
		} else {
			day = String.valueOf(d);

		}
		returnValue = year + month + day;

		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：2005-08-28
	 * <p>
	 * 
	 * @return String
	 */
	public static String getSystemDate() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			returnValue = fmt.format(c.getTime());
		} catch (Exception e) {

		}
		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串。例如：2005-08-28 15:00
	 * <p>
	 * 
	 * @return String
	 */
	public static String getSystemHour() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:00");
			returnValue = fmt.format(c.getTime());
		} catch (Exception e) {

		}
		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串,精确到分钟。例如：2001-01-01 00:00
	 * <p>
	 * 
	 * @return String
	 */
	public static String getSystemMin() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			returnValue = fmt.format(c.getTime());
		} catch (Exception e) {
		}
		return returnValue;
	}

	/**
	 * <p>
	 * 返回系统时间组成的字符串,精确到秒。例如：2001-01-01 00:00:00
	 * <p>
	 * 
	 * @return String
	 */
	public static String getSystemTime() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			returnValue = fmt.format(c.getTime());
		} catch (Exception e) {
		}
		return returnValue;

	}

	/**
	 * 格式化日期,返回一个类似 yyyy-MM-dd HH:00:00 格式的整点日期
	 */
	public static Date getOnClickDate(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH");
		String strReturn = fmt.format(date);
		Date onClickDate = StringDateClever(strReturn);
		return onClickDate;
	}

	/**
	 * 获取基准日期若干天前的日期
	 * 
	 * 
	 * @param curDate
	 *            String 基准日期
	 * @param days
	 *            int 需要获取基准日期几天前的日期
	 * 
	 * @return String
	 */
	public static String getBeforeDay(String curDate, int days) {
		String oldDate = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			if (curDate.length() == 19) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (curDate.length() == 16) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (curDate.length() == 13) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH");
			}

			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());
			caldate.add(Calendar.DATE, 0 - days);
			/*
			 * oldDate = caldate.get(Calendar.YEAR) + "-" + (caldate.get(Calendar.MONTH) + 1) + "-" + caldate.get(Calendar.DAY_OF_MONTH);
			 */
			oldDate = fmt.format(caldate.getTime());
		} catch (Exception e) {
			return curDate;
		}
		return oldDate;
	}

	/**
	 * 获取基准日期若干天后的日期
	 * 
	 * 
	 * @param curDate
	 *            String 基准日期
	 * @param days
	 *            int 需要获取基准日期几天后的日期
	 * 
	 * @return String
	 */
	public static String getAfterDay(String curDate, int days) {
		String oldDate = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			if (curDate.length() == 19) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (curDate.length() == 16) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (curDate.length() == 13) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH");
			}

			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());
			caldate.add(Calendar.DATE, days);
			/*
			 * oldDate = caldate.get(Calendar.YEAR) + "-" + (caldate.get(Calendar.MONTH) + 1) + "-" + caldate.get(Calendar.DAY_OF_MONTH);
			 */
			oldDate = fmt.format(caldate.getTime());
		} catch (Exception e) {
			return curDate;
		}
		return oldDate;
	}

	/**
	 * 获取基准日期若干天后的日期
	 * 
	 * 
	 * @param date
	 *            Date 基准日期
	 * @param days
	 *            int 需要获取基准日期几天后的日期
	 * 
	 * @return Date
	 */
	public static Date getAfterDay(Date date, int days) {
		Calendar caldate = new GregorianCalendar();
		caldate.setTime(date);
		caldate.add(Calendar.DATE, days);
		return caldate.getTime();
	}

	/**
	 * 获取系统日期若干天前的日期
	 * 
	 * 
	 * @param days
	 *            int 需要获取系统日期几天前的日期
	 * 
	 * @return Date
	 */
	public static Date getSystemTimeBeforeDay(int days) {

		Calendar caldate = new GregorianCalendar();
		if (days >= 0)
			caldate.add(Calendar.DATE, 0 - days);
		return caldate.getTime();
	}

	public static Date getTodayDate() {
		return StringDate(getSystemDate());
	}


	/**
	 * 对给定的时间值转为全文检索语法的时间，没有秒，只精确到分 如将2006-05-02 12:12 转为全文检索语法的时间 200605021212
	 * 
	 * @param strDate
	 *            String
	 * @return String
	 */
	public static String getContentTimeNoSec(String strDate) {
		if (strDate == null || "null".equals(strDate)) {
			return ("null");
		}
		String strContent = "";
		if (strDate.length() == 16) {
			strContent = strDate + ":00";
		} else if (strDate.length() == 19) {
			strContent = strDate;
		}

		strContent = strContent.replaceAll(" ", "");
		strContent = strContent.replaceAll("-", "");
		strContent = strContent.replaceAll(":", "");
		return (strContent);
	}

	/**
	 * 对给定的时间值转为全文检索语法的时间，不补时分秒 如将2006-05-02转为全文检索语法的时间 20060502
	 * 
	 * @param strDate
	 *            String
	 * @return String
	 */
	public static String transContentTime(String strDate) {
		if (strDate == null || "null".equals(strDate)) {
			return ("null");
		}
		String strContent = strDate;
		strContent = strContent.replaceAll(" ", "");
		strContent = strContent.replaceAll("-", "");
		strContent = strContent.replaceAll(":", "");
		return (strContent);
	}

	/**
	 * 返回指定时间若干小时前的时间
	 * 
	 * @param curDate
	 *            String
	 * @param hours
	 *            int
	 * @return long
	 */
	public static long getBeforeHour(String curDate, int hours) {
		long oldtime = 0;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());

			caldate.add(Calendar.HOUR_OF_DAY, 0 - hours);

			oldtime = caldate.getTimeInMillis();
		} catch (Exception e) {

		}
		return oldtime;
	}

	public static long getBeforeHour(Date curDate, int hours) {
		long oldtime = 0;
		try {
			Calendar caldate = new GregorianCalendar();
			caldate.setTimeInMillis(curDate.getTime());

			caldate.add(Calendar.HOUR_OF_DAY, 0 - hours);

			oldtime = caldate.getTimeInMillis();
		} catch (Exception e) {

		}
		return oldtime;
	}

	/**
	 * 获取给定时间date的前day天
	 * 
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBeforeDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		Date _date = calendar.getTime();
		return _date;
	}

	/**
	 * 获取给定时间date的前hours个小时
	 * 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date getDateBeforHour(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, -hours);
		Date _date = calendar.getTime();
		return _date;
	}

	/**
	 * 获取给定时间date的前mis分钟；当mis为正数时间向前推移mis分钟，负数时则时间向后推移mis分钟。 eg：mis=10,向前推10分钟，mis=-10，向后推10分钟
	 * 
	 * @param date
	 * @param mis
	 * @return
	 */
	public static Date getDateBeforMin(Date date, int mis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -mis);
		Date _date = calendar.getTime();
		return _date;
	}

	/**
	 * 以"yyyy-MM-dd HH:mm形式返回给定时间若干小时前的时间
	 * 
	 * @param curDate
	 *            String
	 * @param hours
	 *            int
	 * @return String
	 */
	public static String getStrBeforeHour(String curDate, int hours) {
		String olddate = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			if (curDate.length() == 19) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (curDate.length() == 16) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (curDate.length() == 13) {
				fmt = new SimpleDateFormat("yyyy-MM-dd HH");
			}
			Calendar caldate = new GregorianCalendar();
			Date date = fmt.parse(curDate);
			caldate.setTimeInMillis(date.getTime());

			caldate.add(Calendar.HOUR_OF_DAY, 0 - hours);

			olddate = fmt.format(caldate.getTime());
		} catch (Exception e) {

		}
		return olddate;
	}

	/**
	 * formatTime 格式化输出时间
	 * 
	 * @param aLongMillis
	 *            long the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this date.
	 * @param aStringFormat
	 *            格式
	 * @return String
	 */
	public static String formatTime(long aLongMillis, String aStringFormat) {
		String ret = "";

		try {
			Calendar caldate = new GregorianCalendar();
			caldate.setTimeInMillis(aLongMillis);
			SimpleDateFormat fmt = new SimpleDateFormat(aStringFormat);
			ret = fmt.format(caldate.getTime());

		} catch (Exception e) {

		}

		return ret;
	}

	/**
	 * 以yyyy-MM-dd HH:mm:ss的形式返回给定时间若干分钟前的时间
	 * 
	 * @param strCurDate
	 *            String 给的时间
	 * @param iMinutes
	 *            int 多少分钟
	 * @return String
	 */
	public static String getTimeBeforeMinute(String strCurTime, int iMinutes) {
		String strReturntime = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = new GregorianCalendar();
			Date date = fmt.parse(strCurTime);
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, 0 - iMinutes);
			strReturntime = fmt.format(calendar.getTime());
		} catch (Exception e) {
			return strCurTime;
		}
		return strReturntime;
	}

	public static String getBeforeMinute(String strCurTime, int iMinutes) {
		String strReturntime = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calendar = new GregorianCalendar();
			Date date = fmt.parse(strCurTime);
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, 0 - iMinutes);
			strReturntime = fmt.format(calendar.getTime());
		} catch (Exception e) {
			return strCurTime;
		}
		return strReturntime;
	}

	/**
	 * 处理全文检索返回的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String processDateString(String date) {
		String strRet = "";
		if (date == null) {
			return strRet;
		}
		if (date.indexOf("-") > -1) {
			strRet = date;
		} else if (date.length() == 14) {
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6, 8);
			String hour = date.substring(8, 10);
			String min = date.substring(10, 12);
			String sec = date.substring(12, 14);
			strRet = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;

		} else {
			strRet = date;
		}
		return strRet;
	}

	/**
	 * 处理全文检索返回的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateByFullText(String date) {
		String strRet = "";
		if (date == null) {
			return strRet;
		}
		if (date.indexOf("-") > -1) {
			strRet = date;
		} else {
			if (date.length() == 14) {
				String year = date.substring(0, 4);
				String month = date.substring(4, 6);
				String day = date.substring(6, 8);
				String hour = date.substring(8, 10);
				String min = date.substring(10, 12);
				strRet = year + "-" + month + "-" + day + " " + hour + ":" + min;
			}
		}
		return strRet;
	}

	/**
	 * 返回时间差，单位/秒
	 * 
	 * @param str
	 * @return
	 */
	public static long getDateDifference(Date after, Date before) {

		long diff = after.getTime() - before.getTime();
		long days = diff / (1000);

		return days; 
	}

	/**
	 * 返回时间差的字符串，天，小时，分钟，秒
	 * 
	 * @param after
	 * @param before
	 * @return
	 */
	public static String getDateDiffStr(Date after, Date before) {
		String rtn = "";

		long diff = after.getTime() - before.getTime();

		long day = diff / (24 * 60 * 60 * 1000);
		long hour = (diff / (60 * 60 * 1000) - day * 24);
		long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		rtn = "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
		return rtn;
	}

	/**
	 * 24小时记时法 将日期(Date)转换为字符串形式,默认转换为："yyyy-MM-dd HH:mm"
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToMediaSting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * 24小时记时法 将日期(Date)转换为字符串形式,默认转换为："MM-dd HH:mm"
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToMonthSting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		String strDate = sdf.format(date);
		return strDate;
	}

	public static String formatDateToDaySting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String strDate = sdf.format(date);
		return strDate;
	}

	public static String formatDateToMinSting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * 12小时记时法 12小时记时法 将日期(Date)转换为字符串形式,默认转换为："yyyy-MM-dd hh:mm:ss"
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate2Sting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(date);
		return strDate;
	}

	public static String formatDate2Sting(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * 24小时记时法 24小时记时法 将日期(Date)转换为字符串形式,默认转换为："yyyy-MM-dd"
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate3Sting(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(date);
		return strDate;
	}

	public static String formatDateString(String date, SimpleDateFormat sdf) {
		String strDate = sdf.format(StringDateClever(date));
		return strDate;
	}

	/**
	 * 计算时间点
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Map<String, String> getTimeMap(String beginDate, String endDate) {
		Map<String, String> map = new TreeMap<String, String>();
		Date end = String2DateBasePattern(endDate, "yyyy-MM-dd HH:mm");
		Date begin = String2DateBasePattern(beginDate, "yyyy-MM-dd HH:mm");

		long len = (end.getTime() - begin.getTime()) / (long) (100); // 时间点间隔
		for (int i = 1; i < 101; i++) {
			map.put(formatTime(end.getTime() - (i - 1) * len, "MM-dd HH:mm"), end.getTime() - i * len + "," + (end.getTime() - (i - 1) * len));
		}
		return map;
	}

	/**
	 * 计算100个时间点：以天为单位
	 * 
	 * @return
	 */
	public static Map<String, String> getHundredTime() {
		Map<String, String> timeMap = new TreeMap<String, String>();
		Date end = new Date();
		Date endDay = formateDateToDay(end);
		for (int i = 0; i < 100; i++) {
			if (i == 0) {
				timeMap.put(formatDate2Sting(endDay, "yyyy-MM-dd"), endDay.getTime() + "," + end.getTime());
			} else {
				timeMap.put(getBeforeDay(formatDate2Sting(endDay, "yyyy-MM-dd"), i), getBeforeHour(endDay, i * 24) + "," + getBeforeHour(endDay, (i - 1) * 24));
			}
		}
		return timeMap;
	}

	private static Date formateDateToDay(Date formatDate) {
		Date date = new Date();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(formatDate);
			date = format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return date;
	}

	public static boolean isBeforeDate(Date nowDate, Date beforeDate) {

		boolean isBefore = false;
		if (nowDate != null && beforeDate != null) {
			long now = nowDate.getTime();
			long before = beforeDate.getTime();
			if (now < before) {
				isBefore = true;
			}
		}

		return isBefore;
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfWeek(String date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(StringDateClever(date));
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return formatDate2Sting(c.getTime());
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDayOfWeek(String date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(StringDateClever(date));
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return formatDate2Sting(c.getTime());
	}

	/**
	 * 获取前一个月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfMonth(String date, int month) {
		Calendar c = new GregorianCalendar();
		c.setTime(StringDateClever(date));
		c.add(Calendar.MONTH, 0 - month);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return formatDate2Sting(c.getTime());
	}

	/**
	 * 获取前一个月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDayOfLastMonth(String date) {
		Calendar c = new GregorianCalendar();
		c.setTime(StringDateClever(date));
		c.set(Calendar.DAY_OF_MONTH, 0);// 设置为0号,当前日期既为本月最后一天
		return formatDate2Sting(c.getTime());
	}

	/***
	 * 判断时间是否提前几个小时
	 * 
	 * @param nowDate
	 * @param beforeDate
	 * @param hours
	 * @return
	 */
	public static boolean isBeforeHours(Date nowDate, Date beforeDate, int hours) {

		return (nowDate.getTime() - beforeDate.getTime()) < hours * MILLIS_PER_HOUR;
	}

	/***
	 * sphinx检索时间条件
	 * 
	 * @param beginDate
	 * @return
	 */
	public static long getSphinxTime(String beginDate) {
		return StringDateClever(beginDate).getTime() / 1000;
	}

	/**
	 * 计算日期所在周的周一日期
	 */
	public static Date getMondayDate(Date date) {
		Date mondayDate = date;
		Date _date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		Calendar c = Calendar.getInstance();
		c.setTime(_date);
		// 星期：1，2，3，4，5，6，7
		int iWeek = c.get(Calendar.DAY_OF_WEEK);
		// 时期日->星期八
		if (iWeek == 1) {
			iWeek = 8;
		}

		mondayDate = DateUtils.addDays(_date, 2 - iWeek);
		return mondayDate;
	}

	/**
	 * 获取本月1号的时间
	 */
	public static Date getMonthFirstDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		date = c.getTime();
		return date;
	}

	/**
	 * 获取某个月前1号的时间
	 */
	public static Date getMonthFirstDate(Date date, int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		date = c.getTime();
		return date;
	}

	public static Date getAfterHour(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hours);
		return calendar.getTime();
	}

	public static Date getAfterMinutes(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	/**
	 * 本周周一时间
	 * 
	 * @return
	 */
	public static Date getMondayDate() {
		Date date = getTodayDate();
		return getMondayDate(date);
	}

	/**
	 * 返回系统时间所在的自然时间5分钟
	 * 
	 * @return String
	 */
	public static String getSystemFiveMin() {
		Calendar c = Calendar.getInstance();
		String returnValue = "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			returnValue = fmt.format(c.getTime());

			int lastMin = Integer.valueOf(returnValue.substring(returnValue.length() - 1));
			if (lastMin >= 0 && lastMin < 5) {
				returnValue = returnValue.substring(0, returnValue.length() - 1) + "5";
			} else {
				returnValue = formatDateToMediaSting(getAfterMinutes(StringDateClever(returnValue), 5));
				returnValue = returnValue.substring(0, returnValue.length() - 1) + "0";
			}
		} catch (Exception e) {

		}
		return returnValue;
	}
	
	/**
	 * 返回系统时间所在的自然时间10分钟
	 * @return
	 */
	public static String getSystemTenMin() {
		String returnValue = "";

		try {
			Date date = getAfterMinutes(new Date(), 10);
			returnValue = formatDateToMediaSting(date);
			returnValue = returnValue.substring(0, returnValue.length() -1) + 0;
		} catch (Exception e) {

		}
		return returnValue;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getSystemTenMin());
	}

	/**
	 * 计算两个时间的小时自然数的差
	 * 
	 * @param lastRecordDate
	 *            最后操作时间
	 * @param currTime
	 *            当前默认时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getHoursMinus(Date beforeDate, Date currTime) { // TODO
		int lastHours = beforeDate.getHours();
		int cuurHours = currTime.getHours();
		int hoursMinus = cuurHours - lastHours;
		if (hoursMinus < 0) {
			return -(hoursMinus);
		}
		return hoursMinus;
	}

	/**
	 * 判断当前时间是否是整点
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isOnClick(Date date) {
		SimpleDateFormat myFmt = new SimpleDateFormat("mmss");
		String mmss = myFmt.format(date);
		return "0000".equals(mmss);
	}

	/**
	 * 判断两个时间是否处于同一个小时内 true 表示在同一个小时内 false 表示不再同意一个小时内
	 */
	public static boolean isInSameHour(Date beforeDate, Date afterDate) {
		int hoursMinus = getHoursMinus(beforeDate, afterDate);
		if (hoursMinus >= 0) { // 表示不再同一个小时内
			return false;
		}
		return true;
	}
}
