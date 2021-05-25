package com.example.instagram1.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instagram1.MainActivity;
import com.example.instagram1.R;
import com.example.instagram1.data.AppContacts;
import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.model.addPost1;
import com.example.instagram1.model.postAdd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.instagram1.data.AppContacts.users;
import static java.lang.String.valueOf;

public class AddFragment extends Fragment {
    private  static final int PICK_IMAGE_REQUEST = 1;
    private EditText post;
    private Button saveBtn ;
    private ImageButton imgBtn;
    private ImageView imageView;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    SharedPrefsHelper sharedPrefs ;
    String generatedFilePath;



    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference refer;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_fragment, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        post = getView().findViewById(R.id.post);
        imgBtn = getView().findViewById(R.id.imageButton);
        imageView = getView().findViewById(R.id.image_view);
        saveBtn = getView().findViewById(R.id.saveButton);
        sharedPrefs = new SharedPrefsHelper(getContext());


        Picasso.get().load("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg").into(imageView);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Post = post.getText().toString();

                if(TextUtils.isEmpty(Post) ){
                    Toast.makeText(getContext(),"Мағыналы сөз жазыныз!", Toast.LENGTH_SHORT).show();
                } else if (Post.length()<5){
                    Toast.makeText(getContext(), "Мағыналы сөз өте аз!", Toast.LENGTH_SHORT).show();
                }else{

                    uploadImage();
                   /* reference = FirebaseDatabase.getInstance().getReference("PRODUCT");
                    storageReference = FirebaseStorage.getInstance().getReference("PRODUCT");
                    postAdd helperClass = new postAdd(Post);
                    reference.child(System.currentTimeMillis()+"").child(AppContacts.INFO).setValue(helperClass);*/
                }


            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  ){
            Toast.makeText(getContext(),"image alindi",Toast.LENGTH_LONG).show();
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(imageView);
            /*
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);

             */
        }
    }
    private void uploadImage()
    {
        if (imageUri != null) {
            String Post = post.getText().toString();

            reference = FirebaseDatabase.getInstance().getReference("users");
            refer = FirebaseDatabase.getInstance().getReference("post");

            postAdd helperClass = new postAdd(Post);
            String time = System.currentTimeMillis()+"";
            String mail1= sharedPrefs.getUsername();
            reference.child(mail1).child("Post").child(time).setValue(helperClass);

            storageReference = FirebaseStorage.getInstance().getReference("post");

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            addPost1 salu = new addPost1(Post, mail1);
            refer.child(time).setValue(salu);

            // adding listeners on upload
            // or failure of image
            storageReference.child(Post).putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    post.setText("");
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getContext(),
                                                    "Post Salindi!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });

        }
    }

}