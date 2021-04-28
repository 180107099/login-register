package com.example.instagram1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram1.R;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.UserAdapter;
import com.example.instagram1.model.Chat;
import com.example.instagram1.model.UserGetItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<UserGetItem> mUser;

    DatabaseReference reference;

    private List<String> userslist;

    SharedPrefsHelper sharedPrefs ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userslist = new ArrayList<>();
        sharedPrefs = new SharedPrefsHelper(getContext());


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                for (DataSnapshot snapshot1 :snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if(chat.getSender().equals(sharedPrefs.getUsername())){
                        userslist.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(sharedPrefs.getUsername())){
                        userslist.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void readChats(){
        mUser = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    UserGetItem user = snapshot1.getValue(UserGetItem.class);

                    if (userslist.contains(user.getInfo().getUsername())){
                        mUser.add(user);
                    }



                }
                Log.d("++++++",mUser.size()+"");
                userAdapter = new UserAdapter(getContext(),mUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}