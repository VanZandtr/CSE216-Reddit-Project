package edu.lehigh.cse216.buzzboys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.lehigh.cse216.buzzboys.Data.Comment;
import edu.lehigh.cse216.buzzboys.Data.Message;

import static edu.lehigh.cse216.buzzboys.LoginActivity.PREFS_NAME;

public class ShowCommentsActivity extends AppCompatActivity {

    String messageId;
    ArrayList<Comment> comments = new ArrayList<>();


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_comments);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Get the parameter from the calling activity, and put it in the TextView
            Intent input = getIntent();
            messageId = input.getStringExtra("Message_ID");


            RecyclerView rv = (RecyclerView) findViewById(R.id.comments_list_view);

            rv.setLayoutManager(new LinearLayoutManager(this));
            CommentItemList adapter = new CommentItemList(this, comments);
            rv.setAdapter(adapter);

            //have to be logged in to see comments
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            if (!settings.getString("logged", "").toString().equals("logged")) {
                Intent intent = new Intent(ShowCommentsActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Please Login.", Toast.LENGTH_SHORT).show();
            }

            if (VolleySingleton.OFFLINE)
                for (Comment c : Comment.TestComments)
                    comments.add(c);
            else {
                VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleySingleton.commentsUrl + "/" + messageId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                getComments(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TheBuzz", "Error getting comments from server");
                            }
                        }
                );
                volleySingleton.addRequest(stringRequest);
            }

            Button createAComment = (Button) findViewById(R.id.goToCreateComment);
            createAComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowCommentsActivity.this, CreateCommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Message_ID", Integer.parseInt(messageId));
                    intent.putExtras(bundle);
                }
            });

        }

    private void getComments(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray= obj.getJSONArray("mData");
            for (int i = 0; i < jsonArray.length(); ++i) {
               comments.add(Comment.getFromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            Log.d("TheBuzz", "Error parsing JSON message:" + e.getMessage());
            return;
        }
        Log.d("TheBuzz", "Successfully parsed Comments");
        RecyclerView rv = (RecyclerView) findViewById(R.id.comments_list_view);
        rv.getAdapter().notifyDataSetChanged();
    }

}
