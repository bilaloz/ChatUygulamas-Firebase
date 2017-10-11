package com.turkisiwhatsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GirisActivitiy extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    String arkadasMailAdresi ;
    private List<adapter> listAdapter ;
    private ListAdapter adapters;
    private ListView listViewFriends ;
    DatabaseReference userRef,myRef ,mailRef,resimRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static int GALERI_INDEX = 2;
    private StorageReference mStorageRef;
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



    public GirisActivitiy() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.addFriend) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.arkadas_ekleme_dialog,null);
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button btnArkadasIstek = (Button)view.findViewById(R.id.arkadasYolla);
            final EditText arkadasMail = (EditText)view.findViewById(R.id.arkadasMailAdres);

            btnArkadasIstek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!arkadasMail.getText().toString().isEmpty()){
                        arkadasMailAdresi = arkadasMail.getText().toString();
                        arkadasMail.setText(" ");
                        alertDialog.hide();
                        myRef.orderByChild("mail").equalTo(arkadasMailAdresi)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                System.out.println("Değer : "+dataSnapshot.getValue().toString());

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                    Map<String,String> map = (Map<String,String>)dataSnapshot1.getValue();
                                    DatabaseReference rf = myRef.child(mAuth.getCurrentUser().getUid()).child("friends");
                                    rf.push().setValue(map.get("mail"));
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Arkadas Bulunamadı",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }
        else if (item.getItemId()==R.id.Settings) {
            
        }
        else if (item.getItemId()==R.id.SignOut) {
            mAuth.signOut();

        }
        else if (item.getItemId()==R.id.addImage) {
            Intent dntent = new Intent(Intent.ACTION_PICK);
            dntent.setType("image/*");
            userRef = myRef.child(mAuth.getCurrentUser().getUid());
            mailRef = userRef.child("mail");
            mailRef.setValue(mAuth.getCurrentUser().getEmail());
            resimRef=userRef.child("resim");
            startActivityForResult(dntent,GALERI_INDEX);





        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_activitiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        listViewFriends = (ListView)findViewById(R.id.listViewFriends);
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("allUsers");
        listAdapter = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent userNull = new Intent(GirisActivitiy.this,MainActivity.class);
                    startActivity(userNull);
                }

            }
        };
        DatabaseReference arkadaslarim = FirebaseDatabase.getInstance()
                .getReference("allUsers").child(mAuth.getCurrentUser().getUid());

        arkadaslarim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dsArkadaslar =dataSnapshot.child("friends");
                    for (DataSnapshot friend : dsArkadaslar.getChildren()){
                        String valueq = friend.getValue(String.class);
                        System.out.println("Arkadaşlar : "+valueq);

                        myRef.orderByChild("mail").equalTo(valueq).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                   Map<String,String> friendDetay = (Map <String,String>)dataSnapshot1.getValue();
                                   System.out.println("User image : "+friendDetay.get("resim"));
                                   String userImage= friendDetay.get("resim");
                                   listAdapter.add(new adapter(friendDetay.get("mail"),userImage));

                               }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapters = new listAdapter(getApplicationContext(),listAdapter);
        listViewFriends.setAdapter(adapters);
        listViewFriends.invalidateViews();
        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tMails = (TextView)view.findViewById(R.id.customFriendsName);
                Intent intentMesaj = new Intent(GirisActivitiy.this,MesajActivity.class);
                intentMesaj.putExtra("mailss",tMails.getText().toString());
                startActivity(intentMesaj);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALERI_INDEX && resultCode==RESULT_OK){
            Uri uri = data.getData();
            final StorageReference bilo = mStorageRef.child("images/").child(mAuth.getInstance().getCurrentUser().getEmail());
            bilo.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    resimRef.setValue(taskSnapshot.getDownloadUrl().toString());
                    Toast.makeText(getApplicationContext(),"Resim Yüklendi",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Resim Yüklenemedi",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
