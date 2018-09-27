package edu.lehigh.cse216.buzzboys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.lehigh.cse216.buzzboys.Data.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION =VolleySingleton.usersUrl;
    ProgressDialog progressDialog;

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

        registerUser(signupInputName.getText().toString(),
                signupInputFirstName.getText().toString(),
                signupInputLastName.getText().toString(),
                signupInputEmail.getText().toString(),
                signupInputPassword.getText().toString());
    }

    private void registerUser(final String name, final String firstname, final String lastname, final String email, final String password) {
        progressDialog.setMessage("Adding you ...");
        showDialog();


        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
        if (!name.equals("") && !email.equals("") && email.contains("@") && !password.equals(null)) {
            User newUser = new User(100, name, "", "", email, password);
            String currentTime = Calendar.getInstance().getTime().toString();

            //TODO - add hash/salt for password here or in User creation

            String json = "{\"userId\":" + name + ",\"cDate\":\"" + currentTime + "\",\"mFirstName\":\"" + firstname + "\",\"mLastName\":\"" + lastname + "\",\"mPassword\":\" + password}";//TODO - get correct POST string --- backend
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

        /*
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        */

        // Adding request to request queue
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);//.addRequest(String)?
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
