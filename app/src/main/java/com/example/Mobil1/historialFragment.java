package com.example.Mobil1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Mobil1.HistorialAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class historialFragment extends Fragment {


    private RecyclerView rv_historial;
    private RecyclerView.Adapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root=inflater.inflate(R.layout.fragment_historial, container, false);


        rv_historial=root.findViewById(R.id.rv_historial);
        rv_historial.setLayoutManager(new LinearLayoutManager(getActivity()));

        String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        try{
            JSONArray JSONproductos= new JSONArray(productos);

            mAdapter=new HistorialAdapter(JSONproductos,getActivity());
            rv_historial.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }
}
class HistorialAdapter  extends RecyclerView.Adapter<com.example.Mobil1.HistorialAdapter.ViewHolder>{

    private  JSONArray productos;

    private Activity miActividad;

    public HistorialAdapter(JSONArray productos,Activity miActividad){

        this.productos=productos;

        this.miActividad=miActividad;



    }

    @Override
    public com.example.Mobil1.HistorialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_historial,parent,false);
        com.example.Mobil1.HistorialAdapter.ViewHolder viewHolder=new com.example.Mobil1.HistorialAdapter.ViewHolder(v);

        return viewHolder;


    }


    @Override
    public void onBindViewHolder(@NonNull com.example.Mobil1.HistorialAdapter.ViewHolder holder, int position) {

        try{


            String nombre=productos.getJSONObject(position).getString("nombre");
            String imagen=productos.getJSONObject(position).getString("imagen");
            String precio=productos.getJSONObject(position).getString("precio");
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
                    editor.commit();
                    Intent intent=new Intent(miActividad,producto.class);
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
            name_rv = (TextView) v.findViewById(R.id.Txt_hist_name);
            image_rv=(ImageView) v.findViewById(R.id.Image_hist);
            button_hist_info=(Button) v.findViewById(R.id.button_hist_info);


        }
    }





}