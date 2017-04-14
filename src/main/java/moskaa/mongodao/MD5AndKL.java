package moskaa.mongodao;

import java.security.MessageDigest;

import moskaa.collectlog.CollectLog;

public class MD5AndKL {  
	  
    // MD5加码。32位  
    public static String MD5(String inStr) {  
        MessageDigest md5 = null;  
        try {  
            md5 = MessageDigest.getInstance("MD5");  
        } catch (Exception e) {  
            CollectLog.LOG.debug(e.toString());  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
  
        byte[] md5Bytes = md5.digest(byteArray);  
  
        StringBuffer hexValue = new StringBuffer();  
  
        for (int i = 0; i < md5Bytes.length; i++) {  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
  
        return hexValue.toString();  
    }  
  
    // 可逆的加密算法  
    public static String encryption(String inStr, String key) {  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++) {
        	for (int j = 0; j < key.length(); j++) {
        		a[i] = (char) (a[i] ^ key.charAt(j));
			}
        }  
        String s = new String(a);  
        return s;  
    }  
  
    // 加密后解密  
    public static String unEncryption(String inStr, String key) {  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++) {
        	for (int j = 0; j < key.length(); j++) {
        		a[i] = (char) (a[i] ^ key.charAt(j));
			}
        }  
        String k = new String(a);  
        return k;  
    }  
  
    // 生成密文
    public static void main(String args[]) { 
    	String key = "yuzhiguang";
        String s = new String("mongodb://mosyun:mongo141421@112.124.110.91:10023/?authSource=");  
        System.out.println("原始：" + s);
        String pass = encryption(s, key);
        System.out.println("密文：" + pass);
        //遇到特殊字符需要转义成xml正常识别的字符
        String xmlStr = pass.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;").replace(",", "\\,");
        if (unEncryption(pass, key).equals(s)) {
        	System.out.println("key=" + key + "\r\n配置到xml文件的密文=" + xmlStr); 
		}
    }  
}  
