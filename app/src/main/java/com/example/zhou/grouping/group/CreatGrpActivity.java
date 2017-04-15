package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.CreateNewGroupAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database;
import com.example.zhou.grouping.dao.Database.CreateNewGroup;
import com.example.zhou.grouping.httpBean.Result;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreatGrpActivity extends Activity {

    private ImageButton creatgrp_back_btn;
    private EditText groupNameText;// 群名
    private EditText groupPwdText;// 群密码
    private EditText groupConPwdText;// 再次输入群密码
    private EditText sGroupMinNumberText;// 组最小值
    private EditText sGroupMaxNumberText;// 组最大值
    private Button cancel;// 忽略
    private Button confirm;// 确认
    private TextView createconfirm;// 群账号提示确认按钮

    private String groupIDStr;// 群名
    private String groupNameStr;// 群名
    private String groupPwdStr;// 群密码
    private String groupConPwdStr;// 再次输入群密码
    private String sGroupMinNumberStr;// 组最小值
    private String sGroupMaxNumberStr;// 组最大值
    private int min = 0;
    private int max = 0;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_group);

        groupNameText = (EditText) findViewById(R.id.qunming_edit);
        groupPwdText = (EditText) findViewById(R.id.mima_edit);
        groupConPwdText = (EditText) findViewById(R.id.mima_confirm_edit);
        sGroupMinNumberText = (EditText) findViewById(R.id.min_num_limit);
        sGroupMaxNumberText = (EditText) findViewById(R.id.max_num_limit);
        myApplication = (MyApplication) getApplication();

        // 返回按钮
        creatgrp_back_btn = (ImageButton) findViewById(R.id.creatgrp_back);
        creatgrp_back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CreatGrpActivity.this.finish();

            }
        });

        // 确认按钮
        confirm = (Button) findViewById(R.id.creat_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                groupNameStr = groupNameText.getText().toString();
                groupPwdStr = groupPwdText.getText().toString();
                groupConPwdStr = groupConPwdText.getText().toString();
                sGroupMinNumberStr = sGroupMinNumberText.getText().toString();
                sGroupMaxNumberStr = sGroupMaxNumberText.getText().toString();

                if ((groupNameStr == null || groupNameStr.equalsIgnoreCase(""))
                        || (groupPwdStr == null || groupPwdStr
                        .equalsIgnoreCase(""))
                        || (groupConPwdStr == null || groupConPwdStr
                        .equalsIgnoreCase(""))
                        || (sGroupMinNumberStr == null || sGroupMinNumberStr
                        .equalsIgnoreCase(""))
                        || (sGroupMaxNumberStr == null || sGroupMaxNumberStr
                        .equalsIgnoreCase(""))) {
                    Toast.makeText(CreatGrpActivity.this, "群名、密码、分组范围都是必须的！",
                            Toast.LENGTH_SHORT).show();
                } else if (!groupPwdStr.equals(groupConPwdStr)) {
                    Toast.makeText(CreatGrpActivity.this, "两次密码不相同，请重新输入！",
                            Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        min = Integer.parseInt(sGroupMinNumberStr);
                        max = Integer.parseInt(sGroupMaxNumberStr);
                        // 创建群
                        groupIDStr = new Database().getGroupID();
                        CreateNewGroup createnewgroup = new CreateNewGroup(
                                groupIDStr, groupNameStr, groupPwdStr, min,
                                max, 1);
                        Map<String, String> options = new HashMap<>();
                        options.put("cID", myApplication.getCustomers().getcID());
                        options.put("gID", groupIDStr);
                        options.put("gName", groupNameStr);
                        options.put("gPasswd", groupPwdStr);
                        options.put("gMin", "" + min);
                        options.put("gMax", "" + max);
                        options.put("ifOpen", "" + 1);
                        options.put("ifOwner", "" + 1);
                        Retrofitutil.getmRetrofit()
                                .create(CreateNewGroupAPI.class)
                                .createNewGroup(options)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Result>() {
                                    @Override
                                    public void accept(@NonNull Result result) throws Exception {
                                        final AlertDialog dialog = new AlertDialog.Builder(
                                                CreatGrpActivity.this).create();
                                        dialog.show();
                                        dialog.getWindow()
                                                .clearFlags(
                                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                        Window window = dialog.getWindow();
                                        window.setContentView(R.layout.creategroup_messg);

                                        // 在dialog上显示群账号
                                        EditText dialogText = (EditText) window
                                                .findViewById(R.id.create_gid);
                                        dialogText.setText(groupIDStr);
                                        dialogText.setEnabled(false);
                                        createconfirm = (TextView) window
                                                .findViewById(R.id.create_confirm);
                                        createconfirm
                                                .setOnClickListener(new View.OnClickListener() {

                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            // TODO Auto-generated method stub
                                                                            dialog.dismiss();
                                                                            groupNameText.setText("");
                                                                            groupPwdText.setText("");
                                                                            groupConPwdText.setText("");
                                                                            sGroupMinNumberText.setText("");
                                                                            sGroupMaxNumberText.setText("");

                                                                        }
                                                                    }
                                                );

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        Toast.makeText(CreatGrpActivity.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(CreatGrpActivity.this, "输入类型有错，请检查！",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // 忽略按钮
        cancel = (Button) findViewById(R.id.creat_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                CreatGrpActivity.this.finish();

            }
        });
    }

}
