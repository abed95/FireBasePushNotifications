package com.abedo.firebasepushnotifications;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private final String TAG = "UsersFragment";

    private RecyclerView rcAllUsers;

    private ArrayList<NotifyUsers> data;
    private AllUserAdapter allUserAdapter;
    private FirebaseFirestore mFirestore;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        rcAllUsers = root.findViewById(R.id.rcUsersList);
        mFirestore = FirebaseFirestore.getInstance();

        data = new ArrayList<>();
        try {
            mFirestore.collection("NotifyUsers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Log.e(TAG, document.getId() + " => " + document.getData());
                                        String userId = document.getId();
                                        NotifyUsers users = document.toObject(NotifyUsers.class).withId(userId);
                                        data.add(users);
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error getting documents.", task.getException());
                            }

                            allUserAdapter = new AllUserAdapter(data, getContext());

                            rcAllUsers.setHasFixedSize(true);
                            rcAllUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            rcAllUsers.setAdapter(allUserAdapter);

                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, e + "");
        }


        return root;
    }


}
