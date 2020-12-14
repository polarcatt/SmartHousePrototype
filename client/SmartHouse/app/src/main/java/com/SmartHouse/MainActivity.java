package com.SmartHouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v)
    {

     Button verify = (Button) findViewById(R.id.verify);
     
     //if (textNumber.getText().toString().equals("2020"))
     //{
      //Toast.makeText(getApplicationContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();

       //Intent intent = new Intent(MainActivity.this, Second.class);
       //startActivity(intent);
     //}

     //else {
        // Toast.makeText(getApplicationContext(), "Неправильные данные!", Toast.LENGTH_SHORT).show();
    // }

    }
}