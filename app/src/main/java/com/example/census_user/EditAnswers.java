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
import android.view.Gravity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAnswers extends AppCompatActivity {
    String uid,survey_name, entryname, answer;
    DatabaseReference reference, reference2, reference3;
    ScrollView scrollView;
    ArrayList<String> EntryNames = new ArrayList<>();
    TextView nameDisplay, entrynametext, q;
    Integer integer = 1, i = 1, num = 0;
    LinearLayout mainLayout;
    Button submitall;
    Map<String,String> queans=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addentry);
        uid = getIntent().getStringExtra("uid").toString();
        survey_name = getIntent().getStringExtra("name").toString();
        entryname = getIntent().getStringExtra("entryname").toString();
        nameDisplay = (TextView) findViewById(R.id.SurveyNameText);
        entrynametext = (TextView) findViewById(R.id.entrynametext);
        entrynametext.setText(entryname);
        mainLayout = findViewById(R.id.mainlayout);
        nameDisplay.setText(survey_name);
        submitall = (Button) findViewById(R.id.submitall);
        reference = FirebaseDatabase.getInstance().getReference("data").child(survey_name).child(uid).child(entryname);    //reference to get question
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CollectQuestions((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditAnswers.this, "Error Fetching Questions..", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void CollectQuestions(Map<String, Object> Entries) {
        for (Map.Entry<String, Object> entry : Entries.entrySet())
        {
            EntryNames.add(entry.getKey().toLowerCase());
            String Name= entry.getKey();
            Button b=new Button(getApplicationContext());
            b.setText(Name);
            b.setId(++i);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f);
            b.setTextColor(Color.parseColor("#FFFFFF"));
            b.setBackgroundColor(Color.parseColor("#FF871DB6"));
            b.setGravity(Gravity.CENTER_HORIZONTAL);
            b.setPadding(5,50,5,50);
            mainLayout.addView(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                   Intent tooptions = new Intent(EditAnswers.this, ChangeAnswer.class);
                                tooptions.putExtra("uid", uid);
                                tooptions.putExtra("name", survey_name);
                                tooptions.putExtra("question", Name);
                                tooptions.putExtra("entryname", entryname);
                                startActivity(tooptions);
                }

            });

        }


    }

//    private void CollectQuestions(Map<String, Object> questions) {
//
//        for (Map.Entry<String, Object> entry : questions.entrySet()) {
//            String que = entry.getKey();
//            LinearLayout quelayout = new LinearLayout(getApplicationContext());
//            quelayout.setOrientation(LinearLayout.VERTICAL);
//            mainLayout.addView(quelayout);
//            EditText editText=new EditText(EditAnswers.this);
//            editText.setHint(integer.toString()+". "+que);
//            editText.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#FF7F2A8E")));
//            editText.setPadding(10, 120, 10, 20);
//            editText.setId(integer);
//            integer++;
//            reference2 = FirebaseDatabase.getInstance().getReference("data-temp").child(survey_name).child(uid).child(entryname).child(que).child("ans");
//            reference2.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String ans = entry.getValue().toString();
//                    int i = ans.length();
//                    answer = ans.substring(5, i - 1);
//                    queans.put(que, answer);
//                    editText.setText(que + " \n\n" + answer);
//                    editText.setClickable(false);
//                    editText.setFocusable(false);
//                    editText.setTextIsSelectable(false);
//                    editText.setClickable(false);
//                    editText.setFocusable(false);
//                    editText.setCursorVisible(false);
//                    quelayout.removeAllViews();
//                    quelayout.addView(editText);
//                    editText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent tochangeanswer = new Intent(EditAnswers.this, ChangeAnswer.class);
//                            tochangeanswer.putExtra("uid", uid);
//                            tochangeanswer.putExtra("name", survey_name);
//                            tochangeanswer.putExtra("entryname", entryname);
//                            tochangeanswer.putExtra("question", que);
//                            tochangeanswer.putExtra("answer", answer);
//                        }
//                    });
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//
//    }
}
