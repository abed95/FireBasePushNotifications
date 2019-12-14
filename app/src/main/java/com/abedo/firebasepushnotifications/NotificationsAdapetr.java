package com.abedo.firebasepushnotifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * created by Abedo95 on 11/26/2019
 */
public class NotificationsAdapetr  extends RecyclerView.Adapter<NotificationsAdapetr.MyViewHolder> {

    private ArrayList<Notifications> data;
    private FirebaseFirestore firestore;
    private Context context;

    private final static String TAG = "NotificationsAdapetr";
    // private Users users;
    // private DocumentReference doc;
    private View view;

    public NotificationsAdapetr(ArrayList<Notifications> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationsAdapetr.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.notifications_row, null, false);
        return new NotificationsAdapetr.MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationsAdapetr.MyViewHolder holder, final int position) {

        firestore = FirebaseFirestore.getInstance();
        String from_id = data.get(position).getFrom();
        Log.d(TAG, "onBindViewHolder: "+from_id);

        holder.tvMessage.setText(data.get(position).getMessage());

        firestore.collection("NotifyUsers").document(from_id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("name");
                String image = documentSnapshot.getString("image");
                holder.tvFriendName.setText(name);

                final Uri userImage = Uri.parse(image);

                Log.d("Image", "onBindViewHolder: " + userImage);
                if (userImage != null) {
                    Glide.with(context)
                            .load(userImage)
                            .into(holder.imgFriend);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });

        Log.d(TAG, "onBindViewHolder:User" + data);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFriendName;
        private TextView tvMessage;
        private CircleImageView imgFriend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFriendName = itemView.findViewById(R.id.txtUserNameNotifyRow);
            tvMessage = itemView.findViewById(R.id.txtMessageNotifyRow);
            imgFriend = itemView.findViewById(R.id.userImageNotifyRow);

        }


    }


}

