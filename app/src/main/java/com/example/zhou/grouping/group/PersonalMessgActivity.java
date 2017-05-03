package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.UpdatePersonalMsgAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database.UpdatePassword;
import com.example.zhou.grouping.httpBean.Result;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PersonalMessgActivity extends Activity {

	private ImageButton permessgbackbtn;// 返回按钮
	private TextView peruserbtn;
	private TextView modifypwdbtn;// 更改密码
	private TextView yuanpwdconfirm;// 原密码
	private TextView newpwdconfirm;// 新密码
	private TextView modifyusernametext;
	private TextView modifyusernameconfirm;
	private TextView modifyclasstext;
	private TextView modifyclassconfirm;
	private TextView modifyemailconfirm;
	private TextView modifyinfoconfirm;
	private EditText yuanpwdedt;
	private EditText newpwdedt1;
	private EditText newpwdedt2;
    private TextView modifyidtext;
    private TextView modifyemailtext;
    private TextView modifyinfotext;
    private Button edit;// 加入按钮




    private String yuanpwdstr;
	private String newpwdstr1;
	private String newpwdstr2;
	private int csex;
	private boolean editFlag = false;

	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private MyApplication myApplication;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_messg);

		spinner = (Spinner) findViewById(R.id.spinner);//性别下拉列表
        modifyidtext = (TextView) findViewById(R.id.per_userid_edit);//用户ID
        modifyusernametext = (TextView) findViewById(R.id.per_username_edit);//用户名
        modifyclasstext = (TextView) findViewById(R.id.per_class_edit);//行政班
        modifyemailtext = (TextView) findViewById(R.id.per_email_edit);//邮箱
        modifyinfotext = (TextView) findViewById(R.id.other_introduction);//个人简介
        edit = (Button) findViewById(R.id.editing);//编辑button



        // 数据
		data_list = new ArrayList<String>();
		data_list.add("女");
		data_list.add("男");

		// 适配器
		arr_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, data_list);
		// 设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter);

		// 返回
		permessgbackbtn = (ImageButton) findViewById(R.id.per_messg_back);
		permessgbackbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PersonalMessgActivity.this.finish();

			}
		});

		peruserbtn = (TextView) findViewById(R.id.per_userid_edit);
		peruserbtn.setText(Currents.currentCustomer.getcID());
		myApplication = (MyApplication) getApplication();



        //load个人信息
        modifyidtext.setText(myApplication.getCustomers().getcID());
        modifyusernametext.setText(myApplication.getCustomers().getcName());
        int position = 0;
        if(myApplication.getCustomers().getcSex() ==1)
            position = 1;
        spinner.setSelection(position);
        modifyclasstext.setText(myApplication.getCustomers().getcClass());
        modifyemailtext.setText(myApplication.getCustomers().getcMail());
        modifyinfotext.setText(myApplication.getCustomers().getInfo());


        // 编辑个人信息
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				if(!editFlag) {
					modifyusernametext.setEnabled(true);
					spinner.setEnabled(true);
					modifyclasstext.setEnabled(true);
					modifyemailtext.setEnabled(true);
					modifyinfotext.setEnabled(true);
					edit.setText("保存");
					editFlag = true;
				}
				else{
					Map<String, String> options = new HashMap<>();
					options.put("cID", myApplication.getCustomers().getcID());
					options.put("cName", modifyusernametext.getText().toString());
					if(spinner.getSelectedItem().toString().equals("女"))
						options.put("cSex", "0");
					else
						options.put("cSex", "1");
					options.put("cClass", modifyclasstext.getText().toString());
					options.put("cInfo", modifyinfotext.getText().toString());
					options.put("email", modifyemailtext.getText().toString());
					Retrofitutil.getmRetrofit()
							.create(UpdatePersonalMsgAPI.class)
							.updatePersonalMsg(options)
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Consumer<Result>() {
								@Override
								public void accept(@NonNull Result result) throws Exception {
										modifyusernametext.setEnabled(false);
										spinner.setEnabled(false);
										modifyclasstext.setEnabled(false);
										modifyemailtext.setEnabled(false);
										modifyinfotext.setEnabled(false);
										edit.setText("编辑");
										editFlag = false;
									Toast.makeText(
											getApplicationContext(),
											"修改成功",
											Toast.LENGTH_SHORT)
											.show();
								}
							}, new Consumer<Throwable>() {
								@Override
								public void accept(@NonNull Throwable throwable) throws Exception {
									Toast.makeText(PersonalMessgActivity.this,
											throwable.getMessage(), Toast.LENGTH_SHORT).show();
								}
							});


				}
              /*  Map<String, String> options = new HashMap<>();
                options.put("gID", gID);
                options.put("cID", myApplication.getCustomers().getcID());
                options.put("sgID", sgID);
                options.put("ifsgOwner", "0");
                Retrofitutil.getmRetrofit()
                        .create(JoinInSGroupAPI.class)
                        .joinInSGroup(options)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<JoinInSGroupResult>() {
                            @Override
                            public void accept(@NonNull JoinInSGroupResult joinInSGroupResult) throws Exception {
                                if (joinInSGroupResult.getResult().equals("6")) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "加入成功",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    join.setVisibility(View.INVISIBLE);
                                    quit.setVisibility(View.VISIBLE);
                                    setAdapter();
//																	dialog1.dismiss();
                                } else if (joinInSGroupResult.getResult().equals("0")) {
                                    Toast.makeText(EachZuActivity.this,
                                            "当前群被锁定无法操作。", Toast.LENGTH_SHORT).show();
                                } else if (joinInSGroupResult.getResult().equals("1")) {
                                    Toast.makeText(EachZuActivity.this,
                                            "该组人数已达上限。", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Toast.makeText(EachZuActivity.this,
                                        throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }*/
                        }}
            );


		// 修改用户名
	//	modifyusernametext.setText(myApplication.getCustomers().getcID());
		modifyusernametext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(
						PersonalMessgActivity.this).create();
				dialog.show();
				dialog.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.modify_username);

				// 在dialog上显示原用户名
				String str = modifyusernametext.getText().toString();
				final EditText dialogText = (EditText) window
						.findViewById(R.id.new_username_edit);
				dialogText.setText(str);

				modifyusernameconfirm = (TextView) window
						.findViewById(R.id.new_username_confirm);
				modifyusernameconfirm
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
                                modifyusernametext.setText(dialogText.getText());
                                dialog.dismiss();
							}
						});

			}
		});

		// 修改行政班
	//	modifyclasstext.setText(Currents.currentCustomer.getcClass());
		modifyclasstext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(
						PersonalMessgActivity.this).create();
				dialog.show();
				dialog.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.modify_class);


				String str = modifyclasstext.getText().toString();
				final EditText dialogText = (EditText) window
						.findViewById(R.id.new_class_edit);
				dialogText.setText(str);

				modifyclassconfirm = (TextView) window
						.findViewById(R.id.new_class_confirm);
				modifyclassconfirm
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
                                modifyclasstext.setText(dialogText.getText());
                                dialog.dismiss();

							}
						});

			}
		});


		// 修改邮箱
		modifyemailtext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(
						PersonalMessgActivity.this).create();
				dialog.show();
				dialog.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.modify_email);


				String str = modifyemailtext.getText().toString();
				final EditText dialogText = (EditText) window
						.findViewById(R.id.new_email_edit);
				dialogText.setText(str);

				modifyemailconfirm = (TextView) window
						.findViewById(R.id.new_email_confirm);
				modifyemailconfirm
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								modifyemailtext.setText(dialogText.getText());
								dialog.dismiss();

							}
						});

			}
		});


		// 修改个人简介
		modifyinfotext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(
						PersonalMessgActivity.this).create();
				dialog.show();
				dialog.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.modify_info);


				String str = modifyinfotext.getText().toString();
				final EditText dialogText = (EditText) window
						.findViewById(R.id.new_info_edit);
				dialogText.setText(str);

				modifyinfoconfirm = (TextView) window
						.findViewById(R.id.new_info_confirm);
				modifyinfoconfirm
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								modifyinfotext.setText(dialogText.getText());
								dialog.dismiss();

							}
						});

			}
		});




		spinner.setEnabled(false);

		// 修改密码
		modifypwdbtn = (TextView) findViewById(R.id.per_pwd_edit);
		modifypwdbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(
						PersonalMessgActivity.this).create();
				dialog.show();
				dialog.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.verify_pwd);

				yuanpwdedt = (EditText) window.findViewById(R.id.yuan_pwd_edit);

				yuanpwdconfirm = (TextView) window
						.findViewById(R.id.yuan_pwd_confirm);
				yuanpwdconfirm.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						yuanpwdstr = yuanpwdedt.getText().toString();
						if (yuanpwdstr.equals(Currents.currentCustomer
								.getcPasswd())) {
							dialog.dismiss();
							final AlertDialog dialog1 = new AlertDialog.Builder(
									PersonalMessgActivity.this).create();
							dialog1.show();
							dialog1.getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
													| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
							Window window1 = dialog1.getWindow();
							window1.setContentView(R.layout.confirm_pwd);

							newpwdedt1 = (EditText) window1
									.findViewById(R.id.new_pwd_edit);

							newpwdedt2 = (EditText) window1
									.findViewById(R.id.new_pwd_edit2);

							newpwdconfirm = (TextView) window1
									.findViewById(R.id.new_pwd_confirm);

							newpwdconfirm
									.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated
											// method stub
											newpwdstr1 = newpwdedt1.getText()
													.toString();
											newpwdstr2 = newpwdedt2.getText()
													.toString();
											if (newpwdstr1.equals(newpwdstr2)) {
												boolean ifsuccess = false;
												UpdatePassword updatepassword = new UpdatePassword(
														Currents.currentCustomer
																.getcID(),
														newpwdstr1);
												FutureTask<Boolean> ft = new FutureTask<Boolean>(
														updatepassword);
												Thread th = new Thread(ft);
												th.start();
												try {
													th.join();
													ifsuccess = ft.get();
												} catch (InterruptedException e) {
													// TODO Auto-generated catch
													e.printStackTrace();
												} catch (ExecutionException e) {
													// TODO Auto-generated catch
													e.printStackTrace();
												}
												if (ifsuccess == true) {
													dialog1.dismiss();
													Toast.makeText(
															getApplicationContext(),
															"修改成功" + newpwdstr1
																	+ ifsuccess,
															Toast.LENGTH_SHORT)
															.show();
													Currents.currentCustomer
															.setcPasswd(newpwdstr1);
												} else {
													Toast.makeText(
															getApplicationContext(),
															"修改失败" + newpwdstr1
																	+ ifsuccess,
															Toast.LENGTH_SHORT)
															.show();
												}
											} else {
												dialog.dismiss();
												Toast.makeText(
														getApplicationContext(),
														"两次密码不相同，请重新输入。",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});

						} else {
							Toast.makeText(getApplicationContext(),
									"密码错误，请重新输入。", Toast.LENGTH_SHORT).show();
						}
					}
				});

			}
		});
	}

}
