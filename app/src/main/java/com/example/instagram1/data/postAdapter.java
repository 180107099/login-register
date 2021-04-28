package com.example.instagram1.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.MessageActivity;
import com.example.instagram1.R;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.addPost1;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class postAdapter  extends RecyclerView.Adapter<postAdapter.ViewHolder> {

    private Context mContext;
    private List<addPost1> post;
    private StorageReference str;


    public postAdapter(Context context, List<addPost1> post, StorageReference str) {
        this.post = post;
        this.mContext = context;
        this.str = str;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        return new postAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postAdapter.ViewHolder holder, int position) {

        addPost1 post1 = post.get(position);
        holder.username.setText(post1.getUsername());
        holder.author.setText(post1.getUsername());
        holder.description.setText(post1.getPost());
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        str.child(post1.getPost()).getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(holder.post_image);
        });



    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public  ImageView post_image;
        public TextView author;
        public TextView description;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username2);
            profile_image = itemView.findViewById(R.id.profile_image);
            post_image = itemView.findViewById(R.id.post_image);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);



        }
    }


}