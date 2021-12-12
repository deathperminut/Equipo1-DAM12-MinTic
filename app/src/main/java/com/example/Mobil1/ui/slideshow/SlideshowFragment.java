package com.example.Mobil1.ui.slideshow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.Mobil1.LoginActivity;
import com.example.Mobil1.R;
import com.example.Mobil1.Register;
import com.example.Mobil1.databinding.FragmentSlideshowBinding;
import com.example.Mobil1.drawermenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ImageView imagen;
    private EditText EditText_profile;
    private TextView textV_profile;
    private SharedPreferences mipreferencia;
    final int OPEN_GALLERY=1;
    private TextView Txt_ForSale;
    FirebaseFirestore db;
    Uri data1;
    String urlImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Button bottom_Profile_hist = root.findViewById(R.id.bottom_Profile_hist);
        Button bottom_products = root.findViewById(R.id.bottom_Products);
        imagen = root.findViewById(R.id.imagen_perfil);
        Txt_ForSale=root.findViewById(R.id.Txt_Forsale);
        textV_profile = root.findViewById(R.id.TextV_profile);
        mipreferencia = getActivity().getSharedPreferences("MI_PREFERENCIA", Context.MODE_PRIVATE);
        String usuario = mipreferencia.getString("usuario", "");

        if (usuario != "") {

            textV_profile.setText(usuario);
        }


        //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.drawable.anochecer);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        db = FirebaseFirestore.getInstance();
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        DocumentReference docRef = db.collection("FOTOSPERFIL").document(usuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        String urlimagen= document.getData().get("FotoPerfil").toString();
                        Glide.with(getActivity()).load(urlimagen).into(imagen);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());

                }
            }
        });



        bottom_Profile_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_historial);
            }
        });
        bottom_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_AddProduct);
            }
        });
        db.collection("Products")
                .whereEqualTo("User", usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int Count_Products=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TAG", document.getId() + " => " + document.getData());
                                Count_Products=Count_Products+1;



                            }
                            Txt_ForSale.setText(Count_Products+"");


                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }

                });
        imagen.setOnClickListener(new View.OnClickListener() {
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
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY) {

            if (resultCode == Activity.RESULT_OK) {
                data1 = data.getData();
                try {

                    subirImagen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void subirImagen(){
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
                                    Map<String, Object> ProfilePicture = new HashMap<>();
                                    ProfilePicture.put("FotoPerfil", urlImage);

                                    db.collection("FOTOSPERFIL").document(usuario)
                                            .set(ProfilePicture)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e("TAG", "DocumentSnapshot successfully written!");
                                                    Toast.makeText(getActivity(),"ProfilePicture",Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_drawermenu).navigate(R.id.nav_slideshow);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("TAG", "Error writing document", e);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}