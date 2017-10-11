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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button giris,kayit;
    EditText edt_KulAdi;


    EditText edt_KulSifre;
    String kulAdi,kulMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        mAuth = FirebaseAuth.getInstance();
        edt_KulAdi=(EditText)findViewById(R.id.et_KulAdi);
        edt_KulSifre=(EditText)findViewById(R.id.et_KulSifre);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kulAdi=edt_KulAdi.getText().toString();
                kulMail=edt_KulSifre.getText().toString();
                mAuth.signInWithEmailAndPassword(kulAdi,kulMail)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Intent i = new Intent(getApplicationContext(),GirisActivitiy.class);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Kullanici adi yada parola hatasi",Toast.LENGTH_SHORT);

                    }
                });
            }
        });
        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),KayitActivitiy.class);
                startActivity(i);
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent i = new Intent(getApplicationContext(),GirisActivitiy.class);
                    startActivity(i);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void tanimla() {
        giris=(Button)findViewById(R.id.btn_Giris);
        kayit=(Button)findViewById(R.id.btn_Kayit);




    }
}
