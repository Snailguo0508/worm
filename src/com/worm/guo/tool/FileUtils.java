package com.worm.guo.tool;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.worm.guo.po.MobileChannelNew;

/**
 * 文件操作
 * 功能描述
 * @author zhxiang zhangxiang@blwit.com
 * @company Bluewit
 * @createDate 2017-8-24 下午12:06:47
 */
public class FileUtils {

	public static void createDirIfNotExists(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static String writeFileTxtString(MobileChannelNew mobileChannelNewStat, String radarWebSiteType) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("%& \r\n");
			String tp = "TP:" + radarWebSiteType + "\r\n";
			sb.append(tp);
			sb.append("CW:0 \r\n");
			String mi = "MI:" + StringFunction.changeUrlValueUtil(mobileChannelNewStat.getLocation()) + "\r\n";
			sb.append(mi);
			sb.append("OI: \r\n");
			sb.append("CU:").append(StringUtils.isNotBlank(mobileChannelNewStat.getCommentUrl()) ? mobileChannelNewStat.getCommentUrl():"").append("\r\n");
			sb.append("IM:").append(mobileChannelNewStat.getPicUrl()).append("\r\n");
			String sn = "SN:" + mobileChannelNewStat.getSiteName() + "\r\n";
			sb.append(sn);
			String bn = "BN:" + mobileChannelNewStat.getChannelName() + "\r\n";
			sb.append(bn);
			sb.append(("TD:" +  RadarUtil.websiteName2Hash(mobileChannelNewStat.getChannelName())+ "\r\n"));
			//sb.append(("FD:" + /*RadarUtil.changeRadarsiteId(mobileChannelNewStat.getMobileNewSiteId())*/+ "\r\n"));
			sb.append("SP:0\r\n");
			sb.append("CL:0\r\n");
			sb.append("DT:301\r\n");
			sb.append("PT:0\r\n");
			sb.append("LC:1\r\n");

			sb.append("II:0\r\n");
			sb.append("NC:0\r\n");
			sb.append("NR:").append(mobileChannelNewStat.getCommentCount()).append("\r\n");
			sb.append("LR:0\r\n");
			String title = "ST:" + mobileChannelNewStat.getTitle() + "\r\n";
			sb.append(title);
			sb.append("AU:").append(StringUtils.isNotBlank(mobileChannelNewStat.getEditorUser()) ? mobileChannelNewStat.getEditorUser():"").append("\r\n");

			String rq = "RQ:" + getRqDate(mobileChannelNewStat) + "\r\n";
			sb.append(rq);
			sb.append("TZ:\r\n");
			String ct = "CT:" + DateFunction.getSystemTime() + "\r\n";
			sb.append(ct);

			sb.append("LV:1\r\n");
			sb.append("SO:").append(StringUtils.isNotBlank(mobileChannelNewStat.getZzSrc()) ? mobileChannelNewStat.getZzSrc():"").append("\r\n");
			sb.append("BD:\r\n");
			sb.append("PN:1\r\n");
			sb.append("XP:0\r\n");
			sb.append("PC:").append((StringUtils.isNotBlank(mobileChannelNewStat.getPicUrl()) ? "1" : "0")).append("\r\n");
			sb.append("EN:0\r\n");
			sb.append("EP:\r\n");
			sb.append("IZ:1\r\n");

			sb.append("AC:0\r\n");
			sb.append("RA:0\r\n");
			sb.append("SD:0\r\n");
			sb.append("IB:0\r\n");
			sb.append("BO:\r\n");
			sb.append("LN:").append(StringUtils.isNotBlank(mobileChannelNewStat.getWebUrl()) ? mobileChannelNewStat.getWebUrl():"").append("\r\n");
			sb.append("CI:0\r\n");
			sb.append("OR:1\r\n");
			String tx = "TX:" + (StringUtils.isNotBlank(mobileChannelNewStat.getContent()) ? mobileChannelNewStat.getContent().trim() : "") + "\r\n";
			sb.append(tx);
			sb.append("IO:0\r\n");
			sb.append("AK:").append(mobileChannelNewStat.getAssetId()).append("\r\n");
			sb.append("&%\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static String getRqDate(MobileChannelNew mobileChannelNewStat) {
		Date date = mobileChannelNewStat.getCreateDate();
		if(date == null) {
			date = mobileChannelNewStat.getUpdateDate();
		}
		
		return DateFunction.formatDate2Sting(date);
	}
	
}
