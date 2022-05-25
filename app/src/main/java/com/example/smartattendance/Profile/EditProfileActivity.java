package com.example.smartattendance.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.Model.Users;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        database.getReference().child("Users").child(mAuth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users = snapshot.getValue(Users.class);
                                Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.img_avatar)
                                        .into(binding.profileImagePic);
                                binding.UserName.setText(users.getUserName());
                                binding.UserDepartment.setText(users.getDepartment());
                                binding.UserAbout.setText(users.getAbout());
                                binding.UserMail.setText(users.getEmail());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editProgress.setVisibility(View.VISIBLE);
                String user_name = binding.UserName.getText().toString();
                String department = binding.UserDepartment.getText().toString();
                String about = binding.UserAbout.getText().toString();
                String mail = binding.UserMail.getText().toString().trim();

                HashMap<String,Object>obj = new HashMap<>();
                obj.put("userName",user_name);
                obj.put("department",department);
                obj.put("about",about);
                obj.put("email",mail);

                database.getReference().child("Users").child(mAuth.getUid())
                        .updateChildren(obj);
                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri file = data.getData();
            binding.profileImagePic.setImageURI(file);

            final StorageReference reference = storage.getReference().child("Profile_picture")
                    .child(mAuth.getUid());
            reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(mAuth.getUid())
                                    .child("profile_pic").setValue(uri.toString());
                            Toast.makeText(EditProfileActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


}