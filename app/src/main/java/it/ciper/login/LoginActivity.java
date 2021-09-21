package it.ciper.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.UsersInterfaceApi;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ciper);

        EditText pass, user;
        pass = findViewById(R.id.passwordEditText);
        user = findViewById(R.id.username);
        Button reg, login;
        reg = findViewById(R.id.registratiButton);
        login = findViewById(R.id.loginButton);
        CheckBox ricordami = findViewById(R.id.ricordamiCheckBox);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serverToken = UsersInterfaceApi.getServerToken();
                String apiKey = UsersInterfaceApi.login(user.getText().toString(), pass.getText().toString(),serverToken);
                if (apiKey==null || apiKey=="false"){
                    Toast.makeText(getApplicationContext(),"Accesso non riuscito!", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    getSharedPreferences("login", MODE_PRIVATE).edit().putString("apiKey", apiKey).apply();

                    if (ricordami.isChecked()) {
                        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("logged", "true");
                        editor.putString("ricordami", "true");
                        editor.apply();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("logged", "true");
                        editor.putString("ricordami", "false");
                        editor.apply();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        Registrati registrati = new Registrati();
        registrati.setParams(this,this,this);
        reg.setOnClickListener(registrati);
    }
}