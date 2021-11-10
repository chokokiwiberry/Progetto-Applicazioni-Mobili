package com.example.prova1progetto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private static final Object LOG_TAG = "";
    Button loginbutton;
    EditText emailtext, passwordtext;
    TextView textviewResult;
    User userlogged;
    DBHelper DB;

    String PREF_NAME = "token";
    String MY_KEY = "";

    SharedPreferences sharedPreferences;




    private String userId;

    private LAM_Api lam_api;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailtext = (EditText) findViewById(R.id.id_email);
        passwordtext = (EditText) findViewById(R.id.id_password);
        loginbutton = (Button) findViewById(R.id.id_login);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        //prendo il valore di userid da register
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USERID", Context.MODE_PRIVATE);
        userId = prefs.getString("userid", "");//"No name defined" is the default value.



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/") //l'url è stato cambiato
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lam_api = retrofit.create(LAM_Api.class);



        loginbutton.setOnClickListener(v -> {
            String token = "";
            if (emailtext.getText().toString().trim().equals("") || passwordtext.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please complete all the form!", Toast.LENGTH_SHORT).show();
            }else{
                token = Login();
                if (token != "") { //tecnicamente deve controllare il vecchio token quindi fare l'accesso al db
                    // lo faccio entrare
                    //riceverà un token e se il token è scaduto, lo butto fuori e gli rifaccio fare il login
                    //altrimenti entra dentro
                    //dato che bisogna sempre controllare il token, glielo passo nella componente

                    Intent Pantry = new Intent(this, Pantry.class);
                    Pantry.putExtra("userId", userId);
                    Pantry.putExtra("token", token);
                    startActivity(Pantry);
                }
            }

        });
    }

    private String Login() {
        this.userlogged = new User(this.emailtext.getText().toString(), this.passwordtext.getText().toString());
        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("email", this.emailtext.getText().toString());
            postData.put("password", this.passwordtext.getText().toString());
            Call<User> call = lam_api.Login(postData);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (!response.isSuccessful()) {
                        textviewResult.setText("prova response" + response.code());
                        return;
                    }
                    if (response.code() == 401) {
                        //rifare il login
                    }

                    userlogged.setToken(response.body().getToken());
                    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                    editor.putString(MY_KEY, response.body().getToken());
                    //editor.putString(KEY_USERID, response.body().get)
                    editor.apply();
                    Log.d("cibo", userlogged.getToken());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    textviewResult.setText(t.getMessage());
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String myData = sharedPreferences.getString(MY_KEY, "");

        return myData;
    }
}

