package edu.lehigh.cse216.buzzboys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.lehigh.cse216.buzzboys.Data.Message;

import static edu.lehigh.cse216.buzzboys.LoginActivity.PREFS_NAME;

/**
 * Main Activity / Landing page for the user. Displays all messages
 * TODO indicate whether a user upvoted/downvoted the message
 * TODO If the user tries to upvote when they aren't logged in, start LoginActivity
 *
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.message_list_view);

        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, messages);
        rv.setAdapter(adapter);
/*
        //have to be logged in to see messages
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (!settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Please Login.", Toast.LENGTH_SHORT).show();
        }
*/
        if (VolleySingleton.OFFLINE)
            for (Message m : Message.TestMessages)
                messages.add(m);
        else {
            VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleySingleton.messagesUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getMessages(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TheBuzz", "Error getting messages from server");
                        }
                    }
            );
            volleySingleton.addRequest(stringRequest);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateMessageActivity.class);
                i.putExtra("From", "MainActivity");
                startActivityForResult(i, 789);
            }
        });
    }

    private void getMessages(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray= obj.getJSONArray("mData");
            for (int i = 0; i < jsonArray.length(); ++i) {
                messages.add(Message.getFromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            Log.d("TheBuzz", "Error parsing JSON message:" + e.getMessage());
            return;
        }
        Log.d("TheBuzz", "Successfully parsed Messages");
        RecyclerView rv = (RecyclerView) findViewById(R.id.message_list_view);
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login_form) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            //i.putExtra("label_contents", "CSE216 is the best");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        //TODO --- logout button
/*
        if (id == R.id.logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();
            finish();
        }
        */
        return super.onOptionsItemSelected(item);
    }
}
