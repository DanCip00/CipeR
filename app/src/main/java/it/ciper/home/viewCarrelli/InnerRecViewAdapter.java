package it.ciper.home.viewCarrelli;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.shop.ShopCartInfoAPI;
import it.ciper.json.DownloadImageTask;

public class InnerRecViewAdapter extends RecyclerView.Adapter<InnerRecViewAdapter.ViewHolder> {

    List<ShopCartInfoAPI> items;
    DataCenter dataCenter;


    void setCartItems(DataCenter dataCenter, String cartCod) {
        items = CarrelliInterfaceApi.getCartSellersInfoList(dataCenter.getApiKey(), cartCod);
        this.dataCenter=dataCenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewAdd = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_item, parent, false);
                InnerRecViewAdapter.ViewHolder holderAdd = new InnerRecViewAdapter.ViewHolder(viewAdd);
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

            new DownloadImageTask((ImageView) holder.shopIconView)
                    .execute(dataCenter.getShopAPI(items.get(position).getSellerCod()).getSrclogo());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopIconView = itemView.findViewById(R.id.shopIconProductSheet);
            shopName = itemView.findViewById(R.id.shopNameProductSheet);
            numeroOggettiTextView = itemView.findViewById(R.id.numeroOggetti);
            this.itemView = itemView;
        }
    }
    public class ViewHolderAddProd extends InnerRecViewAdapter.ViewHolder{

        public ViewHolderAddProd(@NonNull View itemView) {
            super(itemView);

        }
    }
}
