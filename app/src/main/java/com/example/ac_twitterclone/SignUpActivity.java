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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtSignUpEmail, edtSignUpPassword, edtSignUpUserName;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtSignUpEmail = findViewById(R.id.edtEmailSignUp);
        edtSignUpPassword = findViewById(R.id.edtPasswordSignUp);
        edtSignUpUserName = findViewById(R.id.edtUserNameSignUp);
        btnLogin = findViewById(R.id.btnLoginLoginActivity);
        btnSignUp = findViewById(R.id.btnSignUpLoginActivity);
        btnLogin.setOnClickListener(SignUpActivity.this);
        btnSignUp.setOnClickListener(SignUpActivity.this);
//
        if (ParseUser.getCurrentUser() != null) {
            translationToMainActivity();
        }
//        Aktivování možnosti od entrovat heslo pomocí klávesnice
        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }
                return false;
            }
        });
    }
//    Ovladač tlačítek
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLoginLoginActivity:
                Intent intent = new Intent(SignUpActivity.this, TwitterUsers.class);
                startActivity(intent);
                break;
            case R.id.btnSignUpLoginActivity:
//                Registrace uživatele
                final ParseUser user = new ParseUser();
                user.setEmail(edtSignUpEmail.getText().toString());
                user.setUsername(edtSignUpUserName.getText().toString());
                user.setPassword(edtSignUpPassword.getText().toString());
//                Dialogové okno
                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Sign up...");
                progressDialog.show();
//                Podmínka ošetřuující vstupní údaje
                if (edtSignUpEmail.getText().toString().equals("") ||
                        edtSignUpPassword.getText().toString().equals("") ||
                        edtSignUpUserName.getText().toString().equals("")) {
                    FancyToast.makeText(SignUpActivity.this, "Username,Email,Password is request ", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();

                } else {

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpActivity.this, "Sign Up done!", FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();
                                translationToMainActivity();
                            } else {
                                FancyToast.makeText(SignUpActivity.this, e.getMessage(), FancyToast.ERROR, Toast.LENGTH_SHORT, true).show();

                            }
                            progressDialog.dismiss();
                        }
                    });

                }


                break;
        }
    }
//  Přechod na jinou activitu
    public void translationToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }
//    Schování klávesnice při kliknutí na pozadí ošetřený vyjímkou proti pádu
    public void scrollKeyboardOnSignUp(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }catch (Exception e){
            e.getStackTrace();
        }
 }
}
