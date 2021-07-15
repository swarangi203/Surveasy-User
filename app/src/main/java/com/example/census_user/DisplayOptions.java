package com.example.census_user;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import kotlin.text.UStringsKt;

public class DisplayOptions extends AppCompatActivity {
    String uid,survey_name,question, entryname;
    TextView nameDisplay, quetext, clearradio, cleartext;
    LinearLayout mainlayout;
    Button submitentry;
    Integer i = 1, num, selectedId=-1, rexists=0, bexists=0;
    FirebaseAuth mFirebaseAuth;
    EditText b;
    RadioButton r;
    EditText textoption;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase rootnode;
    DatabaseReference reference, reference2, reference3;
    HashMap<String,String> map ;
   // HelperEntryClass mapClass;
    String Myans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_options);
        uid=getIntent().getStringExtra("uid").toString();
        survey_name=getIntent().getStringExtra("name").toString();
        question=getIntent().getStringExtra("question").toString();
        entryname=getIntent().getStringExtra("entryname").toString();
      //  mapClass=(HelperEntryClass) getIntent().getSerializableExtra("map");
        nameDisplay=(TextView)findViewById(R.id.SurveyNameText);
        nameDisplay.setText(survey_name);
        quetext=(TextView)findViewById(R.id.quetext);
        quetext.setText(question);
        submitentry=(Button)findViewById(R.id.submitentry);
        mainlayout=(LinearLayout)findViewById(R.id.mainLayout);
        clearradio = (TextView)findViewById(R.id.clearradio);
        cleartext = (TextView)findViewById(R.id.cleartext);
        rootnode=FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("surveys").child(survey_name).child(question);
        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        mainlayout.addView(radioGroup);
        LinearLayout optionlayout = new LinearLayout(getApplicationContext());
        optionlayout.setOrientation(LinearLayout.VERTICAL);
        mainlayout.addView(optionlayout);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()) {
                    String option = snap.getValue(String.class);
                    if (option.equals("InputFromUser")) {
                        b = new EditText(getApplicationContext());
                        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                        b.setTextColor(Color.parseColor("#FF000000"));
                        b.setPadding(10, 50, 10, 50);
                        b.setGravity(20);
                        b.setId(i);
                        i++;
                        textoption=(EditText)findViewById(--i);
                        b.setText("");
                        b.setHint("Enter Answer");
                        bexists++;
                        optionlayout.addView(b);
                        cleartext.setVisibility(View.VISIBLE);
                        cleartext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                b.setText("");
                            }
                        });
                    } else {
                        r = new RadioButton(getApplicationContext());
                        r.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) ;
                        r.setGravity(20);
                        r.setText(option);
                        r.setId(i);
                        i++;
                        rexists++;
                        radioGroup.addView(r);
                        clearradio.setVisibility(View.VISIBLE);
                        clearradio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                radioGroup.clearCheck();
                            }
                        });
                    }
                }
                    submitentry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog alertDialog = new AlertDialog.Builder(DisplayOptions.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setCancelable(true);
                            alertDialog.setMessage("Submit the chosen answer?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (rexists > 0 && bexists > 0) {
                                                selectedId = radioGroup.getCheckedRadioButtonId();
                                                RadioButton selectedRadio=(RadioButton)findViewById(selectedId);
                                                if (b.getText().toString().equals("") && selectedId == -1)                           //user not entered any option
                                                {
                                                    b.requestFocus();
                                                    Toast.makeText(DisplayOptions.this, "No option has been selected", Toast.LENGTH_SHORT).show();
                                                }
                                                else if (b.getText().toString().equals(""))                                          //user entered radio button
                                                {
                                                    Myans=selectedRadio.getText().toString();
                                                    map = new HashMap<>();
                                                    map.put("ans", selectedRadio.getText().toString());


                                                }
                                                else if (selectedId == -1) {         //user entered text option
                                                    map = new HashMap<>();
                                                      Myans=    b.getText().toString();
                                                    map.put("ans", b.getText().toString());

                                                } else {
                                                    b.requestFocus();
                                                    Toast.makeText(DisplayOptions.this, "Please Enter only one option", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else if(rexists > 0)
                                            {
                                                selectedId = radioGroup.getCheckedRadioButtonId();
                                                RadioButton selectedRadio=(RadioButton)findViewById(selectedId);
                                                if(selectedId == -1)
                                                {
                                                    Toast.makeText(DisplayOptions.this, "Please select an option", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    map = new HashMap<>();
                                                         Myans= selectedRadio.getText().toString();
                                                    map.put("ans", selectedRadio.getText().toString());

                                                }
                                            }
                                            else
                                            {
                                                if(b.getText().toString().equals(""))
                                                {
                                                    b.requestFocus();
                                                    Toast.makeText(DisplayOptions.this, "Enter Your Answer", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    map = new HashMap<>();
                                                    Myans=      b.getText().toString();
                                                    map.put("ans", b.getText().toString());


                                                }
                                            }
                                            reference2 = FirebaseDatabase.getInstance().getReference("data-temp").child(survey_name).child(uid).child(entryname).child(question);
                                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    reference2.setValue(Myans);
                                                    finish();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(DisplayOptions.this, "Options Not Submitted...", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayOptions.this, "Error Fetching Options...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


















/*submitentry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(b.getText().toString().equals(null))
                                {
                                    b.setError("Enter answer");
                                    b.requestFocus();
                                    b.setText("");
                                    Toast.makeText(DisplayOptions.this, "Please provide atleast one input", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    reference2 = FirebaseDatabase.getInstance().getReference("users").child(uid).child("entryNo");
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String entryno = snapshot.getValue(String.class);
                                            num = Integer.parseInt(entryno);
                                            num++;
                                            String entrynumber = num.toString();
                                            reference3 = FirebaseDatabase.getInstance().getReference("data").child(survey_name).child(uid).child(entrynumber).child(question);
                                            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        reference3.setValue(b.getText().toString());
                                                        Intent backtoentrypage = new Intent(DisplayOptions.this, AddEntry.class);
                                                        backtoentrypage.putExtra("uid", uid);
                                                        backtoentrypage.putExtra("name", survey_name);
                                                        startActivity(backtoentrypage);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(DisplayOptions.this, "Options Not Submitted...", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(DisplayOptions.this, "Error Fetching Entry number...", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        });
 */