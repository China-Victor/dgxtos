package com.ruipengkj.dgxtos.activity;


import com.ruipengkj.dgxtos.service.AdvertisementAdmin;

import android.R;
import android.os.Bundle;

/**
 * ���ҳ��
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
		//���¹����ʾ״̬
		AdvertisementAdmin.visible(false);
	}
	
}
