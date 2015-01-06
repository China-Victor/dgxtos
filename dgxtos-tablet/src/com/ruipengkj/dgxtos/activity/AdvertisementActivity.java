package com.ruipengkj.dgxtos.activity;


import com.ruipengkj.dgxtos.R;
import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 广告页面
 * @author ruipengVictor
 *
 */
public class AdvertisementActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_advertise);
		
		findViewById(R.id.bt_advert_closs).setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//更新广告显示状态
		AdvertisementAdmin.visible(false);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.bt_advert_closs){
			finish();
		}
		
	}
	
	@Override
	public void onUserInteraction() {
		finish();
		super.onUserInteraction();
	}
}
