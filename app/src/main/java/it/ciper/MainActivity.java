package it.ciper;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import it.ciper.api.interfacce.SettingsApi;
import it.ciper.data.DataCenter;
import it.ciper.home.viewCarrelli.CreationThreadCarrelli;
import it.ciper.home.viewOfferte.CreationThreadOfferte;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;
import it.ciper.position.PositionHandler;

public class MainActivity extends AppCompatActivity {
    //API
        final String apiKey = SettingsApi.apiKey;
    DataCenter dataCenter = new DataCenter(apiKey);

    //EXECUTOR
    ExecutorService executor = SettingsApi.executor;

    //Carrelli
    CreationThreadCarrelli creationThreadCarrelli = new CreationThreadCarrelli();

    // Offerte
    CreationThreadOfferte creationThreadOfferte = new CreationThreadOfferte();

    //Position
    PositionHandler positionHandler = new PositionHandler();
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            if(exeOfferte.get().booleanValue() && exeCarrelli.get().booleanValue() && locationFuture.get().booleanValue())
                System.out.println("Caricamento completato");
        } catch (ExecutionException e) {
            Toast.makeText(this,"E' necessaria una connessione internet",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

     public void updateInteface(){
        creationThreadOfferte.updateInteface();
        creationThreadCarrelli.updateInteface();
    }


}