package com.annotation.demo;

import android.app.Activity;
import android.os.Bundle;

import com.annotation.api.StaticLauncher;

/**
 * Created by SungGeun on 2015-09-29.
 */
@StaticLauncher
public class ActivityA extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
