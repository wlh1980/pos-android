package com.android.singaporeanorderingsystem;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dialog.DialogBuilder;

public class SettingActivity extends Activity {
	
	private EditText language_set;
	private boolean is_chinese;
	private SharedPreferences sharedPrefs;
	private PopupWindow popupWindow;
	private View view;
	private ImageView menu;
	private Button synchronization_menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		language_set=(EditText) findViewById(R.id.language_set);
		menu=(ImageView) findViewById(R.id.menu_btn);
		synchronization_menu = (Button) findViewById(R.id.synchronization_menu_brn);
		sharedPrefs= getSharedPreferences("language", Context.MODE_PRIVATE);
		String type=sharedPrefs.getString("type", "");
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPopupWindow();
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.showAsDropDown(menu, 0, -5);
			}
		});
		if(type==null){
			type="en";
		}
		if(type.equals("zh")){
			is_chinese=true;
		}else{
			is_chinese=false;
		}
		if(!is_chinese){
			language_set.setText("Eglish");
		}else{
			language_set.setText("中文");
		}
		
		language_set.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, "点击了设置语言", Toast.LENGTH_SHORT).show();
				DialogBuilder builder=new DialogBuilder(SettingActivity.this);
				builder.setTitle(R.string.message_title);
				builder.setMessage(R.string.message_2);
				builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(DailyPayActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
						if(!is_chinese){
						updateLange(Locale.SIMPLIFIED_CHINESE);
						language_set.setText("中文");
						Editor editor = sharedPrefs.edit();
						editor.putString("type", "zh");
						editor.commit();
						is_chinese=true;
						}else{
							updateLange(Locale.ENGLISH);
							language_set.setText("Eglish");
							Editor editor = sharedPrefs.edit();
							editor.putString("type", "en");
							editor.commit();
							is_chinese=false;
						}
					}});
				builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(DailyPayActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
					}});
				builder.create().show();
				
			}});
	}
	
	 public void initPopupWindow() {
			if (popupWindow == null) {
				view = this.getLayoutInflater().inflate(
						R.layout.popupwindow, null);
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.setOutsideTouchable(true);
				TextView popu_setting=(TextView) view.findViewById(R.id.popu_setting);
				TextView popu_exit=(TextView) view.findViewById(R.id.popu_exit);
				TextView popu_daily=(TextView) view.findViewById(R.id.popu_daily);
				TextView popu_diancai=(TextView) view.findViewById(R.id.popu_diancai);
				popu_diancai.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						Intent intent =new Intent(SettingActivity.this , MainActivity.class);
						SettingActivity.this.startActivity(intent);
						//overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
						SettingActivity.this.finish();
					}
				});;
				popu_setting.setVisibility(View.GONE);
				
				popu_daily.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						Intent intent =new Intent(SettingActivity.this , DailyPayActivity.class);
						SettingActivity.this.startActivity(intent);
						overridePendingTransition(
								R.anim.in_from_right,
								R.anim.out_to_left);
						SettingActivity.this.finish();
					}});
				popu_exit.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						CreatedDialog().create().show();
					}
				});
			}
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
		public void updateActivity() {
		  Intent intent = new Intent();
		  intent.setClass(this,SettingActivity.class);//当前Activity重新打开
		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  startActivity(intent);
		  this.finish();
		 
		 }
		public DialogBuilder CreatedDialog(){
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_exit);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					System.exit(0);
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				}});
			return builder;
		}
		private void updateLange(Locale locale){    	
			 Resources res = getResources();
			 Configuration config = res.getConfiguration();
			 config.locale = locale;
			 DisplayMetrics dm = res.getDisplayMetrics();
			 res.updateConfiguration(config, dm);        
	    	Toast.makeText(this, "Locale in "+locale+" !", Toast.LENGTH_LONG).show();
	    	updateActivity();  

	    }

}
