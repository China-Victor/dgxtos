package com.ruipengkj.dgxtos.activity;


import com.ruipengkj.dgxtos.R;
import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

import android.os.Bundle;

/**
 * 广告页面
 * @author ruipengVictor
 *
 */
public class AdvertisementActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_advertise);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//更新广告显示状态
		AdvertisementAdmin.visible(false);
	}
	
}
