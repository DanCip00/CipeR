package it.ciper;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.api.interfacce.ShopInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.dataClasses.Carrello;
import it.ciper.dataClasses.CarrelloJsonFormat;
import it.ciper.dataClasses.LoadedData;
import it.ciper.dataClasses.Offerts;
import it.ciper.dataClasses.Product;
import it.ciper.dataClasses.Shop;
import it.ciper.json.JsonManager;
import it.ciper.home.viewCarrelli.RecViewCarrelliAdapter;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;

public class MainActivity extends AppCompatActivity {
    //API
        final String apiKey = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";
    DataCenter dataCenter = new DataCenter(apiKey);

    LoadedData data = new LoadedData();

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
        /*
        System.out.println(data.carrelli);
        adapterCarrelli = new RecViewCarrelliAdapter();
        adapterCarrelli.setCarrelli(data.carrelli);
        adapterCarrelli.setContext(this);
        nessunCarrello = this.findViewById(R.id.nessunCarrello);
        recyclerViewCarrelli = this.findViewById(R.id.carrelli);
        recyclerViewCarrelli.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCarrelli.setAdapter(adapterCarrelli);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerViewCarrelli.addItemDecoration(itemDecor);

        System.out.println(data.carrelli.size());
        if (data.carrelli.size() !=0) {
            Toast.makeText(this, "Ci sono delle liste:" + data.carrelli.size(), Toast.LENGTH_SHORT);
            nessunCarrello.setVisibility(View.GONE);
        }else
            recyclerViewCarrelli.setVisibility(View.GONE);

         */
    }





}