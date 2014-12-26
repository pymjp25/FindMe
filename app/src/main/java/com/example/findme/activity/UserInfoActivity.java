package com.example.findme.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.example.findme.R;
import com.example.findme.db.FindMeDB;
import com.example.findme.db.FindMeOpenHelper;
import com.example.findme.fragment.MyFragment;
import com.example.findme.model.UserInfo;
import com.example.findme.util.Global;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener{
	
	//显示用户头像
	private ImageView userIcon;
	
	//更改用户头像Layout
	private RelativeLayout changeUserIcon;
	
	//显示用户姓名
	private TextView userName;
	
	//更改用户姓名Layout
	private RelativeLayout changeUserName;
	
	//显示用户性别
	private TextView userSex;
	
	//更改用户性别Layout
	private RelativeLayout changeUserSex;
	
	//显示用户出生年月
	private TextView userBirthday;
	
	// 更改用户出生年月Layout
	private RelativeLayout changeUserBirthday;
	
	//显示用户手机号码
	private TextView userPhoneNumber;
	
	//更改用户手机号码
	private RelativeLayout changeUserPhoneNumber;
	
	//显示用户email
	private TextView userEmail;
	
	// 更改用户email
	private RelativeLayout changeUserEmail;
	
	//返回按钮
	private Button backFromUserInfo;
	
	//头像路径
	private Uri imageUri;
	

	
	//数据库操作
	private FindMeDB findMeDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		findMeDB = FindMeDB.getInstance(this);
		imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "user_icon.jpg"));// 初始化头像路径
		initViews();
		initUserInfo();
		
	}

	/**
	 * 初始化用户信息，从MyFragment传过来的用户信息进行初始化
	 */
	private void initUserInfo() {
		// TODO Auto-generated method stub
//		Intent intentFromLogin = getIntent();
//		userInfo = intentFromLogin.getParcelableExtra("data_user_info");
		File isUserIcon= new File(Environment.getExternalStorageDirectory(),"user_icon.jpg");
		if(isUserIcon.exists()){//如果存在就直接读取本地的头像
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory
						.decodeStream(getContentResolver().openInputStream(
										imageUri));
				userIcon.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			userIcon.setImageResource(R.drawable.ic_launcher);
			//如果没有的话就设置默认头像
		}
		//设置姓名，如果之前未设置过的，填写空
		if(MainActivity.getUserInfo().getName()==null){
			userName.setText("未填写");
		}else{
			userName.setText(MainActivity.getUserInfo().getName());
		}		
		//设置性别
		if(MainActivity.getUserInfo().getSex()==null){
			userSex.setText("未填写");
		}else{
			userSex.setText(MainActivity.getUserInfo().getSex());
		}
		//设置出生年月
		if(MainActivity.getUserInfo().getBirthday()==0){
			userBirthday.setText("0000-00-00");
		}else{
			//把userInfo里面的long类型的出生年月转换一下
			int year = (int) MainActivity.getUserInfo().getBirthday()/10000;
			int month = (int) ((MainActivity.getUserInfo().getBirthday()-year*10000)/100);
			int day = (int) (MainActivity.getUserInfo().getBirthday()-year*10000-month*100);
			userBirthday.setText(year+"-"+month+"-"+day);
		}
		userPhoneNumber.setText(MainActivity.getUserInfo().getPhoneNumber());
		if(MainActivity.getUserInfo().getEmail()==null){
			userEmail.setText("未填写");
		}else{
			userEmail.setText(MainActivity.getUserInfo().getEmail());
		}
		
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		userIcon=(ImageView) findViewById(R.id.user_icon);
		userName=(TextView) findViewById(R.id.user_name);
		userSex=(TextView) findViewById(R.id.user_sex);
		userBirthday=(TextView) findViewById(R.id.user_birthday);
		userPhoneNumber=(TextView) findViewById(R.id.user_phone_number);
		userEmail=(TextView) findViewById(R.id.user_email);
		
		changeUserIcon=(RelativeLayout) findViewById(R.id.change_user_icon);
		changeUserName=(RelativeLayout) findViewById(R.id.change_user_name);
		changeUserSex=(RelativeLayout) findViewById(R.id.change_user_sex);
		changeUserBirthday=(RelativeLayout) findViewById(R.id.change_user_birthday);
		changeUserPhoneNumber=(RelativeLayout) findViewById(R.id.change_user_phone_number);
		changeUserEmail=(RelativeLayout) findViewById(R.id.change_user_email);
		backFromUserInfo=(Button) findViewById(R.id.back_from_user_info);
		
		changeUserIcon.setOnClickListener(this);
		changeUserName.setOnClickListener(this);
		changeUserSex.setOnClickListener(this);
		changeUserBirthday.setOnClickListener(this);
		changeUserPhoneNumber.setOnClickListener(this);
		changeUserEmail.setOnClickListener(this);
		backFromUserInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.change_user_icon:
			String[] items = new String[] { "相册", "拍照" };
			// 弹出对话框
			new AlertDialog.Builder(this)
					.setTitle("设置头像")
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							File userIcon = new File(Environment
									.getExternalStorageDirectory(),
									"user_icon.jpg");
							switch (which) {
							case 0:// 选择相册
								try {
									if (userIcon.exists()) {
										userIcon.delete();
									}
									userIcon.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
//								imageUri = Uri.fromFile(userIcon);
								//获取本地资源
								Intent intentToGallery = new Intent(
										Intent.ACTION_GET_CONTENT);
								//设置资源为图片
								intentToGallery.setType("image/*");
								intentToGallery.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// 设置输出路径
								startActivityForResult(intentToGallery,Global.GRALLERY);//启动相册
								break;
							case 1:// 选择拍照
								try {
									// 如果已经存在文件则先删除掉
									if (userIcon.exists()) {
										userIcon.delete();
									}
									// 创建新的文件
									userIcon.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
								imageUri = Uri.fromFile(userIcon);
								Intent intentToTakePhoto = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								startActivityForResult(intentToTakePhoto, Global.TAKE_PHOTO);// 启动相机
								break;
							default:
								break;
							}
						}
					})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();
			break;
		case R.id.change_user_name:
			final EditText editTextForName = new EditText(this);//设置获取对话框中内容
			new AlertDialog.Builder(this).setTitle("请输入姓名：").setView(editTextForName).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(editTextForName.getText().toString().equals("")){
						Toast.makeText(UserInfoActivity.this, "请输入姓名...", Toast.LENGTH_SHORT).show();
					}else{
						userName.setText(editTextForName.getText());
						MainActivity.getUserInfo().setName(editTextForName.getText().toString());
						findMeDB.updateUserInfo(MainActivity.getUserInfo());
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
		case R.id.change_user_sex:
			new AlertDialog.Builder(this).setTitle("请选择性别：").setSingleChoiceItems(new String[] {"男","女"} , 0, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int index) {
					// TODO Auto-generated method stub
					String newSex;
					if(index==0){
						newSex="男";
					}else{
						newSex="女";
					}
					userSex.setText(newSex);
					MainActivity.getUserInfo().setSex(newSex);
					findMeDB.updateUserInfo(MainActivity.getUserInfo());
					dialog.dismiss();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
		case R.id.change_user_birthday:
			Calendar cal=Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month= cal.get(Calendar.MONTH)+1;
			int day=cal.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					
							userBirthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
							MainActivity.getUserInfo().setBirthday(year*10000+(monthOfYear+1)*100+dayOfMonth);
							findMeDB.updateUserInfo(MainActivity.getUserInfo());
						}
					}, year, month, day);
			datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());//
			datePickerDialog.getDatePicker().init(year, month, day, null);//
			datePickerDialog.setTitle("请选择您的出生年月日：");
			datePickerDialog.show();
			break;
		case R.id.change_user_phone_number:
			final EditText editTextForPhoneNumber = new EditText(this);//设置获取对话框中内容
			new AlertDialog.Builder(this).setTitle("请输入手机号码：").setView(editTextForPhoneNumber).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(editTextForPhoneNumber.getText().toString().equals("")){
						Toast.makeText(UserInfoActivity.this, "请输入手机号码...", Toast.LENGTH_SHORT).show();
					}else{
						userPhoneNumber.setText(editTextForPhoneNumber.getText());
						MainActivity.getUserInfo().setPhoneNumber(editTextForPhoneNumber.getText().toString());
						MainActivity.getUser().setPhoneNumber(editTextForPhoneNumber.getText().toString());
						findMeDB.updateUserInfo(MainActivity.getUserInfo());
						findMeDB.updateUser(MainActivity.getUser());
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
		case R.id.change_user_email:
			final EditText editTextForEmail = new EditText(this);//设置获取对话框中内容
			new AlertDialog.Builder(this).setTitle("请输入电子邮箱：").setView(editTextForEmail).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(editTextForEmail.getText().toString().equals("")){
						Toast.makeText(UserInfoActivity.this, "请输入电子邮箱...", Toast.LENGTH_SHORT).show();
					}else{
						userEmail.setText(editTextForEmail.getText());
						MainActivity.getUserInfo().setEmail(editTextForEmail.getText().toString());
						MainActivity.getUser().setEmail(editTextForEmail.getText().toString());
						findMeDB.updateUserInfo(MainActivity.getUserInfo());
						findMeDB.updateUser(MainActivity.getUser());
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
		case R.id.back_from_user_info:
			Intent intentToMain = new Intent();
			setResult(RESULT_OK, intentToMain);
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Intent intentToCrop = new Intent("com.android.camera.action.CROP");
		switch(requestCode){
		case Global.GRALLERY:
			//当选择了相册时，下面的Data要设置为data.getData()才能读取图片，如果用imageUri会出现无法加载图片的情况
			intentToCrop.setDataAndType(data.getData(), "image/*");  	  
			intentToCrop.putExtra("crop", true);// 是否允许裁剪
			intentToCrop.putExtra("scale", true);// 是否允许缩放
			// aspectX aspectY 是宽高的比例  
			intentToCrop.putExtra("aspectX", 1);  
			intentToCrop.putExtra("aspectY", 1);  
			intentToCrop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        startActivityForResult(intentToCrop, Global.GET_PHOTO);
			break;
		case Global.TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				intentToCrop.setDataAndType(imageUri, "image/*");  
		        // 设置裁剪  
				intentToCrop.putExtra("crop", true);  
				intentToCrop.putExtra("scale", true);
		        // aspectX aspectY 是宽高的比例  
				intentToCrop.putExtra("aspectX", 1);  
				intentToCrop.putExtra("aspectY", 1);  
				intentToCrop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		        startActivityForResult(intentToCrop, Global.GET_PHOTO);
			}
			break;
		case Global.GET_PHOTO:
			
			try {
				//	 把裁剪后的图片显示到头像那里
				Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
				userIcon.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

}
