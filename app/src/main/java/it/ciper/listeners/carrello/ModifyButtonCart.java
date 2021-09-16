package it.ciper.listeners.carrello;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import it.ciper.MainActivity;
import it.ciper.data.DataCenter;

public class ModifyButtonCart implements View.OnClickListener {
    Context context;
    Activity activity;
    DataCenter dataCenter;
    MainActivity main;

    public void setContextAndActivity(Context context, Activity activity, MainActivity main, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.dataCenter = dataCenter;
        this.main = main;
    }

    @Override
    public void onClick(View view) {

    }
}
