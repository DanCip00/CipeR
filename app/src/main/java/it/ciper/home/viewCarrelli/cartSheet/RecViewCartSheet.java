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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.Carrello;
import it.ciper.data.dataClasses.carrello.CartItem;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.home.viewCarrelli.RecViewCarrelliAdapter;

public class RecViewCartSheet extends RecyclerView.Adapter<RecViewCartSheet.ViewHolder> {


    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    protected Carrello carrello;
    protected boolean empty = false;
    protected List<ShopAPI> shopAPIList =null;

    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }

    public void setParams(Carrello carrello){
        this.carrello = carrello;
        Map<ShopAPI, List<CartItem>> map = carrello.getCartItems();
        if (map == null || map.isEmpty()){
            shopAPIList = new LinkedList<>();
            empty=true;
            return;
        }
        shopAPIList = map.keySet().stream().collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cart_sheet, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopAPI shop = shopAPIList.get(position);

        holder.shopName.setText(shop.getSellername());
        holder.modCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fare questo listener
            }
        });
        holder.startShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        Glide.with(context)
                .load(shop.getSrclogo())
                .into(holder.shopLogo);

        //TODO add Inner RecView
        InnerRecViewCartSheet adapter = new InnerRecViewCartSheet();
        adapter.setContext(context,activity,main);
        adapter.setParams(carrello,shop);
        holder.productsRecView.setAdapter(adapter);
        holder.productsRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return shopAPIList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView shopName, startShopButton;
        private Button modCartButton;
        private RecyclerView productsRecView;
        private ImageView shopLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopLogo = itemView.findViewById(R.id.shopIconCartSheet);
            shopName = itemView.findViewById(R.id.shopNameCartSheet);
            modCartButton = itemView.findViewById(R.id.modShopCartSheet);
            startShopButton = itemView.findViewById(R.id.shopNameCartSheet);
            productsRecView = itemView.findViewById(R.id.productListCartSheet);
        }
    }
}
