package it.ciper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import it.ciper.api.interfacce.SettingsApi;
import it.ciper.data.DataCenter;
import it.ciper.home.viewCarrelli.CreationThreadCarrelli;
import it.ciper.home.viewOfferte.CreationThreadOfferte;
import it.ciper.home.viewSearch.CreationTreahSearch;
import it.ciper.login.LoginActivity;
import it.ciper.position.PositionHandler;

public class MainActivity extends AppCompatActivity {
    //API
    DataCenter dataCenter;

    //EXECUTOR
    ExecutorService executor = SettingsApi.executor;

    //Carrelli
    CreationThreadCarrelli creationThreadCarrelli = new CreationThreadCarrelli();

    // Offerte
    CreationThreadOfferte creationThreadOfferte = new CreationThreadOfferte();

    //Search
    CreationTreahSearch creationTreahSearch = new CreationTreahSearch();

    //Position
    PositionHandler positionHandler = new PositionHandler();
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GESTIONE LOG IN
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        String logged = preferences.getString("logged", "");

        if (logged.compareTo("false")==0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putString("logged", "false").apply();
                preferences.edit().putString("ricordami", "false").apply();
                preferences.edit().putString("apiKey", "").apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(logged.compareTo("false")==0)
            return;
        if (preferences.getString("ricordami", "").compareTo("false")==0){
            preferences.edit().putString("logged", "false").apply();
        }

        String apiKey = preferences.getString("apiKey", "");
        dataCenter = new DataCenter(apiKey);

        //Gestione CARRELLI
        creationThreadCarrelli.setParams(this,this,this,dataCenter);
        Future<Boolean> exeCarrelli=executor.submit(creationThreadCarrelli);

        //Gestione OFFERTE
        creationThreadOfferte.setParams(this,this,this,dataCenter);
        Future<Boolean> exeOfferte=executor.submit(creationThreadOfferte);


        //Posizione
            //Permessi
        positionHandler.setParams(this,this,this,dataCenter);
        Future<Boolean> locationFuture =executor.submit(positionHandler);

        try {
            if(exeOfferte.get().booleanValue() && exeCarrelli.get().booleanValue() && locationFuture.get().booleanValue()) {
                System.out.println("Caricamento completato");
                System.gc();
            }
        } catch (ExecutionException e) {
            Toast.makeText(this,"E' necessaria una connessione internet",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        findViewById(R.id.search_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setParamsSearch();
                creationTreahSearch.disp();
            }
        });

    }
    private void setParamsSearch(){
        creationTreahSearch.setParams(this,this,this,dataCenter);
    }

     public void updateInteface(){
        creationThreadOfferte.updateInteface();
        creationThreadCarrelli.updateInteface();
        System.gc();
    }

}