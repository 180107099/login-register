package com.example.instagram1.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram1.MessageActivity;
import com.example.instagram1.R;
import com.example.instagram1.model.Chat;
import com.example.instagram1.model.UserGetItem;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private  String imageurl;
    SharedPrefsHelper sharedPrefs ;


    public MessageAdapter(Context context, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = context;
        this.imageurl = imageurl;
        sharedPrefs = new SharedPrefsHelper(context);
    }



    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        return new MessageAdapter.ViewHolder(view);}
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);



            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);


        }
    }



    @Override
    public int getItemViewType(int position) {
        Log.d("******", "getItemViewType: " + sharedPrefs.getUsername() );
        if(mChat.get(position).getSender().equals(sharedPrefs.getUsername())){
            return MSG_TYPE_RIGHT;
        }else
        {return MSG_TYPE_LEFT;}
    }
}
