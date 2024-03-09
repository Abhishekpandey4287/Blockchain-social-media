package com.example.social_media_using_blockchain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageViewProfilePic;
    private EditText editTextUsername;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = getIntent().getStringExtra("USER_ID");
        String username = getIntent().getStringExtra("USERNAME");

        // Initialize views
        imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        editTextUsername = findViewById(R.id.editTextUsername);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button addProfileButton = findViewById(R.id.addProfileButton);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePicture();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            imageViewProfilePic.setImageURI(imageUri);
        }
    }

    private void uploadProfilePicture() {
        if (imageUri != null) {
            Log.d("Image URI", imageUri.toString());
            // Get a reference to store the image in 'profile_pics' folder with a unique name
            StorageReference profilePicsRef = FirebaseStorage.getInstance()
                    .getReference("profile_pics/" + userId + ".jpg");

            profilePicsRef.putFile(imageUri)
                    .addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                // If successful, get the download URL
                                profilePicsRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            String username = editTextUsername.getText().toString().trim();
                                            saveUserDataToFirestore(downloadUri.toString(), username);
                                        } else {
                                            Toast.makeText(EditProfileActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataToFirestore(String downloadUrl, String username) {
        String uid = userId;

        // Create a Map to store user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("profilePicture", downloadUrl);
        userData.put("username", username);

        // Save the user data to Firestore
        db.collection("users")
                .document(uid)
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after successful upload
                        } else {
                            // Log the error message for more details
                            Log.e("Firestore Error", "Error updating profile: " + task.getException());

                            // Display a more informative error message
                            if (task.getException() != null) {
                                Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
