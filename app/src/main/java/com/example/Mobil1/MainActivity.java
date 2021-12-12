package com.example.Mobil1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private SharedPreferences mipreferencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mipreferencia=getSharedPreferences("MI_PREFERENCIA",MODE_PRIVATE);
        String usuario=mipreferencia.getString("usuario","");
        if(usuario!=""){

            Intent intent=new Intent(this, drawermenu.class);
            startActivity(intent);
            finish();
        }
        Button Btn_login=findViewById(R.id.Btn_login);
        Button Btn_register=findViewById(R.id.Btn_register);
        Button Btn_Exit=findViewById(R.id.Btn_Exit);
        Btn_login.setOnClickListener(this);
        Btn_register.setOnClickListener(this);
        Btn_Exit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){


            case R.id.Btn_login:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.Btn_register:
                Intent intent2=new Intent(this,Register.class);
                startActivity(intent2);
                break;
            case R.id.Btn_Exit:
                finish();


        }
    }
}

