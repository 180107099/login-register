package com.example.instagram1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.likedFotosPosts;
import com.example.instagram1.model.likedPosts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HeartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.heart_fragment, container, false);
    }

    SharedPrefsHelper sharedPrefs;
    private List<likedPosts> posts;
    private likedFotosPosts kj;


    private RecyclerView recyclerView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        sharedPrefs = new SharedPrefsHelper(getContext());
        String usernameSh = sharedPrefs.getUsername();

        posts = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("LikedPosts");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    likedPosts posst = snapshot1.getValue(likedPosts.class);
                    posts.add(posst);
                }

                kj = new likedFotosPosts(getContext(), posts);
                recyclerView.setAdapter(kj);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


