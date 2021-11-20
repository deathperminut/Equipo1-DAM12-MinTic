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

public class Register extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button Btn_register_register=findViewById(R.id.Btn_register_register);
        Btn_register_register.setEnabled(false);
        EditText EditText_Name_Register=findViewById(R.id.EditText_Name_Register);
        EditText EditText_User_Register=findViewById(R.id.EditText_User_Register);
        EditText EditText_Password_Register=findViewById(R.id.EditText_Password_Register);
        CheckBox CheckBox_Register=findViewById(R.id.Checkbox_Register);
        TextView EditText_Terms=findViewById(R.id.EditText_Terms);
        EditText_Terms.setOnClickListener(this);
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
                Intent intent2=new Intent(this,LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}