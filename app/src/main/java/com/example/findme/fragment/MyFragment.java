package com.example.findme.fragment;

import java.io.File;
import java.io.FileNotFoundException;

import com.example.findme.R;
import com.example.findme.activity.ChangePasswordActivity;
import com.example.findme.activity.LoginActivity;
import com.example.findme.activity.MainActivity;
import com.example.findme.activity.UserInfoActivity;
import com.example.findme.db.FindMeDB;
import com.example.findme.model.UserInfo;
import com.example.findme.util.Global;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFragment extends Fragment implements OnClickListener{

	// 未登录时显示的按钮
	private Button unloginButton;

	// 登录后显示的您好文字
	private TextView helloForUser;

	// 登录后显示的头像
	private ImageView iconImage;

	// 未登录时显示的Layout
	private RelativeLayout unloginLayout;

	// 登录后显示的Layout
	private RelativeLayout loginedLayout;
	
	//修改密码的Layout
	private LinearLayout changePasswordLayout;
	
	//退出登录的Layout
	private LinearLayout logoutLayout;

	//头像路径
	private Uri imageUri;



	// 是否去登录
	private boolean isToLogin;

	// 数据库操作
	private FindMeDB findMeDB;
	
//	private int userId;
//
//	private UserInfo userInfo;
	
	private SharedPreferences pref;
	
	private SharedPreferences.Editor editor;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View myLayout = inflater.inflate(R.layout.my_layout, container, false);
		return myLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initViews();
		findMeDB = FindMeDB.getInstance(getActivity());// 初始化数据库
		imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "user_icon.jpg"));// 初始化头像路径
//		userId = getArguments().getInt("data_user_id");
		isLogin();
		super.onActivityCreated(savedInstanceState);
	}

	private void isLogin() {
		// TODO Auto-generated method stub
		if(MainActivity.getUserInfo() == null){
			isToLogin=true;
			unloginLayout.setVisibility(View.VISIBLE);
			loginedLayout.setVisibility(View.GONE);//登录后的Layout先隐藏起来
			logoutLayout.setVisibility(View.GONE);//登录后的退出登录Layout先隐藏起来
			unloginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new Global().toLogin(MyFragment.this);
				}
			});
		}else{
			isToLogin=false;
//			userInfo = findMeDB.loadUserInfo(userId);
			if(MainActivity.getUserInfo().getName()==null){
				helloForUser.setText("您好," +MainActivity.getUserInfo().getPhoneNumber());
			}else{
				helloForUser.setText("您好," +MainActivity.getUserInfo().getName());
			}
			unloginLayout.setVisibility(View.GONE);//登录之后把未登录的Layout隐藏掉
			loginedLayout.setVisibility(View.VISIBLE);//登录之后显示新的Layout
			logoutLayout.setVisibility(View.VISIBLE);//登录之后的退出登录Layout显示出来
			File userIcon=new File(Environment.getExternalStorageDirectory(),"user_icon.jpg");
			if (userIcon.exists()) {//如果本地有已经存在的头像，则直接调取出来做头像
				Bitmap bitmap;
				try {
					bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
					iconImage.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				iconImage.setImageResource(R.drawable.ic_launcher);
				//如果没有的话就设置默认头像
			}
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		unloginButton = (Button) getView().findViewById(R.id.unlogin_button);
		helloForUser = (TextView) getView().findViewById(R.id.hello);
		iconImage = (ImageView) getView().findViewById(R.id.user_icon);
		unloginLayout = (RelativeLayout) getView().findViewById(R.id.unlogin_layout);
		loginedLayout = (RelativeLayout) getView().findViewById(R.id.logined_layout);
		changePasswordLayout=(LinearLayout) getView().findViewById(R.id.change_password_layout);
		logoutLayout=(LinearLayout) getView().findViewById(R.id.log_out_layout);
		
		loginedLayout.setOnClickListener(this);
		changePasswordLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case Global.LOGIN:// 登录之后执行以下代码
			if (resultCode == android.app.Activity.RESULT_OK) {
//				UserInfo userInfo = new UserInfo();
//				userInfo = data.getParcelableExtra("data_user_info");//把返回来的userInfo读取出来
//				MainActivity.setUserInfo(userInfo);
				isToLogin = false;//登录之后变成false
				//因为回传回来的getName()有可能是NULL，需要先赋值给本地才能进行判断，不然会出现空指针
//				String userName = MainActivity.getUserInfo().getName();
				isLogin();
//				if(MainActivity.getUserInfo().getName()==null){
//					helloForUser.setText("您好," +MainActivity.getUserInfo().getPhoneNumber());
//				}else{
//					helloForUser.setText("您好," +MainActivity.getUserInfo().getName());
//				}
//				unloginLayout.setVisibility(View.GONE);//登录之后把未登录的Layout隐藏掉
//				loginedLayout.setVisibility(View.VISIBLE);//登录之后显示新的Layout
//				logoutLayout.setVisibility(View.VISIBLE);//登录之后的退出登录Layout显示出来
//				File userIcon=new File(Environment.getExternalStorageDirectory(),"user_icon.jpg");
//				if (userIcon.exists()) {//如果本地有已经存在的头像，则直接调取出来做头像
//					Bitmap bitmap;
//					try {
//						bitmap = BitmapFactory
//								.decodeStream(getActivity()
//										.getContentResolver().openInputStream(
//												imageUri));
//						iconImage.setImageBitmap(bitmap);
//					} catch (FileNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					iconImage.setImageResource(R.drawable.ic_launcher);
//					//如果没有的话就设置默认头像
//				}
			}
			break;
		case Global.CHANGE_USER_INFO:
			isLogin();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.logined_layout:
			Intent intentForUserInfo = new Intent(getActivity(), UserInfoActivity.class);
			getActivity().startActivityFromFragment(MyFragment.this, intentForUserInfo, Global.CHANGE_USER_INFO);
			break;
		case R.id.change_password_layout:
			if(MainActivity.getUserInfo()== null){
			    Global.toLogin(this);
			}else{
				Intent intentForChangePassword = new Intent(getActivity(), ChangePasswordActivity.class);
				getActivity().startActivity(intentForChangePassword);
			}
			break;
		case R.id.log_out_layout:
			pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
			editor = pref.edit();
			editor.putString("account", "");
			editor.putString("password", "");
			editor.commit();
			isToLogin=true;
			UserInfo userInfo = null;
			MainActivity.setUserInfo(userInfo);
			isLogin();
			Global.toLogin(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 去LoginActivity登录
	 */
	/*public void toLogin() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		// 要先获取宿主getActivity，再用宿主的startActivityFromFragment方法去启动跳转，不然MyFragment的onActivityResult不会执行，只会执行宿主的
		getActivity().startActivityFromFragment(MyFragment.this, intent, Global.LOGIN);
	}*/

}
