package com.abedo.firebasepushnotifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyViewHolderAddGroup> {


    private ArrayList<NotifyUsers> data;
    private Context context;
    private final static String TAG = "AddFriendsAdapter";
   // private Users users;
   // private DocumentReference doc;
    private View view;

    public AllUserAdapter(ArrayList<NotifyUsers> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AllUserAdapter.MyViewHolderAddGroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.users_row, null, false);
        return new AllUserAdapter.MyViewHolderAddGroup(root);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserAdapter.MyViewHolderAddGroup holder, final int position) {



        holder.tvFriendName.setText(data.get(position).getName());
        Log.d(TAG, "onBindViewHolder:User" + data);


        final Uri userImage = Uri.parse(data.get(position).getImage());


        Log.d("Image", "onBindViewHolder: " + userImage);
        if (userImage != null) {
            Glide.with(context)
                    .load(userImage)
                    .into(holder.imgFriend);

        }

        final String userId = data.get(position).userId;
        final String userName = data.get(position).name;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Sendctivity.class);
                intent.putExtra("user_id",userId);
                intent.putExtra("user_name",userName);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolderAddGroup extends RecyclerView.ViewHolder {
        private TextView tvFriendName;
        private CircleImageView imgFriend;

        public MyViewHolderAddGroup(@NonNull View itemView) {
            super(itemView);

            tvFriendName = itemView.findViewById(R.id.txtUserNameRow);
            imgFriend = itemView.findViewById(R.id.userImageRow);

        }


    }


}


