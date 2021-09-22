package it.ciper.listeners.carrello;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.function.Consumer;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.listeners.product.RecViewCartAddShopAdapter;

public class CreateNewCart<T extends RecyclerView.Adapter> implements View.OnClickListener {
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

     private T item = null;
     private Consumer<T> consumer = null;
     public void setFunct(T adapter, Consumer<T> cons){
        item = adapter;
        consumer = cons;
     }

    @Override
    public void onClick(View view) {
         context=view.getContext();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.new_cart_sheet,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        Button createButton = bottomSheetDialog.findViewById(R.id.createCartButton);
        EditText text =(EditText) bottomSheetDialog.findViewById(R.id.cartNameTextEdit);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buff = text.getText().toString();
                System.out.println("Stringa--->"+buff);
                if (buff == null || buff.length()==0){
                    Toast.makeText(context.getApplicationContext(),"Inserire un titolo!",Toast.LENGTH_SHORT).show();
                    return;
                }

                bottomSheetDialog.hide();
                CarrelliInterfaceApi.addCart(dataCenter.getApiKey(),text.getText().toString());
                if (item!=null)
                    consumer.accept(item);
                main.updateInteface();
            }
        });
    }
}
