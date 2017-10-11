package com.turkisiwhatsapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MesajActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseResim;
    TextView mesaj ;
    ImageView imageSend ;
    ListView listViewim ;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String>chatList;
    private StorageReference mStorageRef;
    String arkadasMail,benimMail,taraf1,taraf2;
    private customListAdapter adap2;
    private List<customChatAdapter> mListAdap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);
        Bundle bundle = getIntent().getExtras();
        arkadasMail = bundle.getString("mailss");
        mAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        benimMail = mAuth.getCurrentUser().getEmail();
        taraf1=benimMail.substring(0,benimMail.indexOf("@"));
        taraf2=arkadasMail.substring(0,benimMail.indexOf("@"));
        imageSend = (ImageView) findViewById(R.id.imageSend);
        mesaj = (TextView)findViewById(R.id.edtMessage);
        final ImageView imageView = (ImageView) findViewById(R.id.imgFriend);
        final TextView tvFriends = (TextView)findViewById(R.id.friendMailChat);
        tvFriends.setText(arkadasMail);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getReference("mesaj");
        StorageReference resim = mStorageRef.child("images").child(arkadasMail);
             try {
            final File localFile = File.createTempFile("images","jpg");
                 resim.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                         imageView.setImageURI(Uri.parse(localFile.toString()));
                     }
                 });
        } catch (IOException e) {
            e.printStackTrace();}

        chatList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,chatList);

        //Mesajları lİSTELEME
        mListAdap2 = new ArrayList<>();


        final String taraflar2;

        if (taraf1.compareTo(taraf2)>0){
            taraflar2 = taraf1+"x"+taraf2;
        }
        else
            taraflar2 = taraf2+"x"+taraf1;

        listViewim = (ListView)findViewById(R.id.listMesaj) ;
        listViewim.setAdapter(arrayAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();
                mListAdap2.clear();
                DataSnapshot dataSnapshot1 = dataSnapshot.child(taraflar2);
                for (DataSnapshot dt:dataSnapshot1.getChildren()){
                    String value = dt.getValue(String.class);
                    System.out.println("Mesajlar : "+value);
                    String kisi = value.substring(0,value.indexOf(":")).trim();
                    if (kisi.compareTo(benimMail)==0){
                        System.out.println("Ben "+value);
                        mListAdap2.add(new customChatAdapter(value,"ben"));;
                    }
                    else
                        mListAdap2.add(new customChatAdapter(value,"sen"));
                        System.out.println("Sen "+value);
                }
                adap2 = new customListAdapter(getApplicationContext(),mListAdap2);
                listViewim.setAdapter(adap2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gelenMesaj = mesaj.getText().toString();
                String taraflar ;
                if (taraf1.compareTo(taraf2)>0){
                    taraflar = taraf1+"x"+taraf2;
                }
                else
                    taraflar = taraf2+"x"+taraf1;

                databaseReference.child(taraflar).push().setValue(benimMail + " : "+ gelenMesaj);
                mesaj.setText("");
            }
        });

}}
