package com.ruipengkj.dgxtos.activity;

import com.ruipengkj.dgxtos.R;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * 禁用锁屏应用
		 */
		disableLockScreen();
	}

}
