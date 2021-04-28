package com.example.instagram1.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.LoginActivity;
import com.example.instagram1.Message;
import com.example.instagram1.R;
import com.example.instagram1.RegisterActivity;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.UserAdapter;
import com.example.instagram1.data.postAdapter;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.addPost1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.*;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    SharedPrefsHelper sharedPrefs ;


    private postAdapter postAdapter;
    private List<addPost1> posts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container,false);

        recyclerView = view.findViewById(R.id.recycler_view_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        posts = new ArrayList<>();

        readUsers();

        return view;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        sharedPrefs = new SharedPrefsHelper(getContext());


        ImageView message = (ImageView) getView().findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Message.class);
                startActivity(intent);
            }
        });

    }


    private void readUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("post");
        StorageReference str = FirebaseStorage.getInstance().getReference().child("post");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    addPost1 post = snapshot1.getValue(addPost1.class);



                    posts.add(post);
                }
                postAdapter = new postAdapter(getContext(), posts,str);
                recyclerView.setAdapter(postAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
