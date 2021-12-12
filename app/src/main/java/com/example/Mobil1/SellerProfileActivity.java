package com.example.Mobil1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Mobil1.databinding.ActivitySellerProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SellerProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        Activity miactividad=this;
        TextView TXV_SELLERNAMEPROFILE=findViewById(R.id.TXV_SellerNameProfile);
        TextView TXV_SELLERTOTALPRODUCT=findViewById(R.id.TXV_SellerTotalProducts);
        RecyclerView RCV_SELLERPRODUCTS=findViewById(R.id.RCV_SellerProfile);
        SharedPreferences mipreferencia= getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
        String NameSeller=mipreferencia.getString("usuario_seller","nulo");
        db = FirebaseFirestore.getInstance();
        TXV_SELLERNAMEPROFILE.setText(NameSeller);
        RCV_SELLERPRODUCTS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RCV_SELLERPRODUCTS.setLayoutManager(new LinearLayoutManager(this));
        int ContProducts=0;
        db.collection("Products")
                .whereEqualTo("User", NameSeller)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int ContProducts=0;
                        if (task.isSuccessful()) {
                            JSONArray ListaProductos=new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContProducts=ContProducts+1;
                                Log.e("TAG", document.getId() + " => " + document.getData());
                                String user=document.getData().get("User").toString();
                                int Amount=Integer.parseInt(document.getData().get("Amount").toString());
                                float Price=Float.parseFloat(document.getData().get("Price").toString());
                                String Url=document.getData().get("Url").toString();
                                String Info=document.getData().get("Info").toString();
                                String Name=document.getData().get("Name").toString();
                                JSONObject producto=new JSONObject();
                                try{
                                    producto.put("User",user);
                                    producto.put("Amount",Amount);
                                    producto.put("precio",Price);
                                    producto.put("imagen",Url);
                                    producto.put("Info",Info);
                                    producto.put("nombre",Name);
                                    ListaProductos.put(producto);

                                }catch(JSONException e){


                                }
                                mAdapter=new SellerAdapter(ListaProductos,miactividad);
                                RCV_SELLERPRODUCTS.setAdapter(mAdapter);
                                TXV_SELLERTOTALPRODUCT.setText(ContProducts+"");

                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });








    }


}
class SellerAdapter  extends RecyclerView.Adapter<com.example.Mobil1.SellerAdapter.ViewHolder> implements result {

    private  JSONArray productos;

    private Activity miActividad;

    public SellerAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.SellerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sellerprofile,parent,false);
        com.example.Mobil1.SellerAdapter.ViewHolder viewHolder=new com.example.Mobil1.SellerAdapter.ViewHolder(v);

        return viewHolder;


    }
    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.SellerAdapter.ViewHolder holder, int position) {

        try{


            String nombre=productos.getJSONObject(position).getString("nombre");
            String precio=productos.getJSONObject(position).getString("precio");
            String imagen=productos.getJSONObject(position).getString("imagen");
            String Amount=productos.getJSONObject(position).getString("Amount");
            String Info=productos.getJSONObject(position).getString("Info");
            String User=productos.getJSONObject(position).getString("User");
            holder.name_rv.setText(nombre);
            Glide.with(miActividad).load(imagen).into(holder.image_rv);
            holder.button_hist_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences miprefencia= miActividad.getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=miprefencia.edit();
                    editor.putString("nombre_producto",nombre);
                    editor.putString("imagen_producto",imagen);
                    editor.putString("precio_producto",precio);
                    editor.putString("Amount_producto",Amount);
                    editor.putString("Info_producto",Info);
                    editor.putString("User_producto",User);
                    editor.commit();
                    Intent intent=new Intent(miActividad, producto.class);
                    miActividad.startActivity(intent);
                }
            });






        }catch (Exception  e)
        {
            String name="no se pudo";
            holder.name_rv.setText(name);



        }
    }

    @Override

    public int getItemCount(){

        return this.productos.length();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView name_rv;
        private final ImageView image_rv;
        private final Button button_hist_info;

        public ViewHolder(View v) {

            super(v);
            name_rv = (TextView) v.findViewById(R.id.TXV_SellerProfileNameProduct);
            image_rv=(ImageView) v.findViewById(R.id.IMV_SellerProfileImagProduct);
            button_hist_info=(Button) v.findViewById(R.id.BTN_SellerProfileBtnDetails);


        }
    }







}