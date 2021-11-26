package com.example.Mobil1.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.Mobil1.LoginActivity;
import com.example.Mobil1.R;
import com.example.Mobil1.Register;
import com.example.Mobil1.databinding.FragmentSlideshowBinding;
import com.example.Mobil1.drawermenu;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ImageView imagen;
    private EditText EditText_profile;
    private TextView textV_profile;
    private SharedPreferences mipreferencia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Button bottom_Profile_hist = root.findViewById(R.id.bottom_Profile_hist);
        Button bottom_products = root.findViewById(R.id.bottom_Products);
        imagen = root.findViewById(R.id.imagen_perfil);
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
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        imagen.setImageDrawable(roundedDrawable);
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}