package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class EncryptionTool {
    public static String getFileSha1(File file) {
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];

            int len = 0;
            while ((len = in.read(buffer)) > 0)
            {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0)
            {
                for (int i = 0; i < length; i++)
                {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
        return "";
    }
    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
