package com.abedo.firebasepushnotifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView txtN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        txtN  = findViewById(R.id.txtNBody);
        String messageBody = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");

        txtN.setText("From : " +dataFrom+ " | Message :" +messageBody);
    }
}
