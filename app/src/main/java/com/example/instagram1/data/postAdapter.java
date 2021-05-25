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
import com.example.instagram1.constants.Constants;
import com.example.instagram1.model.addPost1;
import com.example.instagram1.model.likedPosts;
import com.example.instagram1.model.postAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {

    private Context mContext;
    private List<addPost1> post;
    private StorageReference str;
    SharedPrefsHelper sharedPrefs;
    DatabaseReference reference;


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

        sharedPrefs = new SharedPrefsHelper(mContext);
        String usernameSh = sharedPrefs.getUsername();

        addPost1 post1 = post.get(position);
        holder.username.setText(post1.getUsername());
        holder.author.setText(post1.getUsername());
        holder.description.setText(post1.getPost());




        StorageReference str1 = FirebaseStorage.getInstance().getReference().child(Constants.ProfileImage).child(post1.getUsername()).child("1");
        str1.getDownloadUrl().addOnSuccessListener(downloadUrl -> Picasso.get().load(downloadUrl).into(holder.profile_image));


        str.child(post1.getPost()).getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(holder.post_image);
        });


        holder.saveImg.setOnClickListener(v -> {
            //Log.e("fja;dfa;",post.get(position).getPost());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("SaveFotos").child(post1.getPost());
            DatabaseReference refere = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("SaveFotos");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        refere.child(post1.getPost()).setValue(post1.getPost());

                    } else
                        reference.setValue(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        holder.favImg.setOnClickListener(v -> {
            //Log.e("fja;dfa;",post.get(position).getPost());
            DatabaseReference refere = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("LikedPosts");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(usernameSh).child("LikedPosts").child(post1.getPost()+" "+ post1.getUsername() + " "+usernameSh);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (!snapshot.exists()) {
                        likedPosts helperClass = new likedPosts(post1.getPost(),post1.getUsername(),usernameSh);
                        refere.child(post1.getPost()+" "+ post1.getUsername() + " "+usernameSh).setValue(helperClass);

                    } else
                        reference.setValue(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView username;
        public ImageView profile_image;
        public ImageView post_image;
        public TextView author;
        public TextView description;
        public ImageView saveImg;
        public ImageView favImg;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username2);
            profile_image = itemView.findViewById(R.id.profile_image);
            post_image = itemView.findViewById(R.id.post_image);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            saveImg = itemView.findViewById(R.id.save);
            favImg = itemView.findViewById(R.id.like);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}