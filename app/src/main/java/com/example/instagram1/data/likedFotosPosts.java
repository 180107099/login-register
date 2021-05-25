package com.example.instagram1.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.R;
import com.example.instagram1.model.likedPosts;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class likedFotosPosts extends RecyclerView.Adapter<likedFotosPosts.ViewHolder> {

    private Context mContext;
    private List<likedPosts> post;
    SharedPrefsHelper sharedPrefs;


    public likedFotosPosts(Context context, List<likedPosts> post) {
        this.post = post;
        this.mContext = context;

    }


    @NonNull
    @Override
    public likedFotosPosts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_posts_item, parent, false);

        return new likedFotosPosts.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull likedFotosPosts.ViewHolder holder, int position) {
        sharedPrefs = new SharedPrefsHelper(mContext);
        likedPosts posts = post.get(position);

        holder.username1.setText(posts.getUsername2());
        holder.post1.setText(posts.getPost());

    }



    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username1;
        public TextView post1;
        public ViewHolder(View itemView) {
            super(itemView);
            username1 = itemView.findViewById(R.id.username1);
            post1 = itemView.findViewById(R.id.post);

        }
    }
}

