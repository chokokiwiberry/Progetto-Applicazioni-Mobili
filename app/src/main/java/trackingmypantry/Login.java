package trackingmypantry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trackingmypantry.R;

import java.util.HashMap;
import java.util.Map;

import Interfaces.LAM_Api;
import POJO.User;
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





    private String token;

    private String userId;

    private LAM_Api lam_api;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailtext = (EditText) findViewById(R.id.id_email);
        passwordtext = (EditText) findViewById(R.id.id_password);
        loginbutton = (Button) findViewById(R.id.id_login);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/") //l'url è stato cambiato
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lam_api = retrofit.create(LAM_Api.class);



        loginbutton.setOnClickListener(v -> {

            if (emailtext.getText().toString().trim().equals("") || passwordtext.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please complete all the form!", Toast.LENGTH_SHORT).show();
            }else{
                Login();
            }

        });
    }

    private void Login() {
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
                        if (response.code() == 401) {
                           return;
                        }
                    } else{

                        userlogged.setToken(response.body().getToken());
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", response.body().getToken());
                        editor.apply();

                        Pantry();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("diamond4", "haifalato");
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Context getContext() {
        return (Context)this;
    }

    private void Pantry(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", "");
        userId = preferences.getString("userid", "");
        Intent Pantry = new Intent(this, Pantry.class);
        Pantry.putExtra("userId", userId);
        Pantry.putExtra("token", token);
        startActivity(Pantry);
    }
}

