package com.example.Mobil1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class termsconditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsconditions);
        TextView TextView_terms=findViewById(R.id.TextView_terms);
        TextView_terms.setMovementMethod(ScrollingMovementMethod.getInstance());

    }
}