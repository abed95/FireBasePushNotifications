package com.abedo.firebasepushnotifications;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";

    private Button btnLogOut;

    private FirebaseAuth mAuth;

    private CircleImageView mProfileImage;
    private TextView mProfileName;

    private FirebaseFirestore mFirestore;
    private String mUserId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mUserId =mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreateView: userId "+mUserId);

        btnLogOut = view.findViewById(R.id.log_out_btn);
        mProfileImage = view.findViewById(R.id.profileImageFragment);
        mProfileName = view.findViewById(R.id.userNameProfile);

        mFirestore.collection("NotifyUsers")
                .document(mUserId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");
                Log.d(TAG, "onSuccess: image :" +user_image);

                mProfileName.setText(user_name);

                RequestOptions placeHolderOption = new RequestOptions();
                placeHolderOption.placeholder(R.drawable.profile);
                Glide.with(container.getContext()).setDefaultRequestOptions(placeHolderOption)
                        .load(user_image).into(mProfileImage);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id","");
                tokenMapRemove.put("token_id", FieldValue.delete());
                mFirestore.collection("NotifyUsers")
                        .document(mUserId).update(tokenMapRemove)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mAuth.signOut();
                                Intent intent = new Intent(container.getContext(),LoginActivity.class);
                                startActivity(intent);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());

                    }
                });


            }
        });


        return view;
    }

}
