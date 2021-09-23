package it.ciper.home.viewCarrelli;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;

import it.ciper.home.viewCarrelli.cartSheet.CreateCartSheet;
import it.ciper.json.DividerItemDecorationCiper;
import it.ciper.listeners.carrello.CreateNewCart;
import it.ciper.listeners.carrello.ModifyButtonCart;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecViewCarrelliAdapter extends RecyclerView.Adapter<RecViewCarrelliAdapter.ViewHolder> {


    private List<CarrelloAPI> carrelliAPI;
    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;

    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }

    public void setCarrelli(DataCenter dataCenter) {
        this.carrelliAPI = dataCenter.getAllCarrelliAPI();
        this.dataCenter = dataCenter;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View viewAdd = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_carrello, parent, false);
                RecViewCarrelliAdapter.ViewHolderCreateCart holderAdd = new RecViewCarrelliAdapter.ViewHolderCreateCart(viewAdd);
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

                break;
            default:
                ModifyButtonCart listener = new ModifyButtonCart();
                listener.setContextAndActivity(main,activity,dataCenter,carrelliAPI.get(posizione));
                holder.modCartButton.setOnClickListener(listener);

                CreateCartSheet createCartSheet = new CreateCartSheet();
                createCartSheet.setContext(context,activity,main,dataCenter);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createCartSheet.setCarrelloAPI(carrelliAPI.get(posizione));
                        createCartSheet.display();
                    }
                });

                //TODO implements avatar for each user
                InnerRecViewAdapter adapter = new InnerRecViewAdapter();
                adapter.setParams(activity,context,main,dataCenter);
                adapter.setCartItems(dataCenter, carrelliAPI.get(posizione).getCartcod(),carrelliAPI.get(posizione));
                holder.titoloCarrelloTextView.setText(carrelliAPI.get(posizione).getTitolo());
                holder.shopsListRec.setAdapter(adapter);
                holder.shopsListRec.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                DividerItemDecorationCiper itemDecor = new DividerItemDecorationCiper(context, HORIZONTAL);
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
        private ConstraintLayout topBarCarrello;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            topBarCarrello = itemView.findViewById(R.id.topBarCarrello);
            titoloCarrelloTextView = itemView.findViewById(R.id.titoloCarrelloProductSheet);
            modCartButton = itemView.findViewById(R.id.modCart);
            shopsListRec = itemView.findViewById(R.id.shopsListProductSheet);
            this.itemView = itemView;
            this.parent = itemView.findViewById(R.id.carrelloHome);
        }
    }

    public class ViewHolderCreateCart extends RecViewCarrelliAdapter.ViewHolder{

        public ViewHolderCreateCart(@NonNull View itemView) {
            super(itemView);
            CreateNewCart listener = new CreateNewCart();
            listener.setContextAndActivity(context, activity,main, dataCenter);
            itemView.setOnClickListener(listener);
        }
    }
}
