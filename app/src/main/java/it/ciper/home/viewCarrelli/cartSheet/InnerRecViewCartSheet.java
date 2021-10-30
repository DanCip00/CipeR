package it.ciper.home.viewCarrelli.cartSheet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.Carrello;
import it.ciper.data.dataClasses.carrello.CartItem;
import it.ciper.data.dataClasses.shop.ShopAPI;

public class InnerRecViewCartSheet extends RecyclerView.Adapter<InnerRecViewCartSheet.ViewHolder> {

    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    protected Carrello carrello;
    protected ShopAPI shopAPI;
    protected List<CartItem> products = null;

    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }
    public void setParams(Carrello carrello, ShopAPI shopAPI){
        this.carrello = carrello;
        this.shopAPI = shopAPI;
        products = carrello.getCartItems().get(shopAPI);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_cart_sheet, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = products.get(position);

        holder.productName.setText(cartItem.getProductAndPriceAPI().getName());
        holder.numOggetti.setText(cartItem.getQuantity().toString());
        NumberFormat formatter = new DecimalFormat("#0.00");
        if (cartItem.getProductAndPriceAPI().isOffert()){
            holder.oldPrice.setVisibility(View.VISIBLE);
            holder.oldPrice.setText(formatter.format(Double.valueOf(cartItem.getQuantity()*cartItem.getProductAndPriceAPI().getPrice().getPrice()))+"€");
            holder.newPrice.setText(formatter.format(Double.valueOf(cartItem.getQuantity()*cartItem.getProductAndPriceAPI().getPrice().getOffertprice()))+"€");
        }else
            holder.newPrice.setText(formatter.format(Double.valueOf(cartItem.getQuantity()*cartItem.getProductAndPriceAPI().getPrice().getPrice()))+"€");


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productName, numOggetti, oldPrice, newPrice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameCartSheet);
            oldPrice = itemView.findViewById(R.id.oldPriceCartSheet);
            newPrice = itemView.findViewById(R.id.newPriceCartSheet);
            numOggetti = itemView.findViewById(R.id.numeroOggettiCartSheet);
        }
    }
}
