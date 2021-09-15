package it.ciper.home.viewCarrelli;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;

import it.ciper.json.DownloadImageTask;
import kotlin._Assertions;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RecViewCarrelliAdapter extends RecyclerView.Adapter<RecViewCarrelliAdapter.ViewHolder> {


    private List<CarrelloAPI> carrelliAPI;
    private Context context;
    private DataCenter dataCenter;
    private Activity activity;

    public void setCarrelli(DataCenter dataCenter) {
        this.carrelliAPI = dataCenter.getAllCarrelliAPI();
        this.dataCenter = dataCenter;
        notifyDataSetChanged();
    }
    public  void setContext(Context context, Activity activity){
        this.context= context;
        this.activity =activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO distinzione tra le due view 0->default 1->Creation line
        switch (viewType){
            case 1:
                View viewAdd = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_carrello, parent, false);
                RecViewCarrelliAdapter.ViewHolder holderAdd = new RecViewCarrelliAdapter.ViewHolder(viewAdd);
                return holderAdd;
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrello, parent, false);
                RecViewCarrelliAdapter.ViewHolder holder = new RecViewCarrelliAdapter.ViewHolder(view);
                return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int posizione = position-1;
        switch (holder.getItemViewType()){
            case 1:
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetStyleDialogTheme);
                        View bottomSheetView = LayoutInflater.from(context.getApplicationContext())
                                .inflate(R.layout.new_cart_sheet,(LinearLayout)activity.findViewById())//TODO finire qua
                    }
                });
                break;
            default:
                holder.titoloCarrelloTextView.setText(carrelliAPI.get(posizione).getTitolo());
                //TODO set lissener for button
                //TODO implements avatar for each user
                InnerRecViewAdapter adapter = new InnerRecViewAdapter();
                adapter.setCartItems(dataCenter, carrelliAPI.get(posizione).getCartcod());

                holder.shopsListRec.setAdapter(adapter);
                holder.shopsListRec.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DividerItemDecoration itemDecor = new DividerItemDecoration(context, HORIZONTAL);
                holder.shopsListRec.addItemDecoration(itemDecor);

                if (CarrelliInterfaceApi.getCartSellersInfoList(dataCenter.getApiKey(),carrelliAPI.get(posizione).getCartcod()).size()==0){
                    holder.shopsListRec.setBackground(context.getDrawable(R.drawable.shape_carello_add_first_prod_recycler));
                }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position>0)
            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return carrelliAPI.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titoloCarrelloTextView;
        private Button modCartButton;
        private RecyclerView shopsListRec;
        private View itemView;
        private ConstraintLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titoloCarrelloTextView = itemView.findViewById(R.id.titoloCarrello);
            modCartButton = itemView.findViewById(R.id.modCart);
            shopsListRec = itemView.findViewById(R.id.shopsList);
            this.itemView = itemView;
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
