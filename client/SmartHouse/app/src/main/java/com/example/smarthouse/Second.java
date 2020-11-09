package com.example.smarthouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Second extends Activity {
    private  String LOG_TAG   = "SOCKET";
    private EditText code;
    private Button verify;
    private Second.Connection mConnect  = null;
    private  String HOST = "192.168.43.53";
    private  int PORT = 11813;
    private boolean started = false;
    private boolean curstate = false;
    private Handler handler = new Handler();


    private static OutputStream output;
    private static InputStream input;
    private static DataOutputStream dos;
    private static DataInputStream dstream;

    private Runnable timer = new Runnable () {

        @Override
        public void run() {
            try {
                Tick();
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.postDelayed(timer, 100);
        }
    };

    public void stop() {
        started = false;
        handler.removeCallbacks(timer);
    }

    public void startLoop() {
        started = true;
        handler.postDelayed(timer, 100);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        Button connect = (Button) findViewById(R.id.button4);
        Button close = (Button) findViewById(R.id.button5);
        close.setEnabled(false);
        startLoop();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenClick();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseClick();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void onOpenClick()
    {
        // Создание подключения
        mConnect = new Second.Connection(HOST, PORT);
        // Открытие сокета в отдельном потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mConnect.openConnection();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.i(LOG_TAG, "Соединение установлено");
                    Log.i(LOG_TAG, "(mConnect != null) = "
                            + (mConnect != null));
                    // Отправка "Hello" серверу
                    dos.writeBytes("Hello");
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                    mConnect = null;
                }
            }
        }).start();
    }

    public void onCloseClick() {
        mConnect.closeConnection();
        Log.i(LOG_TAG, "Соединение закрыто");
    }

    public static class Connection {
        private Socket mSocket = null;
        private String mHost = null;
        private int mPort = 0;
        private EditText textView2 = null;

        public static final String LOG_TAG = "SOCKET";

        //public Connection() {}

        public Connection(final String host, final int port) {
            this.mHost = host;
            this.mPort = port;
        }

        public void openConnection() throws Exception {
            try {
                mSocket = new Socket(mHost, mPort);
                output = mSocket.getOutputStream();
                input = mSocket.getInputStream();
                dos = new DataOutputStream(output);
                dstream = new DataInputStream(input);
            } catch (IOException e) {
                throw new Exception("Невозможно создать сокет: "
                        + e.getMessage());
            }
        }

        /**
         * Метод закрытия сокета
         */
        public void closeConnection() {
            if (mSocket != null && !mSocket.isClosed()) {
                try {
                    mSocket.close();
                    dos.close();
                    input.close();
                    output.close();
                    dstream.close();
                } catch (IOException e) {
                    Log.i(LOG_TAG, "Ошибка при закрытии сокета :"
                            + e.getMessage());
                } finally {
                    mSocket = null;
                }
            }
            mSocket = null;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            closeConnection();
        }
    }

    public void onSwitchClick(View v)
    {
        Switch swi = (Switch) findViewById(R.id.switch1);
        boolean isChecked = swi.isChecked();
        Log.i(LOG_TAG, "Свитч");

        if(dos != null) {
            try {
                if (isChecked) {
                    dos.writeBytes("turnOn");
                } else {
                    dos.writeBytes("turnOff");
                }
            } catch (IOException e) {
                Log.i(LOG_TAG, "Ошибка при записи"
                        + e.getMessage());
            }
        }
    }

    public String GetReceivedData() throws IOException {
        if(dstream != null && dstream.available() > 0) {
            Log.i(LOG_TAG, "ЖДЕМ");

            byte[] messageByte = new byte[1000];
            String dataString = "";
            int bytesRead;

            while((bytesRead = dstream.read(messageByte)) > 0)
            {
                String temp = new String(messageByte, 0, bytesRead);
                dataString += temp;
                if(temp.endsWith("!"))break;
            }
            Log.i(LOG_TAG, dataString);
            return dataString;
        }else return "";
    }

    public void Tick() throws IOException {
        String data = GetReceivedData();
        if(data.equals("1!"))
            curstate = true;
        else if(data.equals("0!"))
            curstate = false;
        ApplyState(curstate);
        Log.i(LOG_TAG, "ТИК");
        Log.i(LOG_TAG, data);
    }

    public void ApplyState(boolean state)
    {
        Switch swi = (Switch) findViewById(R.id.switch1);
        swi.setOnCheckedChangeListener(null);
        swi.setChecked(state);
    }
}