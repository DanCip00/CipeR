package it.ciper.home.viewProdotto.carrelliProductSheet;

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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.json.DownloadImageTask;
import it.ciper.listeners.product.AddToCart;

public class InnerRecViewProdAdapter extends RecyclerView.Adapter<InnerRecViewProdAdapter.ViewHolder> {

    protected List<ProductAndPriceAPI> items;
    protected DataCenter dataCenter;
    protected List<ShopAPI>shops;
    protected ProductAPI productAPI;
    protected CarrelloAPI cart;
    private Context context;
    private Activity activity;
    private MainActivity main;

    void setCartItems(DataCenter dataCenter, String cartCod, ProductAPI productAPI) {

        this.productAPI = productAPI;
        this.cart = dataCenter.getCarrelloAPI(cartCod);
        this.dataCenter=dataCenter;

        shops = dataCenter.getCartSellers(cart);
        if (shops.size()!=0)
            items = shops.stream()
                    .filter(s->dataCenter.getProductAndPriceAPI(productAPI.getProductcod(), s.getSellercod())!=null)
                    .map(s->dataCenter.getProductAndPriceAPI(productAPI.getProductcod(), s.getSellercod()))
                    .collect(Collectors.toList());
        if (items==null)
            items = new LinkedList<>();
    }

    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_add_product_productsheet, parent, false);
        InnerRecViewProdAdapter.ViewHolder holder = new InnerRecViewProdAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductAndPriceAPI pap = items.get(position);
        if (pap.isOffert()){
            holder.oldPrice.setText(pap.getPrice().getPrice().toString());
            holder.newPrice.setText(pap.getPrice().getOffertprice().toString());
            holder.oldPrice.setVisibility(View.VISIBLE);
        }else
            holder.newPrice.setText(pap.getPrice().getPrice().toString());

        ShopAPI shop = shops.stream().filter(s->s.getSellercod()==pap.getPrice().getSellercod()).findAny().get();

        holder.shopName.setText(shop.getSellername());


        new DownloadImageTask((ImageView) holder.shopIcon)
                .execute(shop.getSrclogo());

        AddToCart addToCart = new AddToCart();
        addToCart.setContextAndActivity(context,activity,main,dataCenter,cart,pap);
        holder.addButton.setOnClickListener(addToCart);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView oldPrice, newPrice, shopName;
        private ImageView shopIcon;
        private Button addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addButton = itemView.findViewById(R.id.addToCartProdictSheet);
            oldPrice = itemView.findViewById(R.id.oldPriceProductSheet);
            newPrice = itemView.findViewById(R.id.newPriceProductSheet);
            shopName = itemView.findViewById(R.id.shopNameProductSheet);
            shopIcon = itemView.findViewById(R.id.shopIconProductSheet);

        }
    }
}
