package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class EditEntry extends AppCompatActivity {
    FirebaseDatabase rootnode;
    DatabaseReference reference,refdelete;
    ArrayList<String> EntryNames = new ArrayList<>();
    LinearLayout entryListLayout;
    String uid, surveyname;
    Integer i=0;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        uid=getIntent().getStringExtra("uid").toString();
        surveyname = getIntent().getStringExtra("name").toString();
        entryListLayout=(LinearLayout)findViewById(R.id.xyz);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("data").child(surveyname).child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    CollectEntryNames((Map<String, Object>) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEntry.this, "Error Fetching Entry Names..", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void CollectEntryNames(Map<String, Object> Entries) {
        for (Map.Entry<String, Object> entry : Entries.entrySet())
        {
            EntryNames.add(entry.getKey().toLowerCase());
            String Name= entry.getKey().toLowerCase();
            b=new Button(getApplicationContext());
            b.setText(Name);
            b.setId(++i);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f);
            b.setTextColor(Color.parseColor("#FFFFFF"));
            b.setBackgroundColor(Color.parseColor("#FF871DB6"));
            b.setGravity(Gravity.CENTER_HORIZONTAL);
            b.setPadding(5,50,5,50);
            entryListLayout.addView(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(EditEntry.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Do you wish to edit the entry:\n" + Name +"?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                      Intent toeditanswer = new Intent(EditEntry.this, EditAnswers.class);
                                      toeditanswer.putExtra("uid", uid);
                                      toeditanswer.putExtra("name", surveyname);
                                      toeditanswer.putExtra("entryname", Name);
                                      startActivity(toeditanswer);
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            });

        }


    }

}