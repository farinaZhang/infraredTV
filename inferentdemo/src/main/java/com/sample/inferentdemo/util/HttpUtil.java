package com.sample.inferentdemo.util;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import ai.olami.aiCloudService.sdk.utils.FileUtil;
import ai.olami.aiCloudService.sdk.utils.HttpsSSLSocketFactory;

public class HttpUtil {
	protected static final int CONNECT_TIMEOUT = 5000;
	protected static final int SO_TIMEOUT = 20000;
	
	protected static final String MULTIPART_FORM_DATA = "multipart/form-data";
	protected static final String TWOHYPHENS = "--";
	protected static final String BOUNDARY = getBoundry();
	protected static final String END_BOUNDARY = TWOHYPHENS + BOUNDARY + TWOHYPHENS;
	protected static final String LINEEND = "\r\n";
	
	public static String sendPostCommand(Context context, String url, List<NameValuePair> lstValue)
	{
		String result = null;
		HttpClient httpClient = null;
		HttpResponse response = null;

		try {
			HttpPost httpRequest = new HttpPost(url);
			if(url.toLowerCase().startsWith("https"))
			{
				httpClient = HttpsSSLSocketFactory.createMyHttpClient(CONNECT_TIMEOUT, SO_TIMEOUT);
				httpRequest.setEntity(new UrlEncodedFormEntity(lstValue, HTTP.UTF_8));
			}
			else
			{
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
				httpClient = new DefaultHttpClient(httpParameters);
			}

			response = httpClient.execute(httpRequest);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public static String sendMultiPartPostCommand(Context context, String url, List<NameValuePair> lstValue, Map<String, File> files) {
		String result = null;
		HttpClient httpClient = null;
		HttpUriRequest request = null;
		HttpResponse response = null;
		ByteArrayOutputStream bos = null;

		try {
			HttpPost httpRequest = new HttpPost(url);
			request = httpRequest;
			if (url.toLowerCase().startsWith("https")) {
				httpClient = HttpsSSLSocketFactory.createMyHttpClient(CONNECT_TIMEOUT, SO_TIMEOUT);
			} else {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
				httpClient = new DefaultHttpClient(httpParameters);
			}
			httpRequest.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			
			byte[] data = null;
			bos = new ByteArrayOutputStream();
			contentToUpload(bos, lstValue, files);
			data = bos.toByteArray();
			bos.close();
			ByteArrayEntity formEntity = new ByteArrayEntity(data);
			httpRequest.setEntity(formEntity);

			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static void contentToUpload(OutputStream out, List<NameValuePair> lstValue, Map<String, File> files) {
		try {
			for (int i = lstValue.size() - 1 ; i >= 0 ; i--) {
				StringBuilder temp = new StringBuilder();
				temp.append(TWOHYPHENS + BOUNDARY).append(LINEEND);
				temp.append("content-disposition: form-data; name=\"");
				temp.append(lstValue.get(i).getName());
				temp.append("\"" + LINEEND + LINEEND);
				temp.append(lstValue.get(i).getValue()).append(LINEEND);
				byte[] res = temp.toString().getBytes();
				out.write(res);
				
				if (!files.containsKey(lstValue.get(i).getName())) {
					lstValue.remove(i);
				}
			}

			// process files
			for (int i = 0; i < lstValue.size(); i++) {
				if (files.containsKey(lstValue.get(i).getName())) {
					StringBuilder temp = new StringBuilder();

					temp.append(TWOHYPHENS + BOUNDARY).append(LINEEND);
					temp.append("Content-Disposition: form-data; name=\"" + lstValue.get(i).getName() + "\"; filename=\"");
					temp.append(lstValue.get(i).getValue());
					temp.append("\"" + LINEEND);
					String mime = FileUtil.getMimeType(files.get(lstValue.get(i).getName()).getName());
					if(mime != null){
						temp.append("Content-Type: ").append(mime).append(LINEEND);
					}else{
						// Log.e("HttpUtil", "mime type is null.");
					}
					temp.append(LINEEND);

					byte[] res = temp.toString().getBytes();
					FileInputStream input = null;
					out.write(res);
					input = new FileInputStream(files.get(lstValue.get(i).getName()));
					byte[] buffer = new byte[1024 * 50];
					while (true) {
						int count = input.read(buffer);
						if (count == -1) {
							break;
						}
						out.write(buffer, 0, count);
					}
					out.write(LINEEND.getBytes());
					out.write((LINEEND + END_BOUNDARY).getBytes());

					if (null != input) {
						try {
							input.close();
						} catch (IOException e) {
							e.getStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String sendGetCommand(Context context, String url)
	{
		return sendGetCommand(context, url, null);
	}
	
	
	public static String sendGetCommand(Context context, String url, String retDataType)
	{
		String result = null;
		HttpClient httpClient = null;
		HttpResponse response = null;
		
		
		try {
			HttpGet httpRequest = new HttpGet(url);
			
			if(url.toLowerCase().startsWith("https"))
			{
				httpClient = HttpsSSLSocketFactory.createMyHttpClient(CONNECT_TIMEOUT, SO_TIMEOUT);
			}
			else
			{
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
				httpClient = new DefaultHttpClient(httpParameters);
			}

			response = httpClient.execute(httpRequest);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				if(retDataType != null)
					result = EntityUtils.toString(response.getEntity(), retDataType);
				else
					result = EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 产生11位的boundary
	 */
	static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

}