package it.ciper.home.viewProdotto.carrelliProductSheet;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;

public class RecViewProductAdapter extends RecyclerView.Adapter<RecViewProductAdapter.ViewHolder> {
    //TODO questo è per carrelli ProductSheet
    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    private List<CarrelloAPI> carrelloAPIList;
    protected ProductAPI productAPI;
    protected Boolean empty =false;
    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }

    public void setDataCenter(DataCenter dataCenter, ProductAPI productAPI) {
        this.dataCenter = dataCenter;
        this.productAPI = productAPI;
        carrelloAPIList=dataCenter.getAllCarrelliAPI();
        carrelloAPIList=carrelloAPIList.stream().filter(c->{
            List<ShopAPI> shopAPIS = CarrelliInterfaceApi.getCartSellers(dataCenter.getApiKey(),c);
            if (shopAPIS == null || shopAPIS.size()==0)
                return false;
            shopAPIS =shopAPIS.stream().filter(s->{
                ProductAndPriceAPI pap = dataCenter.getProductAndPriceAPI(productAPI.getProductcod(), s.getSellercod());
                if (pap==null)
                    return false;
                return true;
                }).collect(Collectors.toList());
            if (shopAPIS==null || shopAPIS.size()==0)
                return false;
            return true;
        }).collect(Collectors.toList());
        if (carrelloAPIList==null || carrelloAPIList.size()==0)
            empty=true;
    }

    public java.lang.Boolean getEmpty() {
        return empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrello_product_sheet, parent, false);
        RecViewProductAdapter.ViewHolder holder = new RecViewProductAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrelloAPI carrelloAPI = carrelloAPIList.get(position);
        holder.cartName.setText(carrelloAPI.getTitolo());

        InnerRecViewProdAdapter adapter = new InnerRecViewProdAdapter();
        adapter.setCartItems(dataCenter, carrelloAPI.getCartcod(), productAPI);
        adapter.setContext(context,activity,main);
        holder.innerRec.setAdapter(adapter);
        holder.innerRec.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, HORIZONTAL);
        holder.innerRec.addItemDecoration(itemDecor);

    }

    @Override
    public int getItemCount() {
        return carrelloAPIList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView cartName;
        private RecyclerView innerRec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        cartName = itemView.findViewById(R.id.titoloCarrelloProductSheet);
        innerRec = itemView.findViewById(R.id.shopsListProductSheet);
        }
    }
}
