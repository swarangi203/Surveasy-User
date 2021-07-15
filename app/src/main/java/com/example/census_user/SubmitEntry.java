package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SubmitEntry extends AppCompatActivity {
    String uid,survey_name, entryname, answer;
    DatabaseReference reference, reference2, reference3;
    ScrollView scrollView;
    TextView nameDisplay, entrynametext, q;
    Integer integer = 1, i = 1, num = 0;
    LinearLayout mainLayout;
    Button submitall;
    Map<String,String> queans=new HashMap<String,String>();
    boolean b=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addentry);
        uid=getIntent().getStringExtra("uid").toString();
        survey_name=getIntent().getStringExtra("name").toString();
        entryname=getIntent().getStringExtra("entryname").toString();
        nameDisplay=(TextView)findViewById(R.id.SurveyNameText);
        entrynametext=(TextView)findViewById(R.id.entrynametext);
        entrynametext.setText(entryname);
        mainLayout = findViewById(R.id.mainlayout);
        nameDisplay.setText(survey_name);
        submitall = (Button)findViewById(R.id.submitall);
            //reference to get question
        if(b) {
            reference = FirebaseDatabase.getInstance().getReference("data-temp").child(survey_name).child(uid).child(entryname);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CollectQuestions((Map<String, Object>) snapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SubmitEntry.this, "Error Fetching Questions..", Toast.LENGTH_SHORT).show();
                }
            });
        }
        submitall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!b) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SubmitEntry.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Have you finished reviewing the answers? Once you submit them you an edit through the Edit Entry section. \n");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    reference3 = FirebaseDatabase.getInstance().getReference("data").child(survey_name).child(uid).child(entryname);
                                    for (Map.Entry<String, String> entry : queans.entrySet()) {
                                        String question1 = entry.getKey();
                                        String ans1 = entry.getValue();
                                        Map<String, String> tempmap = new HashMap<String, String>();
                                        tempmap.put("ans", ans1);
                                        reference3.child(question1).setValue(ans1);
                                    }
                                    reference.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(SubmitEntry.this, "Entry Successfully Submited", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                    finish();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, Continue Reviewing",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        
    }

    private void CollectQuestions(Map<String, Object> questions) {
        if (b) {

            for (Map.Entry<String, Object> entry : questions.entrySet()) {
                String que = entry.getKey();
                LinearLayout quelayout = new LinearLayout(getApplicationContext());
                quelayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.addView(quelayout);
                EditText editText = new EditText(SubmitEntry.this);
                editText.setHint(integer.toString() + ". " + que);
                editText.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#FF7F2A8E")));
                editText.setPadding(10, 120, 10, 20);
                editText.setId(integer);
                integer++;
                        String ans = entry.getValue().toString();
                        int i = ans.length();
//                        answer = ans.substring(5, i - 1);
                        queans.put(que, ans);
                        editText.setText(que + " \n\n" + ans);
                        editText.setClickable(false);
                        editText.setFocusable(false);
                        editText.setTextIsSelectable(false);
                        editText.setClickable(false);
                        editText.setFocusable(false);
                        editText.setCursorVisible(false);
                        quelayout.removeAllViews();
                        quelayout.addView(editText);
                    }



            }
            b=false;


    }
}
