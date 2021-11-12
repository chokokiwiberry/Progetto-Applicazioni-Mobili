package trackingmypantry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity {
    Button register_button;
    EditText username, email, password;




    private LAM_Api lam_api;

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_button = (Button) findViewById(R.id.id_register_button);
        username = (EditText) findViewById(R.id.id_username);
        email = (EditText) findViewById(R.id.id_email_register);
        password = (EditText) findViewById(R.id.id_password_register);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/") //l'url è stato cambiato
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lam_api = retrofit.create(LAM_Api.class);

        register_button.setOnClickListener(v -> {
            //fare il pacchetto e fare l'autenticazione
            if (username.getText().toString().trim().equals("")
                    || email.getText().toString().trim().equals("")
                    || password.getText().toString().trim().equals("") ){
                Toast.makeText(this, "Please complete all the form!", Toast.LENGTH_SHORT).show();
            }else{
                register();
            }

        });
    }
    private void register() {
        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("username", this.username.getText().toString());
            postData.put("email", this.email.getText().toString());
            postData.put("password", this.password.getText().toString());
            Call<User> call = lam_api.Register(postData);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(Register.this, "Try again", Toast.LENGTH_LONG).show();
                        return;
                    } else{
                        //se tutto ok lo rimando a login
                        Toast.makeText(Register.this, "Now you can login!", Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid",response.body().getId());
                        editor.apply();


                        Login();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        }catch  (Exception e){
            e.printStackTrace();
        }
    }
    private void Login(){
        Intent Login = new Intent(this, Login.class);
        startActivity(Login);

    }
    public Context getContext() {
        return (Context)this;
    }
}
