package com.example.test1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.model.LoginUser;
import com.example.test1.model.UserInfo;
import com.example.test1.service.UserClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private TextView createAccount;
    private TextInputLayout edEmail, edPass;
    private Button signIn;
    private String retrivedToken, retrivedEmail, retrivedPass;
    private static String email, date;
    Intent intent;

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.gologin.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    UserClient client = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edEmail = findViewById(R.id.emailEd);
        edPass = findViewById(R.id.edPass);
        createAccount = findViewById(R.id.createAccount);
        signIn = findViewById(R.id.sign_in_btn);

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrivedToken = preferences.getString("TOKEN", null);
        SharedPreferences inputPref = getSharedPreferences("INPUT", Context.MODE_PRIVATE);
        retrivedEmail = inputPref.getString("emailEd", null);
        retrivedPass = inputPref.getString("passEd", null);
        edEmail.getEditText().setText(retrivedEmail);
        edPass.getEditText().setText(retrivedPass);

        if (retrivedToken != null) {
            getInfoFromRequest();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser user = new LoginUser(edEmail.getEditText().getText().toString(),
                        edPass.getEditText().getText().toString());
                sendNetworkRequest(user);
                validation();
                getInfoFromRequest();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void sendNetworkRequest(LoginUser user) {
        Call<LoginUser> call = client.login(user);
        call.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "login response: " + response.body());
                    Log.d(TAG, "login response: " + response.body().getToken());

                } else {
                    Log.d(TAG, "login fail: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Log.d(TAG, "onFailureLogin: " + t.getMessage());

            }
        });
    }

    private void getInfoFromRequest() {
        Call<UserInfo> call = client.getInfo("Bearer " + retrivedToken);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    email = response.body().getEmail();
                    date = response.body().getCreatedAt().toString();
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("createdAt", date);
                    intent.putExtras(bundle);

                    if (edEmail.getEditText().getText().toString().equals(retrivedEmail) &&
                            edPass.getEditText().getText().toString().equals(retrivedPass)) {
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
            }
        });
    }

    private boolean validation() {
        String emailInput = edEmail.getEditText().getText().toString();

        if (emailInput.isEmpty() || !emailInput.equals(emailInput)) {
            edEmail.setError("Please enter a valid email address");
            return false;

        } else if (edPass.getEditText().getText().toString().isEmpty()) {
            edPass.setError("Please add password");
            return false;
        }
        return true;
    }
}
