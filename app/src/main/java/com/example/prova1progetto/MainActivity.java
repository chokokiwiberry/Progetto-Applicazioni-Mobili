package com.example.prova1progetto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.*;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    Button register_button;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ciò che vogliamo creare, i tipi di oggetti
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register_button = findViewById(R.id.id_register); // bisogna fare attenzione agli id giusti da passare
        login_button = findViewById(R.id.id_login_text);
        register_button.setOnClickListener(v -> {
            Intent register = new Intent(this, Register.class);
            startActivity(register);
        });
        login_button.setOnClickListener(v -> {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        });


    }

    //e poi dichiariamo le funzioni
    public void prova(View v) {
        System.out.println("prova ciao prova");
    }


}