package edu.lehigh.cse216.buzzboys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import edu.lehigh.cse216.buzzboys.Data.Comment;


class CommentItemList extends RecyclerView.Adapter<CommentItemList.ViewHolder> {
    public int currentMessageId;
    boolean getComment;

    class ViewHolder extends RecyclerView.ViewHolder {
        //TextView index;
        TextView text;
        int messageId = -1;
        int commentId = -1;

        ViewHolder(final View itemView) {
            super(itemView);
            //this.index = (TextView) itemView.findViewById(R.id.CommentItemIndex);
            this.text = (TextView) itemView.findViewById(R.id.CommentItemText);
        }
    }

    private ArrayList<Comment> comments;
    private LayoutInflater mLayoutInflater;

    CommentItemList(Context context, ArrayList<Comment> data) {
        comments = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        //holder.index.setText(m.subject);
        holder.text.setText(comment.mComment);
        holder.commentId = comment.mId;
    }
}
