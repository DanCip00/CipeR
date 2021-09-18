package it.ciper.home.viewProdotto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Callable;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.home.viewProdotto.carrelliProductSheet.RecViewProductAdapter;
import it.ciper.json.DownloadImageTask;

public class CreationOnCllickProductSheet implements Callable<Boolean>, View.OnClickListener {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    protected RecyclerView carrelliProdotto;
    protected RecViewProductAdapter carrelliProductAdapter;
    protected TextView description;


    protected ProductAPI productAPI;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter,ProductAPI productAPI){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
        this.productAPI = productAPI;
    }


    @Override
    public Boolean call() throws Exception {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.product_sheet,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetDialog.findViewById(R.id.backImageProductSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
                mainActivity.updateInteface();
            }
        });

        new DownloadImageTask((ImageView) bottomSheetDialog.findViewById(R.id.productImageProductSheet))
                .execute(productAPI.getSrcimage());
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
        bottomSheetDialog.show();
        return true;
    }

    @Override
    public void onClick(View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetStyleDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.product_sheet,(ConstraintLayout)activity.findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);


        bottomSheetDialog.getWindow().setLayout(-1,-1);
        bottomSheetDialog.show();

        bottomSheetDialog.findViewById(R.id.backImageProductSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.hide();
                mainActivity.updateInteface();
            }
        });

        new DownloadImageTask((ImageView) bottomSheetDialog.findViewById(R.id.productImageProductSheet))
                .execute(productAPI.getSrcimage());
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

    }
}
