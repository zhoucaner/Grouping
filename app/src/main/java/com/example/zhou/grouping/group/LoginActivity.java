package com.example.zhou.grouping.group;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhou.grouping.Bean.Customers;
import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.LoginAPI;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.httpBean.UserBean;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends Activity {

    private Button login_button;
    private TextView register_text;

    private AutoCompleteTextView cidAuto;// 账号,带提示
    private EditText pwdText;// 密码
    private boolean ifFit;// 判断是否密码符合

    CheckBox savePasswordCB;// 是否记住密码选项
    SharedPreferences sp;// 存储一些轻量级的数据
    private String cIDStr;
    private String pwdStr;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myApplication = (MyApplication) getApplication();
        cidAuto = (AutoCompleteTextView) findViewById(R.id.edittext_id);
        pwdText = (EditText) findViewById(R.id.edittext_pwd);

        sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        savePasswordCB = (CheckBox) findViewById(R.id.select_password);// 是否记住密码选项
        savePasswordCB.setChecked(true);// 默认为不记住密码
        cidAuto.setThreshold(1);// 输入1个字母就开始自动提示
        pwdText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
        // 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91

        cidAuto.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String[] allCustomerName = new String[sp.getAll().size()];
                // sp.getAll().size()返回的是有多少个键值对
                allCustomerName = sp.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        LoginActivity.this, R.layout.login_userid_item,
                        allCustomerName);

                cidAuto.setAdapter(adapter);// 设置数据适配器

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                pwdText.setText(sp.getString(cidAuto.getText().toString(), ""));// 自动输入密码

            }
        });

        login_button = (Button) findViewById(R.id.loginInButton);
        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cIDStr = cidAuto.getText().toString();
                pwdStr = pwdText.getText().toString();

                if ((cIDStr == null || cIDStr.equalsIgnoreCase(""))
                        || (pwdStr == null || pwdStr.equalsIgnoreCase(""))) {
                    Toast.makeText(LoginActivity.this, "账号、密码不能为空！",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Retrofitutil.getmRetrofit()
                            .create(LoginAPI.class)
                            .login(cIDStr)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<UserBean>() {
                                @Override
                                public void accept(@NonNull UserBean userBean) throws Exception {
                                    if (userBean != null) {
                                        if (!userBean.getCPasswd().equals(pwdStr)) {
                                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (savePasswordCB.isChecked()) {// 登陆成功才保存密码
                                                sp.edit().putString(cIDStr, pwdStr).apply();
                                            }
                                            Customers customers= new Customers();
                                            customers.setcID(cIDStr);
                                            customers.setcName(userBean.getCName());
                                            customers.setcPasswd(pwdStr);
                                            customers.setcClass(userBean.getCClass());
                                            customers.setcMail(userBean.getEmail());
                                            customers.setInfo(userBean.getcInfo());
                                            myApplication.setCustomers(customers);
                                            Toast.makeText(LoginActivity.this, "登录成功，加载中...",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(LoginActivity.this,
                                                    MainActivity.class);
                                            startActivity(i);
                                        }
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    Toast.makeText(LoginActivity.this, throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        register_text = (TextView) findViewById(R.id.TextViewRegister);
        register_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent t = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(t);

            }
        });
    }

}
