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
import it.ciper.data.dataClasses.carrello.CarrelloAPI;

public class ModifyButtonCart implements View.OnClickListener {
    protected Context context;
    protected Activity activity;
    protected DataCenter dataCenter;
    protected MainActivity main;
    protected CarrelloAPI cart;

    public void setContextAndActivity(MainActivity main,Activity activity, DataCenter dataCenter, CarrelloAPI cart){
        this.activity = activity;
        this.dataCenter = dataCenter;
        this.main = main;
        this.cart = cart;
    }

    @Override
    public void onClick(View view) {

        context=view.getContext();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.modify_cart_sheet,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        Button renameButton = bottomSheetDialog.findViewById(R.id.renameButton);
        Button deleteButton = bottomSheetDialog.findViewById(R.id.deleteButtonModifyCart);
        EditText text =(EditText) bottomSheetDialog.findViewById(R.id.modifyTitleTextEdit);

        text.setText(cart.getTitolo());

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buff = text.getText().toString();
                System.out.println("Stringa--->"+buff);
                if (buff == null || buff.length()==0){
                    Toast.makeText(context.getApplicationContext(),"Inserire un titolo!",Toast.LENGTH_SHORT).show();
                    return;
                }
                dataCenter.renameCart(cart.getCartcod(),buff);
                bottomSheetDialog.hide();
                main.updateInteface();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCenter.remCarrello(cart);
                bottomSheetDialog.hide();
                main.updateInteface();
            }
        });



    }
}
