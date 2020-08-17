package com.example.pruebanavdrawer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebanavdrawer.Interfaces.Api;
import com.example.pruebanavdrawer.MainActivity;
import com.example.pruebanavdrawer.Models.AccessToken;
import com.example.pruebanavdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    TextView linkRegister;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);

        linkRegister = (TextView) findViewById(R.id.link_register);


        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToRegister();

            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    void login() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.7:3333/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<AccessToken> call = api.login(
                username.getText().toString(),
                password.getText().toString());

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {


                if (!response.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();


                    return;
                }

                startActivity(new Intent(LoginActivity.this, PruebaWSActivity.class));
                finish();



            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                //textViewResult.setText(t.getMessage());

                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void goToRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }


}
