package edu.lehigh.cse216.buzzboys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import edu.lehigh.cse216.buzzboys.Data.Message;
import edu.lehigh.cse216.buzzboys.Data.User;

/**
 * A screen for creating and posting a message
 * TODO Logic for updating
 * TODO button for comments/comment implementation
 */
public class CreateMessageActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        // Get the parameter from the calling activity, and put it in the TextView
        Intent input = getIntent();
        String label_contents = input.getStringExtra("label_contents");
        TextView tv = (TextView) findViewById(R.id.specialMessage);
        tv.setText(label_contents);
/*
        //If we're not logged in, make the user log in
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (!(settings.getString("logged", "").equals("logged"))) {
            Intent intent = new Intent(CreateMessageActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "You need to login in order to vote.", Toast.LENGTH_SHORT).show();
        }
*/
        // The OK button gets the text from the input box and returns it to the calling activity
        final EditText subjectBox = (EditText) findViewById(R.id.create_message_subject_box);
        final EditText messageBox = (EditText) findViewById(R.id.create_message_object_box);
        Button bOk = (Button) findViewById(R.id.buttonOk);
        bOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                if (!subjectBox.getText().toString().equals("") && !messageBox.getText().toString().equals("")) {
                    Message m = new Message(subjectBox.getText().toString(), messageBox.getText().toString());
                    String currentTime = Calendar.getInstance().getTime().toString();

                    try {
                        user = User.getCurrentUser();
                    }catch (JSONException e) {
                        Log.d("TheBuzz", "Error getting current user");
                    }

                    String json = "{\"uid\":\"" + user.uid + "\",\"date_created\":\"" + currentTime + "\",\"content\":\"" + messageBox.getText().toString() + "\",\"title\":\"" + subjectBox.getText().toString() + "\"}";
                    try {
                        JSONObject jsonObject = new JSONObject(json);

                        VolleySingleton volleySingleton = VolleySingleton.getInstance(view.getContext());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VolleySingleton.messagesUrl, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_LONG).show();
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("TheBuzz", "Error posting message");
                                        Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        volleySingleton.addRequest(jsonObjectRequest);
                    } catch (JSONException e) {
                        Log.d("TheBuzz", "Error creating json object");
                    }
                }
            }
        });

        // The Cancel button returns to the caller without sending any data
        Button bCancel = (Button) findViewById(R.id.buttonCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //If the LoginActivity was cancelled, this activity should cancel too. Send the user back
        //to the main screen
        if(resultCode == RESULT_CANCELED){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}
