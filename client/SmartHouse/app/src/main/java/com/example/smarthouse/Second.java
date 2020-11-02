package com.example.smarthouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
    private  String HOST = "10.120.51.22";
    private  int PORT = 9574;
    private Button close = null;

    private static OutputStream output;
    private static InputStream input;
    private static DataOutputStream dos;
    private static DataInputStream dstream;

    private Timer timer;
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            try {
                Tick();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void startLoop() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 500);
    }

    public void stopLoop() {
        timer.cancel();
        timer = null;
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
                    // Разблокирование кнопок в UI потоке
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            close.setEnabled(true);
                        }
                    });
                    Log.d(LOG_TAG, "Соединение установлено");
                    Log.d(LOG_TAG, "(mConnect != null) = "
                            + (mConnect != null));
                    // Отправка "Hello" серверу
                    dos.writeBytes("Hello");
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                    mConnect = null;
                }
            }
        }).start();
    }

    public void onCloseClick() {
        // Закрытие соединения
        mConnect.closeConnection();
        // Блокирование кнопок
        close.setEnabled(false);
        Log.d(LOG_TAG, "Соединение закрыто");
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

        // Метод открытия сокета
        public void openConnection() throws Exception {
            // Если сокет уже открыт, то он закрывается
            closeConnection();
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
                    Log.e(LOG_TAG, "Ошибка при закрытии сокета :"
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
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                try {
                    if (isChecked) {
                        dos.writeBytes("turnOn");
                    } else {
                        dos.writeBytes("turnOff");
                    }
                }catch (IOException e){

                }
            }
        }
        );
    }

    public String GetReceivedData() throws IOException {
        // читает строку с сервера
        if(dstream != null && dstream.available() > 0) {
            String line;
            line = dstream.readUTF();
            // проверка состояния лампочки
            return line;
        }else return "";
    }

    public void Tick() throws IOException {
        String data = GetReceivedData();
        ApplyState(data.equals("On"));
    }

    public void ApplyState(boolean state)
    {
        Switch swi = (Switch) findViewById(R.id.switch1);
        swi.setOnCheckedChangeListener(null);
        swi.setChecked(state);
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                try {
                    if (isChecked) {
                        dos.writeBytes("turnOn");
                    } else {
                        dos.writeBytes("turnOff");
                    }
                }catch (IOException e){

                }
            }
        });
    }
}