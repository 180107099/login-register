package com.example.instagram1.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public  class SaveFotos extends RecyclerView.Adapter<SaveFotos.ViewHolder> {

    private Context mContext;
    private List<String> post;
    private StorageReference str;
    SharedPrefsHelper sharedPrefs;


    public SaveFotos(Context context, List<String> post) {
        this.post = post;
        this.mContext = context;

    }


    @NonNull
    @Override
    public SaveFotos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_foto_save_my, parent, false);

        return new SaveFotos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String posts = post.get(position);

        Log.d("^^^^^^^^^^" , posts);
        StorageReference str = FirebaseStorage.getInstance().getReference().child("post");


        str.child(posts).getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(holder.profile_post);
        });

    }



    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_post;
        public ViewHolder(View itemView) {
            super(itemView);
            profile_post = itemView.findViewById(R.id.image_pr_sv);
        }
    }
}