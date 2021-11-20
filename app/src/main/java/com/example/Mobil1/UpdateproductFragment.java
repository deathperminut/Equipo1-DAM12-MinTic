package com.example.Mobil1;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Mobil1.productAdapter;

import org.json.JSONArray;
import org.json.JSONException;


public class UpdateproductFragment extends Fragment {
    private RecyclerView lista_h;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_updateproduct, container, false);

        lista_h=root.findViewById(R.id.lista_H);
        lista_h.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        lista_h.setLayoutManager(new LinearLayoutManager(getActivity()));
        String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        try{
            JSONArray JSONproductos= new JSONArray(productos);

            mAdapter=new productAdapter(JSONproductos,getActivity());
            lista_h.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        return root;
    }

}
class productAdapter  extends RecyclerView.Adapter<com.example.Mobil1.productAdapter.ViewHolder>{

    private  JSONArray productos;

    private Activity miActividad;

    public productAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.productAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_buy,parent,false);
        com.example.Mobil1.productAdapter.ViewHolder viewHolder=new com.example.Mobil1.productAdapter.ViewHolder(v);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.productAdapter.ViewHolder holder, int position) {

        try{


            String nombre=productos.getJSONObject(position).getString("nombre");
            String precio=productos.getJSONObject(position).getString("precio");
            String imagen=productos.getJSONObject(position).getString("imagen");
            holder.name_rv.setText(nombre);
            holder.precio_rv.setText(precio);
            Glide.with(miActividad).load(imagen).into(holder.image_rv);






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

        public ViewHolder(View v) {

            super(v);
            name_rv = (TextView) v.findViewById(R.id.name_rv);
            precio_rv = (TextView) v.findViewById(R.id.precio_rv);
            image_rv=(ImageView) v.findViewById(R.id.image_rv);


        }
    }





}