package com.example.pruebanavdrawer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.health.SystemHealthManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebanavdrawer.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class PruebaWSActivity extends AppCompatActivity {

    Button btn, btnOpen;
    OkHttpClient client;
    TextView txt;
    WebSocket ws;



    private final class EchoWebSocketListener extends WebSocketListener{
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response){

            if (Looper.myLooper() == null)

            {
                Looper.prepare();
                Toast.makeText(PruebaWSActivity.this, response.message(), Toast.LENGTH_LONG).show();
                return;
            }

            System.out.println("printing on open...");
            System.out.println(response.message());
           // webSocket.send("dispense");

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
              //  Toast.makeText(PruebaWSActivity.this, , Toast.LENGTH_LONG).show();

            }
            System.out.println("printing on message...");
            System.out.println(text);

        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

            super.onClosing(webSocket, code, reason);
            System.out.println(reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response){

            if (Looper.myLooper() == null)
            {
                Looper.prepare();
                Toast.makeText(PruebaWSActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_ws);

        btn = findViewById(R.id.btn);
        txt = findViewById(R.id.txt);
        btnOpen = findViewById(R.id.btnOpen);
        client = new OkHttpClient();

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action();
            }
        });




    }

    void action(){


        Request request = new Request.Builder().url("ws://192.168.100.7:3333/adonis-ws").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();

        ws = client.newWebSocket(request, listener);


       JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject("{\"t\":1, \"d\":{\"topic\":\"servo\"}}\"");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ws.send(String.valueOf(jsonObj));
        System.out.println(jsonObj);


        client.dispatcher().executorService().shutdown();

    }

    void open(){

        JSONObject jsonObj2 = null;
        try {
            jsonObj2 = new JSONObject("{\"t\":7,\"d\":{\"topic\":\"servo\",\"event\":\"dispense\"}}");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ws.send(String.valueOf(jsonObj2));

    }
}
