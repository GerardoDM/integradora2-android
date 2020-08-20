package com.example.pruebanavdrawer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.pruebanavdrawer.Interfaces.Api;
import com.example.pruebanavdrawer.MainActivity;
import com.example.pruebanavdrawer.Models.User;
import com.example.pruebanavdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {


    Button btnregister;
    TextView linkLogin;
    EditText email, password;
    AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnregister = (Button) findViewById(R.id.btn_register);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        linkLogin = (TextView) findViewById(R.id.link_login);
        validator = new AwesomeValidation(ValidationStyle.BASIC);

        setupRules();


        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToLogin();

            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });



    }


    void register(){

        validator.clear();

        if (validator.validate()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://165.227.23.126:3333/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<User> call = api.register(
                    email.getText().toString(),
                    password.getText().toString());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {


                    if (!response.isSuccessful()){

                        Toast.makeText(RegisterActivity.this, response.message(), Toast.LENGTH_LONG).show();


                        return ;
                    }

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();




                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    //textViewResult.setText(t.getMessage());

                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }



    }

    void goToLogin(){

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

    }

    public void setupRules () {
        validator.addValidation(this, R.id.input_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.input_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }


}
