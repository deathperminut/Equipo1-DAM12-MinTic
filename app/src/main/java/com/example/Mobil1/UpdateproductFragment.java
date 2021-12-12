package com.example.Mobil1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Mobil1.productAdapter;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UpdateproductFragment extends Fragment {
    private RecyclerView lista_h;
    private RecyclerView.Adapter mAdapter;
    private EditText EditText_NameProduct;
    private EditText EditText_AmountProduct;
    private ImageView ImageView_ImageProduct;
    private EditText EditText_InfoProduct;
    private EditText EditText_PriceProduct;
    private Button Btn_AddProduct;
    FirebaseStorage storage;
    SharedPreferences mipreferencia;
    private Button Btn_AddPicture;
    final int OPEN_GALLERY=1;
    Uri data1;
    FirebaseFirestore db;
    String urlImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_updateproduct, container, false);

        EditText_NameProduct=root.findViewById(R.id.EditText_NameProduct);
        storage = FirebaseStorage.getInstance();
        Btn_AddProduct=root.findViewById(R.id.Btn_AddProduct);
        db = FirebaseFirestore.getInstance();
        EditText_AmountProduct=root.findViewById(R.id.EditText_AmountProduct);
        EditText_InfoProduct=root.findViewById(R.id.EditText_InfoProduct);
        EditText_PriceProduct=root.findViewById(R.id.EditText_PriceProduct);
        Btn_AddPicture=root.findViewById(R.id.Btn_AddPicture);
        mipreferencia=getActivity().getSharedPreferences("MI_PREFERENCIA",Context.MODE_PRIVATE);
        String usuario=mipreferencia.getString("usuario","");
        lista_h=root.findViewById(R.id.lista_H);
        lista_h.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        lista_h.setLayoutManager(new LinearLayoutManager(getActivity()));
        //String productos="[{\"nombre\":\"XBox\",\"precio\":\"2500000\",\"imagen\":\"https://falabella.scene7.com/is/image/FalabellaCO/9461744_1?wid=800&hei=800&qlt=70\"},{\"nombre\":\"Balon Futbol\",\"precio\":\"150000\",\"imagen\":\"https://assets.adidas.com/images/w_600,f_auto,q_auto/59f5472a183d4721a5c1a81200c2ffad_9366/Balon_Oficial_Copa_Mundial_de_la_FIFA_Blanco_CE8083_01_standard.jpg\"},{\"nombre\":\"teclado Mecanico\",\"precio\":\"350000\",\"imagen\":\"https://m.media-amazon.com/images/I/71uMfiDAEbL._AC_SY450_.jpg\"},{\"nombre\":\"luces led\",\"precio\":\"50000\",\"imagen\":\"https://m.media-amazon.com/images/I/71XTreYu6DL._AC_SX466_.jpg\"}]";
        db.collection("Products")
                .whereEqualTo("User", usuario)
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
                                mAdapter=new productAdapter(ListaProductos,getActivity());
                                lista_h.setAdapter(mAdapter);

                            }
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        Btn_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NameProduct=EditText_NameProduct.getText().toString();
                String AmountProduct=EditText_AmountProduct.getText().toString();
                String PriceProduct=EditText_PriceProduct.getText().toString();
                String InfoProduct=EditText_InfoProduct.getText().toString();
                if(NameProduct.equals("")||AmountProduct.equals("")||PriceProduct.equals("")||InfoProduct.equals("")){
                    Toast.makeText(getActivity(),"COMPLETAR LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
                else{

                    subirImagen(NameProduct,InfoProduct,AmountProduct,PriceProduct);
                    /*Map<String, Object> Product = new HashMap<>();
                    Product.put("Name", NameProduct);
                    Product.put("Amount", Integer.parseInt(AmountProduct));
                    Product.put("Price", Float.parseFloat(PriceProduct));
                    Product.put("Info", InfoProduct);
                    Product.put("User",usuario);

                    db.collection("Products")
                            .add(Product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(getActivity(),"Correct Register",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                    Toast.makeText(getActivity(),"Incorrect Register",Toast.LENGTH_SHORT).show();
                                }
                            });*/

                }


                }



        });
        Btn_AddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),OPEN_GALLERY);
            }
        });


        return root;




    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==OPEN_GALLERY){

            if(resultCode==Activity.RESULT_OK){
                data1=data.getData();
                try{

                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data1);
                    ImageView_ImageProduct.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }
    public void subirImagen(String NameProduct,String InfoProduct,String AmountProduct,String PriceProduct){
        mipreferencia=getActivity().getSharedPreferences("MI_PREFERENCIA",Context.MODE_PRIVATE);
        String usuario=mipreferencia.getString("usuario","");

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        if(data1!=null){

            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Subiendo");
            progressDialog.show();

            Calendar c=Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String strDate=sdf.format(c.getTime());
            String nombreImagen=strDate + ".jpg";
            StorageReference riversRef=storageReference.child(usuario+"/"+nombreImagen);
            riversRef.putFile(data1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"File Uploaded",Toast.LENGTH_LONG).show();
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlImage=uri.toString();
                                    Log.e("URL_IMAGE",urlImage);
                                    Map<String, Object> Product = new HashMap<>();
                                    Product.put("Name", NameProduct);
                                    Product.put("Amount", Integer.parseInt(AmountProduct));
                                    Product.put("Price", Float.parseFloat(PriceProduct));
                                    Product.put("Info", InfoProduct);
                                    Product.put("User",usuario);
                                    Product.put("Url",urlImage);

                                    db.collection("Products")
                                            .add(Product)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(getActivity(),"Correct Register",Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_AddProduct);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error adding document", e);
                                                    Toast.makeText(getActivity(),"Incorrect Register",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Handle any arrors
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded"+((int)progress)+"%...");



                }
            });


        }
        else{
           //
        }




    }




}





class productAdapter  extends RecyclerView.Adapter<com.example.Mobil1.productAdapter.ViewHolder> implements result {

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

        public ViewHolder(View v) {

            super(v);
            name_rv = (TextView) v.findViewById(R.id.name_rv);
            precio_rv = (TextView) v.findViewById(R.id.precio_rv);
            image_rv=(ImageView) v.findViewById(R.id.image_rv);
            button_hist_info=(Button) v.findViewById(R.id.boton_detalles_carrito);


        }
    }







}