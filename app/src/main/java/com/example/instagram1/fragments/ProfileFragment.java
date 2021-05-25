package com.example.instagram1.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.instagram1.EditProfile;
import com.example.instagram1.LoginActivity;
import com.example.instagram1.MainActivity;
import com.example.instagram1.Message;
import com.example.instagram1.R;
import com.example.instagram1.RegisterActivity;
import com.example.instagram1.constants.Constants;
import com.example.instagram1.data.ProfileFotoAdapter;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.data.postAdapter;
import com.example.instagram1.model.MessageAdapter;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.example.instagram1.model.addPost1;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    DrawerLayout drawerLayout;
    private Button logout;
    SharedPrefsHelper sharedPrefs;
    Button editProfile;
    TextView profile_username, followers, following, number_post, fullname, bio;
    ImageView profileFoto;

    private postAdapter postAdapter;
    private List<addPost1> posts;


    private int numOfTabs;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        posts = new ArrayList<>();

        drawerLayout = getView().findViewById(R.id.drawer_layout);
        logout = view.findViewById(R.id.logout);
        number_post = getView().findViewById(R.id.posts);
        profile_username = getView().findViewById(R.id.profile_username_set);
        followers = getView().findViewById(R.id.followers);
        following = getView().findViewById(R.id.followig);
        fullname = getView().findViewById(R.id.fullname);
        ImageView menuIcon = (ImageView) getView().findViewById(R.id.iptions);
        editProfile = getView().findViewById(R.id.profile_edit);
        bio = getView().findViewById(R.id.bio);
        profileFoto = getView().findViewById(R.id.image_profile);

        drawerLayout.bringToFront();

        sharedPrefs = new SharedPrefsHelper(getContext());

        followers.setText("0");
        following.setText("0");
        profile_username.setText(sharedPrefs.getUsername());

        getPostCount();
        getFullname();
        getFullBio();
        getProfileFoto();

        TabLayout tabLayout = getView().findViewById(R.id.tabLayoutPr);
        TabItem tabChat = getView().findViewById(R.id.saveFotoTab);
        TabItem tabUser = getView().findViewById(R.id.myFotoTab);
        ViewPager viewPager1 = getView().findViewById(R.id.viewPagerPr);
        //tabLayout.setupWithViewPager(viewPager);
        ProfileFotoAdapter pagerAdapter = new
                ProfileFotoAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager1.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerView.bringToFront();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        logout.setOnClickListener(v -> {

            sharedPrefs.clear();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void getPostCount() {

        FirebaseDatabase.getInstance().getReference().child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    addPost1 post = snapshot1.getValue(addPost1.class);
                    if (post != null) {
                        if (post.getUsername().equals((sharedPrefs.getUsername()))) {
                            counter++;
                        }
                    }
                }
                number_post.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFullname() {

        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(sharedPrefs.getUsername()).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserHelperClass user = snapshot.getValue(UserHelperClass.class);

                fullname.setText(user.getName());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFullBio() {
        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(sharedPrefs.getUsername()).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserHelperClass user = snapshot.getValue(UserHelperClass.class);

                bio.setText(user.getBio());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProfileFoto() {

        StorageReference str = FirebaseStorage.getInstance().getReference().child(Constants.ProfileImage).child(sharedPrefs.getUsername()).child("1");
        str.getDownloadUrl().addOnSuccessListener(downloadUrl -> Picasso.get().load(downloadUrl).into(profileFoto));
    }

/*
    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickAboutUs(View view){
        closeDrower(drawerLayout);
    }

    private  static void closeDrower(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void Logout(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        closeDrower(drawerLayout);
    }*/
}



