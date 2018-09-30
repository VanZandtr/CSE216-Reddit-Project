package edu.lehigh.cse216.buzzboys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.lehigh.cse216.buzzboys.Data.User;

import static edu.lehigh.cse216.buzzboys.LoginActivity.PREFS_NAME;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION =VolleySingleton.usersUrl;
    ProgressDialog progressDialog;

    User user;
    Boolean flag = false;



    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputFirstName, signupInputLastName;
    private Button btnSignUp;
    private Button btnLinkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputFirstName = (EditText) findViewById(R.id.signup_input_first_name);
        signupInputLastName = (EditText) findViewById(R.id.signup_input_last_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitForm() {

        if (checkUser(signupInputEmail.getText().toString()) == true){
            Toast.makeText(getApplicationContext(), "Found a user with that email. Redirecting to login", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

        registerUser(signupInputName.getText().toString(),
                signupInputFirstName.getText().toString(),
                signupInputLastName.getText().toString(),
                signupInputEmail.getText().toString(),
                signupInputPassword.getText().toString());

    }

    private void registerUser(final String name, final String firstname, final String lastname, final String email, final String password) {
        progressDialog.setMessage("Adding you ...");
        showDialog();

        if (!name.equals("") && !email.equals("") && email.contains("@") && !password.equals(null)) {

            User user = new User(100, name, "", "", email, password);
            String currentTime = Calendar.getInstance().getTime().toString();

            //TODO - add hash/salt for password here or in User creation

            //TODO -- add a randomized user token, add it to json string
            String json = "{\"userId\":" + name + ",\"cDate\":\"" + currentTime + "\",\"mFirstName\":\"" + firstname + "\",\"mLastName\":\"" + lastname + "\",\"mPassword\":\" + password}";//TODO - get correct POST string --- backend
            //check if user exits, reidrect back to page
            try {
                JSONObject jsonObject = new JSONObject(json);

                VolleySingleton volleySingleton = VolleySingleton.getInstance(getApplicationContext());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VolleySingleton.userUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TheBuzz", "Error posting message");
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                volleySingleton.addRequest(jsonObjectRequest);
                User.setCurrentUser(user);
            } catch (JSONException e) {
                Log.d("TheBuzz", "Error creating json object");
            }
        }
    }



    private boolean checkUser(final String email){
        progressDialog.setMessage("Checking email...");
        showDialog();
        user.email = email;
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VolleySingleton.usersUrl,
                new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(getUser(response) == true){
                                flag = true;
                            }
                            else{
                                flag = false;
                            }
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
            return flag;
        }

    private boolean getUser(String response) {
        ArrayList<User> users = new ArrayList<>();
        boolean gotAUser = false;
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray= obj.getJSONArray("mData");//TODO --- ???
            for (int i = 0; i < jsonArray.length(); ++i) {
                users.add(User.getFromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            Log.d("TheBuzz", "Error parsing JSON user:" + e.getMessage());
            gotAUser =  true;
        }
        Log.d("TheBuzz", "Successfully parsed Users");
        for (int i = 0; i<users.size(); i++){
            if(user == users.get(i)){
                Toast.makeText(getApplicationContext(), "Redirecting to Login", Toast.LENGTH_SHORT).show();
                gotAUser =  true;
            }
            else{
                Log.d("TheBuzz", "Did not find a user");
                Toast.makeText(getApplicationContext(), "Email accepted", Toast.LENGTH_SHORT).show();
                gotAUser = false;
            }
        }
        return gotAUser;
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
