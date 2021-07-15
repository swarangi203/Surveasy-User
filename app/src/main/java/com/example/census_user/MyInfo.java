package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInfo extends AppCompatActivity {
    TextInputLayout email, name,monNo,post,accountType;
    String mail ,username,MobileNo,userpost,usertype,uid;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference reference;
    FirebaseUser muser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        email = (TextInputLayout)findViewById(R.id.editTextTextEmail);
        name =(TextInputLayout)findViewById(R.id.editTextTextName);
        post =(TextInputLayout)findViewById(R.id.editTextTextPost);
        monNo=(TextInputLayout)findViewById(R.id.editTextTextMobileNo);
        accountType=(TextInputLayout)findViewById(R.id.editTextTextState);
        email.getEditText().setClickable(false);
        email.getEditText().setFocusable(false);
        name.getEditText().setClickable(false);
        name.getEditText().setFocusable(false);
        post.getEditText().setClickable(false);
        post.getEditText().setFocusable(false);
        monNo.getEditText().setClickable(false);
        monNo.getEditText().setFocusable(false);
        accountType.getEditText().setClickable(false);
        accountType.getEditText().setFocusable(false);

        reference=FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid().toString();
        reference=reference.child(uid);

        for (UserInfo profile : user.getProviderData()) {
            mail = profile.getEmail();
            email.getEditText().setText(mail);
        };

        reference.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username=snapshot.getValue(String.class);
                name.getEditText().setText(username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyInfo.this, "Failed to get Name ", Toast.LENGTH_SHORT).show();
            }
        });

        reference.child("mobile_No").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mobile_No=snapshot.getValue(String.class);
                monNo.getEditText().setText(mobile_No);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyInfo.this, "Failed to get Mobile Number ", Toast.LENGTH_SHORT).show();
            }
        });

        reference.child("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userpost=snapshot.getValue(String.class);
                if(userpost=="user")
                {
                    userpost="surveyor";
                }
                post.getEditText().setText(userpost);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyInfo.this, "Failed to get Account Type", Toast.LENGTH_SHORT).show();
            }
        });

        reference.child("state").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state=snapshot.getValue(String.class);
                accountType.getEditText().setText(state);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyInfo.this, "Failed to get Account Status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}