package com.example.pruebanavdrawer.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pruebanavdrawer.Classes.GetUrl;
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
    private Button btn, btnOpen;
    private OkHttpClient client;
    private TextView tvDistance, tvRemaingFood;
    private WebSocket ws;
    private String text1;
    private static  final String  CHANNEL_ID = "NOTIFICATION";
    private static  final int NOTIFICATION_ID = 0;
    private Handler handler;

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


                }
            });

        }


        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

            super.onClosing(webSocket, code, reason);
            System.out.println(reason);
            showToast(String.valueOf(code+"\n")+reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response){

            if (Looper.myLooper() == null)
            {
                Looper.prepare();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }

        }
    } /*class EchoWebSocketListener END*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = view.findViewById(R.id.btn);
        tvDistance = view.findViewById(R.id.txt);
        tvRemaingFood = view.findViewById(R.id.txt2);
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

        handler = new Handler(getActivity().getMainLooper());


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

        Request request = new Request.Builder().url(GetUrl.getWsServer()).build();
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

        JSONObject d = new JSONObject();
        try {

            JSONObject data = new JSONObject();
            data.put("description","started servo");
            data.put("username",GetUrl.getUsername());

            d.put("topic","iot");
            d.put("event","dispense");
            d.put("data",data);


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("t",7);
            jsonObject.put("d",d);

            ws.send(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                String distance = jsonObject.getJSONObject("d").getJSONObject("data").getString("distance") + "cm";
                tvDistance.setText(distance);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }


        if(event.equals("START_SERVO")){
            createNotificationChannel();
            createNotification();
            return;
        }

        Double num = null;
        try {
            num = Double.parseDouble(jsonObject.getJSONObject("d").getJSONObject("data").getString("distance"));
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            return;
        }
        if (num >= 14){
            tvRemaingFood.setText("Vacio");
            return;
        }
        tvRemaingFood.setText("Lleno");

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


}


