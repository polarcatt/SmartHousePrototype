package com.SmartHouse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class Second extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        Button close = (Button) findViewById(R.id.button5);
        close.setEnabled(false);
    }
}