package com.example.Mobil1.ui.gallery;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Mobil1.R;

import com.example.Mobil1.producto;
import com.example.Mobil1.ui.gallery.BuyAdapter;
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

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView recyclerV;
    private RecyclerView.Adapter mAdapter;
    private TextView Txt_CountProducts;
    private TextView Txt_CountPrice;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences mipreferencia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);


        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        Txt_CountPrice=root.findViewById(R.id.Txt_CountPrice);
        Txt_CountProducts=root.findViewById(R.id.Txt_CountProducts);
        mipreferencia=getActivity().getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
        recyclerV=root.findViewById(R.id.recyclerVbuy);
        recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        String usuario=mipreferencia.getString("usuario","");
        //String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        db.collection("CARRITO").document(usuario).collection("CARRITO_PRODUCTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int Count_products=0;
                            float Count_price=0;

                            JSONArray ListaProductos=new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TAG", document.getId() + " => " + document.getData());
                                String user=document.getData().get("User").toString();
                                int Amount=Integer.parseInt(document.getData().get("Amount").toString());
                                float Price=Float.parseFloat(document.getData().get("Price").toString());
                                Count_price=Count_price+Price;
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

                            mAdapter=new BuyAdapter(ListaProductos,getActivity());
                            recyclerV.setAdapter(mAdapter);
                            Txt_CountPrice.setText(Count_price+"");
                            Txt_CountProducts.setText(Count_products+"");



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

class BuyAdapter  extends RecyclerView.Adapter<com.example.Mobil1.ui.gallery.BuyAdapter.ViewHolder>{

    private  JSONArray productos;

    private Activity miActividad;

    public BuyAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.ui.gallery.BuyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_buy,parent,false);
        com.example.Mobil1.ui.gallery.BuyAdapter.ViewHolder viewHolder=new com.example.Mobil1.ui.gallery.BuyAdapter.ViewHolder(v);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.ui.gallery.BuyAdapter.ViewHolder holder, int position) {

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
                    db.collection("CARRITO").document(usuario).collection("CARRITO_PRODUCTS").document(User+nombre)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(miActividad,"Delete from shopping cart",Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(miActividad,R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_gallery);
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
            Btn_Delete=v.findViewById(R.id.Btn_Delete);


        }
    }





}







