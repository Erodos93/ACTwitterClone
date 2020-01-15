package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLoginLoginActivity);
        btnSignUp = findViewById(R.id.btnSignUpLoginActivity);
        btnLogin.setOnClickListener(LogInActivity.this);
        btnSignUp.setOnClickListener(LogInActivity.this);
        if (ParseUser.getCurrentUser() != null) {
            translationToMainActivity();
        }
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {


            switch (view.getId()) {
                case R.id.btnLoginLoginActivity:
                    if (edtPassword.getText().toString().equals("") && edtEmail.getText().toString().equals("")) {
                    FancyToast.makeText(LogInActivity.this, "Username,Email,Password is request ", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();

                } else {
                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Logged In...");
                        progressDialog.show();
                        ParseUser.logInInBackground(edtEmail.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null) {
                                    FancyToast.makeText(LogInActivity.this, "Log in is done!", FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();
                                    translationToMainActivity();
                                } else {
                                    FancyToast.makeText(LogInActivity.this, e.getMessage(), FancyToast.ERROR, Toast.LENGTH_SHORT, true).show();

                                }
                                progressDialog.dismiss();
                            }
                        });



                    }
                    break;
                        case R.id.btnSignUpLoginActivity:
                            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                            startActivity(intent);

                            break;

                    }
        }


    public void translationToMainActivity() {
        Intent intent = new Intent(LogInActivity.this, TwitterUsers.class);
        startActivity(intent);
    }
    public void scrollKeyboard(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
