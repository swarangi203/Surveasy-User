package com.example.census_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    TextInputLayout otp;
    Button signInButton,resendOtp,loginpage;
    String userphno;
    ProgressBar bar;
    String optid,uID,mail,pass;
    private  FirebaseAuth mAuth;
    boolean isSubmit=false;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = (TextInputLayout) findViewById(R.id.otp);
        signInButton = (Button) findViewById(R.id.SignInButton);
        resendOtp = (Button) findViewById(R.id.resendotp);
        loginpage = (Button) findViewById(R.id.backtologin);
        bar = findViewById(R.id.progressBar2);
        userphno = getIntent().getStringExtra("mobileNo").toString();
        uID = getIntent().getStringExtra("uid").toString();
        mail = getIntent().getStringExtra("mail").toString();//newww
        pass = getIntent().getStringExtra("pass").toString();//newww
        mAuth = FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("Queries").child("Data_number_change_query").child(uID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                Userqueries3 userqueries3 = snapshot.getValue(Userqueries3.class);
                if (userqueries3.getNewNum() != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(OtpActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("You will receive OTP on " + userphno + "\nAdmin has not updated your number yet");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(OtpActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("You will receive OTP on " + userphno );
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                    initiateotp();

                    resendOtp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OtpActivity.this, OtpActivity.class);
                            intent.putExtra("mobileNo", userphno);
                            intent.putExtra("uid", uID);
                            intent.putExtra("mail", mail);  //newww
                            intent.putExtra("pass", pass);//newww
                            startActivity(intent);
                            finish();
                        }
                    });
                    loginpage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    signInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isSubmit) {
                                otp.setError(null);
                                if (otp.getEditText().getText().toString().isEmpty()) {
                                    otp.setError("Please Enter OTP");
                                    otp.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Fields can not be empty..", Toast.LENGTH_SHORT).show();
                                } else if ((otp.getEditText().getText().toString().length() != 6)) {
                                    otp.setError("OTP Should Be 6 Digit");
                                    otp.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Invalid Fields In OTP..", Toast.LENGTH_SHORT).show();
                                } else {
                                    bar.setVisibility(View.VISIBLE);
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(optid, otp.getEditText().getText().toString());
                                    signInWithPhoneAuthCredential(credential);
                                    bar.setVisibility(View.INVISIBLE);
                                }

                            }
                        }
                    });
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void initiateotp()
    {
        bar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(userphno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                            {
                                bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(OtpActivity.this, "OTP has been sent...", Toast.LENGTH_SHORT).show();
                                optid=s;
                                isSubmit=true;
                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                            {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e)
                            {
                                bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(OtpActivity.this, "Error Signing In with OTP...", Toast.LENGTH_SHORT).show();
                                resendOtp.setClickable(true);
                                resendOtp.setEnabled(true);
                                resendOtp.setVisibility(View.VISIBLE);
                                loginpage.setClickable(true);
                                loginpage.setEnabled(true);
                                loginpage.setVisibility(View.VISIBLE);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {//newww
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {//newww
                                    if (!task.isSuccessful())//newww
                                    {
                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(OtpActivity.this, "Error In Signing In ...", Toast.LENGTH_SHORT).show();
                                    }
                                    else {//newww
                                        bar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(OtpActivity.this, Surveyselection.class);
                                        intent.putExtra("uid",uID);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
                        else {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(OtpActivity.this, "Error In Signing In ...", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

}