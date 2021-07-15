package com.example.census_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddEntry extends AppCompatActivity {
    String uid,survey_name, entryname;
    DatabaseReference reference, reference2,ref;
    ScrollView scrollView;
    TextView nameDisplay, entrynametext;
    Integer integer = 1, i = 1, num = 0;
    LinearLayout mainLayout;
    Button submitall;
    String question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addentry);
        uid=getIntent().getStringExtra("uid").toString();
        survey_name=getIntent().getStringExtra("name").toString();
        entryname=getIntent().getStringExtra("entryname").toString();
   //     map =(HelperEntryClass) getIntent().getSerializableExtra("map");
        nameDisplay=(TextView)findViewById(R.id.SurveyNameText);
        entrynametext=(TextView)findViewById(R.id.entrynametext);
        entrynametext.setText(entryname);
        mainLayout = (LinearLayout)findViewById(R.id.mainlayout);
        nameDisplay.setText(survey_name);
        submitall = (Button)findViewById(R.id.submitall);
        reference=FirebaseDatabase.getInstance().getReference("surveys").child(survey_name);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    CollectQuestions((Map<String, Object>) snapshot.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddEntry.this, "Error Fetching Questions..", Toast.LENGTH_SHORT).show();
            }
        });

        submitall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference2 = FirebaseDatabase.getInstance().getReference("data-temp").child(survey_name).child(uid).child(entryname);
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long count = snapshot.getChildrenCount();
                        if(count==0)
                        {
                            Toast.makeText(AddEntry.this, "No Questions answered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (integer.longValue()-1 != count) {
                                Toast.makeText(AddEntry.this, "Answer  all question to submit", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent tosubmitallpage = new Intent(AddEntry.this, SubmitEntry.class);
                                tosubmitallpage.putExtra("uid",uid );
                                tosubmitallpage.putExtra("name",survey_name );
                                tosubmitallpage.putExtra("entryname", entryname);
                                startActivity(tosubmitallpage);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddEntry.this, "Temporary data not fetched", Toast.LENGTH_SHORT).show();
                    }
                });//to uid

             //   HashMap<String,String>hashMap = map.getMap();

//                reference2 = FirebaseDatabase.getInstance().getReference("data").child(survey_name).child(uid).child(entryname);
//                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        reference2.setValue(map);
//                        Toast.makeText(AddEntry.this, "Answer Recorded", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(AddEntry.this, "You may not have answerd all questions...", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
     /*   submitall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference2 = FirebaseDatabase.getInstance().getReference("users").child(uid).child("entryNo");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddEntry.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Do you wish to submit all questions? If none of the questions are entered, entry won't be recorded. \n");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String entryno = snapshot.getValue(String.class);
                                        num = Integer.parseInt(entryno);
                                        num++;
                                        String entrynumber = num.toString();
                                        reference2.setValue(entrynumber);
                                        Intent intent = new Intent(AddEntry.this, HomeActivity.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("name", survey_name);
                                        dialog.dismiss();
                                        startActivity(intent);

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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddEntry.this, "Error Submitting Entry...", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }); */
    }

    private void CollectQuestions(Map<String, Object> questions) {
        for (Map.Entry<String, Object> entry : questions.entrySet()) {
            LinearLayout quelayout = new LinearLayout(getApplicationContext());
            quelayout.setOrientation(LinearLayout.VERTICAL);
            String que = entry.getKey();
            Button q = new Button(getApplicationContext());
            q.setText("  " + integer + ". " + que);
            q.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            q.setTextColor(Color.parseColor("#FF000000"));
            q.setBackgroundColor(Color.TRANSPARENT);
            q.setPadding(10, 50, 10, 50);
            q.setGravity(20);
            q.setId(integer);
            integer++;
            quelayout.addView(q);
            mainLayout.addView(quelayout);
            q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("data-temp").child(survey_name).child(uid).child(entryname).child(que).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                q.setBackgroundColor(Color.parseColor("#9C27B0"));
                                q.setTextColor(Color.parseColor("#FFFFFF"));

                                        Toast.makeText(AddEntry.this, "You Already answered this question. Go to Edit Entry for changes", Toast.LENGTH_SHORT).show();
                                        
                            }
                            else
                            {
                                Intent tooptions = new Intent(AddEntry.this, DisplayOptions.class);
                                tooptions.putExtra("uid", uid);
                                tooptions.putExtra("name", survey_name);
                                tooptions.putExtra("question", que);
                                tooptions.putExtra("entryname", entryname);
                                startActivity(tooptions);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });
        }

    }
}
