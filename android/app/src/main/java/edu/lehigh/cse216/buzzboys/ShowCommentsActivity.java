package edu.lehigh.cse216.buzzboys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ShowCommentsActivity extends AppCompatActivity {

    String messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the parameter from the calling activity, and put it in the TextView
        Intent input = getIntent();
        messageId = input.getStringExtra("Message_ID");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCommentsActivity.this, CreateCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Message_ID", Integer.parseInt(messageId));
                intent.putExtras(bundle);
            }
        });
    }

        //TODO --- on create show comments view bunlde messageID
        //TODO --- create a button to craete and add an new message with messageID
    }
