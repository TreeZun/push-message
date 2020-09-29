package com.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import cn.wps.yun.ApiException;
import cn.wps.yun.ApiResponse;
import cn.wps.yun.api.UserFilesApi;
import cn.wps.yun.model.FileContent;
import cn.wps.yun.model.FileCreateConflictBehavior;
import cn.wps.yun.model.FileCreateRequest;
import cn.wps.yun.model.FileCreateType;
import cn.wps.yun.model.Files;
import cn.wps.yun.model.Hashes;
import cn.wps.yun.model.SearchFiles;
import cn.wps.yun.model.UploadConflictBehavior;
import cn.wps.yun.model.UploadMethod;
import cn.wps.yun.model.UploadTransactionCreateRequest;
import cn.wps.yun.model.UploadTransactionPatchResponse;


public class WpsUtils {
	private static String wpsSid = ""; //用户登陆cookie:wps_sid
	private static String graphUrl; //graph host地址
	private static String appId;    //应用 appid
	private static String appSecret; //应用秘钥
	
	 /**
     * 通用的文件大小，涉及到上传的文件均取自改值
     */
    private static final Long localFileSize = 5612973L;
    /**
     * 新版本的文件大小，涉及到版本接口
     */
    private static final Long newLocalFileSize = 5611576L;
    /**
    * 通用的文件地址，涉及到上传的文件均取自改值
    */
   private static final String localFilePath = "e:/test.docx";
   /**
    * 新版本文件地址，涉及到版本接口
    */
   private static final String newLocalFilePath = "e:/test_new.docx";
	 
	 /**
	  * 获取wpsId
	  * @param graphUrl
	  * @param appId
	  * @param appSecret
	  * @return
	  */
	public static String  getSid(String wpsSidUrl,String url,String aId,String aSecret,String loginName){
		graphUrl = url;
		appId = aId;
		appSecret = aSecret;
		try {
	  CloseableHttpClient httpclient =  HttpClientUtils.acceptsUntrustedCertsHttpClient();
      HttpPost httpPost = new HttpPost(wpsSidUrl + "/login/getwpssid");
      httpPost.setConfig(RequestConfig.custom()
              .setConnectionRequestTimeout(60000)
              .setConnectTimeout(60000)
              .setSocketTimeout(60000)
              .build());
      httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");

      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put("appkey", "apptest");
      paramMap.put("thirdid", AESUtils.encrypt(loginName + "|" + System.currentTimeMillis()/1000+10));

      String formparams = JSON.toJSONString(paramMap);
      StringEntity param = new StringEntity(formparams, "UTF-8");
      //通过setEntity()设置参数给post
      httpPost.setEntity(param);

      CloseableHttpResponse req = httpclient.execute(httpPost);
      if (req.getStatusLine().getStatusCode() == 200){
    	String str =  EntityUtils.toString(req.getEntity());
    	
    	 Map map = (Map) JSON.parse(str);
    	 if(map.containsKey("wps_sid")){
    		 wpsSid = String.valueOf(map.get("wps_sid"));
    		 return wpsSid;
    	 }
      }else{
    	  return null;  
      }
      
		}catch (Exception e){
//            System.out.println(e.getResponseBody());
            e.printStackTrace();
        }
		return null;
	}  
	
	/**
	 * 上传文件至个人云盘
	 * @param folder
	 * @param filePath
	 */
	public static Object userCreateUploadTransaction(String folder,String filePath)  {
		UserFilesApi api = new UserFilesApi(
				graphUrl,
	            //appid
				appId,
	            //appsecret
				appSecret,
	            //重定向地址
	            "http://www.baidu.com",
	            //权限列表scope
	            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
	            //用户登陆cookie:wps_sid
	            wpsSid
	    );
		
        try {
        	String volume = "workspace";
        	String file = "root";
        	if(StringUtils.isNoneEmpty(folder)){
        		file = folder;
        	}
        	File file1 = new File(filePath);
            String fileName = file1.getName();
            String md5=EncryptionTool.getMD5(file1);
            String sha1 = EncryptionTool.getFileSha1(file1);
            long length = file1.length();
   
		   UploadTransactionCreateRequest body=new UploadTransactionCreateRequest(); 
		   body.setFileName(fileName);
		   Hashes fileHashes= new Hashes();
		   fileHashes.setMd5(md5);
		   fileHashes.setSha1(sha1);
		   
		   body.setFileHashes(fileHashes);
		   body.setFilePath(filePath);
		   body.setFileSize(length);
		   body.setUploadMethod(UploadMethod.POST);
		   body.setFileNameConflictBehavior(UploadConflictBehavior.RENAME);
		   UploadTransactionPatchResponse result = api.userCreateUploadTransaction(volume, file, body);
   
          System.out.println(result);
         return  JSON.toJSON(result);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return null;
        }
    }
	  
	  /**
	   * 获得文件信息
	   * @throws ApiException
	   */
	  public static cn.wps.yun.model.File userGetFile(String fileId)  {
		  UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	        try {
	            cn.wps.yun.model.File response = api.userGetFile("workspace", fileId);
	            System.out.println(response.toString());
	            return response;
	        }catch (Exception e){
	            e.getStackTrace();
	            return null;
	        }

	    }
	  
	  
	  /**
	     * 获取文件内容地址 (下载)
	     * <p>
	     * 获取文件内容地址 (下载)
	     *
	     * @throws ApiException if the Api call fails
	     */
	    public static String userGetFileContent(String fileId) throws ApiException {
	    	UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	    	 
	        try {
	            String format = null;
	            FileContent response = api.userGetFileContent("workspace", fileId, format);
	            System.out.println(response.toString());
                if(StringUtils.isNotEmpty(response.getUrl())){
                	 System.out.println(response.getUrl());
                	return response.getUrl();
                }
	           // Assert.assertNotNull(response.getUrl());
	        }catch (ApiException e){
	            System.out.println(e.getResponseBody());
	        }
	        
	        return "";

	    }
	    
	    
	    /**
	     * 获取文件列表
	     * <p>
	     * 获取文件列表
	     *
	     * @throws ApiException if the Api call fails
	     */
	    public static List<cn.wps.yun.model.File> userGetFiles(String folder) {
	    	UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	    	 try {
	        String volume = "workspace";
	        Integer offset = null;
	        Integer limit = null;
	        String file = "root";
        	if(StringUtils.isNoneEmpty(folder)){
        		file = folder;
        	}
	        Files response = api.userGetFiles(volume, file, offset, limit);
	        System.out.println(response.toString());
	        if(null != response.getValue() && response.getValue().size() > 0){
	         // return   JSON.toJSONString(response.getValue());
	        	return response.getValue();
	        }
	        
	    	 }catch (ApiException e){
		            System.out.println(e.getResponseBody());
		            return null;
		      }
	       return null;
	    }
	    
	    /**
	     * 创建文件夹
	     * <p>pFolder  父级文件夹id
	     * folderName  文件名称
	     * 创建文件夹
	     *
	     * @throws ApiException if the Api call fails
	     */
	    public static Object userCreateFile(String pFolder,String folderName) throws ApiException {
	    	UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	    try {
	        String volume = "workspace";
	        String file = pFolder;
	        FileCreateRequest body = new FileCreateRequest();
	        body.setFileName(folderName);
	        body.setFileNameConflictBehavior(FileCreateConflictBehavior.RENAME);
	        body.setFileType(FileCreateType.FOLDER);
	        
	        cn.wps.yun.model.File response = api.userCreateFile(volume, file, body);

	        System.out.println(response.toString());
	        return JSON.toJSON(response);
		    }catch (ApiException e){
	            System.out.println(e.getResponseBody());
	            return null;
	      }
	    }
	    
	    
	    /**
	     * 高级检索
	     * <p>
	     * 高级检索
	     *
	     * @throws ApiException if the Api call fails
	     */
	    public static List userAdvSearch(String beginTime,String endTime,String name) throws ApiException {
	    	UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	        try {
	            String volume = "workspace";
	            /*String begin = "1580524906";
	            String end = "1599554057";
	            String name = "123";*/
	            String body = null;
	            String type = null;
	            Integer offset = null;
	            Integer limit = null;
	            SearchFiles response = api.userAdvSearch(volume, beginTime, endTime, name, body, type, offset, limit);
	            if(null != response.getValue()){
	            	 return  response.getValue();
	            }else{
	            	 return null;	
	            }
	           
	        }catch (ApiException e){
	            System.out.println(e.getResponseBody());
	            e.printStackTrace();
	            return null;
	        }

	    }
	    
	    
	    /**
	     * 删除文件
	     * <p>
	     *
	     * @throws ApiException if the Api call fails
	     */
	    public static void userDeleteFile(String fileId) throws ApiException {
	    	UserFilesApi api = new UserFilesApi(
					graphUrl,
		            //appid
					appId,
		            //appsecret
					appSecret,
		            //重定向地址
		            "http://www.baidu.com",
		            //权限列表scope
		            "User.Search Admin.Search User.Files.ReadWrite User.Files.Read",
		            //用户登陆cookie:wps_sid
		            wpsSid
		    );
	        try {
	        	 String volume = "workspace";
	             ApiResponse<Void> response = api.userDeleteFile(volume, fileId);

	             System.out.println(response.getStatusCode());
	           
	        }catch (ApiException e){
	            System.out.println(e.getResponseBody());
	            e.printStackTrace();
	        }

	    }

	    

}
