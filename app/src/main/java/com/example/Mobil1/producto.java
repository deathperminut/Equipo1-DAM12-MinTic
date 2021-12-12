package com.example.Mobil1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Mobil1.databinding.ActivityProductoBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class producto extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityProductoBinding binding;
    FirebaseFirestore db;
    Activity miactividad=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_producto);
        SharedPreferences mipreferencia= getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
        String nombre=mipreferencia.getString("nombre_producto","nulo");
        String precio=mipreferencia.getString("precio_producto","nulo");
        String Amount=mipreferencia.getString("Amount_producto","nulo");
        String Info=mipreferencia.getString("Info_producto","nulo");
        String User=mipreferencia.getString("User_producto","nulo");
        String imagen=mipreferencia.getString("imagen_producto","nulo");


        TextView nombre_producto=findViewById(R.id.nombre_producto);
        ImageView imagen_producto=findViewById(R.id.imagen_producto);
        TextView precio_producto=findViewById(R.id.precio_producto);
        TextView Info_producto=findViewById(R.id.informacion_producto);
        TextView Amount_producto=findViewById(R.id.Amount_producto);
        TextView User_producto=findViewById(R.id.User_producto);
        Button Btn_Seller=findViewById(R.id.Btn_Seller);

        Info_producto.setText(Info);
        Amount_producto.setText(Amount);
        User_producto.setText(User);

        nombre_producto.setText(nombre);
        precio_producto.setText(precio);
        Glide.with(this).load(imagen).into(imagen_producto);
        Button Btn_carrito=findViewById(R.id.Btn_Carrito);
        Btn_carrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> Product = new HashMap<>();
                    Product.put("Name", nombre);
                    Product.put("Amount", Integer.parseInt(Amount));
                    Product.put("Price", Float.parseFloat(precio));
                    Product.put("Info", Info);
                    Product.put("User",User);
                    Product.put("Url",imagen);
                    String usuario=mipreferencia.getString("usuario","");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("CARRITO").document(usuario).collection("CARRITO_PRODUCTS").document(User+nombre)
                            .set(Product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(miactividad,"ADDED TO SHOPPING CAR",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Error writing document", e);
                                    Toast.makeText(miactividad,"Error",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
        Btn_Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=mipreferencia.edit();
                editor.putString("usuario_seller",User);
                editor.commit();
                Intent intent=new Intent(miactividad,SellerProfileActivity.class);
                startActivity(intent);

            }
        });








    }

}