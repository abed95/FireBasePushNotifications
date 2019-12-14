package com.abedo.firebasepushnotifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Sendctivity extends AppCompatActivity {

    private TextView txtUserID;
    private EditText inputSendNotify;
    private Button btnSendNotify;
    private ProgressBar mProgressBar;

    private FirebaseFirestore mFirestore;

    private String mUserID;
    private String mUserName;
    private String mCurrentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendctivity);

        mFirestore=FirebaseFirestore.getInstance();
        mCurrentID= FirebaseAuth.getInstance().getUid();

        txtUserID = findViewById(R.id.txt_sendT);
        inputSendNotify=findViewById(R.id.inputNotify);
        btnSendNotify=findViewById(R.id.btnSendNotify);
        mProgressBar=findViewById(R.id.progressBarSend);

        mUserID = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("user_name");
        txtUserID.setText("Send Message To : "+mUserName);

        btnSendNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = inputSendNotify.getText().toString();

                if (!TextUtils.isEmpty(message))
                {
                    mProgressBar.setVisibility(View.VISIBLE);

                    Map<String,Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message",message);
                    notificationMessage.put("from",mCurrentID);

                    // mFirestore.collection("NotifyUsers/"+mUserID+"Notification");

                    mFirestore.collection("NotifyUsers")
                            .document(mUserID).
                            collection("Notification")
                            .add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            inputSendNotify.setText("");
                            Toast.makeText(Sendctivity.this,"Message Send Successfully ",
                                    Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Sendctivity.this,"Message Error "+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });


    }
}
