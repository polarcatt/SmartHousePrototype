package com.example.smarthouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    public void onButtonClick(View v) {

        EditText textNumber = (EditText) findViewById(R.id.edit1);
        Button verify = (Button) findViewById(R.id.button3);
        //int number = Integer.parseInt(textNumber.getText().toString());

        if (textNumber.getText().toString().equals("2020")) {
            Toast.makeText(getApplicationContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, Second.class);
        startActivity(intent);
        }

        else {
            Toast.makeText(getApplicationContext(), "Неправильные данные!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(View view) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}