package com.example.Mobil1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button Btn_login_login;
    EditText EditText_User_login;
    EditText EditText_Password_login;
    private SharedPreferences mipreferencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mipreferencia=getSharedPreferences("MI_PREFERENCIA",MODE_PRIVATE);
        String usuario=mipreferencia.getString("usuario","");
        if(usuario!=""){

            Intent intent=new Intent(this, drawermenu.class);
            startActivity(intent);
            finish();
        }

        Btn_login_login=findViewById(R.id.Btn_login_login);
        EditText_User_login=findViewById(R.id.EditText_User_login);
        EditText_Password_login=findViewById(R.id.EditText_Password_login);
        Btn_login_login.setOnClickListener(this);
        Btn_login_login.setEnabled(false);
        EditText_User_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(EditText_User_login.getText().toString().length() > 0 && EditText_Password_login.getText().toString().length() > 0) {
                    ;
                    Btn_login_login.setEnabled(true);
                }
                else{
                    Btn_login_login.setEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(EditText_User_login.getText().toString().length() > 0 && EditText_Password_login.getText().toString().length() > 0) {
                    ;
                    Btn_login_login.setEnabled(true);
                }
                else{
                    Btn_login_login.setEnabled(false);
                }
            }

        });
        EditText_Password_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(EditText_User_login.getText().toString().length() > 0 && EditText_Password_login.getText().toString().length() > 0) {
                    ;
                    Btn_login_login.setEnabled(true);
                }
                else{
                    Btn_login_login.setEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(EditText_User_login.getText().toString().length() > 0 && EditText_Password_login.getText().toString().length() > 0) {

                    Btn_login_login.setEnabled(true);
                }
                else{
                    Btn_login_login.setEnabled(false);
                }
            }

        });



    }
    @Override
    public void onClick(View v) {

        if (EditText_User_login.getText().toString().equals("deathperminut") && EditText_Password_login.getText().toString().equals("carrito1")) {
            mipreferencia=getSharedPreferences("MI_PREFERENCIA",MODE_PRIVATE);

            SharedPreferences.Editor editor=mipreferencia.edit();
            editor.putString("usuario",EditText_User_login.getText().toString());
            editor.commit();
            Intent intent=new Intent(this, drawermenu.class);
            startActivity(intent);
            finish();

        }
    }


    }

