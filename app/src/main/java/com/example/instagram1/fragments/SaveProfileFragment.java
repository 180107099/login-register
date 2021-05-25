package com.example.instagram1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.example.instagram1.data.SaveFotos;
import com.example.instagram1.data.SharedPrefsHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SaveProfileFragment extends Fragment {

    SharedPrefsHelper sharedPrefs ;
    private List<String> posts;
    private SaveFotos kj;


    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.save_foto_fragment_pr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view_save);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        sharedPrefs = new SharedPrefsHelper(getContext());
        String usernameSh = sharedPrefs.getUsername();

        posts = new ArrayList<>();

        StorageReference str = FirebaseStorage.getInstance().getReference().child("post");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("SaveFotos");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot snapshot1  :snapshot.getChildren()){
                    String posst = snapshot1.getValue(String.class);
                    Log.d("^^^^^^^^^^" , posst);
                    posts.add(posst);
                }
                kj = new SaveFotos(getContext(), posts);
                recyclerView.setAdapter(kj);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

