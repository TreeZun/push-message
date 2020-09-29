package com.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
  * 
  *     
  * 类名称:      <br> 
  * 类描述:    描述   <br>
  * 创建人:    杨小龙   <br> 
  * 创建时间:   2020年9月10日 下午5:09:43  <br>  
  * 修改人:    杨小龙  <br>    
  * 修改备注:   说明本次修改内容   <br> 
  * 版本:      v1.0  <br>  
  *
 */
public class AESUtils {
	
	private static  String safetyKey = "66adfa@P3.daU";


	public AESUtils() {

	}

	public AESUtils(String safetyKey) {
		this.safetyKey = safetyKey;
	}


	/**
     * AES加密字符串
     * 
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static String encrypt(String content) {
    	KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
		
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(safetyKey.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器 
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化 
        byte[] result = cipher.doFinal(byteContent);
        return encodeBASE64(result); // 加密 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 解密AES加密过的字符串
     * 
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static String decrypt(String str) {
    	try{
    	 KeyGenerator kgen = KeyGenerator.getInstance("AES");
         SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
         secureRandom.setSeed(safetyKey.getBytes());
         kgen.init(128, secureRandom);
         SecretKey secretKey = kgen.generateKey();
         byte[] enCodeFormat = secretKey.getEncoded();
         SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
         Cipher cipher = Cipher.getInstance("AES");// 创建密码器 
         cipher.init(Cipher.DECRYPT_MODE, key);// 初始化 
         byte[] base64Dec = Base64.decodeBase64(str);
         byte[] result = cipher.doFinal(base64Dec);
         return new String(result);
     } catch (Exception e) {
     }
     return null;
    }
    
    
    public static String encodeBASE64(byte[] content) throws Exception {
        if (content == null || content.length == 0)
            return null;
        try {
            return Base64.encodeBase64String(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
	
	public static void main(String[] args) throws Exception {
		AESUtils aesUtils = new AESUtils();
		/*System.out.println("加密==" + AESUtils.encrypt("Admin@123!"));
		String  encode = AESUtils.encrypt("abc222");*/
		
		/*String encode = "OulA+RV9EFWIbEvQLUmhHfeNLMCs0bFR1weBa2UTWHM=";
		System.out.println(new String(AESUtils.decrypt(encode)));*/
		
		
		System.out.println(System.currentTimeMillis()/1000);
		
		
	}

	
	
}
