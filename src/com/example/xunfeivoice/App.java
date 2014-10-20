package com.example.xunfeivoice;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

public class App extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		SpeechUtility.createUtility(this, SpeechConstant.APPID+"=54429d66");
		super.onCreate();
	}
	
	
}
