package com.example.smarthouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.OutputStream;
import java.io.DataOutputStream;


public class Second extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
    //}

    //public void onSwitchClick(View v)
    //{
       //@SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = (Switch)findViewById(R.id.switch1);

        try {
            Socket sock = new Socket("192.168.43.167", 11815);
            String m = "turnOff";
            OutputStream ostream = sock.getOutputStream();
            DataOutputStream dos = new DataOutputStream(ostream);
            dos.writeBytes(m);
            dos.close();
            ostream.close();
            sock.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}