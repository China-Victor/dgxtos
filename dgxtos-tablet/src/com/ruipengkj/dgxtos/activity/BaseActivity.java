package com.ruipengkj.dgxtos.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.ruipengkj.dgxtos.ConstantValue;
import com.ruipengkj.dgxtos.inter.ScreenStateListener;
import com.ruipengkj.dgxtos.receiver.ScreenObserver;
import com.ruipengkj.dgxtos.service.AdvertisementAdmin;
/**
 * 应用中所有Activity的基类<br>
 * 
 * @author ruipengVictor
 * 
 */
public abstract class BaseActivity extends Activity implements OnTouchListener {

	private static final String TAG = "BaseActivity";

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	private static final boolean D = ConstantValue.DEBUG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AdvertisementAdmin.newInstance(this);
		
		AdvertisementAdmin.start(); // 开启广告

		/*
		 * 让系统将home键事件传给onKeyDown()方法
		 */
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码

		/*
		 * 双向横屏
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); 
		//单向横屏 
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		/*
		 * 禁用锁屏应用
		 */
//		disableLockScreen();  //在这里调用会导致每个开启的Activity都会注册一个广播接收者
	}

	/**
	 * 禁用系统锁屏应用
	 */
	protected void disableLockScreen() {
		ScreenObserver observer = new ScreenObserver(this);
		observer.requestScreenStateUpdate(new ScreenStateListener() {
			private KeyguardManager mKeyguardManager = null;
			@SuppressWarnings("deprecation")
			private KeyguardManager.KeyguardLock mKeyguardLock = null;

			@Override
			public void onScreenOn() {
				Log.i(TAG, "开启显示屏");
				displaySysLock();
			}

			@Override
			public void onScreenOff() {
				Log.i(TAG, "哈哈  广告时间到了！！");
				displaySysLock();
			}

			/**
			 * 屏蔽系统的锁屏应用
			 */
			@SuppressWarnings("deprecation")
			private void displaySysLock() {
				mKeyguardManager = (KeyguardManager) getApplicationContext()
						.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("FxLock");
				// 屏蔽手机内置的锁屏
				mKeyguardLock.disableKeyguard();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "keyCode" + keyCode);
		// 更新操作时间
		AdvertisementAdmin.updateTime();
		// 拦截电源键
		if (keyCode == KeyEvent.KEYCODE_POWER) {
			Toast.makeText(this, "电源键被禁不了", 0).show();
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN: // 屏蔽音量键下
		case KeyEvent.KEYCODE_VOLUME_UP: // 屏蔽音量键上
		case KeyEvent.KEYCODE_HOME: // 屏蔽home键
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 在Activity内每次触屏都会触发的函数 在Activity外的控件需要注册OnTouchListener事件
	 */
	@Override
	public void onUserInteraction() {
		Log.i(TAG, " onUserInteraction ");
		// 更新时间
		AdvertisementAdmin.updateTime();
		super.onUserInteraction();
	}

	/**
	 * Activity外的界面的空件回调函数<br>
	 * 如：dialog里的控件需要注册该事件
	 * 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 更新时间
		AdvertisementAdmin.updateTime();
		if(D)Log.i(TAG, "onTouch --> " );
		return false;
	}
}
