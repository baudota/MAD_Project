package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Book> mDataset;
    private LayoutInflater inflater;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private  TextView mDescView;
        private ImageView coverBook ;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_item);
            mDescView = (TextView) itemView.findViewById(R.id.text_item_description);
            coverBook = (ImageView) itemView.findViewById(R.id.bookCover);

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
        holder.mDescView.setText(tmp.getDescription());

        holder.itemView.setOnClickListener(v ->

                {
                    Intent intent = new Intent(holder.itemView.getContext(), BookActivity.class);
                    intent.putExtra("Book",tmp);

                    Bundle bundle = new Bundle();
                    holder.itemView.getContext().startActivity(intent);

                }

        );

        
       // holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }






}

