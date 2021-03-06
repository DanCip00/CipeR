package it.ciper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Timer;
import java.util.TimerTask;
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
import it.ciper.userSettings.UserSetting;

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
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        findViewById(R.id.search_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setParamsSearch();
                creationTreahSearch.disp();
            }
        });

        //Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeItemMenu:
                        break;
                    case R.id.accountItemMenu:
                            getSupportFragmentManager().beginTransaction().replace(R.id.ConstraintLayoutMain ,new UserSetting()).commit();
                }

                return false;
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
    public void updateCart(){
        showProgressBar();
        creationThreadCarrelli.updateInteface();
        hideProgressBar();
    }

    public void showProgressBar(){
        findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);

        findViewById(R.id.progressBar2).setElevation(100);
    }

    public void showProgressBarTimer(){
        findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar2).setElevation(2000);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hideProgressBar();
            }
        }, 600);
    }
    public void hideProgressBar(){
        findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
    }

}