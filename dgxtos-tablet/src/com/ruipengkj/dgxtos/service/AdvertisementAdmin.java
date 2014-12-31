package com.ruipengkj.dgxtos.service;



import java.util.Timer;
import java.util.TimerTask;

import com.ruipengkj.dgxtos.activity.AdvertisementActivity;
import com.ruipengkj.dgxtos.inter.OnVadertisementStatusChangeListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ��洴���͹�����
 * @author ruipengVictor
 *
 */
public class AdvertisementAdmin {
	
	private static final String TAG = "AdvertisementAdmin";
	
	private static AdvertisementAdmin instance = null;
	/**
	 * ��ʱ��
	 */
	private Timer mTimer = null;
	/**
	 * ��ʱʱ��
	 */
	private long timeout;
	/**
	 * ��¼������ʱ��
	 */
	private static long lastTouchTime;
	/**
	 * Ĭ�ϳ�ʱʱ��
	 * //3����
	 */
//	public final long DEFAULT_TIMEROUT = 180000; 
	public static final long DEFAULT_TIMEROUT = 10000; 
	
	/**
	 * ��ǹ���Ƿ��Ѿ���ʾ
	 */
	private static boolean visible;
	/**
	 * ����Ƿ�ʼ���
	 */
	private boolean start;
	
	private Context mContext;
	/**
	 * ���״̬������
	 */
	private static OnVadertisementStatusChangeListener listener;
	
	/**
	 * ˽�й��캯��
	 */
	private AdvertisementAdmin(Context context) {
		this.mContext = context;
		
		timeout = DEFAULT_TIMEROUT;  //Ĭ�ϳ�ʱʱ��
		
		lastTouchTime = System.currentTimeMillis();  //��ʼ����������Ļʱ��
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//��ǿ�ʼ && ��ʱ && ��ǰû����ʾ���
				if(start && System.currentTimeMillis() - lastTouchTime >= timeout && !isVisible()){ 
					System.out.println("���� ���ʱ�䵽��");
					showAdvertisement();
					visible(true); //�ı���ʾ״̬
				}
			}
		}, timeout, 1000);//timeout���ʼ��⣬�Ժ�û��1����һ��
	}
	/**
	 * �õ�һ����������ʵ��
	 * @param context  ��Ҫһ��������
	 * @return
	 */
	public static AdvertisementAdmin newInstance(Context context){
		if(instance == null){
			synchronized (AdvertisementAdmin.class) {
				if(instance == null){
					instance = new AdvertisementAdmin(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * ��ʼ��浹��ʱ
	 */
	public void start() {
		this.start = true;
	}
	/**
	 * ��ʼ��浹��ʱ,��ָ��һ��������
	 */
	public void start(OnVadertisementStatusChangeListener listener) {
		AdvertisementAdmin.listener = listener;
		start();
	}
	
	/**
	 * ��ʼ��浹��ʱ,��ָ��һ���������ͳ�ʱʱ��
	 */
	public void start(OnVadertisementStatusChangeListener listener,long timeout) {
		AdvertisementAdmin.listener = listener;
		this.timeout = timeout;
		start();
	}
	
	/**
	 * ��ʼ��浹��ʱ
	 */
	public void stop() {
		this.start = false;
	}
	
	/**
	 * ����������ʱ��
	 */
	public static void updateTime() {
		lastTouchTime = System.currentTimeMillis();  
		Log.i(TAG, "updateTime to " + lastTouchTime);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public static void visible(boolean bool) {
		if(listener!=null && visible != bool){  //ָ���˼�����  && ǰ��״̬�����ı�
			listener.onStatusChange(visible);
		}
		visible = bool;
	}
	
	public long getTimeout() {
		return timeout;
	}
	/**
	 * ���ó�ʱʱ��
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	private void showAdvertisement() {
		Intent intent = new Intent(mContext, AdvertisementActivity.class);
		mContext.startActivity(intent);
	}
}
