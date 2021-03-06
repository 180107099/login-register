package com.example.instagram1.data;




import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram1.MessageActivity;
import com.example.instagram1.R;
import com.example.instagram1.constants.Constants;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UserGetItem> mUser;
    SharedPrefsHelper sharedPrefs ;
    private StorageReference str;


    public UserAdapter(Context context, List<UserGetItem> mUser, StorageReference str) {
        this.mUser = mUser;
        this.mContext = context;
        this.str = str;
    }



    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        UserGetItem user = mUser.get(position);
        sharedPrefs = new SharedPrefsHelper(mContext);

        holder.username.setText(user.getInfo().getUsername());

        StorageReference str = FirebaseStorage.getInstance().getReference().child(Constants.ProfileImage).child(user.getInfo().getUsername()).child("1");
        str.getDownloadUrl().addOnSuccessListener(downloadUrl -> Picasso.get().load(downloadUrl).into(holder.profile_image));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("user", user.getInfo().getUsername());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;


        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username_message);
            profile_image = itemView.findViewById(R.id.image_profile_message);

        }
    }


}