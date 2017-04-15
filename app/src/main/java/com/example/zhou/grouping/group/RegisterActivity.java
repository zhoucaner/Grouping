package com.example.zhou.grouping.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.RegisterAPI;
import com.example.zhou.grouping.httpBean.Result;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends Activity {

    private Button RegisterButton;// 注册按钮
    private EditText cIDText;// 用户ID
    private EditText cNameText;// 姓名
    private EditText mPasswordText;// 密码
    private EditText mConPasswordText;// 确认密码

    private String cIDStr;
    private String cNameStr;
    private String pwdStr;
    private String conPwdStr;
    private boolean ifSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cIDText = (EditText) findViewById(R.id.creat_id);
        cNameText = (EditText) findViewById(R.id.creat_account);
        mPasswordText = (EditText) findViewById(R.id.creat_pwd1);
        mPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mConPasswordText = (EditText) findViewById(R.id.creat_pwd2);
        mConPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        RegisterButton = (Button) findViewById(R.id.registerButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                cIDStr = cIDText.getText().toString();
                cNameStr = cNameText.getText().toString();
                pwdStr = mPasswordText.getText().toString();
                conPwdStr = mConPasswordText.getText().toString();

                if ((cIDStr == null || cIDStr.equalsIgnoreCase(""))
                        || (cNameStr == null || cNameStr.equalsIgnoreCase(""))
                        || (pwdStr == null || pwdStr.equalsIgnoreCase(""))
                        || (conPwdStr == null || conPwdStr.equalsIgnoreCase(""))) {
                    Toast.makeText(RegisterActivity.this,
                            "The user id、name and password are necessary.",
                            Toast.LENGTH_SHORT).show();
                } else if (!pwdStr.equals(conPwdStr)) {
                    Toast.makeText(RegisterActivity.this,
                            "Two different password, please enter again.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 开始注册

                    Map<String, String> options = new HashMap<>();
                    options.put("cID", cIDStr);
                    options.put("cName", cNameStr);
                    options.put("cPasswd", pwdStr);
                    options.put("cSex", "0");
                    options.put("cClass", "默认");
                    Retrofitutil.getmRetrofit()
                            .create(RegisterAPI.class)
                            .register(options)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Result>() {
                                @Override
                                public void accept(@NonNull Result result) throws Exception {
                                    if (result.isResult()) {
                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Register successfully. Wait for going to the login page...",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(RegisterActivity.this,
                                                LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "The customer id already exists.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    System.out.println(throwable.getMessage());
                                    Toast.makeText(RegisterActivity.this,
                                            throwable.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });

    }

}
