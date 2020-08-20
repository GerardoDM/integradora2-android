package com.example.pruebanavdrawer.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pruebanavdrawer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btn, btnOpen, btnNotification, btnToken;
    private OkHttpClient client;
    private TextView txt, txt2;
    private WebSocket ws;
    private String text1;
    private static  final String  CHANNEL_ID = "NOTIFICATION";
    private static  final int NOTIFICATION_ID = 0;

    private EchoWebSocketListener echoWebSocketListener;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response){

            if (Looper.myLooper() == null)

            {
                Looper.prepare();
            }

            System.out.println("printing on open...");
            System.out.println(response.message());


        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            if (Looper.myLooper() == null) {
                Looper.prepare();


            }


            //System.out.println("printing on message...");
            //System.out.println(text);
            Handler handler = new Handler(getActivity().getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(text);
                        _action(jsonObject);
                    } catch (JSONException err) {
                        Log.d("Error", err.toString());
                    }


                    //   Gson gson = new Gson();
                    //     Example example = gson.fromJson(String.valueOf(jsonObject), Example.class);

                    //   Toast.makeText(getActivity(), example.getD().getEvent(), Toast.LENGTH_LONG).show();

                    //_action(example);

                }
            });

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
        txt2 = view.findViewById(R.id.txt2);
        btnOpen = view.findViewById(R.id.btnOpen);
        btnToken = view.findViewById(R.id.btnToken);

        client = new OkHttpClient();


        btnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToken();
            }
        });


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

        if(ws != null){
            showToast("Ya se ha conectado al servidor...");
            return;
        }

        Request request = new Request.Builder().url("ws://165.227.23.126:8888/adonis-ws").build();
        EchoWebSocketListener listener = new HomeFragment.EchoWebSocketListener();

        ws = client.newWebSocket(request, listener);


        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject("{\"t\":1, \"d\":{\"topic\":\"iot\"}}\"");
            System.out.println("making json");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        ws.send(String.valueOf(jsonObj));
        System.out.println(jsonObj);

        client.dispatcher().executorService().shutdown();


    }

    private void open(){

        if(ws == null){
            showToast("Primero debe conectarse al servidor...");
            return;
        }

        JSONObject jsonObj2 = null;
        try {
            jsonObj2 = new JSONObject("{\"t\":7,\"d\":{\"topic\":\"iot\",\"event\":\"dispense\"}}");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        ws.send(String.valueOf(jsonObj2));

    }

    private void _action(JSONObject jsonObject){

        String event = null;

        try {
            event = jsonObject.getJSONObject("d").getString("event").toUpperCase();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if(event.equals("MESSAGE")){

            try {
                showToast(jsonObject.getJSONObject("d").toString(8));
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            try{
                boolean servo_active = jsonObject.getJSONObject("d").getJSONObject("data").getBoolean("servo_active");
                if(servo_active){
                    createNotificationChannel();
                    createNotification();
                    return;
                }

            } catch (JSONException e){
                e.printStackTrace();
                return;
            }

        }

        try {
            jsonObject.getJSONObject("d").getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        if(event.equals("MEASURE")){
            try {
           txt.setText(jsonObject.getJSONObject("d").getJSONObject("data").getString("distance"));

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }


        if(event.equals("START_SERVO")){
            createNotificationChannel();
            createNotification();
            return;
            /*try {
                createNotificationChannel();
                createNotification();
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }*/
        }

        Double num = null;
        try {
            num = Double.parseDouble(jsonObject.getJSONObject("d").getJSONObject("data").getString("distance"));
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            return;
        }
        if (num <= 170){
            txt2.setText("Vacio");
            return;
        }
        txt2.setText("Lleno");

    }

    private void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        System.out.println("printing message/below toast");

    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);
        builder.setContentTitle("Notificacion Puerta")
                .setContentText("Se abrió la compuerta de croquetas")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.dog);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());




    }


    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notification";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)  getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void showToken(){
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        Toast.makeText(getActivity(), retrivedToken, Toast.LENGTH_LONG).show();
    }





}


