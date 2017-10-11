package com.turkisiwhatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitActivitiy extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText et_kulAdi,et_kulSifre;
    String kulAdi,kulSifre;
    Button kayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_activitiy);
        et_kulAdi = (EditText)findViewById(R.id.edt_KulAdi);
        et_kulSifre = (EditText)findViewById(R.id.edt_KulSifre);
        kulAdi=et_kulAdi.getText().toString();
        kulSifre=et_kulSifre.getText().toString();
        kayit=(Button)findViewById(R.id.btn_Kayit);
        mAuth =FirebaseAuth.getInstance();
        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(et_kulAdi.getText().toString(),et_kulSifre.getText().toString())
                        .addOnCompleteListener(KayitActivitiy.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mAuth.signInWithEmailAndPassword(et_kulAdi.getText().toString(),et_kulSifre.getText().toString())
                                        .addOnCompleteListener(KayitActivitiy.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Intent i = new Intent(getApplicationContext(),GirisActivitiy.class);
                                                startActivity(i);
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Hata Kullanıcı Oluşturulamadı",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

}
