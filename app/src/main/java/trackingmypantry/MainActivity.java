package trackingmypantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.trackingmypantry.R;

/**
 * PROGETTO DI LABORATORIO APPLICAZIONI MOBILI
 * A.A 2020/2021
 * "Tracking my Pantry"
 *
 */
public class MainActivity extends AppCompatActivity {
    Button register_button;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


}