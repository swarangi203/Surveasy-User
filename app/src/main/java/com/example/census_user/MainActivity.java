package com.example.census_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class MainActivity extends AppCompatActivity {

    TextInputLayout email, password;
    Button btn;
    TextView forgetPass,changeemail;
    ProgressBar bar;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    String mobileNo,mail,pass;
    Boolean b=true;//newww
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int nightModeFlags = this.getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.mylayout);
               // Resources layout =getResources();
               //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backgrounddark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ConstraintLayout layoutlight = (ConstraintLayout)findViewById(R.id.mylayout);
                layoutlight.setBackgroundResource(R.drawable.background);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                ConstraintLayout layoutd = (ConstraintLayout)findViewById(R.id.mylayout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backgrounddark);
                break;
        }
        forgetPass=(TextView)findViewById(R.id.forgot);//newww
        changeemail=(TextView)findViewById(R.id.changeEmail);//newww
        changeemail.setVisibility(View.INVISIBLE);
        changeemail.setClickable(false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = (TextInputLayout)findViewById(R.id.editTextTextQuestionStatement);
        bar=(ProgressBar) findViewById(R.id.progressBar2);
        password = (TextInputLayout)findViewById(R.id.editTextTextoption);
//        signin = findViewById(R.id.textView2);
        btn = (Button) findViewById(R.id.img2);
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if (mFirebaseUser != null) {
//                    String id=mFirebaseUser.getUid();
//                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                    i.putExtra("uid",id);
//                    startActivity(i);
//                } else {
//                    Toast.makeText(MainActivity.this, "Please Log in", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        };

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeEmail.class);
                startActivity(intent);
                finish();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {//newww
            @Override
            public void onClick(View v) {//newww
                email.setError(null);
                password.setError(null);
                if (b) {
                    mail = email.getEditText().getText().toString();
                    if (mail.isEmpty()) {
                        bar.setVisibility(View.INVISIBLE);
                        email.setError("Please Enter an Email");
                        b=true;//newww
                        email.requestFocus();
                    }
                    else {//newww
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();//newww
                        alertDialog.setTitle("Alert");//newww
                        alertDialog.setCancelable(false);//newww
                        alertDialog.setMessage("Password reset link will be sent to entered Email..You might not receive reset link if Email is Not a Registered Email\n\nclick Yes to confirm");//newww
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",//newww
                                new DialogInterface.OnClickListener() {//newww
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.sendPasswordResetEmail(mail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, "Password Reset Email has been sent to your Email", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();//newww
                                                            alertDialog.setTitle("Alert");//newww
                                                            alertDialog.setCancelable(false);//newww
                                                            alertDialog.setMessage("This Email Is Not a Registered Email");//newww
                                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",//newww
                                                                    new DialogInterface.OnClickListener() {//newww
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    });
                                                            alertDialog.show();
                                                        }
                                                    }
                                                });

                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",//newww
                                new DialogInterface.OnClickListener() {//newww
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.show();
                    }
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b=false;//newww
                email.setError(null);
                password.setError(null);
                bar.setVisibility(View.VISIBLE);
                mail = email.getEditText().getText().toString();
                pass = password.getEditText().getText().toString();
                if (mail.isEmpty()) {
                    bar.setVisibility(View.INVISIBLE);
                    email.setError("Please Enter an Email");
                    b=true;//newww
                    email.requestFocus();
                } else if (pass.isEmpty()) {
                    bar.setVisibility(View.INVISIBLE);
                    password.setError("Please Enter a Password");
                    password.requestFocus();
                    b=true;//newww
                } else if (mail.isEmpty() && pass.isEmpty()) {
                    bar.setVisibility(View.INVISIBLE);
                    email.setError("Please Enter an Email");
                    password.setError("Please Enter a Password");
                    email.requestFocus();
                    password.requestFocus();
                    b=true;//newww
                } else if (!(mail.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "Sign In Unsuccessful", Toast.LENGTH_SHORT).show();
                                email.setError("Please Enter Correct Email");
                                password.setError("Please Enter Correct Password");
                                email.requestFocus();
                                password.requestFocus();
                                b=true;//newww

                            } else {
                                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                String id=mFirebaseUser.getUid();
                                rootnode = FirebaseDatabase.getInstance();
                                reference = rootnode.getReference("users").child(id);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        UserHelperClass userdata = snapshot.getValue(UserHelperClass.class);
                                        mobileNo = userdata.Mobile_No;
//                                        Toast.makeText(MainActivity.this,mobileNo, Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.INVISIBLE);
                                        if(userdata.state.equals("disabled"))
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setCancelable(false);
                                            alertDialog.setMessage("Your Account is DISABLED by ADMIN..Contact Admin for Details");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            finish();
                                                        }
                                                    });
                                            alertDialog.show();

                                        }
                                        else if(!userdata.post.equals("user"))
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setCancelable(false);
                                            alertDialog.setMessage("Your Account is not registered as user...Contact Admin for Details");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            finish();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                        else {
                                            if(mFirebaseUser.isEmailVerified()) {
                                                Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                                                intent.putExtra("mobileNo", mobileNo);
                                                intent.putExtra("uid", id);
                                                intent.putExtra("mail", mail);//newww
                                                intent.putExtra("pass", pass);//newww
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                mFirebaseUser.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    bar.setVisibility(View.INVISIBLE);
                                                                    changeemail.setVisibility(View.VISIBLE);
                                                                    changeemail.setClickable(true);
                                                                    Toast.makeText(MainActivity.this, "Verify Email to Sign in..", Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(MainActivity.this, "Verification Email Sent to registered Email..", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        bar.setVisibility(View.INVISIBLE);
                                        b=true;//newww
                                        Toast.makeText(MainActivity.this, "Error in getting mobile no", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error Occurred Contact App Dev", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//    }

}
