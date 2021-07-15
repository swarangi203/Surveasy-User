package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetEntryName extends AppCompatActivity {
    TextInputLayout entryname;
    String uid, survey_name, name;
    Button submit;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    ArrayList<String> EntryNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid=getIntent().getStringExtra("uid").toString();
        survey_name=getIntent().getStringExtra("name").toString();
        setContentView(R.layout.activity_set_entry_name);
        entryname=(TextInputLayout)findViewById(R.id.editTextTextQuestionStatement);
        submit=(Button)findViewById(R.id.button3);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference().child("data").child(survey_name).child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CollectEntryNames((Map<String, Object>) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetEntryName.this, "Error Fetching Survey Names..", Toast.LENGTH_SHORT).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=entryname.getEditText().getText().toString();
                if(name.isEmpty())
                {
                    entryname.requestFocus();
                    entryname.setError("Enter an Entry number");
                }
                else if(EntryNames.contains(name))
                {
                     entryname.setError("Entry name already Used");
                     Toast.makeText(SetEntryName.this, "Set a new entry name or Edit existing Entry", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(SetEntryName.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Entry name is valid. Submit all questions to generate an Entry");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent toaddentry = new Intent(SetEntryName.this, AddEntry.class);
                                    toaddentry.putExtra("uid", uid);
                                    toaddentry.putExtra("name", survey_name);
                                    toaddentry.putExtra("entryname", name);
                                    
                                //     toaddentry.putExtra("map", amap);
                                    startActivity(toaddentry);
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }
    private void CollectEntryNames(Map<String, Object> Surveys) {
        for (Map.Entry<String, Object> entry : Surveys.entrySet()) {
            EntryNames.add(entry.getKey().toString());
           
        }
    }
}