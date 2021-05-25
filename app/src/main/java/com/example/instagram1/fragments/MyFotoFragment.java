package com.example.instagram1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.profileFotoMySaveAdapter;
import com.example.instagram1.model.addPost1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyFotoFragment extends Fragment {
    private RecyclerView recyclerView;
    SharedPrefsHelper sharedPrefs;
    private profileFotoMySaveAdapter kj;
    private List<addPost1> posts;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_foto_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        posts = new ArrayList<>();
        sharedPrefs = new SharedPrefsHelper(getContext());


        imageView = getView().findViewById(R.id.image_pr_sv);

        recyclerView = view.findViewById(R.id.recycler_view_myFoto);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("post");
        StorageReference str = FirebaseStorage.getInstance().getReference().child("post");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    addPost1 post = snapshot1.getValue(addPost1.class);
                    if (sharedPrefs.getUsername().equals(post.getUsername())) {
                        posts.add(post);
                    }
                }
                Collections.reverse(posts);
                kj = new profileFotoMySaveAdapter(getContext(), posts, str);
                recyclerView.setAdapter(kj);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
