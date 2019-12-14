package com.abedo.firebasepushnotifications;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView rcNotifications;

    private final String TAG = "NotificationsFragment";

    private ArrayList<Notifications> data;
    private NotificationsAdapetr notificationsAdapetr;
    private FirebaseFirestore mFirestore;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            notificationsAdapetr.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "onResume: " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        rcNotifications = view.findViewById(R.id.rcNotifications);

        data = new ArrayList<>();
        notificationsAdapetr = new NotificationsAdapetr(data, getContext());


        mFirestore = FirebaseFirestore.getInstance();

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "onCreateView: " + currentUserId);

        try {
            mFirestore.collection("NotifyUsers").document(currentUserId)
                    .collection("Notification")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Log.e(TAG, document.getId() + " => " + document.getData());
                                        String userId = document.getId();
                                        Notifications notifications = document.toObject(Notifications.class);
                                        data.add(notifications);
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error getting documents.", task.getException());
                            }

                            notificationsAdapetr = new NotificationsAdapetr(data, getContext());

                            rcNotifications.setHasFixedSize(true);
                            rcNotifications.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            rcNotifications.setAdapter(notificationsAdapetr);

                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, e + "");
        }

        return view;
    }

}
