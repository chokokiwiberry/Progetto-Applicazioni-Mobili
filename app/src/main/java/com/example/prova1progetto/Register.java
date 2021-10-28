package com.example.prova1progetto;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    Button register_button;
    EditText username, email, password;


    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_button = (Button) findViewById(R.id.id_register_button);
        username = (EditText) findViewById(R.id.id_username);
        email = (EditText) findViewById(R.id.id_email_register);
        password = (EditText) findViewById(R.id.id_password_register);

        register_button.setOnClickListener(v -> {
            //fare il pacchetto e fare l'autenticazione
        });
    }
}
