package com.example.pruebanavdrawer.ui.register;

import androidx.lifecycle.ViewModelProviders;

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
import com.example.pruebanavdrawer.Models.User;
import com.example.pruebanavdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {


    private Button btnregister;
    private TextView linkLogin;
    private EditText email, password;
    private AwesomeValidation validator;

    private RegisterViewModel mViewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnregister = (Button) view.findViewById(R.id.btn_register);
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        linkLogin = (TextView) view.findViewById(R.id.link_login);
        validator = new AwesomeValidation(ValidationStyle.BASIC);

       setupRules();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.loginFragment);
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }

    private void register(){

        validator.clear();

      if (validator.validate()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://165.227.23.126:8888/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<User> call = api.register(email.getText().toString(), password.getText().toString());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {


                    if (!response.isSuccessful()){

                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();


                        return ;
                    }





                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).navigate(R.id.loginFragment);





                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    //textViewResult.setText(t.getMessage());

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
