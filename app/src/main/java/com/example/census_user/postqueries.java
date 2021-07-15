package com.example.census_user;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class postqueries extends AppCompatActivity {

    Button btn;
    TextInputLayout userquery,title;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postqueries);
        btn = (Button)findViewById(R.id.button);
        userquery=(TextInputLayout)findViewById(R.id.editTextTextMultiLine);
        title=(TextInputLayout)findViewById(R.id.editTextTextPersonName2);
        uid=getIntent().getStringExtra("uid").toString();
        reference=rootnode.getInstance().getReference("Queries").child("OtherQueries").child(uid);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertuserqueries();
            }
        });
    }
    private void insertuserqueries() {
        title.setError(null);
        userquery.setError(null);
        String qtitle = title.getEditText().getText().toString();
        String query = userquery.getEditText().getText().toString();
        String status = "unsolved";
        String feedback = "No feedback";
        if(qtitle.isEmpty())
        {
               title.setError("Enter Title");
               title.requestFocus();
        }
        else if(query.isEmpty())
        {
            userquery.setError("Enter Title");
            userquery.requestFocus();
        }
        else
        {
      /*  HashMap<String , String> userMap=new HashMap<>();
        userMap.put("Title",qtitle);
        userMap.put("Query",query);
        userMap.put("Status",status);
        userMap.put("feedback",feedback);



        userqueries userqueries= new userqueries(qtitle,query,status, feedback);   */

        Userqueries userqueries = new Userqueries(qtitle, query, status, feedback);
        reference.push().setValue(userqueries).addOnCompleteListener((task) -> {
            Toast.makeText(postqueries.this, "query posted", Toast.LENGTH_LONG).show();
        });


        //  Toast.makeText(postqueries.this,"query posted",Toast.LENGTH_LONG).show();

        title.getEditText().setText(null);
        userquery.getEditText().setText(null);
    }

    }
}