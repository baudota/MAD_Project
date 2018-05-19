package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Book> mDataset;
    private LayoutInflater inflater;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_item);
        }
    }

    public Adapter(Context context, ArrayList<Book> myDataset) {
        mDataset = myDataset;
        inflater = LayoutInflater.from(context);
    }

    // Create new views
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of the view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book tmp = mDataset.get(position);
        holder.mTextView.setText(tmp.getTitle());
       // holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

