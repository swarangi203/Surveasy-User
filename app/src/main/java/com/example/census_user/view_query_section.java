package com.example.census_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class view_query_section extends AppCompatActivity {
    Button btn1,btn2;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_query_section);
        btn1=findViewById(R.id.button1);
        btn2=findViewById(R.id.button2);
        uid=getIntent().getStringExtra("uid").toString();



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_query_section.this, postqueries.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_query_section.this, my_posted_queries.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

    }
} 