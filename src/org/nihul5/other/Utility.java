package org.nihul5.other;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {
	public static boolean verifyAlphaNumeric(String str, int maxLen) {
		return str != null && 
				str.length() > 0 && str.length() < maxLen && 
				str.matches(CONST.REGX_ALPHANUMERIC);
	}
	
	/**
	 * http://stackoverflow.com/questions/415953/generate-md5-hash-in-java/421696#421696
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5(String str) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(str.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
			hashtext = "0" + hashtext;
		}
		
		return hashtext;
	}
}
