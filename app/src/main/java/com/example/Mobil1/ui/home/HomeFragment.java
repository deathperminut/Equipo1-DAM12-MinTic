package com.example.Mobil1.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Mobil1.R;
import com.example.Mobil1.producto;
import com.example.Mobil1.ui.home.ProductosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerV;
    private RecyclerView.Adapter mAdapter;
    //SharedPreferences mipreferencia;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerV=root.findViewById(R.id.recyclerV);
        //mipreferencia=getActivity().getSharedPreferences("MI_PREFERENCIA",Context.MODE_PRIVATE);
        recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        //String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            JSONArray ListaProductos=new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
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

                            }

                                //JSONArray JSONproductos= new JSONArray(productos);

                                mAdapter=new ProductosAdapter(ListaProductos,getActivity());
                                recyclerV.setAdapter(mAdapter);


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });






        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
class ProductosAdapter  extends RecyclerView.Adapter<com.example.Mobil1.ui.home.ProductosAdapter.ViewHolder>{

    private  JSONArray productos;

    private Activity miActividad;

    public ProductosAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.ui.home.ProductosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_productos,parent,false);
        com.example.Mobil1.ui.home.ProductosAdapter.ViewHolder viewHolder=new com.example.Mobil1.ui.home.ProductosAdapter.ViewHolder(v);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.ui.home.ProductosAdapter.ViewHolder holder, int position) {

        try{

            String nombre=productos.getJSONObject(position).getString("nombre");
            String precio=productos.getJSONObject(position).getString("precio");
            String imagen=productos.getJSONObject(position).getString("imagen");
            String Amount=productos.getJSONObject(position).getString("Amount");
            String Info=productos.getJSONObject(position).getString("Info");
            String User=productos.getJSONObject(position).getString("User");

            holder.name_rv.setText(nombre);
            holder.precio_rv.setText(precio);
            Glide.with(miActividad).load(imagen).into(holder.image_rv);
            holder.btn_details.setOnClickListener(new View.OnClickListener() {
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
            holder.btn_favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> Product = new HashMap<>();
                    Product.put("Name", nombre);
                    Product.put("Amount", Integer.parseInt(Amount));
                    Product.put("Price", Float.parseFloat(precio));
                    Product.put("Info", Info);
                    Product.put("User",User);
                    Product.put("Url",imagen);
                    SharedPreferences mipreferencia= miActividad.getSharedPreferences("MI_PREFERENCIA",Context.MODE_PRIVATE);
                    String usuario=mipreferencia.getString("usuario","");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("FAVORITES").document(usuario).collection("FAVORITES_PRODUCTS").document(User+nombre)
                            .set(Product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(miActividad,"ADDED TO FAVORITES",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Error writing document", e);
                                    Toast.makeText(miActividad,"Error",Toast.LENGTH_LONG).show();
                                }
                            });



                }
            });






        }catch (Exception  e)
        {
            String name="no se pudo";
            String precio="no se pudo";
            holder.name_rv.setText(name);
            holder.precio_rv.setText(precio);



        }
    }

    @Override

    public int getItemCount(){

        return this.productos.length();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView name_rv;
        private final TextView precio_rv;
        private final ImageView image_rv;
        private final Button btn_details;
        private final Button btn_favorites;


        public ViewHolder(View v) {

            super(v);
            name_rv = (TextView) v.findViewById(R.id.name_rv);
            precio_rv = (TextView) v.findViewById(R.id.precio_rv);
            image_rv=(ImageView) v.findViewById(R.id.image_rv);
            btn_details=(Button) v.findViewById(R.id.Btn_details);
            btn_favorites=(Button) v.findViewById(R.id.Btn_favorites);


        }
    }





}
