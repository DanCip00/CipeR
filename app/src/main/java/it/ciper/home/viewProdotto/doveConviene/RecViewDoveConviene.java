package it.ciper.home.viewProdotto.doveConviene;

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

import com.bumptech.glide.Glide;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.listeners.product.AddNewShopToCart;

public class RecViewDoveConviene extends RecyclerView.Adapter<RecViewDoveConviene.ViewHolder> {

    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    protected List<ProductAndPriceAPI> productAndPriceAPIList;
    protected ProductAPI productAPI;
    protected Boolean empty = false;

    public  void setContext(Context context, Activity activity, MainActivity main, DataCenter dataCenter, ProductAPI productAPI){
        this.context= context;
        this.productAPI = productAPI;
        this.activity =activity;
        this.dataCenter = dataCenter;
        this.main = main;
        productAndPriceAPIList = dataCenter.getAllProductAndPriceAPI(productAPI);
        if (productAndPriceAPIList==null)
            productAndPriceAPIList = new LinkedList<>();
        else
            productAndPriceAPIList=productAndPriceAPIList.stream()
                    .filter(pap->{
                        float metri = dataCenter.getDistace(dataCenter.getShopAPI(pap.getPrice().getSellercod()));
                        if (metri<1000 * 20)
                            return true;
                        return false;
                    })
                    .sorted(Comparator.comparing(pap->pap.isOffert()?pap.getPrice().getOffertprice():pap.getPrice().getPrice())).collect(Collectors.toList());
        if (productAndPriceAPIList.size()==0)
            empty =true;
        notifyDataSetChanged();
    }

    public Boolean getEmpty() {
        return empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dove_conviene_item, parent, false);
        RecViewDoveConviene.ViewHolder holder = new RecViewDoveConviene.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductAndPriceAPI pap = productAndPriceAPIList.get(position);
        if (pap.isOffert()){
            holder.oldPrice.setText(pap.getPrice().getPrice().toString()+"€");
            holder.newPrice.setText(pap.getPrice().getOffertprice().toString()+"€");
            holder.oldPrice.setVisibility(View.VISIBLE);
        }else {
            holder.newPrice.setText(pap.getPrice().getPrice().toString()+"€");
            holder.oldPrice.setVisibility(View.INVISIBLE);
        }

        ShopAPI shop = dataCenter.getShopAPI(pap.getPrice().getSellercod());
        holder.shopName.setText(shop.getSellername());

        Glide.with(holder.itemView)
                .load(shop.getSrclogo())
                .into(holder.shopImage);

        float metri =dataCenter.getDistace(dataCenter.getShopAPI(pap.getPrice().getSellercod()));
        if (metri<1000){
            holder.unit.setText("metri");
            holder.distance.setText(Integer.valueOf((int)metri).toString());
        }else
            holder.distance.setText(Integer.valueOf((int)(metri/1000)).toString());
        AddNewShopToCart addNewShopToCart = new AddNewShopToCart();
        addNewShopToCart.setParams(activity,context, main, dataCenter, pap);
        holder.addToCart.setOnClickListener(addNewShopToCart);
    }

    @Override
    public int getItemCount() {
        return productAndPriceAPIList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView shopName, oldPrice, newPrice, distance, unit;
        ImageView shopImage;
        Button addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.shopImage = itemView.findViewById(R.id.shopIconDoveConviene);
            this.shopName= itemView.findViewById(R.id.productNameSearch);
            this.oldPrice = itemView.findViewById(R.id.oldPriceDoveConviene);
            this.newPrice = itemView.findViewById(R.id.newPriceDoveConviene);
            this.distance = itemView.findViewById(R.id.distanzaDoveConviene);
            this.addToCart = itemView.findViewById(R.id.addToCartDoveConviene);
            this.unit = itemView.findViewById(R.id.staticTextKm);
        }
    }

}
