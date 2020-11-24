package com.SmartHouse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class Third extends Activity
{
    private EditText text3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);
    }
}