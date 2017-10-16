package com.sample.inferentdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.sample.inferentdemo.handler.AppCrashHandler;
import com.sample.inferentdemo.imain.CommunicationAssist;

public class MyApplication extends Application{
	 private static MyApplication myApplication;
	   private static CommunicationAssist mActivityToServiceListener;
	   private static CommunicationAssist mServiceToActivityListener;
	   public static Activity assitActivity;
	   
	   public static MyApplication getInstance()
	   {
		   return myApplication;
	   }
	   
	   
	   public Context getContext(){
		   return getApplicationContext();
	   } 
	   
	   public void setActivityToServiceListener(CommunicationAssist listener)
	   {
		   mActivityToServiceListener = listener;
	   }
	   
	   public void setServiceToActivityListener(CommunicationAssist listener)
	   {
		   mServiceToActivityListener = listener;
	   }
	   
	   public  CommunicationAssist getActivityToServiceListener()
	   {
		   return mActivityToServiceListener;
	   }
	   public  CommunicationAssist getServiceToActivityListener()
	   {
		   return mServiceToActivityListener;
	   }


	   
	   @Override
		public void onCreate() {
		   super.onCreate();
		   myApplication = this;
		   AppCrashHandler handler = AppCrashHandler.getInstance();
		   handler.initUcExceptionHandler(this);
	    }
}
