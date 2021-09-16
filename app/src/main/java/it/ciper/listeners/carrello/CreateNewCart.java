package it.ciper.listeners.carrello;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;

public class CreateNewCart implements View.OnClickListener {
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

                CarrelliInterfaceApi.addCart(dataCenter.getApiKey(),text.getText().toString());
                bottomSheetDialog.hide();
                main.updateInteface();
            }
        });
    }
}
