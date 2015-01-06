package com.ruipengkj.dgxtos.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.util.Log;
import android.view.KeyEvent;

import com.ruipengkj.dgxtos.ConstantValue;
import com.ruipengkj.dgxtos.activity.BaseActivity;
import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

public class MyDialogBuilder implements OnDismissListener, OnShowListener, OnKeyListener {
	private static final String TAG = MyDialogBuilder.class.getName();
	private static final boolean D = ConstantValue.DEBUG;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	private BaseActivity context;
	
	public MyDialogBuilder(BaseActivity context) {
		this.context = context;
	}
	
	public AlertDialog create() {
		AlertDialog.Builder builder = new Builder(this.context);
		AlertDialog dialog = builder.create();
		//为每个对话框注册显示和消除监听器
		dialog.setOnDismissListener(this);
		dialog.setOnShowListener(this);
		dialog.setOnKeyListener(this);
		
		dialog.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		
		return dialog;
	}


	@Override
	public void onShow(DialogInterface dialog) {
		//停止广告
		AdvertisementAdmin.stop();
	}


	@Override
	public void onDismiss(DialogInterface dialog) {
		//更新时间
		AdvertisementAdmin.updateTime();
		//开始广告
		AdvertisementAdmin.start();
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(D)Log.i(TAG, "onKey --> " + keyCode);
		//传给所在Activity处理
		return context.onKeyDown(keyCode, event);
	}
	
	
	
}
