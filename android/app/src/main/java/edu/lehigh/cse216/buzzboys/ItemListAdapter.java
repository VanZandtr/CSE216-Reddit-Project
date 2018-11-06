package edu.lehigh.cse216.buzzboys;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.lehigh.cse216.buzzboys.Data.Message;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView index;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            this.index = (TextView) itemView.findViewById(R.id.listItemIndex);
            this.text = (TextView) itemView.findViewById(R.id.listItemText);
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
        holder.index.setText(Integer.toString(m.ID));
        holder.text.setText(m.message);
    }
}
