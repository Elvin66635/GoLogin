package com.example.test1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.model.User;
import com.example.test1.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends Activity {
    private static final String TAG = "RegistrationActivity";
    private EditText edEmail, edPass, edConfirmPass;
    private Button signUp;
    private TextView logIn;
    private String emailInput, passInput;
    private Intent intent;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.gologin.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserClient client = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edEmail = findViewById(R.id.regEmailEd);
        edPass = findViewById(R.id.regPassEd);
        edConfirmPass = findViewById(R.id.regConfPassEd);
        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.sign_up_btn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(edEmail.getText().toString(), edPass.getText().toString(),
                        edConfirmPass.getText().toString());

                validation();
                sendNetworkRequest(user);

            }
        });
    }

    public void sendNetworkRequest(User user) {
        Call<User> call = client.createAccount(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN", token).apply();

                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Toast.makeText(RegistrationActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "fail: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }

    private boolean validation() {
        emailInput = edEmail.getText().toString().trim();
        passInput = edPass.getText().toString().trim();
        SharedPreferences preferences = getSharedPreferences("INPUT", Context.MODE_PRIVATE);
        preferences.edit().putString("emailEd", emailInput).apply();
        preferences.edit().putString("passEd", passInput).apply();

        if (emailInput.isEmpty()) {
            edEmail.setError("Please enter email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            edEmail.setError("Please enter a valid email address");
        }
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            edEmail.setError("Please enter a valid email");
        } else if (TextUtils.isEmpty(edPass.getText().toString())) {
            edPass.setError("Please add password");
        } else if (TextUtils.isEmpty(edConfirmPass.getText().toString())) {
            edConfirmPass.setError("Please add confirm password");
        } else if ((!edPass.getText().toString().equals(edConfirmPass.getText().toString()))) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
