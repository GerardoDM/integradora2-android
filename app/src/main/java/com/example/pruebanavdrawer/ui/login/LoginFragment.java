package com.example.pruebanavdrawer.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.pruebanavdrawer.Interfaces.Api;
import com.example.pruebanavdrawer.Models.Example2;
import com.example.pruebanavdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private Button btnlogin;
    private TextView linkRegister;
    private EditText email, password;
   // private String inputemail, inputpassword;

    private AwesomeValidation validator;

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
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);

   //     email.setText("e@e.com");
     //   password.setText("123123123");

        linkRegister = (TextView) view.findViewById(R.id.link_register);

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        setupRules();



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.registerFragment);
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

    private void login() {

       //String inputemail = email.getText().toString();
        //String inputpassword = password.getText().toString();

        validator.clear();

        if (validator.validate()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://165.227.23.126:8888/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<Example2> call = api.login(email.getText().toString(), password.getText().toString());

            call.enqueue(new Callback<Example2>() {
                @Override
                public void onResponse(Call<Example2> call, Response<Example2> response) {


                    if (!response.isSuccessful()) {

                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();


                        return;
                    }


                    String token = null;
                    try {
                        Toast.makeText(getActivity(), response.body().getAuth().getToken(), Toast.LENGTH_LONG).show();
                        token = response.body().getAuth().getToken();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN", token).apply();

                    Navigation.findNavController(getView()).navigate(R.id.nav_home);


                }

                @Override
                public void onFailure(Call<Example2> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    private void setupRules () {
        validator.addValidation(getActivity(), R.id.input_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(getActivity(), R.id.input_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }

}
