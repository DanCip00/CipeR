package it.ciper.viewOfferte;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

import it.ciper.R;
import it.ciper.dataClasses.Offerts;
import it.ciper.json.DownloadImageTask;

public class RecViewOffertAdapter extends RecyclerView.Adapter<RecViewOffertAdapter.ViewHolder>{


    private ArrayList<Offerts> offerte = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offert_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productName.setText(offerte.get(position).getProdotto().getSummaryName());
        holder.oldPrice.setText(offerte.get(position).getProdotto().getPrice()+"€");
        holder.newPrice.setText(offerte.get(position).getNewPrice()+"€");
        holder.productImage.setImageURI(Uri.parse(offerte.get(position).getProdotto().getSrcImage()));

        new DownloadImageTask((ImageView) holder.productImage)
                .execute(offerte.get(position).getProdotto().getSrcImage());

        new DownloadImageTask((ImageView) holder.shopLogo)
                .execute(offerte.get(position).getSeller().getLogo());
    }

    @Override
    public int getItemCount() {
        return offerte.size();
    }

    public void setOfferte(LinkedList<Offerts> offerte) {
        this.offerte = offerte.stream().collect(Collectors.toCollection(()->new ArrayList<>()));
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
}
