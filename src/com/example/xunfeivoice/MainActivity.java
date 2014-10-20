package com.example.xunfeivoice;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static final String TAG = "MainActivity";
	
	
	private TextView textView;
	
	private Toast mToast;
	
	private SpeechRecognizer speechRecognizer;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView) findViewById(R.id.textView1);
		
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);

		
		initSpeechRecognizer();

		
	}
	
    /**
     * 初始化
     */
    private void initSpeechRecognizer() {
		speechRecognizer = SpeechRecognizer.createRecognizer(this,initListener);
		setParameter();
	}
    
    /**
     * :SpeechRecognizer初始化监听
     */
    InitListener initListener=new InitListener() {
		
		@Override
		public void onInit(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	/**
     * 给speechRecognizer设置参数
     * setParameter(java.lang.String key, java.lang.String value) 
                      识别参数设置，key可设置的参数有：
		SpeechConstant.NET_TIMEOUT:网络连接超时时间 
		SpeechConstant.KEY_SPEECH_TIMEOUT:语音输入超时时间 
		SpeechConstant.LANGUAGE:语言
		SpeechConstant.ACCENT:语言区域
		SpeechConstant.DOMAIN:应用领域
		SpeechConstant.CLOUD_GRAMMAR:云端语法ID
		SpeechConstant.AUDIO_SOURCE:音频源
		SpeechConstant.VAD_BOS:前端点超时
		SpeechConstant.VAD_EOS:后端点超时
		SpeechConstant.SAMPLE_RATE:识别采样率
		SpeechConstant.ASR_NBEST:句子级多候选
		SpeechConstant.ASR_WBEST:词级多候选
		SpeechConstant.ASR_PTT:设置是否有标点符号
		SpeechConstant.RESULT_TYPE:识别结果类型
		SpeechConstant.SEARCH_AREA:地图搜索设置区域
		SpeechConstant.ASR_AUDIO_PATH识别录音保存路径
     */
	private void setParameter() {
		//设置语言
		speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		
		// 设置语音前端点
		speechRecognizer.setParameter(SpeechConstant.VAD_BOS,  "4000");
		// 设置语音后端点
		speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号
		speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1://开始
             textView.setText("");
			Log.i(TAG,"开始");
			
			int code=speechRecognizer.startListening(recognizerListener);
			if(code != ErrorCode.SUCCESS){
				Log.i(TAG,"听写失败,错误码：" + code);
			}else {
				Log.i(TAG,"请开始说话");
			}
			break;
		case R.id.button2://停止
			speechRecognizer.stopListening();
			break;
        case R.id.button3://取消
			speechRecognizer.cancel();
			break;
		default:
			break;
		}
	}

	RecognizerListener recognizerListener=new RecognizerListener() {
		
		@Override
		public void onVolumeChanged(int arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			/*
			 *  返回识别结果,结果可能为空，请增加判空处理 说明：
			 *  1、SpeechRecognizer采用边录音边发送的方式，可能会多次返回结果
			 */
			
			String text = JsonParser.parseIatResult(results.getResultString());
			textView.append(text);
			
			if(isLast) {
				//TODO 最后的结果
			}
						
		}
		
		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onEndOfSpeech() {
			/**
			 * 录音自动停止回调 说明：
               1、内部集成了端点检测功能，当用户一定时间内不说话，
                                                   默认为用户已经不需要再录入语音，会自动调用此回调函数， 并停止当前录音。
			 */
			
			Toast.makeText(MainActivity.this,"结束说话",Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onBeginOfSpeech() {
			//  录音机开启后调用
			Toast.makeText(MainActivity.this,"开始说话",Toast.LENGTH_SHORT).show();
		}
	};

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		speechRecognizer.destroy();
		super.onDestroy();
	}
	


}
