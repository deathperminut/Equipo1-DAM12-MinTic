package com.example.Mobil1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FavoritesFragment extends Fragment {

    TextView Txt_CountProducts_favorites;
    RecyclerView RecyclerView_Favorites;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences mipreferencia;
    private RecyclerView.Adapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_favorites, container, false);

        Txt_CountProducts_favorites=root.findViewById(R.id.Txt_CountProducts_favorites);
        RecyclerView_Favorites=root.findViewById(R.id.recyclerVFavorites);
        mipreferencia= getActivity().getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);

        String usuario=mipreferencia.getString("usuario","");
        //String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        db.collection("FAVORITES").document(usuario).collection("FAVORITES_PRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int Count_products=0;

                            JSONArray ListaProductos=new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TAG", document.getId() + " => " + document.getData());
                                String user=document.getData().get("User").toString();
                                int Amount=Integer.parseInt(document.getData().get("Amount").toString());
                                float Price=Float.parseFloat(document.getData().get("Price").toString());
                                Count_products=Count_products+1;
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

                            mAdapter=new FavoritesAdapter(ListaProductos,getActivity());
                            RecyclerView_Favorites.setLayoutManager(new LinearLayoutManager(getActivity()));
                            RecyclerView_Favorites.setAdapter(mAdapter);
                            Txt_CountProducts_favorites.setText(Count_products+"");



                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });




        return root;
    }
}
class FavoritesAdapter  extends RecyclerView.Adapter<com.example.Mobil1.FavoritesAdapter.ViewHolder>{

    private  JSONArray productos;

    private Activity miActividad;

    public FavoritesAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_buy,parent,false);
        com.example.Mobil1.FavoritesAdapter.ViewHolder viewHolder=new com.example.Mobil1.FavoritesAdapter.ViewHolder(v);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.FavoritesAdapter.ViewHolder holder, int position) {

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
            holder.Btn_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences mipreferencia= miActividad.getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
                    String usuario=mipreferencia.getString("usuario","");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("FAVORITES").document(usuario).collection("FAVORITES_PRODUCTS").document(User+nombre)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(miActividad,"Delete from favorites",Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(miActividad,R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_Favorites);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error deleting document", e);
                                    Toast.makeText(miActividad,"delete error",Toast.LENGTH_LONG).show();
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
        private final Button button_hist_info;
        private final Button Btn_Delete;

        public ViewHolder(View v) {

            super(v);
            name_rv = (TextView) v.findViewById(R.id.name_rv);
            precio_rv = (TextView) v.findViewById(R.id.precio_rv);
            image_rv=(ImageView) v.findViewById(R.id.image_rv);
            button_hist_info=(Button) v.findViewById(R.id.boton_detalles_carrito);
            Btn_Delete=(Button) v.findViewById(R.id.Btn_Delete);


        }
    }





}
