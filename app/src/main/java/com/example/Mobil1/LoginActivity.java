package com.example.Mobil1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Mobil1.database.model.User;
import com.example.Mobil1.database.model.UserDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDatabase database;
    private User userLogin;
    Button Btn_login_login;
    EditText EditText_User_login;
    EditText EditText_Password_login;
    FirebaseFirestore db;
    private SharedPreferences mipreferencia;
    private List<User> userRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
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
        //User usuario_1=new User("juan","juan@juan","carrito1");
        database=UserDatabase.getInstance(this);
        //long validar=database.getUserDao().insertUser(usuario_1);
        new getUsersTask(this).execute();
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

        }else{

            String usuario=EditText_User_login.getText().toString();
            String contra=EditText_Password_login.getText().toString();

            DocumentReference docRef = db.collection("users").document(usuario);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            String contraseña=document.getData().get("contraseña").toString();
                            if(md5(contra).equals(contraseña)){
                                Toast.makeText(LoginActivity.this,"Correct Login",Toast.LENGTH_SHORT).show();
                                Login(usuario);
                            }else{
                                Toast.makeText(LoginActivity.this,"Incorrect User or Password",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d("TAG", "No such document");


                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                        Toast.makeText(LoginActivity.this,"Error al registrar",Toast.LENGTH_SHORT).show();
                    }
                }
            });



            //new loginTask(this,usuario,md5(contra)).execute();








        }







    }
    public void Login(String usuario){
        SharedPreferences.Editor editor=mipreferencia.edit();
        editor.putString("usuario",usuario);
        editor.commit();
        Intent intent=new Intent(this, drawermenu.class);
        startActivity(intent);
        finish();



    }
    public void ValidarLogin(User userLogin){

        if(userLogin!=null){
            SharedPreferences.Editor editor=mipreferencia.edit();
            editor.putString("usuario",userLogin.getEmail());
            editor.commit();
            Intent intent=new Intent(this, drawermenu.class);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this,"USUARIO Y CONTRASEÑA SIN COINCIDENCIAS",Toast.LENGTH_SHORT).show();




        }



    }
    private static class getUsersTask extends AsyncTask<Void,Void, List<User>>implements com.example.Mobil1.getUsersTask{


        //CREAMOS UN WEAKREFERENCES QUE ES UNA VARIABLE QUE ME PERMITE USAR TODOS LOS ATRIBUTOS QUE YA ESTEN EN UN ACTIVITY

        private WeakReference<LoginActivity> loginActivityWeakReference;
        //CREO LOS METODOS

        getUsersTask(LoginActivity context){

            this.loginActivityWeakReference=new WeakReference<>(context);

        }

        @Override
        protected List<User> doInBackground(Void... voids){

            if(loginActivityWeakReference.get() !=null){


                List<User> users=loginActivityWeakReference.get().database.getUserDao().getUser(); //PRIMERO DEBO CREAR EL DATABASE

                return users;
            }
            return null;
        }


        @Override
        public void OnPostExecute(List<User> users) {
            if(users!=null && users.size()>0){

                loginActivityWeakReference.get().userRegister=users;


            }
            super.onPostExecute(users);

        }
    }



    private static class loginTask extends AsyncTask<Void,Void, User> implements com.example.Mobil1.loginTask {


        //CREAMOS UN WEAKREFERENCES QUE ES UNA VARIABLE QUE ME PERMITE USAR TODOS LOS ATRIBUTOS QUE YA ESTEN EN UN ACTIVITY

        private WeakReference<LoginActivity> loginActivityWeakReference;
        private String usuario;
        private String contra;
        //CREO LOS METODOS

        loginTask(LoginActivity context, String usuario,String contra){

            this.loginActivityWeakReference=new WeakReference<>(context);
            this.usuario=usuario;
            this.contra=contra;

        }

        @Override
        protected User doInBackground(Void... voids){

            if(loginActivityWeakReference.get() !=null){


                User user=loginActivityWeakReference.get().database.getUserDao().getuserLogin(this.usuario,this.contra); //PRIMERO DEBO CREAR EL DATABASE
                OnPostExecute(user);
                return user;
            }
            return null;
        }


        @Override
        public void OnPostExecute(User user) {
            if(user!=null){

                loginActivityWeakReference.get().ValidarLogin(user);


            }
            super.onPostExecute(user);

        }
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













