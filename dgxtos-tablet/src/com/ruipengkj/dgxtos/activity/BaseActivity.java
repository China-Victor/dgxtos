package com.ruipengkj.dgxtos.activity;

import com.ruipengkj.dgxtos.inter.ScreenStateListener;
import com.ruipengkj.dgxtos.receiver.ScreenObserver;
import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * Ӧ��������Activity�Ļ���<br>
 * 
 * @author ruipengVictor
 * 
 */
public abstract class BaseActivity extends Activity implements OnTouchListener {

	private static final String TAG = "BaseActivity";

	private AdvertisementAdmin advertisementAdmin;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ������������
		advertisementAdmin = AdvertisementAdmin.newInstance(this);
		advertisementAdmin.start(); // �������

		/*
		 * ��ϵͳ��home���¼�����onKeyDown()����
		 */
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// �ؼ�����

		/*
		 * ��������Ӧ��
		 */
		disableLockScreen();
	}

	/**
	 * ����ϵͳ����Ӧ��
	 */
	private void disableLockScreen() {
		ScreenObserver observer = new ScreenObserver(this);
		observer.requestScreenStateUpdate(new ScreenStateListener() {
			private KeyguardManager mKeyguardManager = null;
			@SuppressWarnings("deprecation")
			private KeyguardManager.KeyguardLock mKeyguardLock = null;

			@Override
			public void onScreenOn() {
				Log.i(TAG, "������ʾ��");
				displaySysLock();
			}

			@Override
			public void onScreenOff() {
				Log.i(TAG, "����  ���ʱ�䵽�ˣ���");
				displaySysLock();
			}

			/**
			 * ����ϵͳ������Ӧ��
			 */
			@SuppressWarnings("deprecation")
			private void displaySysLock() {
				mKeyguardManager = (KeyguardManager) getApplicationContext()
						.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("FxLock");
				// �����ֻ����õ�����
				mKeyguardLock.disableKeyguard();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "keyCode" + keyCode);
		// ���²���ʱ��
		AdvertisementAdmin.updateTime();
		// ���ص�Դ��
		if (keyCode == KeyEvent.KEYCODE_POWER) {
			Toast.makeText(this, "��Դ����������", 0).show();
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN: // ������������
		case KeyEvent.KEYCODE_VOLUME_UP: // ������������
		case KeyEvent.KEYCODE_HOME: // ����home��
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Activity��ÿ�δ������ᴥ���ĺ��� ��Activity��Ŀؼ���Ҫע��OnTouchListener�¼�
	 */
	@Override
	public void onUserInteraction() {
		Log.i(TAG, " onUserInteraction ");
		// ����ʱ��
		AdvertisementAdmin.updateTime();
		super.onUserInteraction();
	}

	/**
	 * Activity��Ľ���Ŀռ��ص�����<br>
	 * �磺dialog��Ŀؼ���Ҫע����¼�
	 * 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ����ʱ��
		AdvertisementAdmin.updateTime();
		return false;
	}
}
