package com.worm.guo.support;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseFrame {
	private ParseFrame(){}
	private static final String FRAME_TAG = "<frame(.*?)>";
	public static List<String> getFramesUrl(String html){
		List<String> frames = new ArrayList<String>();
		if ( html != null ){
			Matcher m = Pattern.compile(FRAME_TAG, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(html);
			Matcher mFrame = null;
			while ( m.find() ){
				mFrame = Pattern.compile("src=(.*?)\\s+", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(m.group());
				if ( mFrame.find() ){
					frames.add(mFrame.group().indexOf("\"") != -1 ? mFrame.group().substring(5,mFrame.group().trim().length()-1) : mFrame.group().substring(4,mFrame.group().trim().length()));
				}
			}
		}
		return frames;
	}
}
