package it.ciper.home.viewCarrelli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.ciper.R;
import it.ciper.dataClasses.*;
import it.ciper.json.DownloadImageTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class RecViewCarrelliAdapter extends RecyclerView.Adapter<RecViewCarrelliAdapter.ViewHolder> {


    ArrayList<Carrello> carrelli;
    private Context context;

    public void setCarrelli(Collection<Carrello> carrelli) {
        this.carrelli = carrelli.stream().collect(Collectors.toCollection(()->new ArrayList<>()));
        notifyDataSetChanged();
    }
    public  void setContext(Context context){
        this.context= context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrello_item, parent, false);
        RecViewCarrelliAdapter.ViewHolder holder = new RecViewCarrelliAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titoloCarrelloTextView.setText(""+carrelli.get(position).getTitolo());
        holder.numeroOggettiTextView.setText(""+carrelli.get(position).getNumOggetti());
        holder.startShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Pressione" + position, Toast.LENGTH_SHORT).show();
                System.out.println("Pressione");
            }
        });
        new DownloadImageTask((ImageView) holder.shopIconView)
                .execute(carrelli.get(position).getShop().getLogo());
    }

    @Override
    public int getItemCount() {
        return carrelli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView shopIconView;
        private TextView titoloCarrelloTextView, numeroOggettiTextView;
        private Button startShoppingButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopIconView = itemView.findViewById(R.id.shopIcon);
            titoloCarrelloTextView = itemView.findViewById(R.id.titoloCarrello);
            numeroOggettiTextView = itemView.findViewById(R.id.numeroOggetti);
            startShoppingButton = itemView.findViewById(R.id.startShopButton);

        }
    }
}
