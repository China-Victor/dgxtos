package com.ruipengkj.dgxtos.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ruipengkj.dgxtos.ConstantValue;
import com.ruipengkj.dgxtos.GloableParams;
import com.ruipengkj.dgxtos.R;
import com.ruipengkj.dgxtos.utils.MyAsyncTask;
import com.ruipengkj.dgxtos.view.MyDialogBuilder;

public class MainActivity extends BaseActivity implements OnLongClickListener, OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean D = ConstantValue.DEBUG;
	/**
	 * 显示日期
	 */
	private TextView tv_date;
	private TextView tv_time;
	private TextView tv_table_number;

	private Handler handler;
	private Timer timer;
	private SharedPreferences sp;
	private EditText et_pin;
	private AlertDialog dialog;
	private ProgressBar progressBar;
	private TextView tv_wrong;
	private TextView tv_dialog_title;
	private TextView tv_dailog_ok;
	private TextView tv_dailog_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//禁用锁屏应用
		disableLockScreen();
		
		setContentView(R.layout.activity_main);
		//初始化系统数据
		initSystemDate();
		//初始化界面控件
		initView();
		
	}

	/**
	 * 初始化系统数据
	 */
	private void initSystemDate() {
		sp = getPreferences(MODE_PRIVATE);  
		String currentTable = sp.getString("TableNumber", null);
		GloableParams.currentTable = currentTable;
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		//欢迎按钮
		View welcame_action = findViewById(R.id.ll_main_welcame_action);
		//长按监听
		welcame_action.setOnLongClickListener(this);
		//点击监听
		welcame_action.setOnClickListener(this);
		
		tv_date = (TextView)findViewById(R.id.tv_main_date);
		tv_time = (TextView)findViewById(R.id.tv_main_time);
		//监听时间，动态显示
		monitoringTime();
		
		//显示台号  , 在每次打开页面onResume(),都检查更新 
		tv_table_number = (TextView)findViewById(R.id.tv_main_table_number);
		
		//消费明细按钮监听
		findViewById(R.id.ib_main_consumption_list_action).setOnClickListener(this);
		//结账按钮监听
		findViewById(R.id.ib_main_checkout_action).setOnClickListener(this);
		//点餐按钮监听
		findViewById(R.id.ib_main_order_action).setOnClickListener(this);
		//点餐按钮监听
		findViewById(R.id.ib_main_service_action).setOnClickListener(this);
		//帮助按钮监听
		findViewById(R.id.ib_main_help_action).setOnClickListener(this);
	}
	/**
	 * 查阅配置文件类，更新台号
	 */
	private void updateTableNumber() {
		String currentTable = GloableParams.currentTable != null ? GloableParams.currentTable : "5号";
		tv_table_number.setText(currentTable);
	}

	/**
	 * 监听系统时间
	 */
	private void monitoringTime() {
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == 100) {
					String[] split = ((String) msg.obj).split("##");
					tv_date.setText(split[0]);
					tv_time.setText(split[1]);
				}
	        }
		};
		//创建一个计时器
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
	            String str=new SimpleDateFormat("yyyy-MM-dd##HH:mm:ss").format(new Date()); //##用于分割
	            handler.sendMessage(handler.obtainMessage(100,str));
			}
		}, 0, 1000);//0秒后开启，每个一秒执行一次
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateTableNumber();
	}
	
	@Override
	protected void onDestroy() {
		//清除计时器
		timer.cancel();
		super.onDestroy();
	}

	@Override
	public boolean onLongClick(View v) {
		if(v.getId() == R.id.ll_main_welcame_action){
			if (D){
				Log.i(TAG, "onLongClick --> " + "welcame_action");
				//
				Toast.makeText(this, "welcame onLongClick" , 0).show();
			}
			startSelectTableActivity();
			return true;
		}
		return false;
	}
	/**
	 * 打开选择台号页面
	 */
	private void startSelectTableActivity() {
		//输入PIN码
		showEnterPinDialog();
		
	}
	//显示输入PIN对话框
	private void showEnterPinDialog() {
		MyDialogBuilder builder = new MyDialogBuilder(this);
		dialog = builder.create();
		
		View view_dialog = View.inflate(this, R.layout.dialog_enter_pwd, null);
		tv_dialog_title = (TextView) view_dialog.findViewById(R.id.tv_dialog_enter_title);
		progressBar = (ProgressBar) view_dialog.findViewById(R.id.pb_dialog_enter_pin);
		tv_wrong = (TextView) view_dialog.findViewById(R.id.tv_dialog_pin_wrong);
		
		et_pin = (EditText) view_dialog.findViewById(R.id.et_dialog_pin_content);
		tv_dailog_ok = (TextView) view_dialog.findViewById(R.id.tv_dailog_pin_ok);
		tv_dailog_ok.setOnClickListener(this);
		tv_dailog_cancle = (TextView) view_dialog.findViewById(R.id.tv_dailog_pin_cancle);
		tv_dailog_cancle.setOnClickListener(this);
		dialog.setView(view_dialog, 0, 0, 0, 0);
		dialog.setCanceledOnTouchOutside(false);
		
		dialog.show();
		//修改对话框的宽度
		LayoutParams layoutParams = dialog.getWindow().getAttributes();
		layoutParams.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.3);
		dialog.getWindow().setAttributes(layoutParams);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ll_main_welcame_action){
			if (D){
				Log.i(TAG, "onClick --> " + "welcame");
				Toast.makeText(this, "welcame" , 0).show();
			}
			
			return;
		}	
		switch (v.getId()) {
		case R.id.ib_main_consumption_list_action:
			if (D){
				Log.i(TAG, "onClick --> " + "consumption_list");
				Toast.makeText(this, "consumption_list" , 0).show();
			}
			break;
		case R.id.ib_main_checkout_action:
			if (D){
				Log.i(TAG, "onClick --> " + "checkout");
				Toast.makeText(this, "checkout" , 0).show();
			}
			break;
		case R.id.ib_main_order_action:
			if (D){
				Log.i(TAG, "onClick --> " + "order");
				Toast.makeText(this, "order" , 0).show();
			}
			break;
		case R.id.ib_main_service_action:
			if (D){
				Log.i(TAG, "onClick --> " + "service");
				Toast.makeText(this, "service" , 0).show();
			}
			break;
		case R.id.ib_main_help_action:
			if (D){
				Log.i(TAG, "onClick --> " + "help");
				Toast.makeText(this, "help" , 0).show();
			}
			break;
		case R.id.tv_dailog_pin_ok:
			if (D){
				Log.i(TAG, "onClick --> " + "pin_ok");
				Toast.makeText(this, "pin_ok" , 0).show();
			}
			//检验
			checkPIN();
			break;
		case R.id.tv_dailog_pin_cancle:
			if (D){
				Log.i(TAG, "onClick --> " + "pin_ok");
				Toast.makeText(this, "pin_ok" , 0).show();
			}
			if(dialog != null){
				dialog.cancel();
			}
			break;
		default:
			break;
		}
		
	}

	/**
	 * 检验PIN码
	 */
	private void checkPIN() {
		String pin = et_pin.getText().toString();
		if(TextUtils.isEmpty(pin)){
			//播放动画
			et_pin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
		}else{
			//开启一个异步任务
			new MyAsyncTask<String>(this) {
				
				@Override
				protected void onPreExecute() {
					tv_dialog_title.setText("正在校验...");
					tv_dialog_title.setTextColor(getResources().getColor(android.R.color.black));
					progressBar.setVisibility(View.VISIBLE);
					//改变对话框状态
					setDialogEnabled(false);
				}
				
				@Override
				protected Message doInBackground(String... params) {
					//异步响服务器请求数据
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null; 
				}
				
				@Override
				protected void onPostExecute(Message result) {
					progressBar.setVisibility(View.INVISIBLE);
					if(result == null){
						tv_dialog_title.setText("无效PIN码");
						tv_dialog_title.setTextColor(getResources().getColor(R.color.red));
					}
					//改变对话框状态
					setDialogEnabled(true);
				}
			}.execute("123456");
		}
		
	}
	
	/**
	 * 设置验证pin码对话框里的控件是否可用
	 * @param enabled
	 */
	private void setDialogEnabled(boolean enabled) {
		et_pin.setEnabled(enabled);
		tv_dailog_ok.setEnabled(enabled);
//		tv_dailog_cancle.setEnabled(enabled);
	}

}
