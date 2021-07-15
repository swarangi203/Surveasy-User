package com.example.census_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    CardView addentry, deleteentry, editentry, postqueries, settings, logout;
    String uid, survey_name;
    TextView nameView, surveynameview;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nameView=(TextView)findViewById(R.id.textView2);
        uid=getIntent().getStringExtra("uid").toString();
        survey_name = getIntent().getStringExtra("name").toString();
        surveynameview = (TextView)findViewById(R.id.surveyname);
        surveynameview.setText(survey_name);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("users").child(uid);
        nameView.setText("UserName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userdata=snapshot.getValue(UserHelperClass.class);
                nameView.setText(userdata.Name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addentry=(CardView)findViewById(R.id.addentry);
        deleteentry= (CardView)findViewById(R.id.deleteentry);
        editentry=(CardView)findViewById(R.id.editentry);
        postqueries=(CardView)findViewById(R.id.postqueries);
        settings=(CardView)findViewById(R.id.settings);
        logout =(CardView)findViewById(R.id.logout);

        //go to add entry page
        addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosetentry = new Intent(HomeActivity.this, SetEntryName.class);
                tosetentry.putExtra("uid", uid);
                tosetentry.putExtra("name", survey_name);
                startActivity(tosetentry);
            }
        });

        deleteentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toadeleteentry = new Intent(HomeActivity.this, DeleteEntry.class);
                toadeleteentry.putExtra("uid", uid);
                toadeleteentry.putExtra("name", survey_name);
                startActivity(toadeleteentry);
            }
        });

        editentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toeditentry = new Intent(HomeActivity.this, EditEntry.class);
                toeditentry.putExtra("uid", uid);
                toeditentry.putExtra("name", survey_name);
                startActivity(toeditentry);
            }
        });


        postqueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, view_query_section.class);
                intent.putExtra("uid", uid);
                intent.putExtra("name", survey_name);
                startActivity(intent);
            }
        });

        //settings
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserSettings.class);
                intent.putExtra("uid", uid);
                intent.putExtra("name", survey_name);
                startActivity(intent);

            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        postqueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, view_query_section.class);
                intent.putExtra("uid", uid);
                intent.putExtra("name", survey_name);
                startActivity(intent);
                
            }
        });
    }
}