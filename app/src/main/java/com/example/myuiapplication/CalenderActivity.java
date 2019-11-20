package com.example.myuiapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart()");
    }

    @Override
    protected void onStart() {

        super.onStart();
        System.out.println("onStart()");
    }

    @Override
    protected void onResume(){

        super.onResume();
        System.out.println("onResume()");
    }

    @Override
    protected void onPause(){
        System.out.println("onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        System.out.println("onStop()");
        super.onStop();
    }

}
