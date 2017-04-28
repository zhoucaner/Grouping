package com.example.zhou.grouping.group;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database.LoadPersonalInfo;

import java.util.List;
import java.util.concurrent.FutureTask;

public class OtherMessgActivity extends Activity {

	private ImageButton permessgbackbtn;// 返回按钮
	private MyApplication myApplication;


	private TextView otheridtext;
	private TextView othernametext;
	private TextView othersextext;
	private TextView otherclasstext;
	private TextView otheremailtext;
	private TextView otherinfotext;

	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_messg);

		// 返回
		permessgbackbtn = (ImageButton) findViewById(R.id.per_messg_back);
		permessgbackbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OtherMessgActivity.this.finish();

			}
		});
		myApplication = (MyApplication) getApplication();
		otheridtext = (TextView) findViewById(R.id.other_cid);
		othernametext = (TextView) findViewById(R.id.other_cname);
		otherclasstext = (TextView) findViewById(R.id.other_cclass);
		othersextext = (TextView) findViewById(R.id.other_sex);
		otheremailtext = (TextView) findViewById(R.id.other_email_edit);
		otherinfotext = (TextView) findViewById(R.id.other_introduction);


		// 获取当前要查看的人的信息，接口
		LoadPersonalInfo loadpersonalinfo = new LoadPersonalInfo(
				Currents.currentOtherCustomer.getcID());
		FutureTask<Boolean> ft = new FutureTask<Boolean>(loadpersonalinfo);
		Thread th = new Thread(ft);
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		otheridtext.setText(myApplication.getCustomers().getcID());
		othernametext.setText(myApplication.getCustomers().getcName());
		otherclasstext.setText("" + Currents.currentOtherCustomer.getcClass());
		int s = Currents.currentOtherCustomer.getcSex();
		if (s == 1) {
			othersextext.setText("男");
		} else {
			othersextext.setText("女");
		}

	}

}
