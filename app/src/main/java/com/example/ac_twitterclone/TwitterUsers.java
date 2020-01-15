package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;
    private String followedUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);


        try {
            listView = findViewById(R.id.listView);
            tUsers = new ArrayList<>();
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            listView.setOnItemClickListener(this);
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser users : objects) {
                            tUsers.add(users.getUsername());
                        }
                        listView.setAdapter(adapter);
                        for (String users : tUsers) {
//                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(users)) {
                                    followedUser = followedUser + users+"\n";
                                    listView.setItemChecked(tUsers.indexOf(users), true);
                                    FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser()
                                            .getUsername()+"is following with "+ followedUser, FancyToast.INFO, Toast.LENGTH_LONG, true).show();

                                }
//                            }
                        }

                    }
                }
            });
            FancyToast.makeText(TwitterUsers.this, "Welcome " + ParseUser.getCurrentUser()
                    .getUsername(), FancyToast.INFO, Toast.LENGTH_LONG, true).show();
        } catch (Exception e) {
            e.getStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutUserName:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(TwitterUsers.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.sendItem:
                FancyToast.makeText(TwitterUsers.this, "Click send ", FancyToast.INFO, Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(TwitterUsers.this,MyTweet.class);
                startActivity(intent);
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()) {
            FancyToast.makeText(this, tUsers.get(position) + "is folowed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));
        } else {
            FancyToast.makeText(this, tUsers.get(position) + "is unfolowed", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List newList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", newList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this, "Saved", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                }
            }
        });
    }
}
