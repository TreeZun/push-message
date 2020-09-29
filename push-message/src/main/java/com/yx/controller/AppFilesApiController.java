package com.yx.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.utils.FormatDateUtil;
import com.utils.HttpClientUtils;
import com.utils.SslUtils;
import com.utils.WpsUtils;
import com.yx.entity.ErrCode;
import com.yx.entity.RestResult;

import cn.wps.yun.ApiException;
import cn.wps.yun.ApiResponse;
import cn.wps.yun.api.AppFilesApi;
import cn.wps.yun.model.FileContent;
import cn.wps.yun.model.FileEditor;
import cn.wps.yun.model.FilePreview;
import cn.wps.yun.model.UploadConflictBehavior;
import cn.wps.yun.model.UploadMethod;
import cn.wps.yun.model.UploadTransactionCreateRequest;
import cn.wps.yun.model.UploadTransactionPatchResponse;
import cn.wps.yun.model.WebofficeEditorGetUrlRequest;

@Controller
@RequestMapping(value = "/app")
public class AppFilesApiController {
	@Value("${app.graph.prefix}")
	private String graphUrl;

	@Value("${app.id}")
	private String appId;

	@Value("${app.secret}")
	private String appSecret;

	@Value("${app.wpsSidUrl}")
	private String wpsSidUrl;

	@Value("${upFilePath}")
	private String upFilePath;

	/**
	 * 上传文件到云盘(用户身份)
	 * 
	 * @param request
	 * @param loginName
	 * @param fileName
	 * @param fileUrl
	 * @param folder
	 * @return
	 */
	@RequestMapping(value = "/uploadCloudDisk", method = RequestMethod.POST)
	@ResponseBody
	public RestResult uploadCloudDisk(HttpServletRequest request, String loginName, String fileName, String fileUrl,
			String folder) {
		RestResult restResult = new RestResult();
		FileOutputStream fos = null;
		InputStream inputStream = null;
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileUrl)
				|| StringUtils.isEmpty(folder)) {
			restResult.setResult(ErrCode.PARAMETER_WRONG);
			return restResult;
		}
		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}

			// SSLContext sslcontext = SSLContext.getInstance("SSL");
			// sslcontext.init(null, new TrustManager[] { new
			// MyX509TrustManager() },null);
			//

			URL httpurl = new URL(encode(fileUrl, "UTF-8"));
			SslUtils.ignoreSsl();
			URLConnection conn = httpurl.openConnection();

			// HttpURLConnection conn = (HttpURLConnection)
			// httpurl.openConnection();
			// // httpurl.openStream();
			// HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier()
			// {
			// @Override
			// public boolean verify(String s, SSLSession sslsession) {
			// System.out.println("WARNING: Hostname is not matched for cert." +
			// s);
			// return true;
			// }
			// };
			// HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			// HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());

			inputStream = conn.getInputStream();
			// 获取自己数组
			byte[] getData = readInputStream(inputStream);

			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			String dateStr = FormatDateUtil.formatDateToString("yyyyMMdd", new Date());
			String path = File.separator + dateStr + File.separator ;// 相对路径

			String rootPath = upFilePath + path; // 绝对路径
			File localFile = new File(rootPath);
			if (!localFile.exists()) {
				// 先得到文件的上级目录，并创建上级目录，在创建文件
				//localFile.getParentFile().mkdir();
				localFile.mkdirs();
				// 创建文件
				rootPath = rootPath +fileName.trim();
				System.out.println(rootPath);
				File file = new File(rootPath);
				file.createNewFile();
			}else{
				rootPath = rootPath +fileName.trim();
			}

			File file = new File(rootPath);
			fos = new FileOutputStream(file);
			fos.write(getData);
			// 上传文件至个人云盘
			Object relust = WpsUtils.userCreateUploadTransaction(folder, rootPath);
			if (null != relust) {
				restResult.setData(relust);
				restResult.setResult(ErrCode.Success);
				file.delete();
				return restResult;
			} else {
				restResult.setResult(ErrCode.failed);
				return restResult;
			}

		} catch (Exception e) {
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		} finally {
			try {
				if (fos != null) {
					fos.close();
					if (inputStream != null) {
						inputStream.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return restResult;
	}
	
	/**
	 * 获取文件信息
	 */
	@RequestMapping(value = "/userGetFile", method = RequestMethod.GET)
	@ResponseBody
	public RestResult userGetFile(HttpServletRequest request, String loginName, String fileId) {
		RestResult restResult = new RestResult();
		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}
			cn.wps.yun.model.File file = WpsUtils.userGetFile(fileId);
			if (null != file) {
				restResult.setData(file);
				return restResult;
			} else {
				restResult.setResult(ErrCode.failed);
				return restResult;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		return restResult;

	}

	/**
	 * 获取文件内容地址 (下载)
	 * <p>
	 * 获取文件内容地址 (下载)
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@RequestMapping(value = "/getUserFileUrl", method = RequestMethod.GET)
	@ResponseBody
	public RestResult userGetFileContent(HttpServletRequest request, String loginName, String fileId) {
		RestResult restResult = new RestResult();

		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}
			String url = WpsUtils.userGetFileContent(fileId);
			if (StringUtils.isNotEmpty(url)) {
				restResult.setData(url);
				return restResult;
			} else {
				restResult.setResult(ErrCode.failed);
				return restResult;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		return restResult;

	}

	/**
     * 获取文件列表
     * <p>
     * 获取文件列表
     *type 0:文件夹  1：文件
     * @throws ApiException if the Api call fails
     */
	@RequestMapping(value = "/userGetFiles", method = RequestMethod.GET)
	@ResponseBody
    public RestResult userGetFiles(HttpServletRequest request, String loginName,String folder,String type) {
    	 RestResult restResult = new RestResult();
    	 try {
    		//验证用户是否合法，合法就获取wpsSid
 			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret,loginName)) {
 				restResult.setResult(ErrCode.NO_POWER);
 				return restResult;
 			}
        @SuppressWarnings("rawtypes")
        List<cn.wps.yun.model.File> result = WpsUtils.userGetFiles(folder);
        if(null != result && result.size()>0){
        	if(StringUtils.isNotEmpty(type)){
	        	List<cn.wps.yun.model.File> list = new ArrayList<cn.wps.yun.model.File>();
	        	for(cn.wps.yun.model.File file : result){
	        		if(type.equals("0") && (file.getType().equals("FOLDER") || file.getType().getValue().equals("folder"))){
	        			list.add(file);
	        		}
	        		if(type.equals("1") && (file.getType().equals("REGULAR") || file.getType().getValue().equals("regular"))){
	        			list.add(file);
	        		}
	        	}
	        	restResult.setData(JSON.toJSON(list));
	        	return restResult;
        	}else{
        	  restResult.setData(JSON.toJSON(result));
        	return restResult;
        	}
		}else{
			restResult.setResult(ErrCode.Success);	
		}
        return restResult;
    	 }catch (Exception e){
    		 e.printStackTrace();
	         return null;
	      }
    }

	/**
	 * 创建文件夹
	 * <p>
	 * pFolder 父级文件夹id folderName 文件名称 创建文件夹
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@RequestMapping(value = "/userCreateFolder", method = RequestMethod.POST)
	@ResponseBody
	public RestResult userCreateFolder(String pFolder, String folderName, String loginName) throws ApiException {
		RestResult restResult = new RestResult();
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(pFolder) || StringUtils.isEmpty(folderName)) {
			restResult.setResult(ErrCode.PARAMETER_WRONG);
			return restResult;
		}

		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}
			Object result = WpsUtils.userCreateFile(pFolder, folderName);

			if (null !=result) {
				restResult.setData(result);
				return restResult;
			} else {
				restResult.setResult(ErrCode.failed);
			}
			return restResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 高级检索
	 * <p>
	 * 高级检索
	 *
	 */
	@RequestMapping(value = "/userAdvSearch", method = RequestMethod.GET)
	@ResponseBody
	public RestResult userAdvSearch(String beginTime, String endTime, String fileName, String loginName)
			throws ApiException {
		RestResult restResult = new RestResult();
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(fileName)) {
			restResult.setResult(ErrCode.PARAMETER_WRONG);
			return restResult;
		}

		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotEmpty(beginTime)) {
				Date date = sdf.parse(beginTime);
				beginTime = String.valueOf(date.getTime() / 1000);
			}
			if (StringUtils.isNotEmpty(endTime)) {
				Date date = sdf.parse(endTime);
				endTime = String.valueOf(date.getTime() / 1000);
			}

			@SuppressWarnings("rawtypes")
			List result = WpsUtils.userAdvSearch(beginTime, endTime, fileName);
			if (null != result && result.size() > 0) {
				restResult.setData(result);
				return restResult;
			} else {
				restResult.setResult(ErrCode.failed);
			}
			return restResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除文件或文件夹
	 * <p>
	 * pFolder 文件夹id
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@RequestMapping(value = "/userDeleteFile", method = RequestMethod.POST)
	@ResponseBody
	public RestResult userDeleteFile(String fileId, String loginName) throws ApiException {
		RestResult restResult = new RestResult();
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(fileId)) {
			restResult.setResult(ErrCode.PARAMETER_WRONG);
			return restResult;
		}
		try {
			// 验证用户是否合法，合法就获取wpsSid
			if (null == WpsUtils.getSid(wpsSidUrl, graphUrl, appId, appSecret, loginName)) {
				restResult.setResult(ErrCode.NO_POWER);
				return restResult;
			}
			WpsUtils.userDeleteFile(fileId);
			restResult.setResult(ErrCode.Success);
			return restResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ---------------------- 以上为用户对云盘的操作
	// ---------------------- 以下为应用对云盘的操作

	/**
	 * 上传文件到云盘（应用身份）
	 * 
	 * @param request
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/filesUpload", method = RequestMethod.POST)
	@ResponseBody
	public RestResult filesUpload(HttpServletRequest request, String filePath) {
		RestResult restResult = new RestResult();
		if (StringUtils.isEmpty(filePath)) {
			restResult.setResult(ErrCode.PARAMETER_WRONG);
			return restResult;
		}
		try {
			java.io.File f = new java.io.File(filePath);
			if (f.exists() && f.isFile()) {
				System.out.println(f.length());
			}
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");

			String volume = "workspace";
			String file = "root";
			UploadTransactionCreateRequest body = new UploadTransactionCreateRequest();
			body.setFileName(f.getName());
			body.setFileSize(f.length());
			body.setFileNameConflictBehavior(UploadConflictBehavior.RENAME);
			body.setUploadMethod(UploadMethod.POST);
			body.setFilePath(filePath);
			UploadTransactionPatchResponse response = api.appCreateUploadTransaction(volume, file, body);
			restResult.setData(response.getFile().getId());
			System.out.println("fileId==========" + response.toString());
		} catch (ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		}
		return restResult;
	}

	/**
	 * 获取预览地址
	 * 
	 * @param request
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = "/getFilesPreviewUrl", method = RequestMethod.GET)
	@ResponseBody
	public RestResult getFilesPreviewUrl(HttpServletRequest request, String fileId) {
		RestResult restResult = new RestResult();
		try {
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
			String volume = "workspace";
			// String file = "188784392980668416";
			String documentChallenge = null;
			String expiration = null;
			Boolean printable = null;
			String watermarkText = ""; // 水印
			String watermarkImageUrl = null;
			FilePreview response = api.appGetFilePreview(volume, fileId, documentChallenge, expiration, printable,
					printable, watermarkText, watermarkImageUrl, watermarkImageUrl, watermarkImageUrl,
					watermarkImageUrl);

			restResult.setData(response.getUrl());
			System.out.println("getFilesPreviewUrl====" + response.toString());
		} catch (ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		}
		return restResult;
	}

	/**
	 * 获取在线编辑地址
	 * 
	 * @param request
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = "/getFilesEditorUrl", method = RequestMethod.GET)
	@ResponseBody
	public RestResult getFilesEditorUrl(HttpServletRequest request, String fileId,String loginName, String userName) {
		RestResult restResult = new RestResult();
		try {
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
			String volume = "workspace";
			// String file = "188784392980668416";
			WebofficeEditorGetUrlRequest body = new WebofficeEditorGetUrlRequest();
			body.setExtUserid(loginName);
			body.setExtUsername(userName);
			body.setAccountSync("1");
			body.setExtCompanyid("0");
			body.setHistory("0");
			body.setWrite("1");
			FileEditor response = api.appGetFileEditor(volume, fileId, body);
			String url = String.format("http:%s&Apptoken=%s&wps_sid=%s", response.getUrl(), response.getApptoken(),
					response.getWpsSid());
			restResult.setData(url);
			System.out.println("getFilesEditorUrl====" + response.toString());
		} catch (ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		}
		return restResult;
	}

	// /**
	// * 获取在线编辑地址
	// *
	// * @param request
	// * @param fileId
	// * @return
	// */
	// @RequestMapping(value = "/getFilesEditorUrl", method = RequestMethod.GET)
	// @ResponseBody
	// public RestResult getFilesEditorUrl(HttpServletRequest request, String
	// fileId) {
	// RestResult restResult = new RestResult();
	// try {
	// AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
	// "App.Files.Read App.Files.ReadWrite App.Search App.Company.Read
	// App.Company.ReadWrite App.CompanyMembers.Read
	// App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite
	// App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read
	// App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
	// String volume = "workspace";
	// // String file = "188784392980668416";
	// WebofficeEditorGetUrlRequest body = new WebofficeEditorGetUrlRequest();
	// body.setExtUserid("1");
	// body.setExtUsername("1");
	// body.setAccountSync("1");
	// body.setExtCompanyid("0");
	// body.setHistory("0");
	// body.setWrite("1");
	// FileEditor response = api.appGetFileEditor(volume, fileId, body);
	// String url = String.format("http:%s&Apptoken=%s&wps_sid=%s",
	// response.getUrl(), response.getApptoken(),
	// response.getWpsSid());
	// restResult.setData(url);
	// System.out.println("getFilesEditorUrl====" + response.toString());
	// } catch (ApiException e) {
	// System.out.println(e.getResponseBody());
	// e.printStackTrace();
	// restResult.setResult(ErrCode.USER_DEVICE_WRONG);
	// }
	// return restResult;
	// }

	/**
	 * 获取文件下载地址
	 * 
	 * @param request
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = "/getFileDownloadUrl", method = RequestMethod.GET)
	@ResponseBody
	public RestResult getFileDownloadUrl(HttpServletRequest request, String fileId) throws ApiException {
		RestResult restResult = new RestResult();
		try {
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
			String volume = "workspace";

			FileContent response = api.appGetFileVersionContent(volume, fileId, fileId);
			restResult.setData(response.getUrl());
			System.out.println("getFileDownloadUrl====" + response.toString());
		} catch (ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		}
		return restResult;
	}

	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public RestResult deleteFile(HttpServletRequest request, String fileId) {
		RestResult restResult = new RestResult();
		try {
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
			String volume = "workspace";
			ApiResponse<Void> response = api.appDeleteFile(volume, fileId);
			restResult.setData(response);
			System.out.println("删除文件：===" + response.toString());
		} catch (ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		}
		return restResult;
	}

	/**
	 * 获取文件下载地址
	 * 
	 * @throws ApiException
	 */
	public void appGetFileContentTest(HttpServletRequest request, HttpServletResponse re, String fileId)
			throws ApiException {
		RestResult restResult = new RestResult();
		InputStream inputStream = null;
		BufferedInputStream bis = null;
		BufferedOutputStream output = null;
		try {
			AppFilesApi api = new AppFilesApi(graphUrl, appId, appSecret,
					"App.Files.Read App.Files.ReadWrite App.Search App.Company.Read App.Company.ReadWrite App.CompanyMembers.Read App.CompanyMembers.ReadWrite App.Depts.Read App.Depts.ReadWrite App.DeptMembers.Read App.DeptMembers.ReadWrite App.Groups.Read App.Groups.ReadWrite App.GroupMembers.Read App.GroupMembers.ReadWrite");
			String volume = "workspace";
			// String file = "188784392980668416";
			String format = null;
			FileContent response = api.appGetFileContent(volume, fileId, format);
			if (response != null) {
				if (response.getUrl() != null) {

					CloseableHttpClient httpclient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
					// 处理path

					HttpPost httpPost = new HttpPost(response.getUrl());
					httpPost.setConfig(RequestConfig.custom().setConnectionRequestTimeout(60000)
							.setConnectTimeout(60000).setSocketTimeout(60000).build());
					httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");

					Map<String, String> paramMap = new HashMap<String, String>();
					String formparams = JSON.toJSONString(paramMap);
					StringEntity param = new StringEntity(formparams, "UTF-8");
					// 通过setEntity()设置参数给post
					httpPost.setEntity(param);

					CloseableHttpResponse req = httpclient.execute(httpPost);

					if (Objects.nonNull(req.getStatusLine())
							&& "200".equals(Integer.toString(req.getStatusLine().getStatusCode()))) {
						re.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						re.addHeader("charset", "utf-8");
						re.addHeader("Pragma", "no-cache");
						re.setContentType("application/octet-stream;charset=utf-8");
						String encodeName = URLEncoder.encode("文件名", StandardCharsets.UTF_8.toString());

						output = new BufferedOutputStream(re.getOutputStream());
						// 作为附件下载
						re.setHeader("Content-Disposition", "attachment;filename=" + encodeName);
						// 获取自己数组
						byte[] bs = readInputStream(inputStream);
						output.write(bs);
						re.flushBuffer();
					}

				}
			}
			System.out.println(response);
		} catch (Exception e) {
			// System.out.println(e.getResponseBody());
			e.printStackTrace();
			restResult.setResult(ErrCode.USER_DEVICE_WRONG);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	private String encode(String str, String charset) throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}

}
