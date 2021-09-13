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

        CarrelliInterfaceApi.allProducts(apiKey, "791e2bf1-ddb4-4665-816b-7d5997c0588e").forEach(c->System.out.println(c));
        loadDataFiles();
        //Gestione OFFERTE
        setContentView(R.layout.activity_main);
        adapter = new RecViewOffertAdapter();
        adapter.setOfferte(data.offerte);
        recyclerViewOff = this.findViewById(R.id.offerte);
        recyclerViewOff.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewOff.setAdapter(adapter);

        //Gestione CARRELLI
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
    }



    protected void loadDataFiles(){

        InputStream inputStreamProd = null;
        InputStream inputStreamOff = null;
        InputStream inputStreamShop = null;
        InputStream streamCarrelli = null;

        try {
            inputStreamProd = getAssets().open("prodotti_offerte.json");
            inputStreamOff = getAssets().open("top_offerte.json");
            inputStreamShop = getAssets().open("sellers.json");
            streamCarrelli = getAssets().open("carrelli.json");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Prodotti
        Product[] prodo = null;
        prodo = JsonManager.parseJsonClass(inputStreamProd, Product[].class);
        Arrays.stream(prodo).forEach(p->data.prodotti.put(p.getProductCod(), p));

        //shop
        Shop[] sellers = null;
        sellers = JsonManager.parseJsonClass(inputStreamShop, Shop[].class);
        Arrays.stream(sellers).forEach(s->data.negozi.put(s.getSellerCod(), s));

        //Offerte
        Offerts[] off = JsonManager.parseJsonClass(inputStreamOff, Offerts[].class);
        Arrays.stream(off).peek(o->o.setProdotto(data.prodotti.get(o.getProductCod()))).forEach(o->data.offerte.add(o));
        data.offerte.forEach(o->o.setSeller(data.negozi.get(o.getSellerCod())));

        //Carrello
        CarrelloJsonFormat[] cartJson = null;
        cartJson = JsonManager.parseJsonClass(streamCarrelli,CarrelloJsonFormat[].class);

        if (cartJson == null || cartJson.length== 0)
            return;

        Arrays.stream(cartJson).forEach(c->{
            TreeSet<Product> prodotti = new TreeSet<>();
            HashMap<Product,Long> quantity= c.getProdCod().stream().map(p->data.prodotti.get(p))
                                                                   .peek(p-> prodotti.add(p))
                                                                   .collect(Collectors.groupingBy(p->p,
                                                                                                  ()-> new HashMap<>(),
                                                                                                  Collectors.counting()));
            data.carrelli.add(new Carrello(c.getTitolo(),c.getProdCod(),c.getSellerCod(),
                                           data.negozi.get(c.getSellerCod()),
                                           prodotti,quantity));

        });
    }
}