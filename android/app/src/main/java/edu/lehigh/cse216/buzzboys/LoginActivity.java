package edu.lehigh.cse216.buzzboys;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.lehigh.cse216.buzzboys.Data.Message;
import edu.lehigh.cse216.buzzboys.Data.User;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = VolleySingleton.usersUrl;
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;
    public static final String PREFS_NAME = "LoginPrefs";

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check if were are already logged in
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "You are already Logged in, Redirecting Home", Toast.LENGTH_SHORT).show();
        }

            loginInputEmail = (EditText) findViewById(R.id.login_input_email);
            loginInputPassword = (EditText) findViewById(R.id.login_input_password);
            btnlogin = (Button) findViewById(R.id.btn_login);
            btnLinkSignup = (Button) findViewById(R.id.btn_link_signup);
            // Progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser(loginInputEmail.getText().toString(), loginInputPassword.getText().toString());
                }
            });

            btnLinkSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                }
            });
        }

    private void loginUser(final String email,final String password){
        progressDialog.setMessage("Logging you in...");
        showDialog();


        if (VolleySingleton.OFFLINE)
            for (User c : User.TestUsers)
                user = c;
        else {
            user.email = email;
            user.getPassword();
            VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleySingleton.usersUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getUser(response);
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


        /*
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() { //TODO -- change to find a user and login

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        //get sharedPrefences
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "logged");
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Successfull Login, Redirecting Home", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
        VolleySingleton.getInstance(getApplication()).addRequest(strReq);
        */
    }

    private void getUser(String response) {
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray= obj.getJSONArray("mData");
            for (int i = 0; i < jsonArray.length(); ++i) {
                users.add(User.getFromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            Log.d("TheBuzz", "Error parsing JSON user:" + e.getMessage());
            return;
        }
        Log.d("TheBuzz", "Successfully parsed Users");
        for (int i = 0; i<users.size(); i++){
            if(user == users.get(i)){
                Log.d("TheBuzz", "Successfully found a user, logging them in");
                //get sharedPrefences
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("logged", "logged");
                editor.commit();
                Toast.makeText(getApplicationContext(), "Successfull Login, Redirecting Home", Toast.LENGTH_SHORT).show();

                try {
                    User.setCurrentUser(user);
                } catch (JSONException e) {
                    Log.d("TheBuzz", "Error setting current user");
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Log.d("TheBuzz", "Did not find a user");
                Toast.makeText(getApplicationContext(), "Could not find a User, by that name or password. Please try again", Toast.LENGTH_SHORT).show();
                //redirect back to login
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        //RecyclerView rv = (RecyclerView) findViewById(R.id.message_list_view);
        //rv.getAdapter().notifyDataSetChanged();
    }

    private void showDialog(){
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}