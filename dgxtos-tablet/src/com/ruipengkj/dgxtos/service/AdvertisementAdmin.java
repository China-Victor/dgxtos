package com.ruipengkj.dgxtos.service;



import java.util.Timer;
import java.util.TimerTask;

import com.ruipengkj.dgxtos.activity.AdvertisementActivity;
import com.ruipengkj.dgxtos.inter.OnVadertisementStatusChangeListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 广告创建和管理类
 * @author ruipengVictor
 *
 */
public class AdvertisementAdmin {
	
	private static final String TAG = "AdvertisementAdmin";
	
	private static AdvertisementAdmin instance = null;
	/**
	 * 计时器
	 */
	private Timer mTimer = null;
	/**
	 * 超时时间
	 */
	private long timeout;
	/**
	 * 记录最后操作时间
	 */
	private static long lastTouchTime;
	/**
	 * 默认超时时间
	 * //3分钟
	 */
//	public final long DEFAULT_TIMEROUT = 180000; 
	public static final long DEFAULT_TIMEROUT = 10000; 
	
	/**
	 * 标记广告是否已经显示
	 */
	private static boolean visible;
	/**
	 * 标记是否开始广告
	 */
	private static boolean start;
	
	private Context mContext;
	/**
	 * 广告状态监听器
	 */
	private static OnVadertisementStatusChangeListener listener;
	
	/**
	 * 私有构造函数
	 */
	private AdvertisementAdmin(Context context) {
		this.mContext = context;
		
		timeout = DEFAULT_TIMEROUT;  //默认超时时间
		
		lastTouchTime = System.currentTimeMillis();  //初始化最后操作屏幕时间
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//标记开始 && 超时 && 当前没有显示广告
				if(start && System.currentTimeMillis() - lastTouchTime >= timeout && !isVisible()){ 
					System.out.println("哈哈 广告时间到了");
					showAdvertisement();
					visible(true); //改变显示状态
				}
			}
		}, timeout, 1000);//timeout秒后开始检测，以后没隔1秒检测一下
	}
	/**
	 * 得到一个广告管理类实例
	 * @param context  需要一个上下文
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
	 * 开始广告倒计时
	 */
	public static void start() {
		start = true;
	}
	/**
	 * 开始广告倒计时,并指定一个监听器
	 */
	public void start(OnVadertisementStatusChangeListener listener) {
		AdvertisementAdmin.listener = listener;
		start();
	}
	
	/**
	 * 开始广告倒计时,并指定一个监听器和超时时间
	 */
	public void start(OnVadertisementStatusChangeListener listener,long timeout) {
		AdvertisementAdmin.listener = listener;
		this.timeout = timeout;
		start();
	}
	
	/**
	 * 开始广告倒计时
	 */
	public static void stop() {
		start = false;
	}
	
	/**
	 * 更新最后操作时间
	 */
	public static void updateTime() {
		lastTouchTime = System.currentTimeMillis();  
		Log.i(TAG, "updateTime to " + lastTouchTime);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public static void visible(boolean bool) {
		if(listener!=null && visible != bool){  //指定了监听器  && 前后状态发生改变
			listener.onStatusChange(visible);
		}
		visible = bool;
	}
	
	public long getTimeout() {
		return timeout;
	}
	/**
	 * 设置超时时间
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
