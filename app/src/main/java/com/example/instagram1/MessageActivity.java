package com.example.instagram1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram1.data.MessageAdapter;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.UserAdapter;
import com.example.instagram1.model.Chat;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image_toolbar;
    TextView  username_toolbar;
    ImageButton btn_send;
    EditText text_send;
    String t ;
    FirebaseUser fuser;
    DatabaseReference ref;
    SharedPrefsHelper sharedPrefs ;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;
    String imageurl;



    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        String userName = getIntent().getStringExtra("user");

        sharedPrefs = new SharedPrefsHelper(this);

        Toolbar toolbar_m = findViewById(R.id.toolbar_m);
        setSupportActionBar(toolbar_m);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_m.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        recyclerView = findViewById(R.id.send_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image_toolbar = findViewById(R.id.image_profile_message);
        username_toolbar = findViewById(R.id.username_toolbar);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                UserGetItem user = snapshot.getValue(UserGetItem.class);
               // username_toolbar.setText(user.getInfo().getUsername());
                t =(user.getInfo().getUsername()).toString();

//                profile_image_toolbar.setImageResource(R.drawable.icon);

                readMessage(sharedPrefs.getUsername(),t,imageurl);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(sharedPrefs.getUsername(), t ,msg);
                }else{
                    Toast.makeText(MessageActivity.this,"You cannot enter message!",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });

    }

    private void sendMessage (String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid, final String userid, final String imageurl){
        mChat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}