package com.ruipengkj.dgxtos.activity;


import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

import android.R;
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
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//更新广告显示状态
		AdvertisementAdmin.visible(false);
	}
	
}
