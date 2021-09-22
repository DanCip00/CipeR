package it.ciper.listeners.product;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;
import it.ciper.listeners.carrello.CreateNewCart;

public class AddNewShopToCart implements View.OnClickListener {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    protected RecyclerView recyclerView;
    protected RecViewCartAddShopAdapter adapter;
    protected ProductAndPriceAPI productAndPriceAPI;

    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter, ProductAndPriceAPI pap){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
        productAndPriceAPI = pap;
    }


    @Override
    public void onClick(View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.add_shop_to_cart,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);

        recyclerView = bottomSheetView.findViewById(R.id.lrecCarrelliAddShop);
        adapter = new RecViewCartAddShopAdapter();
        adapter.setParams(activity, context, mainActivity, dataCenter, productAndPriceAPI);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        CreateNewCart<RecViewCartAddShopAdapter> createNewCart = new CreateNewCart();
        createNewCart.setContextAndActivity(context,activity,mainActivity,dataCenter);
        createNewCart.setFunct(adapter,(a)->a.updateData());
        bottomSheetDialog.findViewById(R.id.createNewCartAddShop).setOnClickListener(createNewCart);



        bottomSheetDialog.show();
    }
}
