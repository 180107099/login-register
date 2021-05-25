package com.example.instagram1;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram1.data.SharedPrefsHelper;
import com.example.instagram1.model.UserGetItem;
import com.example.instagram1.model.UserHelperClass;
import com.example.instagram1.model.addPost1;
import com.example.instagram1.model.postAdd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    private  static final int PICK_IMAGE_REQUEST = 1;

    Button autobioSave, passwordSave, nameSave;
    EditText autoBio, passLast, passNew1, passNew2,nameNew;
    FirebaseDatabase ref;
    SharedPrefsHelper sharedPrefs ;
    ImageView imageViewEdit;
     ImageButton imgBtn;
     Button saveImg;
    private Uri imageUri;

    StorageReference storageReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        autobioSave = findViewById(R.id.autobiodraphy_save);
        passwordSave = findViewById(R.id.password_save);
        nameSave = findViewById(R.id.save_name);
        imageViewEdit = findViewById(R.id.image_edit);

        autoBio = findViewById(R.id.autobiography);
        passLast = findViewById(R.id.password_last);
        passNew1 = findViewById(R.id.password_new_1);
        passNew2 = findViewById(R.id.password_new_2);
        nameNew = findViewById(R.id.new_name);
        imgBtn = findViewById(R.id.imageBtnEdit);
        saveImg = findViewById(R.id.saveButton);


        sharedPrefs = new SharedPrefsHelper(this);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        autobioSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBiog();

            }
        });

        passwordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passNew1.getText().toString().equals(passNew2.getText().toString())){
                    changePass();
                }
                else {
                    Toast.makeText(EditProfile.this,
                            "Новый пароль не одинаковые, напишите одинаковый пароли",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        nameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeName();
            }
        });

    }

    public void changePass(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(sharedPrefs.getUsername()).child("info").child("password");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(passLast.getText().toString().equals(snapshot.getValue(String.class))){

                    ref.setValue(passNew1.getText().toString());
                    passLast.setText("");
                    passNew1.setText("");
                    passNew2.setText("");
                    Toast.makeText(EditProfile.this,"Вы изменили пароль!", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(EditProfile.this,"Ваш старый пароль не верный!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    UserGetItem pass = snapshot1.getValue(UserGetItem.class);
                    if(pass.getInfo().getUsername().equals(sharedPrefs.getUsername().toString())){
                        if(passLast.getText().toString().equals(pass.getInfo().getPassword().toString())){
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("password", passNew1.getText());
                            ref.child(pass.getInfo().getUsername().toString()).child("info").updateChildren(result);
                        }else {
                            Toast.makeText(EditProfile.this,"Ваш старый пароль не верный!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

    }
    public  void autoBiog(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(sharedPrefs.getUsername()).child("info").child("bio");
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ref.setValue(autoBio.getText().toString());
                 autoBio.setText("");
                 Toast.makeText(EditProfile.this,"все хорошо!", Toast.LENGTH_LONG).show();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }

    public void changeName(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(sharedPrefs.getUsername()).child("info").child("name");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.setValue(nameNew.getText().toString());
                nameNew.setText("");
                Toast.makeText(EditProfile.this,"Вы изменил имя!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            Toast.makeText(EditProfile.this,"image alindi",Toast.LENGTH_LONG).show();
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewEdit);
        }
    }
    private void uploadImage()
    {
        if (imageUri != null) {

            storageReference = FirebaseStorage.getInstance().getReference("profileImage");

            ProgressDialog progressDialog
                    = new ProgressDialog(EditProfile.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // adding listeners on upload
            // or failure of image
            storageReference.child(sharedPrefs.getUsername()).child("1").putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(EditProfile.this,
                                                    "Foto changed!",
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
                                    .makeText(EditProfile.this,
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