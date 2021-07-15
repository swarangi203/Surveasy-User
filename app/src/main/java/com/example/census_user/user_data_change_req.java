package com.example.census_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class user_data_change_req extends AppCompatActivity {
    Button btn1,btn2;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_change_req);
        uID=getIntent().getStringExtra("uid");
        btn1=findViewById(R.id.btn_name);
        btn2=findViewById(R.id.btn_num);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tochangedatareq = new Intent(user_data_change_req.this, change_name.class);
                tochangedatareq.putExtra("uid", uID);
                startActivity(tochangedatareq);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tochangedatareq1 = new Intent(user_data_change_req.this, change_number.class);
                tochangedatareq1.putExtra("uid", uID);
                startActivity(tochangedatareq1);
            }
        });

    }
}