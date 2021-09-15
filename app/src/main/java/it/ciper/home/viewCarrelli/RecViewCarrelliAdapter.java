package it.ciper.home.viewCarrelli;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.ciper.R;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;

import it.ciper.json.DownloadImageTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RecViewCarrelliAdapter extends RecyclerView.Adapter<RecViewCarrelliAdapter.ViewHolder> {


    private List<CarrelloAPI> carrelliAPI;
    private Context context;
    private DataCenter dataCenter;

    public void setCarrelli(DataCenter dataCenter) {
        this.carrelliAPI = dataCenter.getAllCarrelliAPI();
        this.dataCenter = dataCenter;
        notifyDataSetChanged();
    }
    public  void setContext(Context context){
        this.context= context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrello, parent, false);
        RecViewCarrelliAdapter.ViewHolder holder = new RecViewCarrelliAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titoloCarrelloTextView.setText(carrelliAPI.get(position).getTitolo());
        //TODO set lissener for button
        //TODO implements avatar for each user
        InnerRecViewAdapter adapter = new InnerRecViewAdapter();
        adapter.setCartItems(dataCenter, carrelliAPI.get(position).getCartcod());

        holder.shopsListRec.setAdapter(adapter);
        holder.shopsListRec.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, HORIZONTAL);
        holder.shopsListRec.addItemDecoration(itemDecor);
    }

    @Override
    public int getItemCount() {
        return carrelliAPI.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titoloCarrelloTextView;
        private Button modCartButton;
        private RecyclerView shopsListRec;
        private View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titoloCarrelloTextView = itemView.findViewById(R.id.titoloCarrello);
            modCartButton = itemView.findViewById(R.id.modCart);
            shopsListRec = itemView.findViewById(R.id.shopsList);
            itemView = itemView;
        }
    }
}
