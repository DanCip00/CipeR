package it.ciper.home.viewCarrelli;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Callable;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;

public class CreationThreadCarrelli implements Callable<Boolean> {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    //Carrelli
    protected RecViewCarrelliAdapter adapterCarrelli;
    protected RecyclerView recyclerViewCarrelli;
    protected ImageView nessunCarrello;

    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }


    @Override
    public Boolean call() {

        //Gestione CARRELLI

        adapterCarrelli = new RecViewCarrelliAdapter();
        adapterCarrelli.setCarrelli(dataCenter);
        adapterCarrelli.setContext(context,activity,mainActivity);
        nessunCarrello = activity.findViewById(R.id.nessunCarrello);
        recyclerViewCarrelli = activity.findViewById(R.id.carrelli);
        recyclerViewCarrelli.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewCarrelli.setAdapter(adapterCarrelli);

        if (dataCenter.getAllCarrelliAPI().size()!=0)
            nessunCarrello.setVisibility(View.GONE);
        return true;
    }

    public void updateInteface(){
        adapterCarrelli.setCarrelli(dataCenter);
        if (dataCenter.getAllCarrelliAPI().size()!=0)
            nessunCarrello.setVisibility(View.GONE);
        else
            nessunCarrello.setVisibility(View.VISIBLE);
    }
}
