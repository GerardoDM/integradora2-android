package com.example.pruebanavdrawer.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.pruebanavdrawer.Activities.LoginActivity;
import com.example.pruebanavdrawer.Activities.PruebaWSActivity;
import com.example.pruebanavdrawer.Interfaces.Api;
import com.example.pruebanavdrawer.Models.AccessToken;
import com.example.pruebanavdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    Button btnlogin;
    TextView linkRegister;
    EditText username, password;

    AwesomeValidation validator;

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnlogin = (Button) view.findViewById(R.id.btn_login);
        username = (EditText) view.findViewById(R.id.input_username);
        password = (EditText) view.findViewById(R.id.input_password);

        linkRegister = (TextView) view.findViewById(R.id.link_register);

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        setupRules();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    void login() {

        validator.clear();

        if (validator.validate()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://165.227.23.126:3333/user/")
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

                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();


                        return;
                    }





                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    //textViewResult.setText(t.getMessage());

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    public void setupRules () {
        validator.addValidation(getActivity(), R.id.input_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(getActivity(), R.id.input_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }

}
