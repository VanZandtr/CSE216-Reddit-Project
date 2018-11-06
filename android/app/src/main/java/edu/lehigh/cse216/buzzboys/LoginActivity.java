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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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
    private SignInButton gbtnlogin;
    //private Button btnLinkSignup;
    public static final String PREFS_NAME = "LoginPrefs";

    User user;

    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 0;  // request code for starting new activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Google Sign-in phase 3
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("426643010023-nohc4crv4ck9lcp2ru5vurbehv0r48v0.apps.googleusercontent.com") // OAuth 2.0 client id
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            //do something with account
            Toast.makeText(this, "Already Signed In", Toast.LENGTH_LONG).show();
        }


        //check if were are already logged in
        /*
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "You are already Logged in, Redirecting Home", Toast.LENGTH_SHORT).show();
        }
        */

        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        gbtnlogin = findViewById(R.id.sign_in_button);
        //btnLinkSignup = (Button) findViewById(R.id.btn_link_signup);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // onclick for google login button
        gbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loginInputEmail.getText().toString().equals("") && !loginInputPassword.getText().toString().equals("")){
                    loginUser(loginInputEmail.getText().toString(), loginInputPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid credentials!", Toast.LENGTH_LONG).show();
                }

            }
        });
/*
            btnLinkSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                }
            });
*/
        }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            progressDialog.setMessage("Logging you in...");
            showDialog();
            Log.i("TESTINGLOGIN", "BEFOREGETRESULT");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i("TESTINGLOGIN", "AFTERGETRESULT");
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            hideDialog();
            Toast.makeText(getApplicationContext(), "Successful Login, Redirecting Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    // Phase 2
    private void loginUser(final String email,final String password){
        progressDialog.setMessage("Logging you in...");
        showDialog();


        if (VolleySingleton.OFFLINE)
            for (User c : User.TestUsers)
                user = c;
        else {
            user = new User(0, "","","","");
            Log.i("TESTING1", "email is "+email);
            user.email = email;
            user.setPassword(password);
            Log.i("TESTING2", "email: "+email+"  password: "+password);

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
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "Error getting messages from server", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
            );
            Log.i("TESTING3", "After volleysingleton created");

            volleySingleton.addRequest(stringRequest);
            Log.i("TESTING4", "After volleysingleton request");
        }
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
            Toast.makeText(getApplicationContext(), "Error parsing JSON user: "+e.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
            return;
        }
        Log.d("TheBuzz", "Successfully parsed Users");
        for (int i = 0; i<users.size(); i++){
            if(user.email == users.get(i).email && user.getPassword() == users.get(i).getPassword()){
                Log.d("TheBuzz", "Successfully found a user, logging them in");
                //get sharedPrefences
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("logged", "logged");
                editor.commit();
                Toast.makeText(getApplicationContext(), "Successful Login, Redirecting Home", Toast.LENGTH_SHORT).show();

                //set the rest of the user fields
                user = users.get(i);

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