package edu.lehigh.cse216.buzzboys;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import edu.lehigh.cse216.buzzboys.Data.Message;

/**
 * Responsible for communicating with Heroku.
 * TODO: Find a better name for VolleySingleton
 * TODO: Methods for getting basic data
 * TODO: Enable testing functionality; ie be able to return some data without an internet connection
 */
public class VolleySingleton {

    public final static boolean OFFLINE = false;

    private final static String baseUrl = "https://buzzboys.herokuapp.com";
    public final static String usersUrl = baseUrl + "/users";
    public final static String messagesUrl = baseUrl + "/messages";
    public final static String userUrl = baseUrl + "/user/%d"; //insert user id
    public final static String userMessagesUrl = baseUrl + "/user/%d/messages"; //insert user id
    public final static String messageUrl = baseUrl + "/messages/%d"; //insert message id
    public final static String messageUpvoteUrl = baseUrl + "/messages/%d/upvote"; //insert message id
    public final static String messageDownvoteUrl = baseUrl + "/messages/%d/downvote"; //insert message id
    public final static String commentsUrl = baseUrl + "/comments/%d/message"; //insert message id //TODO -- get the correct url for insert a comment on a messageID


    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingleton(Context c) {
        context = c;
        requestQueue = getRequestQueue();
        //sourceUrl = "postgres://bqmyghussmyoch:0b4491be62bc014a18f2f4c7795067a7e1c898b2bf20102729254cbdf748f9cf@ec2-54-83-27-165.compute-1.amazonaws.com:5432/d27ergo5pr6bta";
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized VolleySingleton getInstance(Context c) {
        if (instance == null)
            instance = new VolleySingleton(c);
        return instance;
    }

    public <T> void addRequest(Request<T> req) {
        getRequestQueue().add(req);
    }

}
