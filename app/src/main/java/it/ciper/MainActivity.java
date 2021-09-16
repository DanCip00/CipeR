package it.ciper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import it.ciper.api.interfacce.SettingsApi;
import it.ciper.data.DataCenter;
import it.ciper.home.viewCarrelli.CreationThreadCarrelli;
import it.ciper.home.viewOfferte.CreationThreadOfferte;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;

public class MainActivity extends AppCompatActivity {
    //API
        final String apiKey = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";
    DataCenter dataCenter = new DataCenter(apiKey);

    //EXECUTOR
    ExecutorService executor = SettingsApi.executor;

    //Carrelli
    CreationThreadCarrelli creationThreadCarrelli = new CreationThreadCarrelli();

    // Offerte
    CreationThreadOfferte creationThreadOfferte = new CreationThreadOfferte();



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

        try {
            if(exeOfferte.get().booleanValue() && exeCarrelli.get().booleanValue())
                System.out.println("Caricamento completato");
        } catch (ExecutionException e) {
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