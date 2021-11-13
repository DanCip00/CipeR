package it.ciper.home.viewCarrelli;

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

import com.bumptech.glide.Glide;

import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.shop.ShopCartInfoAPI;
import it.ciper.home.viewCarrelli.cartSheet.CreateCartSheet;
import it.ciper.home.viewSearch.CreationTreahSearch;

public class InnerRecViewAdapter extends RecyclerView.Adapter<InnerRecViewAdapter.ViewHolder> {

    List<ShopCartInfoAPI> items;

    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;
    protected CarrelloAPI carrelloAPI;


    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }
    void setCartItems(DataCenter dataCenter, String cartCod, CarrelloAPI carrelloAPI) {
        items = CarrelliInterfaceApi.getCartSellersInfoList(dataCenter.getApiKey(), cartCod);
        this.carrelloAPI =carrelloAPI;
        this.dataCenter=dataCenter;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewAdd = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_item, parent, false);
                InnerRecViewAdapter.ViewHolderAddProd holderAdd = new InnerRecViewAdapter.ViewHolderAddProd(viewAdd);
                return holderAdd;
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
                InnerRecViewAdapter.ViewHolder holder = new InnerRecViewAdapter.ViewHolder(view);
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType()==0) {
            holder.numeroOggettiTextView.setText(items.get(position).getQuant().toString());
            holder.shopName.setText(dataCenter.getShopAPI(items.get(position).getSellerCod()).getSellername());

            Glide.with(holder.itemView).load(dataCenter.getShopAPI(items.get(position).getSellerCod()).getSrclogo())
                    .into(holder.shopIconView);
        }else if (holder.parent!=null){
            CreationTreahSearch creationTreahSearch = new CreationTreahSearch();
            creationTreahSearch.setParams(activity,context,mainActivity,dataCenter);
            holder.parent.setOnClickListener(creationTreahSearch);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size()==0)
            return 1;
        return 0;
    }
    @Override
    public int getItemCount() {
        if (items.size()==0){
            return 1;
        }
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView shopIconView;
        private TextView shopName, numeroOggettiTextView;
        private View itemView;
        protected ConstraintLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopIconView = itemView.findViewById(R.id.shopIconDoveConviene);
            shopName = itemView.findViewById(R.id.productNameSearch);
            numeroOggettiTextView = itemView.findViewById(R.id.numeroOggetti);
            this.itemView = itemView;

            CreateCartSheet createCartSheet = new CreateCartSheet();
            createCartSheet.setContext(context,activity,mainActivity,dataCenter);
            createCartSheet.setCarrelloAPI(carrelloAPI);
            createCartSheet.load();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createCartSheet.display();
                }
            });
        }
    }
    public class ViewHolderAddProd extends InnerRecViewAdapter.ViewHolder{

        public ViewHolderAddProd(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.addProductItem);

        }
    }
}
