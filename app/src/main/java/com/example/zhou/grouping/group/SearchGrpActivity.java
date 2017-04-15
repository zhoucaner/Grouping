package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.JoinGroupAPI;
import com.example.zhou.grouping.api.SearchGrpAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.httpBean.GroupBean;
import com.example.zhou.grouping.httpBean.JoinInGroupResult;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchGrpActivity extends Activity {

    private ImageButton searchgrp_back_btn;// 返回按钮
    private ImageButton searchgrp_clear_btn;// 清楚按钮
    private ImageButton searchgrp_btn;// 查找按钮
    private EditText groupIDText;// 群号

    private String groupIDStr;// 输入的要查找的群号

    public static ArrayList<String> grouplist = new ArrayList<String>();

    private boolean ifexist;
    private boolean ifjoined;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group);

        groupIDText = (EditText) findViewById(R.id.searchgrp_edittext);// 获取群号
        myApplication = (MyApplication) getApplication();

        // 返回按钮
        searchgrp_back_btn = (ImageButton) findViewById(R.id.searchgrp_back);
        searchgrp_back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SearchGrpActivity.this.finish();
            }
        });

        // 清除按钮
        searchgrp_clear_btn = (ImageButton) findViewById(R.id.searchgrp_delete);
        searchgrp_clear_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                groupIDText.setText("");
            }
        });

        // 搜索群按钮
        searchgrp_btn = (ImageButton) findViewById(R.id.searchgrp_search);
        searchgrp_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                groupIDStr = groupIDText.getText().toString();

                Retrofitutil.getmRetrofit()
                        .create(SearchGrpAPI.class)
                        .searchGrp(groupIDStr)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GroupBean>() {
                            @Override
                            public void accept(@NonNull GroupBean groupBean) throws Exception {
                                if (groupBean != null) {
                                    final EditText editText = new EditText(SearchGrpActivity.this);
                                    AlertDialog dialog = new AlertDialog.Builder(SearchGrpActivity.this)
                                            .setTitle("请输入验证码")
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setView(editText)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {
                                                    Map<String, String> options = new HashMap<>();
                                                    options.put("gID", groupIDStr);
                                                    options.put("cID", myApplication.getCustomers().getcID());
                                                    options.put("gPasswd", editText.getText().toString());
                                                    options.put("ifOwner", "0");
                                                    Retrofitutil.getmRetrofit()
                                                            .create(JoinGroupAPI.class)
                                                            .joinInGroup(options)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Consumer<JoinInGroupResult>() {
                                                                @Override
                                                                public void accept(@NonNull JoinInGroupResult result) throws Exception {
                                                                    if (result.getResult().equals("6")) {
                                                                        Toast.makeText(
                                                                                getApplicationContext(),
                                                                                "加入成功",
                                                                                Toast.LENGTH_SHORT)
                                                                                .show();
                                                                        dialog.dismiss();
//																	dialog1.dismiss();
                                                                    } else if (result.getResult().equals("0")) {
                                                                        Toast.makeText(SearchGrpActivity.this,
                                                                                "当前群被锁定无法操作。", Toast.LENGTH_SHORT).show();
                                                                    } else if (result.getResult().equals("1")) {
                                                                        Toast.makeText(SearchGrpActivity.this,
                                                                                "密码错误。", Toast.LENGTH_SHORT).show();
                                                                    } else if (result.getResult().equals("2")) {
                                                                        Toast.makeText(SearchGrpActivity.this,
                                                                                "你已加入该群。", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(@NonNull Throwable throwable) throws Exception {
                                                                    Toast.makeText(SearchGrpActivity.this,
                                                                            throwable.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "不存在该群",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });

    }
}
