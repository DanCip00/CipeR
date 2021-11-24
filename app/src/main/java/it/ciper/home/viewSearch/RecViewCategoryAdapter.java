package it.ciper.home.viewSearch;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.List;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.data.DataCenter;


public class RecViewCategoryAdapter extends RecyclerView.Adapter<RecViewCategoryAdapter.ViewHolder> {

    //Cambio di stile per la selezione
    protected ConstraintLayout layoutSelected = null;
    protected TextView textSelected = null;
    protected int selected =-1;

    // Vettore di prova TODO Fare implementazione con API
    String[] lista = { "Biscotti", "Tonno", "Sugo","Olive", "Pasta"};

    protected Activity activity;
    protected Context context;
    protected MainActivity mainActivity;
    protected DataCenter dataCenter;

    public void setParams(Activity activity, Context context, MainActivity mainActivity, DataCenter dataCenter){
        this.activity = activity;
        this.context = context;
        this.mainActivity = mainActivity;
        this.dataCenter = dataCenter;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_search, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textCategory.setText(lista[position]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedChangesColor(position,holder.layout,holder.textCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.length;
    }


    private void selectedChangesColor(int position,ConstraintLayout layout, TextView text){

        if (selected>=0){
            layoutSelected.setBackground(context.getDrawable(R.drawable.shape_category_item));
            textSelected.setTextColor(context.getResources().getColor(R.color.ciper));
            if (selected == position){
                selected = -1;
                return;
            }
        }
        selected = position;
        layoutSelected = layout;
        textSelected = text;

        layoutSelected.setBackground(context.getDrawable(R.drawable.shape_category_item_selected));
        textSelected.setTextColor(context.getResources().getColor(R.color.white));
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView textCategory;
        protected ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textCategory = itemView.findViewById(R.id.categoryItemSearch);
            layout = itemView.findViewById(R.id.ConstraintLayoutCategoryItem);

        }
    }
}
