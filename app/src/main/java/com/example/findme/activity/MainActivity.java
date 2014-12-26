package com.example.findme.activity;

import com.example.findme.R;
import com.example.findme.db.FindMeDB;
import com.example.findme.fragment.FocusFragment;
import com.example.findme.fragment.IndexFragment;
import com.example.findme.fragment.MyFragment;
import com.example.findme.fragment.NoticeFragment;
import com.example.findme.model.User;
import com.example.findme.model.UserInfo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	// 首页Fragment
	private IndexFragment indexFragment;

	// 告示Fragment
	private NoticeFragment noticeFragment;

	// 关注Fragment
	private FocusFragment focusFragment;

	// 我的Fragment
	private MyFragment myFragment;

	// 首页Layout
	private View indexLayout;

	// 告示Layout
	private View noticeLayout;

	// 关注Layout
	private View focusLayout;

	// 我的Layout
	private View myLayout;

	// 首页导航图标
	private ImageView indexImage;

	// 告示导航图标
	private ImageView noticeImage;

	// 关注导航图标
	private ImageView focusImage;

	// 我的导航图标
	private ImageView myImage;

	// 首页导航文字
	private TextView indexText;

	// 告示导航文字
	private TextView noticeText;

	// 关注导航文字
	private TextView focusText;

	// 我的导航文字
	private TextView myText;

	// 管理Fragment
	private FragmentManager fragmentManager;

	// 读取SharedPreferences配置出来，实现免密码登录等配置
	private SharedPreferences pref;

	// 数据库操作
	private FindMeDB findMeDB;

	//贯穿整个app的user
	private static User user;
	
	//贯穿整个app的userInfo
	private static UserInfo userInfo;

	

	public static User getUser() {
		return user;
	}



	public static void setUser(User user) {
		MainActivity.user = user;
	}



	public static UserInfo getUserInfo() {
		return userInfo;
	}



	public static void setUserInfo(UserInfo userInfo) {
		MainActivity.userInfo = userInfo;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		// 初始化Views
		initViews();
		fragmentManager = getFragmentManager();
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		String account = pref.getString("account", "");
		String password = pref.getString("password", "");
		// 初始化数据库
		findMeDB = FindMeDB.getInstance(this);
		//如果SharedPreferences里面的账号为空或者密码错误，则赋值为0，表示没有存在的账号
//		user = new User();
//		userInfo = new UserInfo();
		if(account.equals("")){
			userInfo = null;
		}else{
			user = findMeDB.loadUser(account);
			//如果用户更改了电话号码或者电子邮箱，会导致user是null，如果是null，则直接跳去LoginActivity
			if(user == null){
//				userInfo = null;
				Intent intentToLogin = new Intent(this, LoginActivity.class);
				startActivityFromFragment(myFragment, intentToLogin, 1);
			}else{
				if(user.getPassword().equals(password)){
					userInfo = findMeDB.loadUserInfo(user.getId());
				}else{
					userInfo = null;
				}
			}
			
		}
		// 初始设定在首页
		setTabSelection(0);
	}



	/**
	 * 初始化Views
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		indexLayout = findViewById(R.id.index_Rlayout);
		noticeLayout = findViewById(R.id.notice_Rlayout);
		focusLayout = findViewById(R.id.focus_Rlayout);
		myLayout = findViewById(R.id.my_Rlayout);
		indexImage = (ImageView) findViewById(R.id.index_image);
		noticeImage = (ImageView) findViewById(R.id.notice_image);
		focusImage = (ImageView) findViewById(R.id.focus_image);
		myImage = (ImageView) findViewById(R.id.my_image);
		indexText = (TextView) findViewById(R.id.index_text);
		noticeText = (TextView) findViewById(R.id.notice_text);
		focusText = (TextView) findViewById(R.id.focus_text);
		myText = (TextView) findViewById(R.id.my_text);
		indexLayout.setOnClickListener(this);
		noticeLayout.setOnClickListener(this);
		focusLayout.setOnClickListener(this);
		myLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			switch (v.getId()) {

			case R.id.index_Rlayout:
				setTabSelection(0);
				break;
			case R.id.notice_Rlayout:
				setTabSelection(1);
				break;
			case R.id.focus_Rlayout:
				setTabSelection(2);
				break;
			case R.id.my_Rlayout:
				setTabSelection(3);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择页卡
	 * 
	 * @param i
	 */
	private void setTabSelection(int i) {
		// TODO Auto-generated method stub
		// 清空页卡选择状态
		clearSelection();
		// 开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有Fragment
		hideFragments(transaction);
		// Activity向Fragment传递用户信息
//		Bundle bundle = new Bundle();
//		bundle.putInt("data_user_id", userId);
		switch (i) {
		case 0:
			// 当选择的第一个页卡，改变导航图标跟文字颜色
			indexImage.setImageResource(R.drawable.ic_launcher2);
			indexText.setTextColor(Color.BLUE);
			if (indexFragment== null) {
				// 如果indexFragment不为空，则移除再新建，并添加到Fragment事务里
				//如果不先移除的话，再次运行setArguments的时候会报错
				//transaction.remove(indexFragment);
				indexFragment = new IndexFragment();
				transaction.add(R.id.content, indexFragment);
			} else{
				transaction.show(indexFragment);
			}
//			indexFragment.setArguments(bundle);
			break;
		case 1:
			noticeImage.setImageResource(R.drawable.ic_launcher2);
			noticeText.setTextColor(Color.BLUE);
			if (noticeFragment == null) {
//				transaction.remove(noticeFragment);
				noticeFragment = new NoticeFragment();
				transaction.add(R.id.content, noticeFragment);
			} else{
				transaction.show(noticeFragment);
			}
//			noticeFragment.setArguments(bundle);
			break;
		case 2:
			focusImage.setImageResource(R.drawable.ic_launcher2);
			focusText.setTextColor(Color.BLUE);
			if (focusFragment == null) {
//				transaction.remove(focusFragment);
				focusFragment = new FocusFragment();
				transaction.add(R.id.content, focusFragment);
			} else{
				transaction.show(focusFragment);
			}
//			focusFragment.setArguments(bundle);
			break;
		case 3:
			myImage.setImageResource(R.drawable.ic_launcher2);
			myText.setTextColor(Color.BLUE);
			if (myFragment == null) {
//				transaction.remove(myFragment);
				myFragment = new MyFragment();
				transaction.add(R.id.content, myFragment);
			} else{
				transaction.show(myFragment);
			}
//			myFragment.setArguments(bundle);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 隐藏掉所有的Fragment
	 * 
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (indexFragment != null) {
			transaction.hide(indexFragment);
		}
		if (noticeFragment != null) {
			transaction.hide(noticeFragment);
		}
		if (focusFragment != null) {
			transaction.hide(focusFragment);
		}
		if (myFragment != null) {
			transaction.hide(myFragment);
		}
	}

	/**
	 * 清空页卡选择状态
	 */
	private void clearSelection() {
		// TODO Auto-generated method stub
		indexImage.setImageResource(R.drawable.ic_launcher);
		indexText.setTextColor(Color.parseColor("#82858b"));
		noticeImage.setImageResource(R.drawable.ic_launcher);
		noticeText.setTextColor(Color.parseColor("#82858b"));
		focusImage.setImageResource(R.drawable.ic_launcher);
		focusText.setTextColor(Color.parseColor("#82858b"));
		myImage.setImageResource(R.drawable.ic_launcher);
		myText.setTextColor(Color.parseColor("#82858b"));
	}



}
