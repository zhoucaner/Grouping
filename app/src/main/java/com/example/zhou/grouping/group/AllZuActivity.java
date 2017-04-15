package com.example.zhou.grouping.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.zhou.grouping.R;
import com.example.zhou.grouping.api.IfOwnerOfGroup;
import com.example.zhou.grouping.application.MyApplication;
import com.example.zhou.grouping.dao.Database;
import com.example.zhou.grouping.dao.Database.CreateNewSGroup;
import com.example.zhou.grouping.dao.Database.ExitFromGroup;
import com.example.zhou.grouping.dao.Database.JoinInSGroup;
import com.example.zhou.grouping.dao.Database.LoadGroupMembers;
import com.example.zhou.grouping.dao.Database.SelectIfOpen;
import com.example.zhou.grouping.dao.Database.SelectMaxSGroupID;
import com.example.zhou.grouping.dao.Database.SelectNotInSGroup;
import com.example.zhou.grouping.dao.Database.UpdateIfOpen;
import com.example.zhou.grouping.dao.Database.deleteGroup;
import com.example.zhou.grouping.httpBean.LoadGroups;
import com.example.zhou.grouping.httpBean.Result;
import com.example.zhou.grouping.retrofitUtil.Retrofitutil;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AllZuActivity extends Activity {

    private ImageButton allzu_back_btn;// 返回按钮
    private ImageButton allzu_more_btn;// 更多

    private PopupWindow popupwindow;

    private TextView qunfenzu;// 群分组按钮
    private TextView qunchengyuan;// 群成员按钮
    private TextView TextViewCreatZu;
    private TextView creatzuconfirmbtn;
    private TextView DeleteGroup;
    private TextView TextViewgrpmessg;
    private TextView GroupNameText;
    private TextView GrpStateModify;
    private TextView GroupStateConfirm;// 群状态确认
    private RelativeLayout rl;
    private ToggleButton mTogBtn;

    private boolean TBGroupState;

    private ListView zulistview;
    private ListView memberlistview;
    public static ArrayList<String> zulist = new ArrayList<String>();
    public static ArrayList<String> listsgnumber = new ArrayList<String>();
    public static ArrayList<String> memberlist = new ArrayList<String>();

    private MyApplication myApplication;
    private boolean isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_zu);
        myApplication = (MyApplication) getApplication();

        LoadGroups group = (LoadGroups) getIntent().getExtras().get("group");

        GroupNameText = (TextView) findViewById(R.id.allzuming);
        GroupNameText.setText(group.getGName());

        Retrofitutil.getmRetrofit()
                .create(IfOwnerOfGroup.class)
                .isOwner(group.getGID(), myApplication.getCustomers().getcID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(@NonNull Result result) throws Exception {
                        isOwner = result.isResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(AllZuActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // 群分组状态按钮功能
        qunfenzu = (TextView) findViewById(R.id.qunfenzu);
        qunfenzu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setGroupAdapter();
                zulistview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        Currents.currentSGroup.setSgID(Integer
                                .parseInt(AllZuActivity.listsgnumber.get(arg2)));
                        Currents.currentSGroup.setgID(Currents.currentGroup
                                .getgID());
                        Intent t = new Intent(AllZuActivity.this,
                                EachZuActivity.class);
                        startActivity(t);
                    }
                });
            }
        });
        qunfenzu.performClick();

        // 群成员按钮功能
        qunchengyuan = (TextView) findViewById(R.id.qunchengyuan);
        qunchengyuan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setMembersAdapter();
                memberlistview
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                Currents.currentOtherCustomer
                                        .setcID(Currents.gCustomersList.get(
                                                arg2).getcID());

                                Intent t = new Intent(AllZuActivity.this,
                                        OtherMessgActivity.class);
                                startActivity(t);

                            }
                        });

            }
        });

        // 返回按钮
        allzu_back_btn = (ImageButton) findViewById(R.id.allzu_back);
        allzu_back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AllZuActivity.this.finish();
            }
        });

        allzu_more_btn = (ImageButton) findViewById(R.id.zu_more_logo);
        allzu_more_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.zu_more_logo:
                        if (popupwindow != null && popupwindow.isShowing()) {
                            popupwindow.dismiss();
                            return;
                        } else {
                            initmPopupWindowView();
                            popupwindow.showAsDropDown(v, 0, 5);
                        }
                        break;
                    default:
                        break;
                }

            }
        });

    }

    // 刷新群组列表
    protected void setGroupAdapter() {
        Currents.SGroupsList.clear();
        AllZuActivity.zulist.clear();
        AllZuActivity.listsgnumber.clear();
        Database.loadSGroups loadsgroups = new Database.loadSGroups(
                Currents.currentGroup.getgID());
        FutureTask<Boolean> ft = new FutureTask<Boolean>(loadsgroups);
        Thread th = new Thread(ft);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int sgid, oldSGroupID = -1;
        String sgname;
        for (int i = 0; i < Currents.SGroupsList.size(); i++) {
            sgid = Currents.SGroupsList.get(i).getSgID();
            sgname = Currents.SGroupsList.get(i).getcName();
            if (sgid != oldSGroupID) {
                AllZuActivity.zulist.add("G"
                        + Currents.SGroupsList.get(i).getSgID() + " " + sgname);
                AllZuActivity.listsgnumber.add(""
                        + Currents.SGroupsList.get(i).getSgID());
            } else {
                AllZuActivity.zulist
                        .set(AllZuActivity.zulist.size() - 1,
                                AllZuActivity.zulist.get(AllZuActivity.zulist
                                        .size() - 1) + " " + sgname);
            }
            oldSGroupID = sgid;
        }
        this.zulistview = (ListView) findViewById(R.id.listview_allzu);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, zulist);
        this.zulistview.setAdapter(myArrayAdapter);
    }

    // 刷新群成员列表
    protected void setMembersAdapter() {
        AllZuActivity.memberlist.clear();
        Currents.gCustomersList.clear();
        LoadGroupMembers loadgroupmembers = new LoadGroupMembers(
                Currents.currentGroup.getgID());
        FutureTask<Boolean> ft = new FutureTask<Boolean>(loadgroupmembers);
        Thread th = new Thread(ft);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String cname;
        for (int i = 0; i < Currents.gCustomersList.size(); i++) {
            cname = Currents.gCustomersList.get(i).getcName();
            if (i == 0) {
                AllZuActivity.memberlist.add("群主： " + cname);
            } else {
                AllZuActivity.memberlist.add(i + "： " + cname);
            }
        }
        this.memberlistview = (ListView) findViewById(R.id.listview_allzu);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, memberlist);
        this.memberlistview.setAdapter(myArrayAdapter);

    }

    @Override
    protected void onResume() {
        this.setGroupAdapter();
        super.onResume();
    }

    public void initmPopupWindowView() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.allzu_pop, null,
                false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 442, 580);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setAnimationStyle(R.style.AnimationFade);

        popupwindow.setFocusable(true);
        popupwindow.setBackgroundDrawable(new PaintDrawable());

        // 自定义view添加触摸事件
        customView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }

                return false;
            }
        });

        // 在这里可以实现功能，新建组
        TextViewCreatZu = (TextView) customView.findViewById(R.id.creatzu_text);
        TextViewCreatZu.setOnClickListener(new View.OnClickListener() {
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
                    Currents.groupstate = ft1.get();
                    th1.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Currents.groupstate == true) {
                    final AlertDialog dialog = new AlertDialog.Builder(
                            AllZuActivity.this).create();
                    dialog.show();
                    dialog.getWindow()
                            .clearFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.creat_zu);

                    creatzuconfirmbtn = (TextView) window
                            .findViewById(R.id.creat_zu_confirm);
                    creatzuconfirmbtn
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    CreateNewSGroup createnewsgroup = new CreateNewSGroup(
                                            Currents.currentGroup.getgID(),
                                            Currents.currentCustomer.getcID(),
                                            1);
                                    final FutureTask<Boolean> ft = new FutureTask<Boolean>(
                                            createnewsgroup);
                                    Thread th = new Thread(ft);
                                    th.start();
                                    boolean ifcreate = false;
                                    try {
                                        th.join();
                                        ifcreate = ft.get();
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    if (ifcreate == true) {
                                        dialog.dismiss();
                                        setGroupAdapter();
                                        Toast.makeText(getApplicationContext(),
                                                "创建成功", Toast.LENGTH_SHORT)
                                                .show();

                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "无法创建", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "当前群被锁定无法操作。",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 删除、退出群
        DeleteGroup = (TextView) customView.findViewById(R.id.delgrp_text);

        if (isOwner) {
            DeleteGroup.setText("删除群");
        } else {
            DeleteGroup.setText("退出群");
        }
        DeleteGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 判断当前群状态
                SelectIfOpen selectifopen = new SelectIfOpen(
                        Currents.currentSGroup.getgID());
                FutureTask<Boolean> ft1 = new FutureTask<Boolean>(selectifopen);
                Thread th1 = new Thread(ft1);
                th1.start();
                try {
                    Currents.groupstate = ft1.get();
                    th1.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Currents.groupstate == true) {
                    if (isOwner) {
                        // 如果是群主进行删除群操作
                        final AlertDialog dialog = new AlertDialog.Builder(
                                AllZuActivity.this).create();
                        dialog.show();
                        dialog.getWindow()
                                .clearFlags(
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        Window window = dialog.getWindow();
                        window.setContentView(R.layout.ask_delete);
                        TextView askdelete = (TextView) window
                                .findViewById(R.id.ask_delete);
                        askdelete.setText("您是群主，确认解散该群？");
                        TextView askconfirm = (TextView) window
                                .findViewById(R.id.ask_confirm);
                        askconfirm
                                .setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        deleteGroup deletegroup = new deleteGroup(
                                                Currents.currentGroup.getgID());
                                        FutureTask<Boolean> ft1 = new FutureTask<Boolean>(
                                                deletegroup);
                                        new Thread(ft1).start();

                                        Toast.makeText(getApplicationContext(),
                                                "解散成功", Toast.LENGTH_SHORT)
                                                .show();
                                        AllZuActivity.this.finish();
                                    }
                                });

                    } else {
                        // 如果是群成员，进行退出群操作
                        final AlertDialog dialog = new AlertDialog.Builder(
                                AllZuActivity.this).create();
                        dialog.show();
                        dialog.getWindow()
                                .clearFlags(
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        Window window = dialog.getWindow();
                        window.setContentView(R.layout.ask_delete);
                        TextView askdelete = (TextView) window
                                .findViewById(R.id.ask_delete);
                        askdelete.setText("确认退出该群？");
                        TextView askconfirm = (TextView) window
                                .findViewById(R.id.ask_confirm);
                        askconfirm
                                .setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub

                                        ExitFromGroup exitfromgroup = new ExitFromGroup(
                                                Currents.currentGroup.getgID(),
                                                Currents.currentCustomer
                                                        .getcID());
                                        FutureTask<Boolean> ft1 = new FutureTask<Boolean>(
                                                exitfromgroup);
                                        new Thread(ft1).start();
                                        Toast.makeText(getApplicationContext(),
                                                "退出成功", Toast.LENGTH_SHORT)
                                                .show();
                                        AllZuActivity.this.finish();

                                    }
                                });

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "当前群被锁定无法操作。",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 群信息
        TextViewgrpmessg = (TextView) customView
                .findViewById(R.id.grp_messg_text);
        TextViewgrpmessg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final AlertDialog dialog = new AlertDialog.Builder(
                        AllZuActivity.this).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.group_messg);
                EditText idt = (EditText) window
                        .findViewById(R.id.groupid_edit);
                idt.setText(Currents.currentGroup.getgID());
                idt.setEnabled(false);
                EditText nmt = (EditText) window
                        .findViewById(R.id.groupname_edit);
                nmt.setText(Currents.currentGroup.getgName());
                nmt.setEnabled(false);
                EditText pdt = (EditText) window
                        .findViewById(R.id.grouppwd_edit);
                pdt.setText(Currents.currentGroup.getgPasswd());
                pdt.setEnabled(false);
                EditText mint = (EditText) window
                        .findViewById(R.id.grouplimit_min);
                mint.setText("" + Currents.currentGroup.getgMin());
                mint.setEnabled(false);
                EditText maxt = (EditText) window
                        .findViewById(R.id.grouplimit_max);
                maxt.setText("" + Currents.currentGroup.getgMax());
                maxt.setEnabled(false);

            }
        });

        // 群状态
        GrpStateModify = (TextView) customView
                .findViewById(R.id.grp_state_texttt);
        if (isOwner) {
            GrpStateModify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    // 获取群状态
                    SelectIfOpen selectifopen = new SelectIfOpen(
                            Currents.currentSGroup.getgID());
                    FutureTask<Boolean> ft1 = new FutureTask<Boolean>(
                            selectifopen);
                    Thread th1 = new Thread(ft1);
                    th1.start();
                    try {
                        Currents.groupstate = ft1.get();
                        th1.join();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final AlertDialog dialog = new AlertDialog.Builder(
                            AllZuActivity.this).create();
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.grp_state);

                    mTogBtn = (ToggleButton) window.findViewById(R.id.mTogBtn); // 获取到控件
                    mTogBtn.setChecked(Currents.groupstate);
                    TBGroupState = Currents.groupstate;
                    mTogBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub

                            mTogBtn.setChecked(isChecked);
                            TBGroupState = isChecked;

                        }
                    });// mTogBtn添加监听事件

                    GroupStateConfirm = (TextView) window
                            .findViewById(R.id.grp_state_modify_confirm);
                    GroupStateConfirm
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub
                                    if (TBGroupState == true) {
                                        // 选中，左滑，显示灰色,true
                                        UpdateIfOpen updateifopen = new UpdateIfOpen(
                                                Currents.currentGroup.getgID(),
                                                1);
                                        FutureTask<Boolean> ft = new FutureTask<Boolean>(
                                                updateifopen);
                                        Thread th = new Thread(ft);
                                        th.start();
                                        try {
                                            th.join();
                                        } catch (InterruptedException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                        }

                                    } else {
                                        // 未选中，向右划，显示绿色,false
                                        UpdateIfOpen updateifopen = new UpdateIfOpen(
                                                Currents.currentGroup.getgID(),
                                                0);
                                        FutureTask<Boolean> ft = new FutureTask<Boolean>(
                                                updateifopen);
                                        Thread th = new Thread(ft);
                                        th.start();
                                        try {
                                            th.join();
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        // 获取人员名单
                                        SelectNotInSGroup selectnotinsgroup = new SelectNotInSGroup(
                                                Currents.currentGroup.getgID());
                                        FutureTask<Boolean> ft1 = new FutureTask<Boolean>(
                                                selectnotinsgroup);
                                        Thread th1 = new Thread(ft1);
                                        th1.start();
                                        try {
                                            th1.join();
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        int size = Currents.NotInSGroupCustomer
                                                .size();
                                        if (size != 0) {
                                            int i = 0, tempSGroup = 0;
                                            for (i = 0; i < size; i++) {
                                                if ((i)
                                                        % (Currents.currentGroup
                                                        .getgMax()) == 0) {
                                                    // 将下标为i的用户新建组
                                                    CreateNewSGroup createnewsgroup = new CreateNewSGroup(
                                                            Currents.currentGroup
                                                                    .getgID(),
                                                            Currents.NotInSGroupCustomer
                                                                    .get(i)
                                                                    .getcID(),
                                                            1);
                                                    FutureTask<Boolean> ft2 = new FutureTask<Boolean>(
                                                            createnewsgroup);
                                                    Thread th2 = new Thread(ft2);
                                                    th2.start();
                                                    try {
                                                        th2.join();
                                                    } catch (InterruptedException e) {
                                                        // TODO Auto-generated
                                                        // catch
                                                        e.printStackTrace();
                                                    }

                                                    // 将新建组的组号标记在tempSGroup
                                                    SelectMaxSGroupID selectmaxsgroupid = new SelectMaxSGroupID(
                                                            Currents.currentGroup
                                                                    .getgID());
                                                    FutureTask<Boolean> ft3 = new FutureTask<Boolean>(
                                                            selectmaxsgroupid);
                                                    Thread th3 = new Thread(ft3);
                                                    th3.start();
                                                    try {
                                                        th3.join();
                                                        tempSGroup = Currents.maxsgid;
                                                    } catch (InterruptedException e) {
                                                        // TODO Auto-generated
                                                        // catch
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    // 将下标为i的用户加入组
                                                    JoinInSGroup joininsgroup = new JoinInSGroup(
                                                            tempSGroup,
                                                            Currents.NotInSGroupCustomer
                                                                    .get(i)
                                                                    .getcID(),
                                                            Currents.currentGroup
                                                                    .getgID(),
                                                            0);
                                                    FutureTask<Boolean> ft4 = new FutureTask<Boolean>(
                                                            joininsgroup);
                                                    Thread th4 = new Thread(ft4);
                                                    th4.start();
                                                    try {
                                                        th4.join();
                                                    } catch (InterruptedException e) {
                                                        // TODO Auto-generated
                                                        // catch
                                                        // block
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            // 更新列表
                                            setGroupAdapter();
                                        } else {
                                        }

                                    }
                                    Toast.makeText(AllZuActivity.this, "修改成功",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                }
            });
        } else {
            GrpStateModify.setTextColor(Color.parseColor("#21DCDCDC"));
        }

    }
}