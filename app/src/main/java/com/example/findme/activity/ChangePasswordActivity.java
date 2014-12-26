package com.example.findme.activity;

import com.example.findme.R;
import com.example.findme.db.FindMeDB;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

	private EditText newPassword;
	
	private EditText newPasswordConfirm;
	
	private Button confirm;
	
	private FindMeDB findMeDB;
	
	private SharedPreferences pref;
	
	private SharedPreferences.Editor editor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_password);
		findMeDB=FindMeDB.getInstance(this);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		newPassword=(EditText) findViewById(R.id.new_password);
		newPasswordConfirm=(EditText) findViewById(R.id.new_password_confirm);
		confirm=(Button) findViewById(R.id.confirm_from_change_password);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password1 = newPassword.getText().toString();
				String password2 = newPasswordConfirm.getText().toString();
				if(password1.equals(password2)){
					Intent intent = getIntent();
					MainActivity.getUser().setPassword(password1);
					findMeDB.updateUser(MainActivity.getUser());
					editor = pref.edit();
					editor.putString("password", MainActivity.getUser().getPassword());
					Toast.makeText(ChangePasswordActivity.this, "更改密码成功！", Toast.LENGTH_LONG);
					finish();
				}else{
					Toast.makeText(ChangePasswordActivity.this, "两次密码需要一致，请重新输入...", Toast.LENGTH_SHORT).show();
					newPassword.setText("");
					newPasswordConfirm.setText("");
				}
				
			}
		});
	}
}
