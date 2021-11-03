package it.ciper.home.viewCarrelli.cartSheet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.SettingsApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.carrello.Carrello;
import it.ciper.data.dataClasses.shop.ShopAPI;

public class RecViewAvatarAdapter extends RecyclerView.Adapter<RecViewAvatarAdapter.ViewHolder>{

    private Context context;
    private DataCenter dataCenter;
    private Activity activity;
    private MainActivity main;
    protected Carrello carrello;

    //TODO da rifate

    public  void setContext(Context context, Activity activity, MainActivity main){
        this.context= context;
        this.activity =activity;
        this.main = main;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
        //TODO
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avatar_item_cart_sheet, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position==0) {
            Glide.with(context)
                    .load("http://"+ SettingsApi.server+"/ciper/media/avatars/0.png")
                    .into(holder.avatar);

            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "This user!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Glide.with(context)
                    .load("http://"+SettingsApi.server+"/ciper/media/avatars/6.png")
                    .into(holder.avatar);
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Another user!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatarImageCartSheet);
        }
    }
}
