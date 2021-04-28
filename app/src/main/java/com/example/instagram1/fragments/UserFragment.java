package com.example.instagram1.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment  {
    private RecyclerView recyclerView;

    SharedPrefsHelper sharedPrefs ;

    private UserAdapter userAdapter;
    private List<UserGetItem> mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_user, container,false);

        recyclerView = view.findViewById(R.id.recycler_view_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();

        readUsers();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefs = new SharedPrefsHelper(getContext());
    }

    private void readUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUser.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    UserGetItem user = snapshot1.getValue(UserGetItem.class);

                    Log.e(">>>>>>>", sharedPrefs.getUsername() +" | "+user.getInfo().getName());
                    if(!sharedPrefs.getUsername().equals(user.getInfo().getUsername()))
                        mUser.add(user);
                }

                userAdapter = new UserAdapter (getContext(), mUser);
                recyclerView.setAdapter(userAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}