package com.example.findme.fragment;

import com.example.findme.R;
import com.example.findme.activity.LoginActivity;
import com.example.findme.activity.MainActivity;
import com.example.findme.activity.PublishCaseActivity;
import com.example.findme.util.Global;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;

public class FocusFragment extends Fragment implements View.OnClickListener{
	
	//发布按钮
	private Button publishButton;
	
	//没有关注时的文字显示
	private TextView noFocusTextView;
	
	//关注列表
//	private ListView 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View focusLayout = inflater.inflate(R.layout.focus_layout, container, false);
		return focusLayout;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews(){
        publishButton = (Button) getView().findViewById(R.id.publish_btn);
        publishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.publish_btn:
                if(MainActivity.getUserInfo() == null ){
                    Global.toLogin(this);
                }else{
                    Intent intentToPublish = new Intent(getActivity(), PublishCaseActivity.class);
                    getActivity().startActivityFromFragment(FocusFragment.this, intentToPublish, Global.PUBLISH);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ( requestCode ){
            case Global.PUBLISH:
                break;
            default:
                break;
        }
    }

    private void loadCaseInfo(){

    }

}
