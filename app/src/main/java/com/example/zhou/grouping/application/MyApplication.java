package com.example.zhou.grouping.application;

import android.app.Application;

import com.example.zhou.grouping.Bean.Customers;

/**
 * Created by Zhou on 2017/4/15.
 */

public class MyApplication extends Application {
    private Customers customers;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }
}
