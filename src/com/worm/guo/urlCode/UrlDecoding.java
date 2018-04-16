package com.worm.guo.urlCode;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;



/**
 * 功能描述
 * 采集链接解密
 * @author 康小安 kangxa@blwit.com
 * @company Bluewit
 * @createDate 2017-4-26 上午11:31:38
 */
public class UrlDecoding {
	private static final String HMAC_GENERATE_KEY = "254657476b652bb0583fe0c951f29eee5b22982722531f45fbb1632db54f6886";
	private static final String HMAC_KEY = "HMACSHA256";

	
	
	@SuppressWarnings("static-access")
	private static String getHmac(String macKey, String macData){
		Mac mac;
		String hashMac = "";
		try {
			mac = Mac.getInstance(HMAC_KEY);
			byte[] secretByte = macKey.getBytes();
			byte[] dataBytes = macData.getBytes();
			mac.init(new SecretKeySpec(secretByte, HMAC_KEY));
			hashMac = new String(new Hex().encode(mac.doFinal(dataBytes)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	    return hashMac;
	}
	
	/** 
     * 纽约时报英文版加密
     * @Description: 
     * @param str
     * @param str2
     * @return String   
     * @throws
     */
    public static String NYMd5(String str, String str2) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest((str2 + str + "fd8fbe1a04798ea3ed65dd72c1a37434").getBytes());
            StringBuilder stringBuilder = new StringBuilder(32);
            for (byte b : digest) {
                stringBuilder.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new RuntimeException("Missing MD5 Algorithm", e);
        }
    }
    public static String NYAuthor(long timeStamp) {
        String l = Long.toString(timeStamp+1000);
        String b = "/cms/mobile/v4/*";
        return String.format("auth=expires=%s~access=%s~md5=%s", new Object[]{l, b, NYMd5(b, l)});
    }
	
	
}
