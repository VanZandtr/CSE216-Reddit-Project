package edu.lehigh.cse216.buzzboys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.lehigh.cse216.buzzboys.Data.Comment;
import edu.lehigh.cse216.buzzboys.Data.Message;

//TODO - add comment button and listener

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    public int currentMessageId;
    boolean getComment;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView index;
        TextView text;
        TextView upvoteCount;
        Button upvote;
        Button downvote;
        Button comments;
        int messageId = -1;

        ViewHolder(final View itemView) {
            super(itemView);
            this.index = (TextView) itemView.findViewById(R.id.listItemIndex);
            this.text = (TextView) itemView.findViewById(R.id.listItemText);
            this.upvoteCount = (TextView) itemView.findViewById(R.id.listItemUpvoteCount);
            upvote = (Button) itemView.findViewById(R.id.message_upvote);
            downvote = (Button) itemView.findViewById(R.id.message_downvote);
            comments = (Button) itemView.findViewById(R.id.button4);

            View.OnClickListener upListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleySingleton volleySingleton = VolleySingleton.getInstance(itemView.getContext());
                    String fullUrl = String.format(VolleySingleton.messageUpvoteUrl, messageId);
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, fullUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(itemView.getContext(), response,Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("TheBuzz", "Error upvoting");
                                    Toast.makeText(itemView.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    volleySingleton.addRequest(stringRequest);
                }
            };

            upvote.setOnClickListener(upListener);

            View.OnClickListener downListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleySingleton volleySingleton = VolleySingleton.getInstance(itemView.getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, String.format(VolleySingleton.messageDownvoteUrl, messageId),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(itemView.getContext(), response,Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("TheBuzz", "Error downvoting");
                                    Toast.makeText(itemView.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    volleySingleton.addRequest(stringRequest);
                }
            };

            downvote.setOnClickListener(downListener);


            View.OnClickListener commentListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO - fix this code, get right url, get correct space, make getComments method
                    Intent i = new Intent(itemView.getContext(), ShowCommentsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("Message_ID", messageId);
                    i.putExtras(bundle);
                    itemView.getContext().startActivity(i);

                    }
            };

            comments.setOnClickListener(commentListener);
        }
    }

    private ArrayList<Message> messages;
    private LayoutInflater mLayoutInflater;

    ItemListAdapter(Context context, ArrayList<Message> data) {
        messages = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message m = messages.get(position);
        holder.index.setText(m.subject);
        holder.text.setText(m.message);
        holder.upvoteCount.setText(Integer.toString(m.upvotes - m.downvotes));
        holder.messageId = m.ID;
    }
}
