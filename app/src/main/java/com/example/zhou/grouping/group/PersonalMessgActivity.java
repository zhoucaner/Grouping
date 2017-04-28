package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database.UpdateName;
import com.example.zhou.grouping.dao.Database.UpdatePassword;
import com.example.zhou.grouping.dao.Database.UpdatecClass;
import com.example.zhou.grouping.dao.Database.UpdatecSex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
	private EditText yuanpwdedt;
	private EditText newpwdedt1;
	private EditText newpwdedt2;

	private String yuanpwdstr;
	private String newpwdstr1;
	private String newpwdstr2;
	private int csex;

	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private MyApplication myApplication;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_messg);

		spinner = (Spinner) findViewById(R.id.spinner);
		// 数据
		data_list = new ArrayList<String>();
		data_list.add("女");
		data_list.add("男");

		// 适配器
		arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data_list);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

		// 修改用户名
		modifyusernametext = (TextView) findViewById(R.id.per_username_edit);
		modifyusernametext.setText(myApplication.getCustomers().getcID());
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
								String newnamestr = dialogText.getText()
										.toString();
								boolean ifsuccess = false;
								UpdateName updatename = new UpdateName(
										Currents.currentCustomer.getcID(),
										newnamestr);
								FutureTask<Boolean> ft = new FutureTask<Boolean>(
										updatename);
								Thread th = new Thread(ft);
								th.start();

								try {
									th.join();
									ifsuccess = ft.get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (ifsuccess == true) {
									dialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"修改成功", Toast.LENGTH_SHORT).show();
									modifyusernametext.setText(newnamestr);
									Currents.currentCustomer
											.setcName(newnamestr);
								} else {
									dialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"修改失败", Toast.LENGTH_SHORT).show();
								}
							}
						});

			}
		});

		// 修改行政班
		modifyclasstext = (TextView) findViewById(R.id.per_class_edit);
		modifyclasstext.setText(Currents.currentCustomer.getcClass());
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

				// 在dialog上显示原用户名
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
								String newclassstr = dialogText.getText()
										.toString();
								boolean ifsuccess = false;
								UpdatecClass updatecclass = new UpdatecClass(
										Currents.currentCustomer.getcID(),
										newclassstr);
								FutureTask<Boolean> ft = new FutureTask<Boolean>(
										updatecclass);
								Thread th = new Thread(ft);
								th.start();
								try {
									th.join();
									ifsuccess = ft.get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (ifsuccess == true) {
									dialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"修改成功", Toast.LENGTH_SHORT).show();
									modifyclasstext.setText(newclassstr);
									Currents.currentCustomer
											.setcClass(newclassstr);
								} else {
									dialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"修改失败", Toast.LENGTH_SHORT).show();
								}
							}
						});

			}
		});

		// 修改性别
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setSelection(Currents.currentCustomer.getcSex(), true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				boolean ifsuccess = false;
				UpdatecSex updatecsex = new UpdatecSex(Currents.currentCustomer
						.getcID(), arg2);
				FutureTask<Boolean> ft = new FutureTask<Boolean>(updatecsex);
				Thread th = new Thread(ft);
				th.start();
				try {
					th.join();
					ifsuccess = ft.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (ifsuccess == true) {
					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_SHORT).show();
					Currents.currentCustomer.setcSex(arg2);
				} else {
					Toast.makeText(getApplicationContext(), "修改失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

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
