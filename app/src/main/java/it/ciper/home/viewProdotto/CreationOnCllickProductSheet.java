package it.ciper.home.viewProdotto;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Callable;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.home.viewProdotto.carrelliProductSheet.RecViewProductAdapter;
import it.ciper.home.viewProdotto.doveConviene.RecViewDoveConviene;


public class CreationOnCllickProductSheet implements Callable<Boolean>, View.OnClickListener {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    protected RecyclerView carrelliProdotto;
    protected RecViewProductAdapter carrelliProductAdapter;
    protected TextView description;

    protected RecyclerView doveConviene;
    protected RecViewDoveConviene recViewDoveConvieneAdapter;

    protected ImageView productImage;
    protected ProductAPI productAPI;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter,ProductAPI productAPI){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
        this.productAPI = productAPI;
    }
    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }

    public void setProductAPI(ProductAPI productAPI) {
        this.productAPI = productAPI;
    }

    @Override
    public Boolean call() throws Exception {
        display();
        return true;
    }

    @Override
    public void onClick(View view) {
        display();

    }

    public void display(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.product_sheet,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetDialog.findViewById(R.id.backImageProductSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
            }
        });

        productImage = bottomSheetDialog.findViewById(R.id.productImageProductSheet);

        Glide.with(activity)
                .load(productAPI.getSrcimage())
                .into((ImageView) bottomSheetDialog.findViewById(R.id.productImageProductSheet));

        description = bottomSheetDialog.findViewById(R.id.descriptionText);
        description.setText(productAPI.getDescription());

        ((TextView)bottomSheetDialog.findViewById(R.id.productNameProductSheet)).setText(productAPI.getName());
        ((TextView)bottomSheetDialog.findViewById(R.id.produttoreTextProductSheet)).setText(productAPI.getProducer());

        carrelliProdotto = bottomSheetDialog.findViewById(R.id.carrelliProdutSheet);
        carrelliProductAdapter = new RecViewProductAdapter();
        carrelliProductAdapter.setDataCenter(dataCenter,productAPI);
        carrelliProductAdapter.setContext(context,activity,mainActivity);
        carrelliProdotto.setAdapter(carrelliProductAdapter);
        carrelliProdotto.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        if (carrelliProductAdapter.getEmpty())
            bottomSheetDialog.findViewById(R.id.staticTextItuoiCarrelli).setVisibility(View.GONE);

        doveConviene = bottomSheetDialog.findViewById(R.id.doveConvieneRecView);
        recViewDoveConvieneAdapter = new RecViewDoveConviene();
        recViewDoveConvieneAdapter.setContext(context,activity,mainActivity,dataCenter,productAPI);
        if (recViewDoveConvieneAdapter.getEmpty()) {
            doveConviene.setVisibility(View.GONE);
            bottomSheetDialog.findViewById(R.id.staticTextDoveConviene).setVisibility(View.GONE);
        }else {
            doveConviene.setAdapter(recViewDoveConvieneAdapter);
            doveConviene.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                freeMemo();
            }
        });
    }

    private void freeMemo(){
        productImage.setImageBitmap(null);
        context =null;
        activity =null;
        mainActivity =null;
        dataCenter =null;
        productAPI =null;
        doveConviene =null;
        recViewDoveConvieneAdapter =null;
        carrelliProdotto =null;
        carrelliProdotto =null;
        description.setText(null);
        System.gc();
    }
}
