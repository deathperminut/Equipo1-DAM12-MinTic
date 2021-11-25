package com.example.Mobil1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Mobil1.database.model.User;
import com.example.Mobil1.database.model.UserDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.Mobil1.database.model.User;

public class Register extends AppCompatActivity implements View.OnClickListener {


    private UserDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database=UserDatabase.getInstance(this);
        Button Btn_register_register=findViewById(R.id.Btn_register_register);
        Btn_register_register.setEnabled(false);
        EditText EditText_Name_Register=findViewById(R.id.EditText_Name_Register);
        EditText EditText_User_Register=findViewById(R.id.EditText_User_Register);
        EditText EditText_Password_Register=findViewById(R.id.EditText_Password_Register);
        CheckBox CheckBox_Register=findViewById(R.id.Checkbox_Register);
        TextView EditText_Terms=findViewById(R.id.EditText_Terms);
        EditText_Terms.setOnClickListener(this);
        Btn_register_register.setOnClickListener(this);
        EditText_Name_Register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{
                    Btn_register_register.setEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{
                    Btn_register_register.setEnabled(false);
                }

            }



        });
        EditText_Password_Register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{
                    Btn_register_register.setEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{
                    Btn_register_register.setEnabled(false);
                }

            }



        });
        EditText_User_Register.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{
                    Btn_register_register.setEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0
                        && CheckBox_Register.isChecked()) {
                    ;
                    Btn_register_register.setEnabled(true);
                }
                else{

                    Btn_register_register.setEnabled(false);
                }

            }



        });
        CheckBox_Register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && EditText_Name_Register.getText().toString().length() > 0 && EditText_Password_Register.getText().toString().length() > 0
                        && EditText_User_Register.getText().toString().length() > 0) {
                    Btn_register_register.setEnabled(true);

                }else{
                    Btn_register_register.setEnabled(false);
                }
                }

        });



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.EditText_Terms:
                Intent intent=new Intent(this,termsconditions.class);
                startActivity(intent);
                break;
            case R.id.Btn_register_register:
                EditText EditText_Password_Register=findViewById(R.id.EditText_Password_Register);
                String password=EditText_Password_Register.getText().toString();
                if(password.length()<8 && !isValidPassword(password)){
                    Toast.makeText(Register.this,"La contraseña debe ser minimo de 8 caracteres " +
                            "contener caracteres especiales, mayusculas y numeros",Toast.LENGTH_SHORT).show();

                }else{
                    EditText EditText_Name_Register=findViewById(R.id.EditText_Name_Register);
                    EditText EditText_User_Register=findViewById(R.id.EditText_User_Register);
                    String name=EditText_Name_Register.getText().toString();
                    String User=EditText_User_Register.getText().toString();
                    User user=new User(name,User,md5(password));
                    long verificador=database.getUserDao().insertUser(user);
                    if(verificador>0){
                    Intent intent2=new Intent(this,LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                    }


                }

        }
    }
    public static boolean isValidPassword(final String password){

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN="^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).(4,)$";
        pattern=Pattern.compile(PASSWORD_PATTERN);
        matcher= pattern.matcher(password);
        return matcher.matches();
    }
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

