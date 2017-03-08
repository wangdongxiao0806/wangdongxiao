package com.distributed.util;

import java.awt.geom.Area;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Deal8583 {
	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();

	public static String byte2hex(byte[] b) // 二进制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			
			if (stmp.length() == 1){
				hs = hs + "0" + stmp;
			}
			else{
				hs = hs + stmp;
			}
		}
		return hs;
	}
	
	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null){
			return null;
		}
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1){
			return null;
		}
		byte[] b = new byte[len / 2];
		try {for (int i = 0; i < str.length(); i += 2) {
			b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
		}
		return b;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * BASE64 编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeBufferBase64(byte[] buff) {
		return buff == null ? null : encoder.encode(buff);
	}

	/**
	 * BASE64解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decodeBufferBase64(String s) {
		try {
			return s == null ? null : decoder.decodeBuffer(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64 字节数组编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String encodeBase64(byte[] s) {
		if (s == null)
			return null;
		String res = new BASE64Encoder().encode(s);
		res = res.replace("\n", "");
		res = res.replace("\r", "");
		return res;
	}

	/**
	 * BASE64解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decodeBase64(byte[] buff) {
		if (buff == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(new String(buff));

			return b;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getEigthBitsStringFromByte(int b) {
		b |= 256; // mark the 9th digit as 1 to make sure the string
		String str = Integer.toBinaryString(b);
		int len = str.length();
		return str.substring(len - 8, len);
	}

	public static byte getByteFromEigthBitsString(String str) {
		byte b;
		if (str.substring(0, 1).equals("1")) {
			str = "0" + str.substring(1);
			b = Byte.valueOf(str, 2);
			b |= 128;
		} else {
			b = Byte.valueOf(str, 2);
		}
		return b;
	}

	/**
	 * 将一个16字节数组转成128二进制数组
	 * 
	 * @param b
	 * @return
	 */
	public static boolean[] getBinaryFromByte(byte[] b) {
		boolean[] binary = new boolean[b.length * 8 + 1];
		String strsum = "";
		for (int i = 0; i < b.length; i++) {
			strsum += getEigthBitsStringFromByte(b[i]);
		}
		for (int i = 0; i < strsum.length(); i++) {
			if (strsum.substring(i, i + 1).equalsIgnoreCase("1")) {
				binary[i + 1] = true;
			} else {
				binary[i + 1] = false;
			}
		}
		return binary;
	}

	/**
	 * 将一个128二进制数组转成16字节数组
	 * 
	 * @param binary
	 * @return
	 */
	public static byte[] getByteFromBinary(boolean[] binary) {

		int num = (binary.length - 1) / 8;
		if ((binary.length - 1) % 8 != 0) {
			num = num + 1;
		}
		byte[] b = new byte[num];
		String s = "";
		for (int i = 1; i < binary.length; i++) {
			if (binary[i]) {
				s += "1";
			} else {
				s += "0";
			}
		}
		String tmpstr;
		int j = 0;
		for (int i = 0; i < s.length(); i = i + 8) {
			tmpstr = s.substring(i, i + 8);
			b[j] = getByteFromEigthBitsString(tmpstr);
			j = j + 1;
		}
		return b;
		
	}

	/**
	 * 将一个byte位图转成字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String getStrFromBitMap(byte[] b) {
		String strsum = "";
		for (int i = 0; i < b.length; i++) {
			strsum += getEigthBitsStringFromByte(b[i]);
		}
		return strsum;
	}

	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	/**
	 * 十六进制字符串转换成bytes
	 * 
	 * @param src
	 * @return
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	/**
	 * 将BCD码转成int
	 * 
	 * @param b
	 * @return
	 */
	public static int bcdToint(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return Integer.parseInt(sb.toString());
	}

	// 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
	public static int byteToInt(byte[] bRefArr) {
	    int iOutcome = 0;
	    byte bLoop;

	    for (int i = 0; i < bRefArr.length; i++) {
	        bLoop = bRefArr[i];
	        iOutcome += (bLoop & 0xFF) << (8 * i);
	    }
	    return iOutcome;
	}
	
	public static String bitmapHex2Binary(String bitmap){
		String bitMapStr =  bitmap;
		if (bitMapStr == null || bitMapStr.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < bitMapStr.length(); i++)
        {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(bitMapStr
                            .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
	}
	
	public static String getBitMapStr(String str8583){
		return str8583.substring(30, 46);
	}
	
	/**字符串转换成16进制ASCII */
	public static String stringToHexAscii(String str){
		String s = str;
		StringBuffer sb = new StringBuffer();
		byte[] b = s.getBytes();
		int[] in = new int[b.length];
		for (int i = 0; i < in.length; i++) {
		in[i] = b[i]&0xff;
		}
		for (int j = 0; j < in.length; j++) {
			sb.append( Integer.toString(in[j], 0x10));
		}
		return sb.toString();
	}
	
	public static String byteToHexString(byte [] byteHex){
		String strHex="";
		if(byteHex==null)
			return "";
		for(int i=0;i<byteHex.length;i++){
			strHex=strHex+String.format("%02X", byteHex[i]);
		}
		return strHex;
	}
	
	/** 
    * Convert char to byte     
    * @param c char 
    * @return byte  
    */  
    private static byte charToByte(char c) {  
       return (byte) "0123456789ABCDEF".indexOf(c);  
   }  
 /** 
    * Convert hex string to byte[]   把为字符串转化为字节数组 
	    * @param hexString the hex string 
	    * @return byte[] 
	    */  
    public static byte[] hexStringToBytes(String hexString) {  
	       if (hexString == null || hexString.equals("")) {  
	           return null;  
	       }  
	       hexString = hexString.toUpperCase();  
	       int length = hexString.length() / 2;  
	       char[] hexChars = hexString.toCharArray();  
	       byte[] d = new byte[length];  
	       for (int i = 0; i < length; i++) {  
	           int pos = i * 2;  
	           d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	       }  
	       return d;  
	   }  
	
    /**hexString 相互异或*/
    public static String xor(String strX,String strY){   
          
    	//吧StrX 转换成二进制数
    	String binaryXStr = "";
    	int currentIndexX = 1;
    	for (int i = 0; i < strX.length(); i++) {
			String currentHex = strX.substring(i, currentIndexX);
			String currentBinary = Integer.toBinaryString(Integer.parseInt(currentHex, 16));
			while(currentBinary.length()<4){
				currentBinary = "0"+currentBinary;
			}
			binaryXStr += currentBinary;
			currentIndexX += 1;
		}
    	
    	//吧StrY 转换成二进制数
    	String binaryYStr = "";
    	int currentIndexY = 1;
    	for (int i = 0; i < strY.length(); i++) {
			String currentHex = strY.substring(i, currentIndexY);
			String currentBinary = Integer.toBinaryString(Integer.parseInt(currentHex, 16));
			while(currentBinary.length()<4){
				currentBinary = "0"+currentBinary;
			}
			binaryYStr += currentBinary;
			currentIndexY += 1;
		}
    	
    	//对做异或运算
    	String resultStr = "";
    	for (int i = 0; i < binaryXStr.length(); i++) {
    		
			int tempX = Integer.parseInt(String.format("%c",  binaryXStr.charAt(i)));
			int tempY = Integer.parseInt(String.format("%c",  binaryYStr.charAt(i)));
			
			resultStr += (tempX^tempY);
		}
    	
    	//吧结果转换成hexString
    	String resultHexStr = "";
    	for (int i = 0; i < resultStr.length()/4; i++) {
    		String currentBinary = resultStr.substring(i*4, i*4+4 );
    		resultHexStr += Integer.toHexString(Integer.parseInt(currentBinary, 2));
		}
    	
    	return resultHexStr;
    }  
	
	/**
	 * 获得报文的位图数组
	 * 
	 * */
	public static ArrayList<Integer> getBitMap(String bitMapStr){
		ArrayList<Integer> bitMaps = new ArrayList<Integer>();
		if (bitMapStr == null || bitMapStr.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < bitMapStr.length(); i++)
        {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(bitMapStr
                            .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        for(int i = 0 ; i < bString.length() ; i ++){
        	char c = bString.charAt(i);
        	int flag = Integer.parseInt(String.format("%c", c));
        	if (flag==1) {
        		bitMaps.add(i+1);
			}
        }
		return bitMaps;
	}

	
	public static String getMac(String macStr){
		
		String result = "";
		
		//0.保证字符串是16个字节的倍数
		while(macStr.length()%16 != 0){
			macStr = macStr + "0";
		}
		//1.吧所有的字符8字节一组 放到数组里面
		ArrayList<String> arrays = new ArrayList<String>();
		for (int i = 0 ; i < macStr.length()/16 ; i++) {
			arrays.add(macStr.substring(i * 16, i * 16 +16));
		}
		
		//2. 把array里面的每个字符串都做异或处理
		String resultS = "0000000000000000";
		for (int i = 0; i < arrays.size(); i++) {
			resultS = xor(resultS, arrays.get(i));
			System.out.println(resultS);
		}
		
		
		
		
		
		
		return result;
		
	}
	
	/**
	 * 拼接8583报文
	 * 
	 * 
	 * 
	 * */
	public static String pinjie8583(ArrayList<Area> areas){
		String result =null;
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		
	}


}
