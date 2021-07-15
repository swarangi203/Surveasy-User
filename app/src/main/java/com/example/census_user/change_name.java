package com.example.census_user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_name extends AppCompatActivity {
    Button btn;
    TextInputLayout newname;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    String uid,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        btn=findViewById(R.id.button);
        //oldname=findViewById(R.id.editTextOldName);
        newname=findViewById(R.id.editTextNewName);
        uid=getIntent().getStringExtra("uid").toString();

        reference=rootnode.getInstance().getReference("Queries").child("Data_name_change_query").child(uid);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name= newname.getEditText().getText().toString();
                if (Name.isEmpty()) {
                    newname.setError("Please Enter Name");
                    newname.requestFocus();
                    Toast.makeText(change_name.this, "Enter Value", Toast.LENGTH_LONG).show();
                }
                else{
                    Userqueries2 userqueries2 = new Userqueries2();
                    userqueries2.setNewname(Name);

                    reference.setValue(userqueries2).addOnCompleteListener((task) -> {
                        Toast.makeText(change_name.this, "query posted for Name Change", Toast.LENGTH_LONG).show();
                    });

                    newname.getEditText().setText(null);
                    finish();

                }
            }

        });
    }


}