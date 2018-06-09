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
import android.util.Base64;
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
        private TextView mDescView;
        private TextView bookId ;
        private ImageView coverBook ;


        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_item);
            mDescView = (TextView) itemView.findViewById(R.id.text_item_description);
            coverBook = (ImageView) itemView.findViewById(R.id.bookCover);
            bookId = (TextView) itemView.findViewById(R.id.bookId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = bookId.getText().toString();
                    Intent intent = new Intent(view.getContext(),BookInfo.class);


                    intent.putExtra("BOOK_ID",id);
                    view.getContext().startActivity(intent);
                }
            });

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

        if (tmp != null) {

            holder.mTextView.setText(tmp.getTitle());
            holder.mDescView.setText(tmp.getDescription());
            holder.bookId.setText(tmp.getIsbn() + "-" + tmp.getOwner());


            if (tmp.getCover() != null) {

                Bitmap cover = decodeBase64(tmp.getCover());
                holder.coverBook.setImageBitmap(cover);

            } else {
                holder.coverBook.setImageResource(R.drawable.icon_book);
            }

        }

    }


    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }






}

