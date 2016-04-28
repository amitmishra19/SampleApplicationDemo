package com.example.codingtest;

import android.app.Application;

import com.example.codingtest.utils.Utils;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by H179742 on 4/26/2016.
 */
public class DemoApplication extends Application {
    public static Bus eventBus = new Bus(ThreadEnforcer.MAIN);

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize the fresco library
        Utils.initFresco(this);
    }
}
