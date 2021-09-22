package it.ciper.listeners.product;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.home.viewCarrelli.InnerRecViewAdapter;

public class RecViewCartAddShopAdapter extends RecyclerView.Adapter<RecViewCartAddShopAdapter.ViewHolder> {

    protected DataCenter dataCenter;
    protected List<CarrelloAPI> carrelli;
    protected boolean empty = false;

    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected ProductAndPriceAPI productAndPriceAPI;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter, ProductAndPriceAPI pap){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
        productAndPriceAPI = pap;
        updateData();
    }
    public void updateData(){
        carrelli = dataCenter.getAllCarrelliAPI();
        if (carrelli==null || carrelli.size()==0)
            empty=true;
        notifyDataSetChanged();
    }


    public boolean isEmpty() {
        return empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_shop_to_cart_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.shopName.setText(carrelli.get(position).getTitolo());
        AddToCart addToCart = new AddToCart();
        addToCart.setContextAndActivity(context,activity, mainActivity, dataCenter, carrelli.get(position),productAndPriceAPI);
        holder.addToCart.setOnClickListener(addToCart);

    }

    @Override
    public int getItemCount() {
        return carrelli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private Button addToCart;
        private TextView shopName;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.addToCart = itemView.findViewById(R.id.addProductToCartNewShop);
            this.shopName = itemView.findViewById(R.id.cartNameAddShop);
        }
    }
}
