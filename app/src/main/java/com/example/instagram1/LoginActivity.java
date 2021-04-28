package com.example.instagram1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.instagram1.data.AppContacts;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instagram1.data.AppContacts.users;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView registerUser;
    DatabaseReference ref;
    private ImageView iconImage;
    private LinearLayout linearLayout;
    boolean isRemember = false;
    SharedPrefsHelper sharedPrefs ;

    ArrayList<UserGetItem> users = new ArrayList<>();

    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = new SharedPrefsHelper(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);
        iconImage = findViewById(R.id.icon_image);
        linearLayout = findViewById(R.id.linear_layot);



        if (sharedPrefs.getIslogged()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        linearLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation animation = new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new LoginActivity.MyAnimationListener());
        

        iconImage.setAnimation(animation);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkToSignIn();
               // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

        });
    }
    private class MyAnimationListener implements Animation.AnimationListener{


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    private void checkToSignIn() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(username.getText().toString()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(LoginActivity.this,"Неправильный номер или пароль",Toast.LENGTH_LONG).show();
                    return;
                }
                UserHelperClass s = snapshot.getValue(UserHelperClass.class);



                if(s.getPassword().equals(password.getText().toString())){

                    sharedPrefs.setUsername(username.getText().toString().trim());
                    sharedPrefs.setIslogged(true);

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);finish();
                }else {
                    Toast.makeText(LoginActivity.this,"Неправильный номер или пароль",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                    return;

                for(DataSnapshot data : snapshot.getChildren()){

                    UserGetItem user = data.getValue(UserGetItem.class);

                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}