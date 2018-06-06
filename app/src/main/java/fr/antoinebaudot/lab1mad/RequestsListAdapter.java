package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {
    private ArrayList<Pair<String,BookRequest>> mDataset;
    private LayoutInflater inflater;
    //private ArrayList<String> keys ;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView book;
        private TextView start;
        private TextView end ;
        private TextView state ;
        private TextView key ;



        public ViewHolder(final View itemView) {
            super(itemView);
            book = (TextView) itemView.findViewById(R.id.bookId);
            start = (TextView) itemView.findViewById(R.id.startDate);
            end = (TextView) itemView.findViewById(R.id.endDate);
            state = (TextView) itemView.findViewById(R.id.state);
            key = (TextView) itemView.findViewById(R.id.dbkey);


        }
    }



    public RequestsListAdapter(Context context, ArrayList<Pair<String,BookRequest>> myDataset) {
        mDataset = myDataset;
        inflater = LayoutInflater.from(context);

    }

    // Create new views
    @Override
    public RequestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = inflater.inflate(R.layout.list_requests_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        BookRequest tmp = mDataset.get(position).second;
        String key = mDataset.get(position).first;

        holder.start.setText(tmp.getStart());
        holder.end.setText(tmp.getEnd());
        holder.book.setText(tmp.getTitle());
        holder.state.setText(tmp.getState().toString());
        holder.key.setText(key);


        if(tmp.getState().toString().equals(RequestState.ACCEPTED.toString())){
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.accepted));
        } else if (tmp.getState().toString().equals(RequestState.REFUSED.toString())){
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.refused));
        }



    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, RequestState newState);
    }




}

