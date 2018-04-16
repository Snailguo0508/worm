package com.worm.guo.tool;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class RadarUtil {

	/**
	 * 返回系统时间组成的字符串。例如：2005-08-28
	 * 
	 * @return String
	 */
	public static String getSystemDate() {
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
		returnValue = year + "-" + month + "-" + day;

		return returnValue;
	}
	
	/**
	 * string按格式转化为时间类型，并给予默认时间
	 * 
	 * @param string
	 * @param formatter
	 * @param defaultValue
	 * @return
	 */
	public static Date stringDate(String string, DateFormat formatter,
			Date defaultValue) {
		try {
			return formatter.parse(string);
		} catch (Exception ex) {
			return defaultValue;
		}
	}
	
	/**
	 * 获取给定时间date的前day天
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
	
	public static int websiteName2Hash(String name) {
		int num = 0x15051505;
		int num2 = num;

		if (StringUtils.isBlank(name)) {
			return 0;
		}

		byte[] bytes = null;
		try {
			bytes = name.getBytes("UTF-16LE");
		} catch (UnsupportedEncodingException e) {
		}
		if (bytes == null || bytes.length == 0 ){
			return 0;
		}

		int array[] = bytes2int(bytes);

		int j = 0;
		for (int i = name.length(); i > 0; i -= 4) {
			num = (((num << 5) + num) + (num >> 0x1b)) ^ array[j];
			if (i <= 2)
				break;
			num2 = (((num2 << 5) + num2) + (num2 >> 0x1b)) ^ array[j + 1];
			j += 2;
		}
		return (num + (num2 * 0x5d588b65));
	}
	/**
	 * byte数组转换算法
	 * @param bytes
	 * @return
	 * 转换方法，4组byte长度=1 int类型长度，即4字节，0x86机器转换顺序小尾存放，即数据的低位放在首位，在转换一组byte时，即从后向前转换。

	 */
	private static int[] bytes2int(byte[] bytes) {

		int intLen = bytes.length / 4 + (bytes.length % 4 == 0 ? 0 : 1);
		int[] array = new int[intLen];

		for (int i = 0; i < array.length - 1; i++) {
			array[i] = (bytes[i * 4 + 3] << 24) + ((bytes[i * 4 + 2] & 0xFF) << 16) + ((bytes[i * 4 + 1] & 0xFF) << 8)
					+ (bytes[i * 4] & 0xFF);
		}

		int i = bytes.length % 4;
		switch (i) {
		case 2:
			array[array.length - 1] = ((bytes[bytes.length - 1] & 0xFF) << 8) + (bytes[bytes.length - 2] & 0xFF);
			break;
		default:
			array[array.length - 1] = (bytes[bytes.length - 1] << 24) + ((bytes[bytes.length - 2] & 0xFF) << 16)
					+ ((bytes[bytes.length - 3] & 0xFF) << 8) + (bytes[bytes.length - 4] & 0xFF);
			break;
		}
		return array;
	}
}
