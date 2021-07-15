package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class UserSettings extends AppCompatActivity {
    String uID;
    String email;
    CardView resendEmail,changeEmail,data_change_reqq, myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        uID=getIntent().getStringExtra("uid").toString();
        resendEmail=(CardView)findViewById(R.id.changepass);
        changeEmail=(CardView)findViewById(R.id.changeEmail);
        data_change_reqq=findViewById(R.id.data_change_req);
        myinfo=(CardView)findViewById(R.id.info);

        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, MyInfo.class);
                startActivity(intent);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, ChangeEmail.class);
                startActivity(intent);
            }
        });


        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                for (UserInfo profile : user.getProviderData()) {
                    email = profile.getEmail();
                };
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserSettings.this, "Password Reset Email has been sent to your Email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserSettings.this, HomeActivity.class);
                                    intent.putExtra("uid",uID);
                                }
                            }
                        });
            }
        });


        data_change_reqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tochangedatareq = new Intent(UserSettings.this, user_data_change_req.class);
                tochangedatareq.putExtra("uid", uID);
                startActivity(tochangedatareq);
            }
        });

    }
}