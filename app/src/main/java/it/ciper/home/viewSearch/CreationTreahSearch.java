package it.ciper.home.viewSearch;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Callable;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;

public class CreationTreahSearch implements Callable<Boolean>, View.OnClickListener {
    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }

    protected TextView searchBar;
    protected RecyclerView recyclerView;
    protected RecViewSearchAdapter recViewSearchAdapter;
    protected List<ProductAPI> products;

    @Override
    public Boolean call() throws Exception {
        disp();
        return true;
    }


    @Override
    public void onClick(View view) {
        disp();
    }

    public void disp(){
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.popup_search_prod);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        searchBar = dialog.findViewById(R.id.search_bar_popup);
        recyclerView = dialog.findViewById(R.id.recSearchProd);
        recViewSearchAdapter = new RecViewSearchAdapter();
        recViewSearchAdapter.setParams(activity,context,mainActivity,dataCenter);
        recViewSearchAdapter.setProducts(null);
        recyclerView.setAdapter(recViewSearchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        searchBar.setCursorVisible(true);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString()==null||editable.toString().compareTo("")==0){
                    recViewSearchAdapter.setProducts(null);
                    System.gc();
                    return;
                }
                products = dataCenter.searchProduct(editable.toString());
                recViewSearchAdapter.setProducts(products);
            }
        });

        dialog.findViewById(R.id.backImageSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                freeMemory();
            }
        });
        dialog.getWindow().setLayout(-1,-1);
        dialog.show();
        if(searchBar.requestFocus()) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void freeMemory(){
        context = null;
        activity = null;
        mainActivity = null;
        recyclerView =null;
        recViewSearchAdapter =null;
        products =null;
        System.gc();
    }
}
