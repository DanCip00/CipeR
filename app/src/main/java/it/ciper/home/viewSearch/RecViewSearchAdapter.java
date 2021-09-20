package it.ciper.home.viewSearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.home.viewOfferte.RecViewOffertAdapter;
import it.ciper.home.viewProdotto.CreationOnCllickProductSheet;
import it.ciper.json.DownloadImageTask;

public class RecViewSearchAdapter extends RecyclerView.Adapter<RecViewSearchAdapter.ViewHolder> {


    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;
    protected CreationOnCllickProductSheet creationOnCllickProductSheet;

    protected List<ProductAPI> products;

    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
        creationOnCllickProductSheet = new CreationOnCllickProductSheet();
        creationOnCllickProductSheet.setParams(activity,context,mainActivity,dataCenter);
    }
    public void setProducts(List<ProductAPI> products){
        this.products = products;
        if (products==null)
            this.products = new LinkedList<>();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewMoreInfo = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_search, parent, false);
        RecViewSearchAdapter.ViewHolder holderMoreInfo = new RecViewSearchAdapter.ViewHolder(viewMoreInfo);
        return holderMoreInfo;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductAPI productAPI = products.get(position);
        holder.productName.setText(productAPI.getName());

        new DownloadImageTask((ImageView) holder.productImage)
                .execute(productAPI.getSrcimage());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display(position);
            }
        });
    }
    private void display (Integer position){
        creationOnCllickProductSheet.setProductAPI(products.get(position));
        creationOnCllickProductSheet.display();
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productName;
        private ImageView productImage;
        private ConstraintLayout parent;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            productName = itemView.findViewById(R.id.productNameSearch);
            productImage = itemView.findViewById(R.id.productImageSearch);
            parent = itemView.findViewById(R.id.search_item);
        }
    }
}
