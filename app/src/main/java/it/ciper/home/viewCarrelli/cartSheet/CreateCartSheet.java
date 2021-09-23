package it.ciper.home.viewCarrelli.cartSheet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.Carrello;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;

public class CreateCartSheet implements View.OnClickListener {

    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    protected CarrelloAPI carrelloAPI;
    protected Carrello carrello;

    public  void setContext(Context context, Activity activity, MainActivity main, DataCenter dataCenter){
        this.context= context;
        this.activity =activity;
        this.main = main;
        this.dataCenter = dataCenter;
    }

    public void setCarrelloAPI(CarrelloAPI carrelloAPI) {
        this.carrelloAPI = carrelloAPI;
        carrello = dataCenter.getCarrello(carrelloAPI);
    }

    @Override
    public void onClick(View view) {
        display();
    }

    public void display(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.show_carrello,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);

        ((TextView)bottomSheetDialog.findViewById(R.id.cartNameTextViewCartSheet)).setText(carrello.getTitolo());
        Button condividi = bottomSheetDialog.findViewById(R.id.shereCartButton);
        condividi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        //TODO recView per gli avatar
        RecyclerView users = bottomSheetDialog.findViewById(R.id.recViewPartecipanti);
        RecViewAvatarAdapter adapterUsers = new RecViewAvatarAdapter();
        adapterUsers.setContext(context,activity,main);
        adapterUsers.setCarrello(carrello);
        users.setAdapter(adapterUsers);
        users.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //Di prova


        RecyclerView cartsRecView = bottomSheetDialog.findViewById(R.id.recViewCarrelliProdotti);
        RecViewCartSheet adapter = new RecViewCartSheet();
        adapter.setContext(context,activity,main);
        adapter.setParams(carrello);
        cartsRecView.setAdapter(adapter);
        cartsRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        ((ImageView)bottomSheetDialog.findViewById(R.id.backImageCartSheet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.updateInteface();
                bottomSheetDialog.hide();
            }
        });
        bottomSheetDialog.show();
    }
}
