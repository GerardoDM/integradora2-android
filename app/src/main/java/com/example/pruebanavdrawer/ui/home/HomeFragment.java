package com.example.pruebanavdrawer.ui.home;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pruebanavdrawer.Activities.PruebaWSActivity;
import com.example.pruebanavdrawer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btn, btnOpen;
    private OkHttpClient client;
    private TextView txt;
    private WebSocket ws;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response){

            if (Looper.myLooper() == null)

            {
                Looper.prepare();
                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = view.findViewById(R.id.btn);
        txt = view.findViewById(R.id.txt);
        btnOpen = view.findViewById(R.id.btnOpen);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void action(){


        Request request = new Request.Builder().url("ws://192.168.100.7:3333/adonis-ws").build();
        HomeFragment.EchoWebSocketListener listener = new HomeFragment.EchoWebSocketListener();

        ws = client.newWebSocket(request, listener);


        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject("{\"t\":1, \"d\":{\"topic\":\"iot\"}}\"");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ws.send(String.valueOf(jsonObj));
        System.out.println(jsonObj);


        client.dispatcher().executorService().shutdown();

    }

    private void open(){

        JSONObject jsonObj2 = null;
        try {
            jsonObj2 = new JSONObject("{\"t\":7,\"d\":{\"topic\":\"iot\",\"event\":\"dispense\"}}");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ws.send(String.valueOf(jsonObj2));

    }

}