package com.example.Mobil1.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Mobil1.R;
import com.example.Mobil1.ui.home.ProductosAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerV;
    private RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerV=root.findViewById(R.id.recyclerV);
        recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        try{
            JSONArray JSONproductos= new JSONArray(productos);

            mAdapter=new ProductosAdapter(JSONproductos,getActivity());
            recyclerV.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


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