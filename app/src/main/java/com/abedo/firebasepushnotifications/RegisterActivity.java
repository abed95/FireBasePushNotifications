package com.abedo.firebasepushnotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {


    private static final int PICK_IMAGE = 1;
    private static final String TAG = "RegisterActivity";
    private EditText mEmailField, mNameField, mPasswordField;
    private Button mRegbtn, mLoginPageBtn;
    private CircleImageView mProfileImage;
    private ProgressBar progressBar;

    private Uri imageUri;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-application-f0bb8.appspot.com/NotificationsImagesApp");


    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadUi();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("NotificationsImagesApp");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        mRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    progressBar.setVisibility(View.VISIBLE);

                    final String name = mNameField.getText().toString();
                    final String email = mEmailField.getText().toString();
                    String password = mPasswordField.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            final String user_id = mAuth.getCurrentUser().getUid();
                                            Log.d(TAG, "onComplete: " + user_id);
                                            if (imageUri != null) {
                                                loadImageForFirebase(user_id, name, email);
                                            }

                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(RegisterActivity.this, "Error ::::"
                                                    + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }

    private void sendToMain() {

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void loadUi() {

        mEmailField = findViewById(R.id.register_email);
        mNameField = findViewById(R.id.register_name);
        mPasswordField = findViewById(R.id.register_password);

        mRegbtn = findViewById(R.id.btnRegisterActivity);
        mLoginPageBtn = findViewById(R.id.btnBackToLogin);

        mProfileImage = findViewById(R.id.profileImage);
        progressBar = findViewById(R.id.registerProgressBar);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            mProfileImage.setImageURI(imageUri);

        }
    }


    private void loadImageForFirebase(final String userID, final String userName, final String userEmail) {
        progressBar.setVisibility(View.VISIBLE);
        final String nameFile = System.currentTimeMillis() + "_imageNotifyUserProfile.jpg";
        //for random name image
        final StorageReference childRef = storageRef.child(nameFile);
        final UploadTask uploadTask = childRef.putFile(imageUri);

        //add file on Firebase and got Download Link
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                storageRef.child(nameFile).getDownloadUrl().addOnSuccessListener(
                        new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                progressBar.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onSuccess: Url: " + uri.toString());
                                // String DocId =preferences.getString("DocID","ID");
                                final String downloadUrl = uri.toString();


                                String token_id = FirebaseInstanceId.getInstance().getToken();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", userName);
                                userMap.put("image", downloadUrl);
                                userMap.put("email", userEmail);
                                userMap.put("token_id", token_id);

                                mFirestore.collection("NotifyUsers")
                                        .document(userID).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "onSuccess:success ");
                                        sendToMain();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "onFailure: " + e);

                                    }
                                });

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, exception + "");

                    }
                });
                return null;
            }
        });
    }


}
