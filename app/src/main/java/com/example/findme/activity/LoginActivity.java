package com.example.findme.activity;

import java.util.Random;
import java.util.regex.Pattern;

import com.example.findme.R;
import com.example.findme.db.FindMeDB;
import com.example.findme.fragment.MyFragment;
import com.example.findme.model.User;
import com.example.findme.model.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// 登录界面的账号
	private EditText accountEdit;

	// 登录界面的密码
	private EditText passwordEdit;

	// 登录界面的登录按钮
	private Button loginButton;

	//登录界面的动态密码
	private RelativeLayout registRelative;

	// 数据库
	private FindMeDB findMeDB;
	
	//使用SharedPreferences实现记住密码
	private SharedPreferences pref;
	
	//实现SharedPreferences写入功能
	private SharedPreferences.Editor editor;
	
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		//获取上一个调用者的userInfo
		Intent intentFromOther = getIntent();
		userInfo = intentFromOther.getParcelableExtra("data_user_info");
		
		accountEdit = (EditText) findViewById(R.id.account_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
		loginButton = (Button) findViewById(R.id.login_button);
		registRelative = (RelativeLayout) findViewById(R.id.regist_layout);
		findMeDB = FindMeDB.getInstance(this);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//获取用户输入的账号跟密码
				String account = accountEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				//判断账号或者密码是否为空
				if (account.equals("")) {
					Toast.makeText(LoginActivity.this, "请输入账号...",
							Toast.LENGTH_SHORT).show();
				} else if (password.equals("")) {
					Toast.makeText(LoginActivity.this, "请输入密码...",
							Toast.LENGTH_SHORT).show();
				} else {//如果账号密码不为空，则通过数据库查询是否存储改账号信息
					User user = findMeDB.loadUser(account);
					if (user == null) {
						Toast.makeText(LoginActivity.this, "您输入的账号有误，请重新输入...",
								Toast.LENGTH_SHORT).show();
					} else if (password.equals(user.getPassword())) {
						//如果账号密码没有错，则读取该User对应的UserInfo并返回
						//把账号密码写入SharedPreferences文件里
						pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
						editor = pref.edit();
						editor.putString("account", account);
						editor.putString("password", password);
						editor.commit();
						UserInfo userInfo = findMeDB.loadUserInfo(user.getId());
						Intent intent = new Intent();
						MainActivity.setUserInfo(userInfo);
						MainActivity.setUser(user);
						setResult(RESULT_OK, intent);
						finish();
						// }
					} else {
						passwordEdit.setText("");
						Toast.makeText(LoginActivity.this, "您输入的密码有误，请重新输入...",
								Toast.LENGTH_SHORT).show();
					}
				}

			}

		});

		/**
		 * 动态密码
		 */
		registRelative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phoneNumber = accountEdit.getText().toString();
				Pattern pattern = Pattern.compile("[0-9]*");
				// 检查用户输入的手机号码是否够11位并且全部数字
				if (phoneNumber.length() == 11
						&& pattern.matcher(phoneNumber).matches()) {//如果是的话随机生成密码并提示用户
					User user = findMeDB.loadUser(phoneNumber);
					Random random = new Random();
					String password = String.valueOf(random.nextInt(100000));
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.ic_launcher, "您本次登录的密码是：" + password,
							System.currentTimeMillis());
					notification.setLatestEventInfo(LoginActivity.this,
							"登录密码", "您本次登录的密码是：" + password, null);
					notificationManager.notify(1, notification);
					if (user == null) {//如果该手机号码User不存在，就新建
						user = new User();
						user.setPhoneNumber(phoneNumber);
						user.setPassword(password);
						if (!findMeDB.createUser(user)) {
							Toast.makeText(LoginActivity.this,
									"动态密码登录失败，请重新输入手机号码...", Toast.LENGTH_SHORT)
									.show();
						}else{//新建User之后，新建User并与之关联
							UserInfo userInfo = new UserInfo();
							user = findMeDB.loadUser(phoneNumber);
							userInfo.setUserId(user.getId());
							userInfo.setPhoneNumber(user.getPhoneNumber());
							findMeDB.createUserInfo(userInfo);
						}
					} else {//如果存在就把新密码set到User表
						user.setPassword(password);
						if (!findMeDB.updateUser(user)) {
							Toast.makeText(LoginActivity.this,
									"动态密码登录失败，请重新输入手机号码...", Toast.LENGTH_SHORT)
									.show();
						}
					}

				} else {
					Toast.makeText(LoginActivity.this, "您输入的账号有误，请重新输入...",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	

}
