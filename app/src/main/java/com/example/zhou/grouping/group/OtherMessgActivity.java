package com.example.zhou.grouping.group;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.LoadPersonalInfoAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database.LoadPersonalInfo;
import com.example.zhou.grouping.httpBean.LoadPersonalInfoResult;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.List;
import java.util.concurrent.FutureTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
	private String cID;

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
		othernametext = (TextView) findViewById(R.id.other_cName);
		otherclasstext = (TextView) findViewById(R.id.other_cclass);
		othersextext = (TextView) findViewById(R.id.other_sex);
		otheremailtext = (TextView) findViewById(R.id.other_email_edit);
		otherinfotext = (TextView) findViewById(R.id.other_introduction);
		cID = getIntent().getStringExtra("cID");


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
		Retrofitutil.getmRetrofit()
				.create(LoadPersonalInfoAPI.class)
				.loadPersonalInfo(cID)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<LoadPersonalInfoResult>() {
					@Override
					public void accept(@NonNull LoadPersonalInfoResult loadPersonalInfoResult) throws Exception {
						otheridtext.setText(cID);
						othernametext.setText(loadPersonalInfoResult.getCName());
						if (loadPersonalInfoResult.getCSex().equals("1")) {
							othersextext.setText("男");
						} else {
							othersextext.setText("女");
						}
						otherclasstext.setText(loadPersonalInfoResult.getCClass());
						otheremailtext.setText(loadPersonalInfoResult.getEmail());
						otherinfotext.setText(loadPersonalInfoResult.getCInfo());

					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(@NonNull Throwable throwable) throws Exception {
						Toast.makeText(OtherMessgActivity.this,throwable.getMessage(),Toast.LENGTH_LONG).show();


					}
				});
	}

}
