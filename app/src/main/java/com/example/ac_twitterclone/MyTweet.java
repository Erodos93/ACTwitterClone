package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTweet extends AppCompatActivity implements View.OnClickListener {
    private Button btnSendTweet;
    private EditText edtTweet;
    private ListView lvTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tweet);

        btnSendTweet = findViewById(R.id.btnSend);
        edtTweet = findViewById(R.id.edtTweet);
        lvTweet = findViewById(R.id.lvTweet);
        btnSendTweet.setOnClickListener(this);
        HashMap<String,String> number = new HashMap<>();

//        lvTweet.setOnTouchListener(new ListView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        FancyToast.makeText(MyTweet.this, event.getAction() + "",
//                                FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();
//
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        FancyToast.makeText(MyTweet.this, event.getAction() + "",
//                                FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();
//
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                v.onTouchEvent(event);
//                return true;
//            }
//        });
//        lvTweet.setClickable(true);
        edtTweet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSendTweet);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSend) {
//            final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Send Message...");
            progressDialog.show();

            ParseObject parseObject = ParseObject.create("MyTweet");
            parseObject.put("tweet", edtTweet.getText().toString());
            parseObject.put("user", ParseUser.getCurrentUser().getUsername());
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(MyTweet.this, ParseUser.getCurrentUser().getUsername() +
                                        "'s tweet (" + edtTweet.getText().toString() + ") is saved",
                                FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();

                    } else {
                        FancyToast.makeText(MyTweet.this, e.getMessage(),
                                FancyToast.SUCCESS, Toast.LENGTH_SHORT, true).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    public void scrollKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            e.getStackTrace();

        }


    }


}
