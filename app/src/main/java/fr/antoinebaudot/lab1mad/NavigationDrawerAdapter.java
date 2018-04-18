package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.List;

/**
 * Created by Antoine on 18/04/2018.
 */

public  class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder>  {

    private List<NavigationDrawerItem> mDataList = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context ;

    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> data){
        this.context = context ;
        inflater = LayoutInflater.from(context);
        this.mDataList = data ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_list_item_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder ;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NavigationDrawerItem current = mDataList.get(position);

        holder.title.setText(current.getTitle());
        holder.imgIcon.setImageResource(current.getImageId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.title.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title ;
        public ImageView imgIcon ;


        public MyViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            title = (TextView) itemView.findViewById(R.id.itemTitle);
        }

    }


}

