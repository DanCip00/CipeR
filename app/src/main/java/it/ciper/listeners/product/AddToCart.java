package it.ciper.listeners.product;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.api.interfacce.SettingsApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;

public class AddToCart implements View.OnClickListener {

    protected Context context;
    protected Activity activity;
    protected DataCenter dataCenter;
    protected MainActivity main;
    protected ProductAndPriceAPI productAndPriceAPI;
    protected CarrelloAPI carrelloAPI;


    public void setContextAndActivity(Context context, Activity activity, MainActivity main, DataCenter dataCenter, CarrelloAPI carrelloAPI, ProductAndPriceAPI productAndPriceAPI){
        this.activity = activity;
        this.context = context;
        this.dataCenter = dataCenter;
        this.main = main;
        this.carrelloAPI = carrelloAPI;
        this.productAndPriceAPI = productAndPriceAPI;
    }

    @Override
    public void onClick(View view) {
        context=view.getContext();
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.popup_add_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        NumberPicker numberPicker = dialog.findViewById(R.id.numberPikerAddToCart);
        Button start = dialog.findViewById(R.id.addProductPopupButton);

        numberPicker.setValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setMinValue(1);

        Glide.with(dialog.getContext())
                .load(productAndPriceAPI.getSrcimage())
                .into((ImageView) dialog.findViewById(R.id.productImageAddProd));

        TextView sum = dialog.findViewById(R.id.addingPricePopup);
        sum.setText((productAndPriceAPI.isOffert()?productAndPriceAPI.getPrice().getOffertprice():productAndPriceAPI.getPrice().getPrice())+"€");
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                sum.setText(formatter.format(numberPicker.getValue()*(productAndPriceAPI.isOffert()?productAndPriceAPI.getPrice().getOffertprice():productAndPriceAPI.getPrice().getPrice()))+"€");
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductInterfaceApi.addToSherCart(dataCenter.getApiKey(),carrelloAPI.getCartcod(),productAndPriceAPI,numberPicker.getValue());
                dialog.hide();
                main.updateCart();
            }
        });
        dialog.show();
    }
}
