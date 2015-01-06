package com.ruipengkj.dgxtos.utils;

import com.ruipengkj.dgxtos.net.NetTools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;


/**
 * 定义异步处理线程 返回值 Message
 * 
 * @param <T>
 *            代表传递的参数
 */
public abstract class MyAsyncTask<T> extends AsyncTask<T, Integer, Message> {

	private Context context;
	
	public MyAsyncTask(Context context) {
		super();
		this.context = context;
	}


	public AsyncTask<T, Integer, Message> executeProxy(T... params) {
		if (NetTools.checkNet(context)) {
			return execute(params);
		} else {
			return null;
		}
	}
}
