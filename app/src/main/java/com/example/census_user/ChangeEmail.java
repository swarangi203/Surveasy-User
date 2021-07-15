package com.example.census_user;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    TextInputLayout email;
    Button btn;
    ProgressBar bar;
    FirebaseAuth mFirebaseAuth;
    String mail;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = (TextInputLayout)findViewById(R.id.editTextTextEmail);
        bar=(ProgressBar) findViewById(R.id.progressBar2);
        btn = (Button) findViewById(R.id.img2);
        text=(TextView)findViewById(R.id.ghf);
        text.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                mail=email.getEditText().getText().toString();
                if(mail.isEmpty())
                {
                    email.setError("Please Enter an Email");
                    email.requestFocus();
                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangeEmail.this).create();//newww
                    alertDialog.setTitle("Alert");//newww
                    alertDialog.setCancelable(false);//newww
                    alertDialog.setMessage("Email Verification Link Will be Sent To This Email Please Confirm Entered Email");//newww
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Recheck",//newww
                            new DialogInterface.OnClickListener() {//newww
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirmed",//newww
                            new DialogInterface.OnClickListener() {//newww
                                public void onClick(DialogInterface dialog, int which) {
                                    user.updateEmail("user@example.com")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updateEmail(mail)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            AlertDialog alertDialog = new AlertDialog.Builder(ChangeEmail.this).create();//newww
                                                                            alertDialog.setTitle("Alert");//newww
                                                                            alertDialog.setCancelable(false);//newww
                                                                            alertDialog.setMessage("Email verification link Is Sent To New Email\n\nPlease Re-Start the Application Again");//newww
                                                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Login-Again",//newww
                                                                                    new DialogInterface.OnClickListener() {//newww
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            FirebaseAuth.getInstance().signOut();
                                                                                            finishAffinity();
                                                                                            System.exit(0);
                                                                                        }
                                                                                    });
                                                                            alertDialog.show();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(ChangeEmail.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Recheck",//newww
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
}