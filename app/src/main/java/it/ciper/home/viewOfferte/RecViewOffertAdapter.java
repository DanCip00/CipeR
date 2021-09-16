package it.ciper.home.viewOfferte;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.json.DownloadImageTask;

public class RecViewOffertAdapter extends RecyclerView.Adapter<RecViewOffertAdapter.ViewHolder>{

    List<ProductAndPriceAPI> topFiveOfferts ;
    DataCenter dataCenter;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View viewMoreInfo = LayoutInflater.from(parent.getContext()).inflate(R.layout.offert_more_info_item, parent, false);
                ViewHolder holderMoreInfo = new ViewHolder(viewMoreInfo);
                return holderMoreInfo;
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offert_item, parent, false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType()==0) {
            holder.productName.setText(topFiveOfferts.get(position).getSummaryname());
            holder.oldPrice.setText(topFiveOfferts.get(position).getPrice().getPrice() + "€");
            holder.newPrice.setText(topFiveOfferts.get(position).getPrice().getOffertprice() + "€");


            new DownloadImageTask((ImageView) holder.productImage)
                    .execute(topFiveOfferts.get(position).getSrcimage());

            new DownloadImageTask((ImageView) holder.shopLogo)
                    .execute(dataCenter.getShopAPI(topFiveOfferts.get(position).getPrice().getSellercod()).getSrclogo());

        }
    }

    @Override
    public int getItemCount() {
        return topFiveOfferts.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position<topFiveOfferts.size())
            return 0;
        return 1;
    }

    public void setTopFiveOfferts(List<ProductAndPriceAPI> offerts, DataCenter dataCenter) {
        topFiveOfferts = offerts;
        this.dataCenter=dataCenter;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productName, oldPrice, newPrice;
        private ImageView productImage, shopLogo;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            oldPrice = itemView.findViewById(R.id.oldPrice);
            newPrice = itemView.findViewById(R.id.newPrice);
            productImage = itemView.findViewById(R.id.productImage);
            shopLogo = itemView.findViewById(R.id.shopLogoOffert);
        }
    }

    public class ViewHolderMoreInfo extends RecViewOffertAdapter.ViewHolder{

        public ViewHolderMoreInfo(@NonNull View itemView) {
            super(itemView);
        }
    }
}
