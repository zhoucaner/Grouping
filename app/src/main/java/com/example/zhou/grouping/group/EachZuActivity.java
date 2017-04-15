package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.dao.Database.ExitFromSGroup;
import com.example.zhou.grouping.dao.Database.IfExitInSGroup;
import com.example.zhou.grouping.dao.Database.JoinInSGroup;
import com.example.zhou.grouping.dao.Database.LoadSGroupMembers;
import com.example.zhou.grouping.dao.Database.SelectIfOpen;
import com.example.zhou.grouping.dao.Database.SelectIfsgOwnerOfSGroup;
import com.example.zhou.grouping.dao.Database.deleteSGroup;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class EachZuActivity extends Activity {

	private ImageButton eachzu_back_btn;// 返回按钮
	private Button join;// 加入按钮
	private Button quit;// 退出按钮
	private TextView sgNameText;

	private ListView mylistview;
	public static ArrayList<String> sgcustomerlist = new ArrayList<String>();
	private boolean ifjoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.each_zu);

		sgNameText = (TextView) findViewById(R.id.eachzu_name);
		sgNameText.setText("G" + Currents.currentSGroup.getSgID());

		// 是否在当前组内
		IfExitInSGroup ifexitinsgroup = new IfExitInSGroup(
				Currents.currentSGroup.getgID(),
				Currents.currentCustomer.getcID(),
				Currents.currentSGroup.getSgID());
		FutureTask<Boolean> ft1 = new FutureTask<Boolean>(ifexitinsgroup);
		Thread th1 = new Thread(ft1);
		th1.start();
		try {
			th1.join();
			Currents.ifsgroupMemberr = ft1.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 判断是组长还是成员
		SelectIfsgOwnerOfSGroup selectifsgownerofsgroup = new SelectIfsgOwnerOfSGroup(
				Currents.currentSGroup.getgID(),
				Currents.currentCustomer.getcID(),
				Currents.currentSGroup.getSgID());
		FutureTask<Boolean> ft2 = new FutureTask<Boolean>(
				selectifsgownerofsgroup);
		Thread th2 = new Thread(ft2);
		th2.start();
		try {
			th2.join();
			Currents.ifsgroupowner = ft2.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 列表
		setAdapter();

		// 返回按钮
		eachzu_back_btn = (ImageButton) findViewById(R.id.eachzu_back);
		eachzu_back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EachZuActivity.this.finish();
			}
		});

		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Currents.currentOtherCustomer.setcID(Currents.sgCustomersList
						.get(arg2).getcID());
				Intent t = new Intent(EachZuActivity.this,
						OtherMessgActivity.class);
				startActivity(t);
			}
		});

		// 加入组操作
		join = (Button) findViewById(R.id.join_eachzu);
		join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 判断当前群状态
				SelectIfOpen selectifopen = new SelectIfOpen(
						Currents.currentSGroup.getgID());
				FutureTask<Boolean> ft1 = new FutureTask<Boolean>(selectifopen);
				Thread th1 = new Thread(ft1);
				th1.start();
				try {
					th1.join();
					Currents.groupstate = ft1.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Currents.groupstate == true) {
					if (sgcustomerlist.size() < Currents.currentGroup.getgMax()) {
						JoinInSGroup joininsgroup = new JoinInSGroup(
								Currents.currentSGroup.getSgID(),
								Currents.currentCustomer.getcID(),
								Currents.currentSGroup.getgID(), 0);
						FutureTask<Boolean> ft = new FutureTask<Boolean>(
								joininsgroup);
						new Thread(ft).start();
						try {
							ifjoin = ft.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (ifjoin == false) {
							final AlertDialog dialog = new AlertDialog.Builder(
									EachZuActivity.this).create();
							dialog.show();
							dialog.getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
													| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
							Window window = dialog.getWindow();
							window.setContentView(R.layout.prompt_failure);
						} else {
							setAdapter();
							final AlertDialog dialog = new AlertDialog.Builder(
									EachZuActivity.this).create();
							dialog.show();
							dialog.getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
													| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
							Window window = dialog.getWindow();
							window.setContentView(R.layout.prompt_success);

							// ///////////
							onCreate(null);
							// //////////
						}
					} else {
						Toast.makeText(EachZuActivity.this, "该组人数已达上限。",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(EachZuActivity.this, "当前群被锁定无法操作。",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 退出组操作
		quit = (Button) findViewById(R.id.quit_eachzu);
		quit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 判断当前群状态
				SelectIfOpen selectifopen = new SelectIfOpen(
						Currents.currentSGroup.getgID());
				FutureTask<Boolean> ft1 = new FutureTask<Boolean>(selectifopen);
				Thread th1 = new Thread(ft1);
				th1.start();
				try {
					th1.join();
					Currents.groupstate = ft1.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Currents.groupstate == true) {
					if (Currents.ifsgroupMemberr == true) {
						if (Currents.ifsgroupowner == true)// 如果是组长，解散组
						{
							deleteSGroup deletesgroup = new deleteSGroup(
									Currents.currentSGroup.getgID(),
									Currents.currentSGroup.getSgID());
							FutureTask<Boolean> ft4 = new FutureTask<Boolean>(
									deletesgroup);
							Thread th4 = new Thread(ft4);
							th4.start();
							boolean ifdlete = false;
							try {
								th4.join();
								ifdlete = ft4.get();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ExecutionException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (ifdlete = true) {
								Toast.makeText(EachZuActivity.this, "删除成功.",
										Toast.LENGTH_SHORT).show();
								EachZuActivity.this.finish();
							} else {
							}

						} else {// 如果是成员退出组
							ExitFromSGroup exitfromsgroup = new ExitFromSGroup(
									Currents.currentSGroup.getgID(),
									Currents.currentCustomer.getcID(),
									Currents.currentSGroup.getSgID());
							FutureTask<Boolean> ft5 = new FutureTask<Boolean>(
									exitfromsgroup);
							Thread th5 = new Thread(ft5);
							th5.start();
							boolean ifexit = false;
							try {
								th5.join();
								ifexit = ft5.get();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ExecutionException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (ifexit = true) {
								Toast.makeText(EachZuActivity.this, "退出成功.",
										Toast.LENGTH_SHORT).show();
								setAdapter();
								if (EachZuActivity.sgcustomerlist.size() == 0) {
									EachZuActivity.this.finish();
								}
							} else {
							}
						}
					} else {
					}
				} else {
					Toast.makeText(EachZuActivity.this, "当前群被锁定无法操作。",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	// 刷新组列表
	protected void setAdapter() {
		Currents.sgCustomersList.clear();
		EachZuActivity.sgcustomerlist.clear();
		String cname;

		LoadSGroupMembers loadsgroupmembers = new LoadSGroupMembers(
				Currents.currentSGroup.getgID(),
				Currents.currentSGroup.getSgID());
		FutureTask<Boolean> ft = new FutureTask<Boolean>(loadsgroupmembers);
		Thread th = new Thread(ft);
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < Currents.sgCustomersList.size(); i++) {
			cname = Currents.sgCustomersList.get(i).getcName();
			if (i == 0) {
				EachZuActivity.sgcustomerlist.add("组长： " + cname);
			} else {
				EachZuActivity.sgcustomerlist.add("成员：  " + cname);
			}
		}
		this.mylistview = (ListView) findViewById(R.id.listview_eachzu);
		ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sgcustomerlist);
		this.mylistview.setAdapter(myArrayAdapter);
	}

	@Override
	protected void onResume() {
		this.setAdapter();
		super.onResume();
	}

}
