package com.sample.inferentdemo.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;


public class ImageLoaderUtil {

	private static HashMap<String, SoftReference<Bitmap>> mImageCache = new HashMap<String, SoftReference<Bitmap>>();
	private static CallBackInterface callbackObject = null;
	public static int downloadCount = 0;

	public interface CallBackInterface {
		 void callBack(Object obj, int param1, int param2) ;
	}

	/**
	 *   Load image
	 * @param imageview 		  Not null 
	 * @param url  				  Not null 
	 * @param cacheLocalDir   Default image cache directory
	 * @param defImg 			  Setting default image,Loader default image when result is null 
	 * @param commpressWidth  >50
	 */
	public static void loadImageAsync(ImageView imageview, String url,
									  String cacheLocalDir, Drawable defImg, int commpressWidth) {
		if(imageview !=null && url !=null ){
			Bitmap bmp = null;
			SoftReference<Bitmap> reference = mImageCache.get(url);
			if(reference !=null)
				bmp = reference.get(); 

			if(bmp == null)
			{
				ImageAsyncTask task = new ImageAsyncTask();
				try
				{
					task.execute(imageview, url, cacheLocalDir, defImg,commpressWidth);
				}catch(Exception e)
				{
					// there maybe java.util.concurrent.RejectedExecutionException
					e.printStackTrace();
				}
			}
			else
			{
				imageview.setImageBitmap(bmp);
				if (callbackObject != null) {
					callbackObject.callBack(bmp, 0, 0);					
				}
			}
		}
	}
	
	public static void loadImageAsync(ImageView imageview, String url,
									  String cacheLocalDir, Drawable defImg, int commpressWidth, CallBackInterface callback) {
		callbackObject = callback;
		loadImageAsync(imageview, url, cacheLocalDir, defImg, commpressWidth);
	}
	
	public static Bitmap downloadFileFromUrl(String remotePath, String localPath, int minSize)
	{
		Bitmap bmp = null;
		FileOutputStream fsOut = null;
		InputStream is = null;
		
		URL url = null;
		try {
			url = new URL(remotePath);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		
		if(url != null)
		{
			//String localPath = cacheLocalPathDir + MD5(remotePath);
			File flLocal = new File(localPath);
			if(!flLocal.exists())
			{
				File parent = flLocal.getParentFile();
				if(parent != null)
					parent.mkdirs();
			}
			
			try
			{
				is = url.openStream();
				if(is != null)
				{
					fsOut = new FileOutputStream(flLocal);
				
					byte[] buf = new byte[10240];
					int len = -1;
					while((len  = is.read(buf)) != -1)
					{
						if(len > 0)
							fsOut.write(buf, 0, len);
					}
				}
			}
			catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
					
			if(fsOut != null)
			{
				try
				{
					fsOut.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			opt.outHeight = 0;
			opt.outWidth = 0;
			BitmapFactory.decodeFile(localPath, opt);
			if(opt.outWidth > 0 && opt.outHeight > 0)
			{
				int sampleSize = 1;
				if(opt.outWidth >= opt.outHeight && opt.outHeight > minSize)
				{
					sampleSize = opt.outHeight / minSize;
				}
				else if(opt.outWidth < opt.outHeight && opt.outWidth > minSize)
				{
					sampleSize = opt.outWidth / minSize;
				}
				
				opt = new BitmapFactory.Options();
				opt.inSampleSize = sampleSize;
				bmp = BitmapFactory.decodeFile(localPath, opt);
				if(bmp != null)
				{
					mImageCache.put(remotePath, new SoftReference<Bitmap>(bmp));
					try
					{
						fsOut = new FileOutputStream(flLocal);
						bmp.compress(CompressFormat.PNG, 90, fsOut);
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					if(fsOut != null)
					{
						try
						{
							fsOut.close();
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		return bmp;
	}

	public static class ImageAsyncTask extends
			AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String url = null;
		private String cacheLocalPathDir = null;
		private Drawable defaultImage = null;
		//private int minCompressWidth = 50;
		private int commpressWidth = 50;
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			if (params.length > 1)
				url = (String) params[1];
			if (params.length > 2)
				cacheLocalPathDir = (String) params[2];
			if (params.length > 3)
				defaultImage = (Drawable) params[3];
			if(params.length > 4)
				commpressWidth =(Integer) params[4];
			Bitmap bmp = null;
			
			if (cacheLocalPathDir != null && url !=null) {
				File file = new File(cacheLocalPathDir + MD5(url));
				if (file.exists()) {
					try {
						FileInputStream fs = new FileInputStream(file);
						bmp = BitmapFactory.decodeStream(fs);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}else if (url !=null ){
				SoftReference<Bitmap> reference = mImageCache.get(url);
				if(reference !=null)
				   bmp = reference.get(); 
			}
			if (bmp == null && url !=null) {
				if(cacheLocalPathDir == null)
				{
					try {
						bmp = BitmapFactory.decodeStream(new URL(url).openStream());
						if(bmp != null)
							mImageCache.put(url, new SoftReference<Bitmap>(bmp));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
				{
					bmp = downloadFileFromUrl(url, cacheLocalPathDir + MD5(url), commpressWidth);
				}
			}
			return bmp;
		}
		
		protected void onPostExecute(Bitmap result) {
			if (result != null){
				imageView.setImageBitmap(result);
				if (callbackObject != null) {
					callbackObject.callBack(result, 0 , 0);
				}
			} else if (defaultImage != null){
				imageView.setImageDrawable(defaultImage);
			}
			result = null;
		}
	}

	/**
	 * MD5加密字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static void saveBitmap(String path, Bitmap mBitmap) {
		File f = new File(path);
		if(!f.exists())
			createDipPath(path);
		try {
			f.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveBitmap(String path, Bitmap mBitmap, String imgType) {
		File f = new File(path);
		if(!f.exists())
			createDipPath(path);
		try {
			f.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(imgType.equals("png")||imgType.equals("PNG"))
		  mBitmap.compress(CompressFormat.PNG, 100, fOut);
		else	
		  mBitmap.compress(CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件路径 递归创建文件
	 * 
	 * @param file
	 */
	public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);

		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public static void setCallBackInterfaceFree()
	{
		if(callbackObject != null)
			callbackObject = null;
	}	
}
