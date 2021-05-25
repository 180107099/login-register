package com.example.instagram1.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.example.instagram1.model.addPost1;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public  class profileFotoMySaveAdapter extends RecyclerView.Adapter<profileFotoMySaveAdapter.ViewHolder> {

    private Context mContext;
    private List<addPost1> post;
    private StorageReference str;
    SharedPrefsHelper sharedPrefs;


    public profileFotoMySaveAdapter(Context context, List<addPost1> post, StorageReference str) {
        this.post = post;
        this.mContext = context;
        this.str = str;


    }


    @NonNull
    @Override
    public profileFotoMySaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_foto_save_my, parent, false);

        return new profileFotoMySaveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        addPost1 posts = post.get(position);

        str.child(posts.getPost()).getDownloadUrl().addOnSuccessListener(uri -> {
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