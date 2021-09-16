package it.ciper;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.ciper.data.DataCenter;
import it.ciper.home.viewCarrelli.RecViewCarrelliAdapter;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;

public class MainActivity extends AppCompatActivity {
    //API
        final String apiKey = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";
    DataCenter dataCenter = new DataCenter(apiKey);


    // Offerte
    RecViewOffertAdapter adapter;
    RecyclerView recyclerViewOff;

    //Carrelli
    RecViewCarrelliAdapter adapterCarrelli;
    RecyclerView recyclerViewCarrelli;
    ImageView nessunCarrello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Gestione OFFERTE
        setContentView(R.layout.activity_main);
        adapter = new RecViewOffertAdapter();
        adapter.setTopFiveOfferts(dataCenter.getTopFiveOfferts(),dataCenter);
        recyclerViewOff = this.findViewById(R.id.offerte);
        recyclerViewOff.setAdapter(adapter);
        recyclerViewOff.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        //Gestione CARRELLI

        adapterCarrelli = new RecViewCarrelliAdapter();
        adapterCarrelli.setCarrelli(dataCenter);
        adapterCarrelli.setContext(this,this,this);
        nessunCarrello = this.findViewById(R.id.nessunCarrello);
        recyclerViewCarrelli = this.findViewById(R.id.carrelli);
        recyclerViewCarrelli.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCarrelli.setAdapter(adapterCarrelli);

        if (dataCenter.getAllCarrelliAPI().size()!=0)
            nessunCarrello.setVisibility(View.GONE);

    }

     public void updateInteface(){
        adapter.setTopFiveOfferts(dataCenter.getTopFiveOfferts(),dataCenter);
        adapterCarrelli.setCarrelli(dataCenter);
    }




}