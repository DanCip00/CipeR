package it.ciper.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Random;

import it.ciper.MainActivity;
import it.ciper.R;
import it.ciper.api.interfacce.SettingsApi;
import it.ciper.api.interfacce.UsersInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.home.viewCarrelli.RecViewCarrelliAdapter;

public class Registrati implements View.OnClickListener {


    protected Activity activity;
    protected Context context;
    protected LoginActivity loginActivity;


    public void setParams(Activity activity, Context context, LoginActivity loginActivity){
        this.activity = activity;
        this.context = context;
        this.loginActivity = loginActivity;
    }
    @Override
    public void onClick(View view) {
        //TODO implements bottom dialog
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.sign_in_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(-1,-1);
        dialog.show();

        dialog.findViewById(R.id.backImageSearchSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });


        Random rn = new Random();
        int i =rn.nextInt(10);
        Glide.with(dialog.getContext())
                .load("http://"+ SettingsApi.server +"/ciper/media/avatars/"+i+".png")
                .into((ImageView) dialog.findViewById(R.id.avatarImage));



        dialog.findViewById(R.id.submitSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, email, indirizzo, nome, cognome, password;
                try {
                    username = ((EditText) (dialog.findViewById(R.id.usernameTextEditSignIn))).getText().toString();
                    email = ((EditText) (dialog.findViewById(R.id.emailTextEditSignIn))).getText().toString();
                    indirizzo = ((EditText) (dialog.findViewById(R.id.addressTextEditSignIn))).getText().toString();
                    nome = ((EditText) (dialog.findViewById(R.id.nameTextEditSignIn))).getText().toString();
                    cognome = ((EditText) (dialog.findViewById(R.id.cognomeTextEditSignIn))).getText().toString();
                    password = ((EditText) (dialog.findViewById(R.id.editTextTextPasswordSignIn))).getText().toString();
                }catch(NullPointerException e) {
                    Toast.makeText(activity.getApplicationContext(), "Riempire tutti i campi!", Toast.LENGTH_LONG).show();
                    return;
                }


                String apiKey =UsersInterfaceApi.createUser(UsersInterfaceApi.getServerToken(),username,email,nome,cognome,password,indirizzo,i).replace("\"","");
                if (apiKey==null || apiKey.compareTo("false")==0) {
                    Toast.makeText(context.getApplicationContext(), "Username non disponibile!", Toast.LENGTH_LONG).show();
                    return;
                }
                activity.getSharedPreferences("login", activity.MODE_PRIVATE).edit().putString("apiKey", apiKey).apply();
                SharedPreferences preferences = activity.getSharedPreferences("login", activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("logged", "true");
                editor.putString("ricordami", "true");
                editor.apply();

                Intent intent = new Intent(loginActivity, MainActivity.class);
                context.startActivity(intent);
                activity.finish();
            }
        });

    }
}
