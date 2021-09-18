package it.ciper.home.viewOfferte;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Callable;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;

public class CreationThreadOfferte implements Callable<Boolean> {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    // Offerte
    RecViewOffertAdapter adapter;
    RecyclerView recyclerViewOff;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }

    @Override
    public Boolean call() {
        //Gestione OFFERTE
        adapter = new RecViewOffertAdapter();
        adapter.setTopFiveOfferts(dataCenter.getTopFiveOfferts(),dataCenter);
        adapter.setParams(activity,context,mainActivity,dataCenter);
        recyclerViewOff = activity.findViewById(R.id.offerte);
        recyclerViewOff.setAdapter(adapter);
        recyclerViewOff.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        return true;
    }


    public void updateInteface(){
        adapter.setTopFiveOfferts(dataCenter.getTopFiveOfferts(),dataCenter);
    }
}
