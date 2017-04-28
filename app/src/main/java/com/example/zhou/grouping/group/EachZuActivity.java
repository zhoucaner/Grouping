package com.example.zhou.grouping.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.DeleteSGroupAPI;
import com.example.zhou.grouping.api.ExitFromSGroupAPI;
import com.example.zhou.grouping.api.JoinInSGroupAPI;
import com.example.zhou.grouping.api.LoadSGroupMembersAPI;
import com.example.zhou.grouping.api.SelectSgIDAndIfOpenAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.httpBean.NumberResult;
import com.example.zhou.grouping.httpBean.ExitFromSGroupResult;
import com.example.zhou.grouping.httpBean.JoinInSGroupResult;
import com.example.zhou.grouping.httpBean.LoadSGroupMembers;
import com.example.zhou.grouping.httpBean.SelectSgIDAndIfOpen;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EachZuActivity extends Activity {

    private ImageButton eachzu_back_btn;// 返回按钮
    private Button join;// 加入按钮
    private Button quit;// 退出按钮
    private TextView sgNameText;

    private ListView mylistview;
    public static ArrayList<String> sgcustomerlist = new ArrayList<String>();
    private boolean ifjoin;
    private String sgID;
    private String gID;
    private ArrayAdapter<String> myArrayAdapter;
    private MyApplication myApplication;
    private boolean ifsgOwnerFlag = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.each_zu);

        sgID = getIntent().getStringExtra("sgID");
        gID = getIntent().getStringExtra("gID");
        sgNameText = (TextView) findViewById(R.id.eachzu_name);
        join = (Button) findViewById(R.id.join_eachzu);
        quit = (Button) findViewById(R.id.quit_eachzu);
        join.setVisibility(View.INVISIBLE);
        quit.setVisibility(View.INVISIBLE);

        sgNameText.setText("G" + sgID);
        myArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sgcustomerlist);
        this.mylistview = (ListView) findViewById(R.id.listview_eachzu);
        this.mylistview.setAdapter(myArrayAdapter);
        myApplication = (MyApplication) getApplication();

        // 判断是组长还是成员
//		final SelectIfsgOwnerOfSGroup selectifsgownerofsgroup = new SelectIfsgOwnerOfSGroup(
//				sgID,
//				Currents.currentCustomer.getcID(),
//				Currents.currentSGroup.getSgID());
//		FutureTask<Boolean> ft2 = new FutureTask<Boolean>(
//				selectifsgownerofsgroup);
//		Thread th2 = new Thread(ft2);
//		th2.start();
//		try {
//			th2.join();
//			Currents.ifsgroupowner = ft2.get();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ExecutionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

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

        Retrofitutil.getmRetrofit()
                .create(SelectSgIDAndIfOpenAPI.class)
                .selectSgIDAndIfOpen(gID, myApplication.getCustomers().getcID(), sgID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SelectSgIDAndIfOpen>() {
                    @Override
                    public void accept(@NonNull SelectSgIDAndIfOpen selectSgIDAndIfOpen) throws Exception {

                        if (selectSgIDAndIfOpen.getIfOpen().equals("1")) {
                            if (selectSgIDAndIfOpen.getSgid().equals("0")) {
                                if (selectSgIDAndIfOpen.getIfToMax().equals("0"))
                                    join.setVisibility(View.VISIBLE);
                            } else {
                                if (selectSgIDAndIfOpen.getSgid().equals(sgID)) {
                                    quit.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(EachZuActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
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
        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Map<String, String> options = new HashMap<>();
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
                            }
                        });

/*
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
								sgID, 0);
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
				}*/

            }
        });

        // 退出组操作
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> options = new HashMap<>();
                options.put("gID", gID);
                options.put("cID", myApplication.getCustomers().getcID());
                options.put("sgID", sgID);
                if (ifsgOwnerFlag) {
                    Retrofitutil.getmRetrofit()
                            .create(DeleteSGroupAPI.class)
                            .deleteSGroup(gID,sgID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<NumberResult>() {
                                @Override
                                public void accept(@NonNull NumberResult numberResult) throws Exception {
                                    if (numberResult.getResult().equals("6")) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "解散成功",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        EachZuActivity.this.finish();
                                    } else if (numberResult.getResult().equals("0")) {
                                        Toast.makeText(EachZuActivity.this,
                                                "当前群被锁定无法操作。", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    Toast.makeText(EachZuActivity.this,
                                            throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Retrofitutil.getmRetrofit()
                            .create(ExitFromSGroupAPI.class)
                            .exitFromSGroup(options)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ExitFromSGroupResult>() {
                                @Override
                                public void accept(@NonNull ExitFromSGroupResult exitFromSGroupResult) throws Exception {
                                    if (exitFromSGroupResult.getResult().equals("6")) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "退出成功",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        join.setVisibility(View.VISIBLE);
                                        quit.setVisibility(View.INVISIBLE);
                                        setAdapter();
                                    } else if (exitFromSGroupResult.getResult().equals("0")) {
                                        Toast.makeText(EachZuActivity.this,
                                                "当前群被锁定无法操作。", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    Toast.makeText(EachZuActivity.this,
                                            throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                    // TODO Auto-generated method stub
                    // 判断当前群状态
//                SelectIfOpen selectifopen = new SelectIfOpen(
//                        Currents.currentSGroup.getgID());
//                FutureTask<Boolean> ft1 = new FutureTask<Boolean>(selectifopen);
//                Thread th1 = new Thread(ft1);
//                th1.start();
//                try {
//                    th1.join();
//                    Currents.groupstate = ft1.get();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                if (Currents.groupstate == true) {
//                    if (Currents.ifsgroupMemberr == true) {
//                        if (Currents.ifsgroupowner == true)// 如果是组长，解散组
//                        {
//                            deleteSGroup deletesgroup = new deleteSGroup(
//                                    sgID,
//                                    Currents.currentSGroup.getSgID());
//                            FutureTask<Boolean> ft4 = new FutureTask<Boolean>(
//                                    deletesgroup);
//                            Thread th4 = new Thread(ft4);
//                            th4.start();
//                            boolean ifdlete = false;
//                            try {
//                                th4.join();
//                                ifdlete = ft4.get();
//                            } catch (InterruptedException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            } catch (ExecutionException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            }
//                            if (ifdlete = true) {
//                                Toast.makeText(EachZuActivity.this, "删除成功.",
//                                        Toast.LENGTH_SHORT).show();
//                                EachZuActivity.this.finish();
//                            } else {
//                            }
//
//                        } else {// 如果是成员退出组
//                            ExitFromSGroup exitfromsgroup = new ExitFromSGroup(
//                                    sgID,
//                                    Currents.currentCustomer.getcID(),
//                                    Currents.currentSGroup.getSgID());
//                            FutureTask<Boolean> ft5 = new FutureTask<Boolean>(
//                                    exitfromsgroup);
//                            Thread th5 = new Thread(ft5);
//                            th5.start();
//                            boolean ifexit = false;
//                            try {
//                                th5.join();
//                                ifexit = ft5.get();
//                            } catch (InterruptedException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            } catch (ExecutionException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            }
//                            if (ifexit = true) {
//                                Toast.makeText(EachZuActivity.this, "退出成功.",
//                                        Toast.LENGTH_SHORT).show();
//                                setAdapter();
//                                if (EachZuActivity.sgcustomerlist.size() == 0) {
//                                    EachZuActivity.this.finish();
//                                }
//                            } else {
//                            }
//                        }
//                    } else {
//                    }
//                } else {
//                    Toast.makeText(EachZuActivity.this, "当前群被锁定无法操作。",
//                            Toast.LENGTH_SHORT).show();
//                }

                }
            }});
    }

    // 刷新组列表
    protected void setAdapter() {
        Retrofitutil.getmRetrofit()
                .create(LoadSGroupMembersAPI.class)
                .loadSGroupMembers(gID, sgID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LoadSGroupMembers>>() {
                    @Override
                    public void accept(@NonNull List<LoadSGroupMembers> loadSGroupMemberses) throws Exception {
                        EachZuActivity.sgcustomerlist.clear();
                        for (int i = 0; i < loadSGroupMemberses.size(); i++) {
                            if (i == 0) {
                                sgcustomerlist.add("组长： " + loadSGroupMemberses.get(i).getCName());
                                if (myApplication.getCustomers().getcID().equals(loadSGroupMemberses.get(i).getCID())) {
                                    ifsgOwnerFlag = true;
                                }
                            } else {
                                sgcustomerlist.add("组员： " + loadSGroupMemberses.get(i).getCName());

                            }

                        }
                        if (ifsgOwnerFlag) {
                            quit.setText("解散组");
                        } else {
                            quit.setText("退出组");

                        }
                        myArrayAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(EachZuActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


    }

    @Override
    protected void onResume() {
        this.setAdapter();
        super.onResume();
    }

}
