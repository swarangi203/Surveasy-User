package com.example.census_user;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_number extends AppCompatActivity {
    Button btn;
    TextInputLayout newNum;
    String st_newnum,uid;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);
        newNum=findViewById(R.id.editTextNewNumber);
        uid=getIntent().getStringExtra("uid").toString();
        btn=findViewById(R.id.button);
        reference=rootnode.getInstance().getReference("Queries").child("Data_number_change_query").child(uid);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //st_newnum=newNum.getEditText().getText().toString();
                newNum.setError(null);
                String tempmob = newNum.getEditText().getText().toString();
                if (tempmob.isEmpty()) {
                    newNum.setError("Please Enter Mobile No");
                    newNum.requestFocus();
                } else if (tempmob.length() != 10) {
                    newNum.setError(" Mobile No.Should Be 10 Digit");
                    newNum.requestFocus();
                }

                else {
                    String numbertemp = tempmob;
                    int i = 0;
                    for (i = 0; i < numbertemp.length(); i++) {
                        if (!(numbertemp.charAt(i) >= '0' && numbertemp.charAt(i) <= '9')) {
                            break;
                        }
                    }

                    if (i == numbertemp.length()) {
                        tempmob= "+91"+tempmob;
                        Userqueries3 userqueries3 = new Userqueries3();
                        userqueries3.setNewNum(tempmob);
                        reference.setValue(userqueries3).addOnCompleteListener((task) -> {
                            Toast.makeText(change_number.this, "Query posted for change of Number", Toast.LENGTH_LONG).show();
                            finish();
                        });
                    } else {
                        newNum.setError("Mobile No.Should Contain Only Digits");
                        newNum.requestFocus();
                    }


                }
            }
        });



    }
}